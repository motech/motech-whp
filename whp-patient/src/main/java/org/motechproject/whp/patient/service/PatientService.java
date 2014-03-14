package org.motechproject.whp.patient.service;

import org.joda.time.LocalDate;
import org.motechproject.event.MotechEvent;
import org.motechproject.event.annotations.MotechListener;
import org.motechproject.whp.common.event.EventKeys;
import org.motechproject.whp.patient.alerts.scheduler.PatientAlertScheduler;
import org.motechproject.whp.patient.alerts.service.PatientAlertService;
import org.motechproject.whp.patient.contract.PatientRequest;
import org.motechproject.whp.patient.domain.Patient;
import org.motechproject.whp.patient.domain.Therapy;
import org.motechproject.whp.patient.domain.TherapyRemark;
import org.motechproject.whp.patient.mapper.PatientMapper;
import org.motechproject.whp.patient.reporting.PatientReportingService;
import org.motechproject.whp.patient.repository.AllPatients;
import org.motechproject.whp.patient.repository.AllTherapyRemarks;
import org.motechproject.whp.user.domain.Provider;
import org.motechproject.whp.user.domain.ProviderIds;
import org.motechproject.whp.user.service.ProviderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PatientService {

    private AllPatients allPatients;
    private PatientMapper patientMapper;
    private AllTherapyRemarks allTherapyRemarks;
    private ProviderService providerService;
    private PatientAlertService patientAlertService;
    private PatientAlertScheduler patientAlertScheduler;
    private PatientReportingService patientReportingService;

    @Autowired
    public PatientService(AllPatients allPatients, PatientMapper patientMapper,
                          AllTherapyRemarks allTherapyRemarks,
                          ProviderService providerService,
                          PatientAlertService patientAlertService,
                          PatientAlertScheduler patientAlertScheduler,
                          PatientReportingService patientReportingService) {
        this.allPatients = allPatients;
        this.patientMapper = patientMapper;
        this.allTherapyRemarks = allTherapyRemarks;
        this.providerService = providerService;
        this.patientAlertService = patientAlertService;
        this.patientAlertScheduler = patientAlertScheduler;
        this.patientReportingService = patientReportingService;
    }

    public void createPatient(PatientRequest patientRequest) {
        Patient patient = patientMapper.mapPatient(patientRequest);
        allPatients.add(patient);
        patientAlertScheduler.scheduleJob(patient.getPatientId());
        patientReportingService.reportPatient(patient);
    }

    public List<Patient> getAllWithActiveTreatmentForProvider(String providerId) {
        return allPatients.getAllWithActiveTreatmentFor(providerId);
    }

    public Patient findByPatientId(String patientId) {
        return allPatients.findByPatientId(patientId);
    }

    public void update(Patient updatedPatient) {
        patientAlertService.processAllAlerts(updatedPatient);
        allPatients.update(updatedPatient);
        patientReportingService.reportPatient(updatedPatient);
    }

    public void updateBasedOnAlertConfiguration(Patient patient) {
        patientAlertService.processAlertsBasedOnConfiguration(patient);
        allPatients.update(patient);
        patientReportingService.reportPatient(patient);
    }

    public void addRemark(Patient patient, String remark, String user) {
        Therapy therapy = patient.getCurrentTherapy();
        allTherapyRemarks.add(new TherapyRemark(patient.getPatientId(), therapy.getUid(), remark, user));
    }

    public List<TherapyRemark> getCmfAdminRemarks(Patient patient) {
        return allTherapyRemarks.findByTherapyId(patient.getCurrentTherapy().getUid());
    }

    public List<Patient> getAll() {
        return allPatients.getAll();
    }

    @MotechListener(subjects = EventKeys.PROVIDER_DISTRICT_CHANGE)
    public void handleProviderDistrictChange(MotechEvent motechEvent) {
        String providerId = (String) motechEvent.getParameters().get("0");

        Provider provider = providerService.findByProviderId(providerId);
        List<Patient> patients = getAllWithActiveTreatmentForProvider(providerId);

        for (Patient patient : patients) {
            patient.getCurrentTreatment().setProviderDistrict(provider.getDistrict());
            allPatients.update(patient);
        }
    }

    public ProviderIds providersWithActivePatients(ProviderIds providersBelongingToDistrict) {
        return allPatients.providersWithActivePatients(providersBelongingToDistrict);
    }

    public ProviderIds providersWithActivePatients() {
        return allPatients.providersWithActivePatients();
    }

    public ProviderIds getAllProvidersWithPendingAdherence(LocalDate asOf) {
        return allPatients.findAllProvidersWithoutAdherenceAsOf(asOf);
    }

    public List<String> getAllActivePatientIds() {
        return allPatients.findAllActivePatientIds();
    }

    public Boolean updateFlag(String patientId, boolean flagValue) {
        Patient patient = allPatients.findByPatientId(patientId);
        if(patient == null)
            return false;
        patient.getPatientFlag().setFlagValue(flagValue);
        allPatients.update(patient);
        return true;
    }

    public List<Patient> getAll(int pageNumber, int pageSize) {
        return allPatients.getAll(pageNumber - 1, pageSize);
    }

    public void removePatient(String patientId){
        patientAlertScheduler.unscheduleJob(patientId);
        Patient patient = findByPatientId(patientId);
        allPatients.remove(patient);
        patientReportingService.removePatient(patient);
    }
}
