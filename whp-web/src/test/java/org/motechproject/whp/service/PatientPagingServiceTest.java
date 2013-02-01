package org.motechproject.whp.service;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.motechproject.paginator.contract.FilterParams;
import org.motechproject.paginator.contract.SortParams;
import org.motechproject.paginator.response.PageResults;
import org.motechproject.whp.patient.builder.PatientBuilder;
import org.motechproject.whp.patient.domain.Patient;
import org.motechproject.whp.patient.repository.AllPatients;
import org.motechproject.whp.uimodel.PatientDashboardRow;
import org.motechproject.whp.uimodel.PatientInfo;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

public class PatientPagingServiceTest {

    @Mock
    AllPatients allPatients;

    @Mock
    PatientInfo patientInfo;

    FilterParams filterParams;

    int pageNumber = 2;
    final int rowsPerPage = 2;
    final Patient patient1 = new PatientBuilder().withDefaults().withPatientId("patientid1").build();
    final Patient patient2 = new PatientBuilder().withDefaults().withPatientId("patientid2").withProviderId("provider2").build();

    @Before
    public void setUp(){
        initMocks(this);
    }

    @Test
    public void shouldFetchPatientsAccordingToPageNumbersWhenEitherFilterParamIsSelected(){
        List<Patient> patientsPerPage = new ArrayList<Patient>(){{
            add(patient1);
            add(patient2);
        }};

        int expectedCount = 10;

        filterParams = new FilterParams();
        filterParams.put("providerId", "providerID");

        when(allPatients.filter(filterParams, new SortParams(), (pageNumber - 1) * rowsPerPage, rowsPerPage)).thenReturn(patientsPerPage);
        when(allPatients.count(filterParams)).thenReturn(expectedCount);

        PatientPagingService patientPagingService = new PatientPagingService(allPatients);
        PageResults<PatientDashboardRow> pageResults = patientPagingService.page(pageNumber, rowsPerPage, filterParams, new SortParams());

        assertThat(pageResults.getTotalRows(), is(expectedCount));
        assertThat(pageResults.getPageNo(), is(pageNumber));
        assertThat(pageResults.getResults().size(), is(2));
        assertThat(pageResults.getResults().get(0).getPatientId(), is("patientid1"));
        assertThat(pageResults.getResults().get(1).getPatientId(), is("patientid2"));
    }

}
