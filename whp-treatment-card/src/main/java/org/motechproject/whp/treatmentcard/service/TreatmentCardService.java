package org.motechproject.whp.treatmentcard.service;

import org.motechproject.whp.adherence.domain.Adherence;
import org.motechproject.whp.adherence.service.WHPAdherenceService;
import org.motechproject.whp.patient.domain.Patient;
import org.motechproject.whp.patient.domain.Therapy;
import org.motechproject.whp.treatmentcard.domain.TreatmentCard;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class TreatmentCardService {

    WHPAdherenceService whpAdherenceService;

    @Autowired
    public TreatmentCardService(WHPAdherenceService whpAdherenceService) {
        this.whpAdherenceService = whpAdherenceService;
    }

    public TreatmentCard treatmentCard(Patient patient) {
        TreatmentCard treatmentCard = new TreatmentCard(patient);
        Therapy currentTherapy = patient.getCurrentTherapy();
        initializeIpAndEipSection(patient, treatmentCard,currentTherapy);
        initializeCpSection(patient, treatmentCard, currentTherapy);
        return treatmentCard;
    }

    private void initializeIpAndEipSection(Patient patient, TreatmentCard treatmentCard, Therapy therapy) {
        if (therapy.getPhases().isOrHasBeenOnIp()) {
            List<Adherence> ipAndEipAdherenceData = whpAdherenceService.findLogsInRange(
                    patient.getPatientId(),
                    therapy.getUid(),
                    therapy.getStartDate(),
                    treatmentCard.ipBoxAdherenceEndDate()
            );
            treatmentCard.initIPSection(ipAndEipAdherenceData);
        }
    }

    private void initializeCpSection(Patient patient, TreatmentCard treatmentCard, Therapy therapy) {
        if (therapy.getPhases().hasBeenOnCp()) {
            List<Adherence> cpAdherenceData = whpAdherenceService.findLogsInRange(
                    patient.getPatientId(),
                    therapy.getUid(),
                    treatmentCard.cpBoxAdherenceStartDate(),
                    treatmentCard.cpBoxAdherenceEndDate()
            );
            treatmentCard.initCPSection(cpAdherenceData);
        }
    }
}
