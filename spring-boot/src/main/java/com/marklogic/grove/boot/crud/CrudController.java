package com.marklogic.grove.boot.crud;

import com.marklogic.client.DatabaseClient;
import com.marklogic.client.document.*;
import com.marklogic.client.io.DocumentMetadataHandle;
import com.marklogic.client.io.Format;
import com.marklogic.client.io.InputStreamHandle;
import com.marklogic.client.io.StringHandle;
import com.marklogic.client.io.DocumentMetadataHandle.DocumentCollections;
import com.marklogic.grove.boot.error.NotFoundException;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.net.URLDecoder;
import java.util.List;

//logging
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
@RequestMapping("/api/crud")
public class CrudController {

    private Logger logger = LoggerFactory.getLogger(getClass());

    @RequestMapping(value = "/{type}/{uri}", method = RequestMethod.GET)
    void getDoc(@PathVariable String type, @PathVariable String uri, HttpSession session, HttpServletResponse response) throws IOException {
        DatabaseClient client = (DatabaseClient) session.getAttribute("grove-spring-boot-client");
        DocumentPage page = client.newDocumentManager().read(URLDecoder.decode(uri, "UTF-8"));
        if (!page.hasNext()) {
            throw new NotFoundException();
        }
        DocumentRecord documentRecord = page.next();
        String mime = documentRecord.getFormat().getDefaultMimetype();
        response.setContentType(mime);
        response.getWriter().write(documentRecord.getContent(new StringHandle()).get());
    }

    @RequestMapping(value = "/{type}", method = RequestMethod.POST)
    void createDoc(
        @RequestParam(name = "collection", required = false) List<String> collectionParam,
        @PathVariable String type, HttpSession session, HttpServletRequest request, HttpServletResponse response) throws IOException {
        DatabaseClient client = (DatabaseClient) session.getAttribute("grove-spring-boot-client");
        GenericDocumentManager documentManager = client.newDocumentManager();
        DocumentUriTemplate uriTemplate;
        Format format = Format.getFromMimetype(request.getContentType());
        if (format.equals(Format.JSON)) {
            uriTemplate = documentManager.newDocumentUriTemplate("json");
        }
        else if (format.equals(Format.XML)) {
            uriTemplate = documentManager.newDocumentUriTemplate("xml");
        }
        else {
            uriTemplate = documentManager.newDocumentUriTemplate("");
        }

        uriTemplate.setDirectory("/notes/");

        DocumentMetadataHandle meta = new DocumentMetadataHandle();
        DocumentCollections collections = meta.getCollections();
        if (collectionParam != null) {
            for (String collection : collectionParam) {
                collections.add(collection);
                logger.info("Adding collection: " + collection);
            }
        }

        //collections.add("data/notes");
        collections.add("data");
        
        InputStreamHandle content = new InputStreamHandle(request.getInputStream());

        DocumentDescriptor documentDescriptor = client.newDocumentManager().create(uriTemplate, meta, content);

        response.setStatus(HttpStatus.CREATED.value());
        response.addHeader("location", documentDescriptor.getUri());
    }
}
