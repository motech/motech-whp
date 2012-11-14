package org.motechproject.whp.patient.it.service;

import org.joda.time.DateTime;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.motechproject.util.DateUtil;
import org.motechproject.whp.common.domain.District;
import org.motechproject.whp.common.repository.AllDistricts;
import org.motechproject.whp.common.util.SpringIntegrationTest;
import org.motechproject.whp.patient.builder.PatientRequestBuilder;
import org.motechproject.whp.patient.contract.PatientRequest;
import org.motechproject.whp.patient.domain.Patient;
import org.motechproject.whp.patient.repository.AllPatients;
import org.motechproject.whp.patient.service.PatientService;
import org.motechproject.whp.user.contract.ProviderRequest;
import org.motechproject.whp.user.repository.AllProviders;
import org.motechproject.whp.user.service.ProviderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.motechproject.util.DateUtil.now;

@ContextConfiguration(locations = "classpath*:/applicationPatientContext.xml")
public class ProviderDistrictChangeIT  extends SpringIntegrationTest {

    @Autowired
    ProviderService providerService;

    @Autowired
    PatientService patientService;

    @Autowired
    AllPatients allPatients;

    @Autowired
    AllProviders allProviders;

    @Autowired
    private AllDistricts allDistricts;

    private District district1;
    private District muzzafarpurDistrict;

    @Before
    public void setUp() {
        district1 = new District("district");
        allDistricts.add(district1);
        muzzafarpurDistrict = new District("Muzzafarpur");
        allDistricts.add(muzzafarpurDistrict);
    }

    @Test
    public void shouldUpdatePatientWhenProviderDistrictChanges() throws InterruptedException {
        ProviderRequest providerRequest = createProviderRequest();

        providerService.registerProvider(providerRequest);

        String patientId1 = "patientId1";
        String patientId2 = "patientId2";

        createPatient(providerRequest, patientId1);
        createPatient(providerRequest, patientId2);

        //update district
        String newDistrictName = "newDistrict";
        District newDistrict = new District(newDistrictName);
        allDistricts.add(newDistrict);

        providerRequest.setDistrict(newDistrictName);
        providerService.registerProvider(providerRequest);

        Thread.sleep(1500L); //sleep until patient update job is over

        //assert that patients got the new district
        Patient patient1 = patientService.findByPatientId(patientId1);
        Patient patient2 = patientService.findByPatientId(patientId2);

        allDistricts.remove(newDistrict);
        assertThat(patient1.getCurrentTreatment().getProviderDistrict(), is(newDistrictName));
        assertThat(patient2.getCurrentTreatment().getProviderDistrict(), is(newDistrictName));
    }

    private void createPatient(ProviderRequest providerRequest, String patientId) {
        PatientRequest patientRequest = new PatientRequestBuilder().withDefaults()
                .withCaseId(patientId)
                .withProviderId(providerRequest.getProviderId())
                .withLastModifiedDate(DateUtil.newDateTime(1990, 3, 17, 4, 55, 50))
                .withPatientAge(50)
                .withTbId("elevenDigit")
                .build();

        patientService.createPatient(patientRequest);
    }

    private ProviderRequest createProviderRequest() {
        String providerId = "providerId";
        String primaryMobile = "1234567890";
        String district = "Muzzafarpur";
        DateTime now = now();

        return new ProviderRequest(providerId, district, primaryMobile, now);
    }

    @After
    public void tearDown()  {
        allPatients.removeAll();
        allProviders.removeAll();
        allDistricts.remove(district1);
        allDistricts.remove(muzzafarpurDistrict);
    }
}
