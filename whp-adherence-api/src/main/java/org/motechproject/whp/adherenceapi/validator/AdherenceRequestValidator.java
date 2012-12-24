package org.motechproject.whp.adherenceapi.validator;

import org.motechproject.whp.adherenceapi.domain.Dosage;
import org.motechproject.whp.adherenceapi.domain.ProviderId;
import org.motechproject.whp.adherenceapi.errors.AdherenceErrors;
import org.motechproject.whp.adherenceapi.errors.CallStatusRequestErrors;
import org.motechproject.whp.adherenceapi.errors.ValidationRequestErrors;
import org.motechproject.whp.adherenceapi.service.AdherenceService;
import org.motechproject.whp.patient.domain.Patient;
import org.motechproject.whp.patient.service.PatientService;
import org.motechproject.whp.user.domain.Provider;
import org.motechproject.whp.user.service.ProviderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class AdherenceRequestValidator {

    private AdherenceService adherenceService;
    private PatientService patientService;
    private ProviderService providerService;

    @Autowired
    public AdherenceRequestValidator(AdherenceService adherenceService, PatientService patientService, ProviderService providerService) {
        this.adherenceService = adherenceService;
        this.patientService = patientService;
        this.providerService = providerService;
    }

    public AdherenceErrors validatePatientProviderMapping(String patientId, ProviderId providerId) {
        Dosage dosage = adherenceService.dosageForPatient(patientId);
        return adherenceErrors(patientId, providerId, dosage);
    }

    public AdherenceErrors validateProvider(String providerId) {
        Provider provider = providerService.findByProviderId(providerId);
        return new CallStatusRequestErrors(provider != null && provider.getProviderId().equals(providerId));
    }

    private AdherenceErrors adherenceErrors(String patientId, ProviderId providerId, Dosage dosage) {
        return new ValidationRequestErrors(
                !providerId.isEmpty(),
                (dosage != null),
                isValidProviderForPatient(
                        providerId, patientId
                )
        );
    }

    private boolean isValidProviderForPatient(ProviderId providerId, String patientId) {
        Patient patient = patientService.findByPatientId(patientId);
        return patient != null && patient.getCurrentTreatment().getProviderId().equals(providerId.value());
    }
}
