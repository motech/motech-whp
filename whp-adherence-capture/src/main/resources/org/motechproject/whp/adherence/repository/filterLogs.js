function(doc, req) {
    start({'headers': {'Content-Type': 'application/json'}});
    send('{"rows": [');
    var i = 0;
    while (row = getRow()) {
        if(row.doc.type === 'AdherenceLog' && req.query['providers'].indexOf(row.doc.meta.PROVIDER_ID) != -1){
            var text = ((i===0)? "" : ",") + ('{"value" : "' + row.doc.meta.PROVIDER_ID + '"}')
            send(text);
        }
        i++;
    }
    send(']}');
}