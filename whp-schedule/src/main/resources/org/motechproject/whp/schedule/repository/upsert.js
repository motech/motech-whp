function(doc, req){
    log(req);
    if(!doc){
        if(req.id){
            return [JSON.parse(req.body), {}];
        }else{
            return [null, {}];
        }
    }else{
        var updatedDoc = JSON.parse(req.body);
        doc.hour = updatedDoc.hour;
doc.minute = updatedDoc.minute;
doc.dayOfWeek = updatedDoc.dayOfWeek;
doc.scheduled = updatedDoc.scheduled;
return [doc, {}];
}
}