package org.motechproject.whp.treatmentcard.service;

import org.motechproject.whp.adherence.domain.Adherence;
import org.motechproject.whp.adherence.service.WHPAdherenceService;
import org.motechproject.whp.patient.domain.Patient;
import org.motechproject.whp.patient.repository.AllPatients;
import org.motechproject.whp.treatmentcard.domain.TreatmentCard;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

import static org.motechproject.util.DateUtil.today;

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
        TreatmentCard treatmentCard = new TreatmentCard(patient);
        List<Adherence> ipAdherenceData = whpAdherenceService.findLogsInRange(patient.getPatientId(), patient.currentTherapy().getId(), patient.currentTherapy().getStartDate(), today());
        return treatmentCard.initIPSection(ipAdherenceData);
    }

}
