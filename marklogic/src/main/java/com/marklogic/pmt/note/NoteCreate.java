package com.marklogic.pmt.note;

// IMPORTANT: Do not edit. This file is generated.

import com.marklogic.client.io.Format;
import com.marklogic.client.io.marker.AbstractWriteHandle;


import com.marklogic.client.DatabaseClient;

import com.marklogic.client.impl.BaseProxy;

/**
 * Provides a set of operations on the database server
 */
public interface NoteCreate {
    /**
     * Creates a NoteCreate object for executing operations on the database server.
     *
     * The DatabaseClientFactory class can create the DatabaseClient parameter. A single
     * client object can be used for any number of requests and in multiple threads.
     *
     * @param db	provides a client for communicating with the database server
     * @return	an object for session state
     */
    static NoteCreate on(DatabaseClient db) {
        final class NoteCreateImpl implements NoteCreate {
            private BaseProxy baseProxy;

            private NoteCreateImpl(DatabaseClient dbClient) {
                baseProxy = new BaseProxy(dbClient, "/dataservices/note/create/");
            }

            @Override
            public com.fasterxml.jackson.databind.JsonNode execute(String text, String type) {
              return BaseProxy.JsonDocumentType.toJsonNode(
                baseProxy
                .request("notecreate.sjs", BaseProxy.ParameterValuesKind.MULTIPLE_ATOMICS)
                .withSession()
                .withParams(
                    BaseProxy.atomicParam("text", false, BaseProxy.StringType.fromString(text)),
                    BaseProxy.atomicParam("type", true, BaseProxy.StringType.fromString(type)))
                .withMethod("POST")
                .responseSingle(false, Format.JSON)
                );
            }

        }

        return new NoteCreateImpl(db);
    }

  /**
   * create a note
   *
   * @param text	provides input
   * @param type	provides input
   * @return	as output
   */
    com.fasterxml.jackson.databind.JsonNode execute(String text, String type);

}
