package org.motechproject.whp.patient.service;

import org.joda.time.LocalDate;
import org.motechproject.whp.common.exception.WHPErrorCode;
import org.motechproject.whp.common.validation.RequestValidator;
import org.motechproject.whp.patient.command.UpdateCommandFactory;
import org.motechproject.whp.patient.command.UpdateScope;
import org.motechproject.whp.patient.contract.PatientRequest;
import org.motechproject.whp.patient.domain.Patient;
import org.motechproject.whp.patient.domain.Therapy;
import org.motechproject.whp.patient.domain.TherapyRemark;
import org.motechproject.whp.patient.repository.AllPatients;
import org.motechproject.whp.patient.repository.AllTherapyRemarks;
import org.motechproject.whp.refdata.domain.Phase;
import org.motechproject.whp.refdata.domain.TreatmentOutcome;
import org.motechproject.whp.user.domain.Provider;
import org.motechproject.whp.user.service.ProviderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

import static org.motechproject.whp.patient.mapper.PatientMapper.mapPatient;

@Service
public class PatientService {

    private AllPatients allPatients;
    private AllTherapyRemarks allTherapyRemarks;
    private UpdateCommandFactory updateCommandFactory;
    private RequestValidator validator;
    private ProviderService providerService;

    @Autowired
    public PatientService(AllPatients allPatients,
                          AllTherapyRemarks allTherapyRemarks, UpdateCommandFactory updateCommandFactory,
                          RequestValidator validator, ProviderService providerService) {
        this.allPatients = allPatients;
        this.allTherapyRemarks = allTherapyRemarks;
        this.updateCommandFactory = updateCommandFactory;
        this.validator = validator;
        this.providerService = providerService;
    }

    public void createPatient(PatientRequest patientRequest) {
        Patient patient = mapPatient(patientRequest);
        allPatients.add(patient);
    }

    public void update(PatientRequest patientRequest) {
        UpdateScope updateScope = patientRequest.updateScope(canBeTransferred(patientRequest.getCase_id()));
        validator.validate(patientRequest, updateScope.name());
        updateCommandFactory.updateFor(updateScope).apply(patientRequest);
    }

    public void revertAutoCompleteOfLastPhase(Patient patient) {
        patient.getCurrentTherapy().getLastCompletedPhase().setEndDate(null);
        allPatients.update(patient);
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
        List<Provider> providers = providerService.fetchBy(districtName);
        List<String> providerIds = getProviderIds(providers);
        return allPatients.getAllUnderActiveTreatmentWithCurrentProviders(providerIds);
    }

    private List<String> getProviderIds(List<Provider> providers) {
        List<String> providerIds = new ArrayList<>();
        for (Provider provider : providers) {
            providerIds.add(provider.getProviderId());
        }
        return providerIds;
    }

    public void addRemark(String patientId, String remark, String user) {
        Patient patient = allPatients.findByPatientId(patientId);
        Therapy therapy = patient.getCurrentTherapy();
        allTherapyRemarks.add(new TherapyRemark(patientId, therapy.getUid(), remark, user));
    }

    boolean canBeTransferred(String patientId) {
        Patient patient = allPatients.findByPatientId(patientId);
        List<WHPErrorCode> errors = new ArrayList<>();
        if (patient == null) {
            errors.add(WHPErrorCode.CASE_ID_DOES_NOT_EXIST);
            return false;
        } else if (!patient.hasCurrentTreatment()) {
            errors.add(WHPErrorCode.NO_EXISTING_TREATMENT_FOR_CASE);
            return false;
        } else {
            return TreatmentOutcome.TransferredOut.equals(patient.getCurrentTreatment().getTreatmentOutcome());
        }
    }
}
