package org.motechproject.whp.patient.service;

import org.joda.time.DateTime;
import org.motechproject.whp.patient.domain.Patient;
import org.motechproject.whp.patient.domain.ProvidedTreatment;
import org.motechproject.whp.patient.domain.Provider;
import org.motechproject.whp.patient.repository.AllPatients;
import org.motechproject.whp.patient.repository.AllProviders;
import org.motechproject.whp.refdata.domain.PatientType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProviderService {

    AllProviders allProviders;
    AllPatients allPatients;

    @Autowired
    public ProviderService(AllProviders allProviders, AllPatients allPatients) {
        this.allProviders = allProviders;
        this.allPatients = allPatients;
    }

    public String createProvider(String providerId, String primaryMobile, String secondaryMobile, String tertiaryMobile, String district, DateTime lastModifiedDate) {
        Provider provider = new Provider(providerId, primaryMobile, district, lastModifiedDate);
        provider.setSecondaryMobile(secondaryMobile);
        provider.setTertiaryMobile(tertiaryMobile);
        allProviders.addOrReplace(provider);
        return provider.getId();
    }

    public void transferIn(String newProviderId, Patient patient, String tbId, String tbRegistrationNumber, DateTime dateModified) {
        ProvidedTreatment currentProvidedTreatment = patient.getCurrentProvidedTreatment();
        ProvidedTreatment newProvidedTreatment = new ProvidedTreatment(currentProvidedTreatment)
                .updateForTransferIn(
                        tbId,
                        newProviderId,
                        dateModified.toLocalDate()
                );
        newProvidedTreatment.setTbRegistrationNumber(tbRegistrationNumber);
        patient.addProvidedTreatment(newProvidedTreatment, dateModified);
        newProvidedTreatment.setPatientType(PatientType.TransferredIn);
        allPatients.update(patient);
    }
}
