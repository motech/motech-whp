package org.motechproject.whp.patient.service;

import org.joda.time.DateTime;
import org.motechproject.whp.patient.domain.*;
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

    public void transferIn(String newProviderId, Patient patient, String tbId, String tbRegistrationNumber, DateTime dateModified, SmearTestResults newSmearTestResults, WeightStatistics newWeightStatistics) {
        Treatment currentTreatment = patient.getCurrentTreatment();
        Treatment newTreatment = new Treatment()
                .updateForTransferIn(
                        tbId,
                        newProviderId,
                        dateModified.toLocalDate(),
                        currentTreatment
                );

        if (!newSmearTestResults.isEmpty())
            newTreatment.setSmearTestResults(newSmearTestResults);
        if (!newWeightStatistics.isEmpty())
            newTreatment.setWeightStatistics(newWeightStatistics);

        newTreatment.setTbRegistrationNumber(tbRegistrationNumber);
        patient.addTreatment(newTreatment, dateModified);
        newTreatment.setPatientType(PatientType.TransferredIn);
        allPatients.update(patient);
    }

    public boolean hasProvider(String providerId) {
        return allProviders.findByProviderId(providerId)!=null;
    }
}
