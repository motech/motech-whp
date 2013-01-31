package org.motechproject.whp.it;

import org.hamcrest.MatcherAssert;
import org.hamcrest.core.Is;
import org.junit.Before;
import org.junit.Test;
import org.motechproject.paginator.contract.FilterParams;
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
    public void shouldFetchTotalRowsOfPatientsFetchAfterFilteringDistrictAndProviderId(){
        Patient patient1 = new PatientBuilder().withDefaults().withPatientId("patientId1").build();
        Patient patient2 = new PatientBuilder().withDefaults().withPatientId("patientId2").build();
        Patient patient3 = new PatientBuilder().withDefaults().withPatientId("patientId3").withProviderId("DontPickMe").build();
        allPatients.add(patient1);
        allPatients.add(patient2);
        allPatients.add(patient3);

        FilterParams filterParams = new FilterParams();
        filterParams.put("selectedDistrict", patient1.getCurrentTreatment().getProviderDistrict());
        filterParams.put("selectedProvider", patient1.getCurrentTreatment().getProviderId());

        Integer countsPatientsAfterFiltering = patientPagingService.countRowsReturnedAfterFiltering(filterParams);


        MatcherAssert.assertThat(countsPatientsAfterFiltering, Is.is(2));
    }

    @Test
    public void shouldFetchTotalRowsOfPatientsFetchAfterFilteringDistrictAndProviderIdWhenDisctrictIsNull(){
        Patient patient1 = new PatientBuilder().withDefaults().withPatientId("patientId1").build();
        Patient patient2 = new PatientBuilder().withDefaults().withPatientId("patientId2").build();
        Patient patient3 = new PatientBuilder().withDefaults().withPatientId("patientId3").withProviderId("DontPickMe").build();
        allPatients.add(patient1);
        allPatients.add(patient2);
        allPatients.add(patient3);

        FilterParams filterParams = new FilterParams();
        filterParams.put("selectedProvider", patient3.getCurrentTreatment().getProviderId());

        Integer countsPatientsAfterFiltering = patientPagingService.countRowsReturnedAfterFiltering(filterParams);

        MatcherAssert.assertThat(countsPatientsAfterFiltering, Is.is(1));
    }

    @Test
    public void shouldFetchZeroTotalRowsWhenProviderIdAndDistrictIdIsNull(){
        Patient patient1 = new PatientBuilder().withDefaults().withPatientId("patientId1").build();
        Patient patient3 = new PatientBuilder().withDefaults().withPatientId("patientId3").withProviderId("DontPickMe").build();
        allPatients.add(patient1);
        allPatients.add(patient3);

        FilterParams filterParams = new FilterParams();
        Integer countsPatientsAfterFiltering = patientPagingService.countRowsReturnedAfterFiltering(filterParams);

        MatcherAssert.assertThat(countsPatientsAfterFiltering, Is.is(0));
    }


}
