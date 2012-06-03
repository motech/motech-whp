package org.motechproject.whp.patient.service;

import org.joda.time.DateTime;
import org.junit.After;
import org.junit.Test;
import org.motechproject.whp.patient.builder.PatientRequestBuilder;
import org.motechproject.whp.patient.contract.PatientRequest;
import org.motechproject.whp.patient.domain.Patient;
import org.motechproject.whp.patient.domain.Provider;
import org.motechproject.whp.patient.repository.AllPatients;
import org.motechproject.whp.patient.repository.AllProviders;
import org.motechproject.whp.patient.repository.SpringIntegrationTest;
import org.motechproject.whp.refdata.domain.PatientType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;

import static junit.framework.Assert.assertEquals;
import static org.motechproject.util.DateUtil.now;

@ContextConfiguration(locations = "classpath*:/applicationPatientContext.xml")
public class ProviderServiceTest extends SpringIntegrationTest {

    @Autowired
    ProviderService providerService;
    @Autowired
    PatientService patientService;
    @Autowired
    AllPatients allPatients;
    @Autowired
    AllProviders allProviders;

    @After
    public void tearDown() {
        markForDeletion(allPatients.getAll().toArray());
        markForDeletion(allProviders.getAll().toArray());
    }

    @Test
    public void shouldCreateProvider() {
        String providerId = "providerId";
        String primaryMobile = "1234567890";
        String secondaryMobile = "0987654321";
        String tertiaryMobile = "1111111111";
        String district = "Muzzafarpur";
        DateTime now = now();

        providerService.createProvider(providerId, primaryMobile, secondaryMobile, tertiaryMobile, district, now);

        Provider provider = allProviders.findByProviderId(providerId);
        
        assertEquals(providerId.toLowerCase(), provider.getProviderId());
        assertEquals(primaryMobile, provider.getPrimaryMobile());
        assertEquals(secondaryMobile, provider.getSecondaryMobile());
        assertEquals(tertiaryMobile, provider.getTertiaryMobile());
        assertEquals(district, provider.getDistrict());
        assertEquals(now, provider.getLastModifiedDate());
    }

    @Test
    public void shouldTransferInPatient() {

        PatientRequest patientRequest = new PatientRequestBuilder().withDefaults().build();
        patientService.createPatient(patientRequest);

        Patient patient = allPatients.findByPatientId(patientRequest.getCase_id());

        String newProviderId = "newProviderId";
        String tbId = "newTbId";
        String newTbRegistrationNumber = "newTbRegistrationNumber";
        DateTime now = now();

        providerService.transferIn(newProviderId, patient, tbId, newTbRegistrationNumber, now);

        Patient updatedPatient = allPatients.findByPatientId(patient.getPatientId());

        assertEquals(newProviderId.toLowerCase(), updatedPatient.getCurrentTreatment().getProviderId());
        assertEquals(tbId.toLowerCase(), updatedPatient.getCurrentTreatment().getTbId());
        assertEquals(newTbRegistrationNumber, updatedPatient.getCurrentTreatment().getTbRegistrationNumber());
        assertEquals(now.toLocalDate(), updatedPatient.getCurrentTreatment().getStartDate());
        assertEquals(now, updatedPatient.getLastModifiedDate());
        assertEquals(PatientType.TransferredIn, updatedPatient.getCurrentTreatment().getPatientType());
    }

}
