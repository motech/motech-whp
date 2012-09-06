package org.motechproject.whp.patient.service;

import org.motechproject.whp.patient.contract.PatientRequest;
import org.motechproject.whp.patient.domain.Patient;
import org.motechproject.whp.patient.mapper.PatientMapper;
import org.motechproject.whp.patient.repository.AllPatients;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TreatmentService {

    private AllPatients allPatients;
    private PatientMapper patientMapper;

    @Autowired
    public TreatmentService(AllPatients allPatients, PatientMapper patientMapper) {
        this.allPatients = allPatients;
        this.patientMapper = patientMapper;
    }

    public void openTreatment(PatientRequest patientRequest) {
        Patient patient = allPatients.findByPatientId(patientRequest.getCase_id());

        patientMapper.mapNewTreatmentForCategoryChange(patientRequest, patient);
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

        patientMapper.mapTreatmentForTransferIn(patientRequest, patient);
        patient.reviveLatestTherapy();
        allPatients.update(patient);
    }

}
