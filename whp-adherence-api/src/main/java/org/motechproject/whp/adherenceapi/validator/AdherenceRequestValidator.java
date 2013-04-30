package org.motechproject.whp.adherenceapi.validator;

import org.motechproject.whp.adherenceapi.domain.ProviderId;
import org.motechproject.whp.adherenceapi.errors.AdherenceErrors;
import org.motechproject.whp.adherenceapi.errors.CallStatusRequestErrors;
import org.motechproject.whp.adherenceapi.errors.ValidationRequestErrors;
import org.motechproject.whp.patient.domain.Patient;
import org.motechproject.whp.user.domain.Provider;
import org.motechproject.whp.user.service.ProviderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class AdherenceRequestValidator {

    private ProviderService providerService;

    @Autowired
    public AdherenceRequestValidator(ProviderService providerService) {
        this.providerService = providerService;
    }

    public AdherenceErrors validatePatientProviderMapping(ProviderId providerId, Patient patient) {
        return adherenceErrors(providerId, patient);
    }

    public AdherenceErrors validateProvider(String providerId) {
        Provider provider = providerService.findByProviderId(providerId);
        return new CallStatusRequestErrors(provider != null && provider.getProviderId().equals(providerId));
    }

    private AdherenceErrors adherenceErrors(ProviderId providerId, Patient patient) {
        return new ValidationRequestErrors(
                !providerId.isEmpty(),
                (patient != null),
                isValidProviderForPatient(
                        providerId, patient
                )
        );
    }

    private boolean isValidProviderForPatient(ProviderId providerId, Patient patient) {
        return patient != null && patient.getCurrentTreatment().getProviderId().equals(providerId.value());
    }
}
