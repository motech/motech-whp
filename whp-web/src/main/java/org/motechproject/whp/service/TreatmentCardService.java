package org.motechproject.whp.service;

import org.joda.time.LocalDate;
import org.motechproject.whp.adherence.domain.Adherence;
import org.motechproject.whp.adherence.domain.PillStatus;
import org.motechproject.whp.adherence.service.WHPAdherenceService;
import org.motechproject.whp.contract.DailyAdherenceRequest;
import org.motechproject.whp.contract.TreatmentCardModel;
import org.motechproject.whp.contract.UpdateAdherenceRequest;
import org.motechproject.whp.patient.domain.Patient;
import org.motechproject.whp.patient.domain.Therapy;
import org.motechproject.whp.patient.domain.Treatment;
import org.motechproject.whp.patient.repository.AllPatients;
import org.motechproject.whp.refdata.domain.WHPConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
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

    public TreatmentCardModel getIntensivePhaseTreatmentCardModel(Patient patient) {
        if (patient != null && patient.latestTherapy() != null && patient.latestTherapy().getStartDate() != null) {

            TreatmentCardModel ipTreatmentCard = new TreatmentCardModel();
            Therapy latestTherapy = patient.latestTherapy();
            LocalDate ipStartDate = latestTherapy.getStartDate();

            LocalDate therapyEndDate = ipStartDate.plusMonths(5).minusDays(1);
            LocalDate lastDoseDate;

            if (therapyEndDate.isAfter(today()))
                lastDoseDate = today();
            else
                lastDoseDate = therapyEndDate;

            List<Adherence> adherenceData = whpAdherenceService.findLogsInRange(patient.getPatientId(), latestTherapy.getId(), ipStartDate, lastDoseDate);
            ipTreatmentCard.addAdherenceDataForGivenTherapy(patient, adherenceData, latestTherapy, ipStartDate, therapyEndDate);

            return ipTreatmentCard;
        }
        return null;

    }

    public void addLogsForPatient(UpdateAdherenceRequest updateAdherenceRequest, Patient patient) {
        List<Adherence> adherenceData = new ArrayList<>();

        for (DailyAdherenceRequest request : updateAdherenceRequest.getDailyAdherenceRequests()) {
            Adherence datum = new Adherence();
            datum.setPatientId(patient.getPatientId());
            datum.setTreatmentId(updateAdherenceRequest.getTherapy());
            datum.setPillDate(request.getDoseDate());
            datum.setPillStatus(PillStatus.get(request.getPillStatus()));
            adherenceData.add(datum);

            Treatment doseForTreatment = patient.getTreatmentForDateInTherapy(request.getDoseDate(), updateAdherenceRequest.getTherapy());
            if (doseForTreatment != null) {
                datum.setTbId(doseForTreatment.getTbId());
                datum.setProviderId(doseForTreatment.getProviderId());
            } else {
                datum.setTbId(WHPConstants.UNKNOWN);
                datum.setProviderId(WHPConstants.UNKNOWN);
            }
        }

        whpAdherenceService.addOrUpdateLogsByDoseDate(adherenceData, patient.getPatientId());
    }
}
