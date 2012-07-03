package org.motechproject.whp.patient.service;

import org.motechproject.whp.patient.contract.PatientRequest;
import org.motechproject.whp.patient.domain.Patient;
import org.motechproject.whp.patient.repository.AllPatients;
import org.motechproject.whp.user.service.ProviderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static org.motechproject.whp.patient.mapper.PatientMapper.mapNewTreatmentForCategoryChange;
import static org.motechproject.whp.patient.mapper.PatientMapper.mapTreatmentForTransferIn;

@Service
public class TreatmentService {

    private AllPatients allPatients;
    private ProviderService providerService;

    @Autowired
    public TreatmentService(AllPatients allPatients, ProviderService providerService) {
        this.allPatients = allPatients;
        this.providerService = providerService;
    }

    public void openTreatment(PatientRequest patientRequest) {
        Patient patient = allPatients.findByPatientId(patientRequest.getCase_id());

        mapNewTreatmentForCategoryChange(patientRequest, patient);
        allPatients.update(patient);
    }

    public void closeTreatment(PatientRequest patientRequest) {
        Patient patient = allPatients.findByPatientId(patientRequest.getCase_id());
        patient.closeCurrentTreatment(patientRequest.getTreatment_outcome(), patientRequest.getDate_modified());
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

        mapTreatmentForTransferIn(patientRequest, patient);
        patient.reviveLatestTherapy();
        allPatients.update(patient);
    }

}
