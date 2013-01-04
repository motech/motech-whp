function(doc) {
    if (doc.type == 'Patient' && doc.currentTherapy && doc.currentTherapy.status !== "Closed") {
        if(doc.currentTherapy.currentTreatment && !doc.currentTherapy.currentTreatment.endDate)
            emit(doc.lastAdherenceWeekStartDate, doc.currentTherapy.currentTreatment.providerId);
    }
}