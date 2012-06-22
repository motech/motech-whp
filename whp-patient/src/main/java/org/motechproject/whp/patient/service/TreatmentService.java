package org.motechproject.whp.patient.service;

import org.joda.time.DateTime;
import org.motechproject.whp.patient.contract.PatientRequest;
import org.motechproject.whp.patient.domain.*;
import org.motechproject.whp.patient.repository.AllPatients;
import org.motechproject.whp.patient.repository.AllTherapies;
import org.motechproject.whp.refdata.domain.PatientType;
import org.motechproject.whp.user.service.ProviderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static org.motechproject.whp.patient.mapper.PatientMapper.createNewTreatmentForTreatmentCategoryChange;
import static org.motechproject.whp.patient.mapper.TherapyMapper.createNewTreatment;

@Service
public class TreatmentService {

    private AllPatients allPatients;
    private AllTherapies allTherapies;
    private ProviderService providerService;

    @Autowired
    public TreatmentService(AllPatients allPatients, AllTherapies allTherapies, ProviderService providerService) {
        this.allPatients = allPatients;
        this.allTherapies = allTherapies;
        this.providerService = providerService;
    }

    public void openTreatment(PatientRequest patientRequest) {
        Patient patient = allPatients.findByPatientId(patientRequest.getCase_id());

        Therapy newTherapy = createNewTreatment(patient, patientRequest);
        allTherapies.add(newTherapy);

        Treatment newTreatment = createNewTreatmentForTreatmentCategoryChange(patient, patientRequest, newTherapy);
        patient.addTreatment(newTreatment, patientRequest.getDate_modified());

        patient.setOnActiveTreatment(true);
        allPatients.update(patient);
    }

    public void closeTreatment(PatientRequest patientRequest) {
        Patient patient = allPatients.findByPatientId(patientRequest.getCase_id());
        patient.closeCurrentTreatment(patientRequest.getTreatment_outcome(), patientRequest.getDate_modified());
        patient.setOnActiveTreatment(false);
        allPatients.update(patient);
    }

    public void pauseTreatment(PatientRequest patientRequest) {
        Patient patient = allPatients.findByPatientId(patientRequest.getCase_id());
        patient.pauseCurrentTreatment(patientRequest.getReason(), patientRequest.getDate_modified());
        allPatients.update(patient);
    }

    public void restartTreatment(PatientRequest patientRequest) {
        Patient patient = allPatients.findByPatientId(patientRequest.getCase_id());
        patient.restartCurrentTreatment(patientRequest.getReason(), patientRequest.getDate_modified());
        allPatients.update(patient);
    }

    public void transferInPatient(PatientRequest patientRequest) {
        Patient patient = allPatients.findByPatientId(patientRequest.getCase_id());
        DateTime dateModified = patientRequest.getDate_modified();
        SmearTestResults newSmearTestResults = patientRequest.getSmearTestResults();
        WeightStatistics newWeightStatistics = patientRequest.getWeightStatistics();
        Treatment currentTreatment = patient.getCurrentTreatment();

        copyOverTreatment(patientRequest, patient, dateModified, newSmearTestResults, newWeightStatistics, currentTreatment);
        patient.reviveLatestTherapy();
        patient.currentTherapy().setDiseaseClass(patientRequest.getDisease_class());
        patient.setOnActiveTreatment(true);
        allPatients.update(patient);
    }

    private void copyOverTreatment(PatientRequest patientRequest, Patient patient, DateTime dateModified, SmearTestResults newSmearTestResults, WeightStatistics newWeightStatistics, Treatment currentTreatment) {
        Treatment newTreatment = new Treatment(currentTreatment)
                .updateForTransferIn(
                        patientRequest.getTb_id(),
                        patientRequest.getProvider_id(),
                        dateModified.toLocalDate()
                );
        newTreatment.setSmearTestResults(newSmearTestResults.isEmpty() ? currentTreatment.getSmearTestResults() : newSmearTestResults);
        newTreatment.setWeightStatistics(newWeightStatistics.isEmpty() ? currentTreatment.getWeightStatistics() : newWeightStatistics);
        newTreatment.setTbRegistrationNumber(patientRequest.getTb_registration_number());
        patient.addTreatment(newTreatment, dateModified);
        newTreatment.setPatientType(PatientType.TransferredIn);
    }

}
