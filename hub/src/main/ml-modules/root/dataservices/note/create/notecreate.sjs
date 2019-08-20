'use strict';
declareUpdate(); // Note: uncomment if changing the database state

var text; // instance of xs.string
var type; // instance of xs.string?

var sem = require("/MarkLogic/semantics.xqy");

var text; // an xs.string value
var type;  // an xs.string value


var uri = sem.uuidString()+'.json';


var options={
    collections:["data","data/note"]
};

var root={};

var header= {
    createdOn: fn.currentDateTime()
}

var content= {
    text: text
}

var root= {
    header: header,
    content: content
}

xdmp.documentInsert(uri, root, options)
var returnObject={}
returnObject.root=root;
returnObject.uri=uri
returnObject;


