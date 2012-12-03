function(doc) {
    if (doc.type == 'Patient' && doc.currentTherapy && doc.currentTherapy.status !== "Closed") {
        if(doc.currentTherapy.currentTreatment && !doc.currentTherapy.currentTreatment.endDate)
        emit(doc.currentTherapy.currentTreatment.providerId, doc.currentTherapy.currentTreatment.providerId);
    }
}