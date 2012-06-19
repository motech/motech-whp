package org.motechproject.whp.service;

import org.joda.time.LocalDate;
import org.joda.time.Period;
import org.motechproject.adherence.contract.AdherenceData;
import org.motechproject.adherence.repository.AllAdherenceLogs;
import org.motechproject.adherence.service.AdherenceService;
import org.motechproject.whp.adherence.domain.AdherenceConstants;
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

@Component
public class TreatmentCardService {
    AllAdherenceLogs allAdherenceLogs;
    AllPatients allPatients;
    AdherenceService adherenceService;

    @Autowired
    public TreatmentCardService(AllAdherenceLogs allAdherenceLogs, AdherenceService adherenceService, AllPatients allPatients) {
        this.allAdherenceLogs = allAdherenceLogs;
        this.adherenceService = adherenceService;
        this.allPatients = allPatients;
    }

    public TreatmentCardModel getIntensivePhaseTreatmentCardModel(Patient patient) {
        if (patient != null && patient.latestTherapy() != null && patient.latestTherapy().getStartDate() != null) {

            TreatmentCardModel ipTreatmentCard = new TreatmentCardModel();
            Therapy latestTherapy = patient.latestTherapy();
            LocalDate ipStartDate = latestTherapy.getStartDate();
            LocalDate endDate = ipStartDate.plusMonths(5);

            List<AdherenceData> adherenceData = allAdherenceLogs.findLogsInRange(patient.getPatientId(), latestTherapy.getId(), ipStartDate, endDate);
            Period period = new Period().withMonths(5);
            ipTreatmentCard.addAdherenceDataForGivenTherapy(patient, adherenceData, latestTherapy, period);

            return ipTreatmentCard;
        }
        return null;
    }

    public void addLogsForPatient(UpdateAdherenceRequest updateAdherenceRequest, Patient patient) {
        List<AdherenceData> adherenceData = new ArrayList();

        for (DailyAdherenceRequest request : updateAdherenceRequest.getDailyAdherenceRequests()) {
            AdherenceData datum = new AdherenceData(patient.getPatientId(), updateAdherenceRequest.getTherapy(), request.getDoseDate());
            datum.status(request.getPillStatus());
            adherenceData.add(datum);

            Treatment doseForTreatment = patient.getTreatmentForDateInTherapy(request.getDoseDate(), updateAdherenceRequest.getTherapy());
            if (doseForTreatment != null) {
                datum.addMeta(AdherenceConstants.TB_ID, doseForTreatment.getTbId());
                datum.addMeta(AdherenceConstants.PROVIDER_ID, doseForTreatment.getProviderId());
            }
            else {
                datum.addMeta(AdherenceConstants.TB_ID, WHPConstants.UNKNOWN);
                datum.addMeta(AdherenceConstants.PROVIDER_ID, WHPConstants.UNKNOWN);
            }
        }

        adherenceService.addOrUpdateLogsByDosedate(adherenceData, patient.getPatientId());
    }
}