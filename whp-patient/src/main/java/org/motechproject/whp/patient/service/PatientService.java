package org.motechproject.whp.patient.service;

import org.joda.time.LocalDate;
import org.motechproject.event.MotechEvent;
import org.motechproject.event.annotations.MotechListener;
import org.motechproject.whp.common.domain.District;
import org.motechproject.whp.common.event.EventKeys;
import org.motechproject.whp.common.exception.WHPErrorCode;
import org.motechproject.whp.common.exception.WHPRuntimeException;
import org.motechproject.whp.common.repository.AllDistricts;
import org.motechproject.whp.common.validation.RequestValidator;
import org.motechproject.whp.patient.command.UpdateCommandFactory;
import org.motechproject.whp.patient.command.UpdateScope;
import org.motechproject.whp.patient.contract.PatientRequest;
import org.motechproject.whp.patient.domain.Patient;
import org.motechproject.whp.patient.domain.Therapy;
import org.motechproject.whp.patient.domain.TherapyRemark;
import org.motechproject.whp.patient.domain.TreatmentOutcome;
import org.motechproject.whp.patient.mapper.PatientMapper;
import org.motechproject.whp.patient.repository.AllPatients;
import org.motechproject.whp.patient.repository.AllTherapyRemarks;
import org.motechproject.whp.user.domain.Provider;
import org.motechproject.whp.user.domain.ProviderIds;
import org.motechproject.whp.user.service.ProviderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class PatientService {

    private AllPatients allPatients;
    private PatientMapper patientMapper;
    private AllTherapyRemarks allTherapyRemarks;
    private UpdateCommandFactory updateCommandFactory;
    private RequestValidator validator;
    private ProviderService providerService;
    private AllDistricts allDistricts;

    @Autowired
    public PatientService(AllPatients allPatients, PatientMapper patientMapper,
                          AllTherapyRemarks allTherapyRemarks, UpdateCommandFactory updateCommandFactory,
                          RequestValidator validator, ProviderService providerService, AllDistricts allDistricts) {
        this.allPatients = allPatients;
        this.patientMapper = patientMapper;
        this.allTherapyRemarks = allTherapyRemarks;
        this.updateCommandFactory = updateCommandFactory;
        this.validator = validator;
        this.providerService = providerService;
        this.allDistricts = allDistricts;
    }

    public void createPatient(PatientRequest patientRequest) {
        validateDistrict(patientRequest);

        Patient patient = patientMapper.mapPatient(patientRequest);
        allPatients.add(patient);
    }

    public void update(PatientRequest patientRequest) {
        UpdateScope updateScope = patientRequest.updateScope(canBeTransferred(patientRequest.getCase_id()));
        validator.validate(patientRequest, updateScope.name());
        updateCommandFactory.updateFor(updateScope).apply(patientRequest);
    }

    public List<Patient> getAllWithActiveTreatmentForProvider(String providerId) {
        return allPatients.getAllWithActiveTreatmentFor(providerId);
    }

    public Patient findByPatientId(String patientId) {
        return allPatients.findByPatientId(patientId);
    }

    public void update(Patient updatedPatient) {
        allPatients.update(updatedPatient);
    }

    public List<Patient> searchBy(String districtName) {
        return allPatients.getAllUnderActiveTreatmentInDistrict(districtName);
    }

    public void addRemark(Patient patient, String remark, String user) {
        Therapy therapy = patient.getCurrentTherapy();
        allTherapyRemarks.add(new TherapyRemark(patient.getPatientId(), therapy.getUid(), remark, user));
    }

    public List<TherapyRemark> getCmfAdminRemarks(Patient patient) {
        return allTherapyRemarks.findByTherapyId(patient.getCurrentTherapy().getUid());
    }

    public boolean canBeTransferred(String patientId) {
        Patient patient = allPatients.findByPatientId(patientId);
        List<WHPErrorCode> errors = new ArrayList<>();
        if (patient == null) {
            errors.add(WHPErrorCode.INVALID_PATIENT_CASE_ID);
            return false;
        } else if (!patient.hasCurrentTreatment()) {
            errors.add(WHPErrorCode.NO_EXISTING_TREATMENT_FOR_CASE);
            return false;
        } else {
            return TreatmentOutcome.TransferredOut.equals(patient.getCurrentTreatment().getTreatmentOutcome());
        }
    }

    public List<Patient> getAll() {
        return allPatients.getAll();
    }

    public List<Patient> getAll(int pageNumber, int pageSize) {
        return allPatients.getAll(pageNumber, pageSize);
    }

    @MotechListener(subjects = EventKeys.PROVIDER_DISTRICT_CHANGE)
    public void handleProviderDistrictChange(MotechEvent motechEvent) {
        String providerId = (String) motechEvent.getParameters().get("0");

        Provider provider = providerService.findByProviderId(providerId);
        List<Patient> patients = getAllWithActiveTreatmentForProvider(providerId);

        for (Patient patient : patients) {
            patient.getCurrentTreatment().setProviderDistrict(provider.getDistrict());
            update(patient);
        }
    }

    public ProviderIds providersWithActivePatients(ProviderIds providersBelongingToDistrict) {
        return allPatients.providersWithActivePatients(providersBelongingToDistrict);
    }

    public ProviderIds providersWithActivePatients() {
        return allPatients.providersWithActivePatients();
    }

    private void validateDistrict(PatientRequest patientRequest) {
        District district = allDistricts.findByName(patientRequest.getAddress().getAddress_district());
        if (district == null)
            throw new WHPRuntimeException(WHPErrorCode.INVALID_DISTRICT);
    }

    public ProviderIds getAllProvidersWithPendingAdherence(LocalDate asOf) {
        return allPatients.findAllProvidersWithoutAdherenceAsOf(asOf);
    }
}
