package org.motechproject.whp.adherence.service;

import org.motechproject.whp.adherence.domain.AdherenceSummaryByProvider;
import org.motechproject.whp.patient.model.PatientAdherenceStatus;
import org.motechproject.whp.patient.repository.AllPatients;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;


@Component
public class AdherenceDataService {

    private AllPatients allPatients;

    @Autowired
    public AdherenceDataService(AllPatients allPatients) {
        this.allPatients = allPatients;
    }

    public AdherenceSummaryByProvider getAdherenceSummary(String providerId) {
        List<PatientAdherenceStatus> patients = allPatients.getPatientAdherenceStatusesFor(providerId);
        return new AdherenceSummaryByProvider(providerId, patients);
    }
}
