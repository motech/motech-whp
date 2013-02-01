package org.motechproject.whp.it;

import org.junit.Before;
import org.junit.Ignore;
import org.motechproject.whp.common.domain.District;
import org.motechproject.whp.common.repository.AllDistricts;
import org.motechproject.whp.common.util.SpringIntegrationTest;
import org.motechproject.whp.patient.builder.PatientBuilder;
import org.motechproject.whp.patient.repository.AllPatients;
import org.motechproject.whp.patient.service.PatientService;
import org.motechproject.whp.service.PatientPagingService;
import org.motechproject.whp.user.builder.ProviderBuilder;
import org.motechproject.whp.user.repository.AllProviders;
import org.motechproject.whp.user.service.ProviderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;

@ContextConfiguration(locations = "classpath:/META-INF/spring/applicationContext.xml")
@Ignore
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



}
