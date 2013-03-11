package org.motechproject.whp.patient.service;

import org.motechproject.whp.patient.alerts.scheduler.PatientAlertScheduler;
import org.motechproject.whp.patient.contract.PatientRequest;
import org.motechproject.whp.patient.domain.Patient;
import org.motechproject.whp.patient.mapper.PatientMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TreatmentService {

    private PatientService patientService;
    private PatientMapper patientMapper;
    private PatientAlertScheduler patientAlertScheduler;

    @Autowired
    public TreatmentService(PatientService patientService, PatientMapper patientMapper, PatientAlertScheduler patientAlertScheduler) {
        this.patientService = patientService;
        this.patientMapper = patientMapper;
        this.patientAlertScheduler = patientAlertScheduler;
    }

    public void openTreatment(PatientRequest patientRequest) {
        Patient patient = patientService.findByPatientId(patientRequest.getCase_id());

        patientMapper.mapNewTreatmentForCategoryChange(patientRequest, patient);
        patientService.update(patient);
        patientAlertScheduler.scheduleJob(patient.getPatientId());
    }

    public void closeTreatment(PatientRequest patientRequest) {
        Patient patient = patientService.findByPatientId(patientRequest.getCase_id());
        patient.closeCurrentTreatment(patientRequest.getTreatment_outcome(), patientRequest.getRemarks(), patientRequest.getDate_modified());
        patientService.update(patient);
        patientAlertScheduler.unscheduleJob(patient.getPatientId());
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
        patientAlertScheduler.scheduleJob(patient.getPatientId());
    }
}
