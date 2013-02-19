package org.motechproject.whp.service;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.motechproject.paginator.contract.FilterParams;
import org.motechproject.paginator.contract.SortParams;
import org.motechproject.paginator.response.PageResults;
import org.motechproject.whp.common.domain.alerts.AlertColorConfiguration;
import org.motechproject.whp.mapper.AlertsFilterTransformer;
import org.motechproject.whp.patient.alerts.processor.CumulativeMissedDosesCalculator;
import org.motechproject.whp.patient.builder.PatientBuilder;
import org.motechproject.whp.patient.domain.Patient;
import org.motechproject.whp.patient.repository.AllPatients;
import org.motechproject.whp.uimodel.PatientDashboardRow;
import org.motechproject.whp.uimodel.PatientInfo;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.initMocks;

public class PatientPagingServiceTest {

    @Mock
    AllPatients allPatients;

    @Mock
    PatientInfo patientInfo;

    @Mock
    AlertsFilterTransformer alertsFilterTransformer;

    @Mock
    AlertColorConfiguration alertColorConfiguration;

    @Mock
    CumulativeMissedDosesCalculator cumulativeMissedDosesCalculator;

    FilterParams filterParams;

    int pageNumber = 2;
    final int rowsPerPage = 2;
    final Patient patient1 = new PatientBuilder().withDefaults().withPatientId("patientid1").build();
    final Patient patient2 = new PatientBuilder().withDefaults().withPatientId("patientid2").withProviderId("provider2").build();
    private PatientPagingService patientPagingService;

    @Before
    public void setUp(){
        initMocks(this);
        filterParams = new FilterParams();
        patientPagingService = new PatientPagingService(allPatients, alertsFilterTransformer, alertColorConfiguration, cumulativeMissedDosesCalculator);
    }

    @Test
    public void shouldFetchPatientsAccordingToPageNumbersWhenEitherFilterParamIsSelected(){
        List<Patient> patientsPerPage = new ArrayList<Patient>(){{
            add(patient1);
            add(patient2);
        }};

        int expectedCount = 10;

        filterParams.put("providerId", "providerID");

        when(alertsFilterTransformer.transform(filterParams)).thenReturn(filterParams);
        when(allPatients.filter(filterParams, new SortParams(), (pageNumber - 1) * rowsPerPage, rowsPerPage)).thenReturn(patientsPerPage);
        when(allPatients.count(filterParams)).thenReturn(expectedCount);
        when(cumulativeMissedDosesCalculator.getCumulativeMissedDoses(patient1)).thenReturn(12);
        when(cumulativeMissedDosesCalculator.getCumulativeMissedDoses(patient2)).thenReturn(13);

        PatientPagingService patientPagingService = new PatientPagingService(allPatients, alertsFilterTransformer, alertColorConfiguration, cumulativeMissedDosesCalculator);
        PageResults<PatientDashboardRow> pageResults = patientPagingService.page(pageNumber, rowsPerPage, filterParams, new SortParams());

        assertThat(pageResults.getTotalRows(), is(expectedCount));
        assertThat(pageResults.getPageNo(), is(pageNumber));
        assertThat(pageResults.getResults().size(), is(2));
        assertThat(pageResults.getResults().get(0).getPatientId(), is("patientid1"));
        assertThat(pageResults.getResults().get(1).getPatientId(), is("patientid2"));
        assertThat(pageResults.getResults().get(0).getCumulativeMissedDoses(), is(12));
        assertThat(pageResults.getResults().get(1).getCumulativeMissedDoses(), is(13));
    }

    @Test
    public void shouldPassTransformedParamToPagingService(){
        SortParams sortParams = new SortParams();
        FilterParams transformedFilterParams = mock(FilterParams.class);

        when(alertsFilterTransformer.transform(filterParams)).thenReturn(transformedFilterParams);

        patientPagingService.page(pageNumber, rowsPerPage, filterParams, sortParams);

        verify(allPatients).filter(transformedFilterParams, sortParams, 2, rowsPerPage);
        verify(allPatients).count(transformedFilterParams);
    }
}
