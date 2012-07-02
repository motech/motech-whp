package org.motechproject.whp.treatmentcard.service;

import org.motechproject.whp.adherence.domain.Adherence;
import org.motechproject.whp.adherence.service.WHPAdherenceService;
import org.motechproject.whp.patient.domain.Patient;
import org.motechproject.whp.patient.domain.Therapy;
import org.motechproject.whp.patient.repository.AllPatients;
import org.motechproject.whp.treatmentcard.domain.TreatmentCard;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class TreatmentCardService {

    AllPatients allPatients;
    WHPAdherenceService whpAdherenceService;

    @Autowired
    public TreatmentCardService(AllPatients allPatients, WHPAdherenceService whpAdherenceService) {
        this.allPatients = allPatients;
        this.whpAdherenceService = whpAdherenceService;
    }

    public TreatmentCard treatmentCard(Patient patient) {
        Therapy therapy = patient.currentTherapy();

        TreatmentCard treatmentCard = new TreatmentCard(patient);

        if (therapy.getPhases().isOrHasBeenOnIp()) {
            List<Adherence> ipAndEipAdherenceData = whpAdherenceService.findLogsInRange(patient.getPatientId(), therapy.getId(),
                    therapy.getStartDate(), treatmentCard.ipBoxAdherenceEndDate());
            treatmentCard.initIPSection(ipAndEipAdherenceData);
        }

        if (therapy.getPhases().isOrHasBeenOnCp()) {
            List<Adherence> cpAdherenceData = whpAdherenceService.findLogsInRange(patient.getPatientId(), therapy.getId(),
                    therapy.getStartDate(), treatmentCard.ipBoxAdherenceEndDate());
            treatmentCard.initCPSection(cpAdherenceData);
        }

        return treatmentCard;
    }

}
