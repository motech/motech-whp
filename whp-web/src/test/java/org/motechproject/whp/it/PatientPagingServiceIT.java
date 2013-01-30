package org.motechproject.whp.it;

import org.junit.Before;
import org.junit.Test;
import org.motechproject.util.DateUtil;
import org.motechproject.whp.common.domain.District;
import org.motechproject.whp.common.repository.AllDistricts;
import org.motechproject.whp.common.util.SpringIntegrationTest;
import org.motechproject.whp.patient.builder.PatientBuilder;
import org.motechproject.whp.patient.builder.PatientRequestBuilder;
import org.motechproject.whp.patient.contract.PatientRequest;
import org.motechproject.whp.patient.domain.Patient;
import org.motechproject.whp.patient.repository.AllPatients;
import org.motechproject.whp.patient.service.PatientService;
import org.motechproject.whp.service.PatientPagingService;
import org.motechproject.whp.user.builder.ProviderBuilder;
import org.motechproject.whp.user.repository.AllProviders;
import org.motechproject.whp.user.service.ProviderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;

import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

@ContextConfiguration(locations = "classpath:/META-INF/spring/applicationContext.xml")
public class PatientPagingServiceIT extends SpringIntegrationTest {

    public static final String PROVIDER_DISTRICT = "district";

    @Autowired
    PatientPagingService patientPagingService;

    @Autowired
    PatientService patientService;

    @Autowired
    ProviderService providerService;

    @Autowired
    AllProviders allProviders;

    @Autowired
    AllPatients allPatients;

    @Autowired
    AllDistricts allDistricts;

    District district;

    @Before
    public void setUp() {
        super.before();
        allPatients.removeAll();
        allProviders.add(new ProviderBuilder()
                .withDefaults()
                .withProviderId(PatientBuilder.PROVIDER_ID)
                .withDistrict(PROVIDER_DISTRICT)
                .build());

        district = new District("testDistrict");
        allDistricts.add(district);
    }

    @Test
    public void shouldFetchAllPatients(){

        PatientRequest patientRequest = new PatientRequestBuilder().withDefaults()
                .withCaseId("patientId1")
                .withLastModifiedDate(DateUtil.newDateTime(1990, 3, 17, 4, 55, 50))
                .withPatientAge(50)
                .withAddressDistrict("testDistrict")
                .withTbId("elevenDigit")
                .build();
        patientService.createPatient(patientRequest);

        List<Patient> patientList = patientPagingService.getAll();

        assertThat(patientList.size(), is(1));
    }

}
