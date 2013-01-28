package org.motechproject.whp.patient.service;

import org.motechproject.whp.patient.contract.PatientRequest;
import org.motechproject.whp.patient.domain.Patient;
import org.motechproject.whp.patient.mapper.PatientMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TreatmentService {

    private PatientService patientService;
    private PatientMapper patientMapper;

    @Autowired
    public TreatmentService(PatientService patientService, PatientMapper patientMapper) {
        this.patientService = patientService;
        this.patientMapper = patientMapper;
    }

    public void openTreatment(PatientRequest patientRequest) {
        Patient patient = patientService.findByPatientId(patientRequest.getCase_id());

        patientMapper.mapNewTreatmentForCategoryChange(patientRequest, patient);
        patientService.update(patient);
    }

    public void closeTreatment(PatientRequest patientRequest) {
        Patient patient = patientService.findByPatientId(patientRequest.getCase_id());
        patient.closeCurrentTreatment(patientRequest.getTreatment_outcome(), patientRequest.getDate_modified());
        patientService.update(patient);
    }

    public void pauseTreatment(PatientRequest patientRequest) {
        Patient patient = patientService.findByPatientId(patientRequest.getCase_id());
        patient.pauseCurrentTreatment(patientRequest.getReason(), patientRequest.getDate_modified());
        patientService.update(patient);
    }

    public void restartTreatment(PatientRequest patientRequest) {
        Patient patient = patientService.findByPatientId(patientRequest.getCase_id());
        patient.restartCurrentTreatment(patientRequest.getReason(), patientRequest.getDate_modified());
        patientService.update(patient);
    }

    public void transferInPatient(PatientRequest patientRequest) {
        Patient patient = patientService.findByPatientId(patientRequest.getCase_id());

        patientMapper.mapTreatmentForTransferIn(patientRequest, patient);
        patient.reviveLatestTherapy();
        patientService.update(patient);
    }
}
