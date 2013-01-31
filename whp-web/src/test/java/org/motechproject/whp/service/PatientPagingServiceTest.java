package org.motechproject.whp.service;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.motechproject.paginator.contract.FilterParams;
import org.motechproject.paginator.contract.SortParams;
import org.motechproject.paginator.response.PageResults;
import org.motechproject.whp.patient.builder.PatientBuilder;
import org.motechproject.whp.patient.domain.Patient;
import org.motechproject.whp.patient.domain.PatientFilterKeys;
import org.motechproject.whp.patient.repository.AllPatients;
import org.motechproject.whp.uimodel.PatientDashboardRow;
import org.motechproject.whp.uimodel.PatientInfo;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.anyObject;
import static org.mockito.Matchers.anyString;
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
    final Patient patient1 = new PatientBuilder().withDefaults().build();
    final Patient patient2 = new PatientBuilder().withDefaults().withPatientId("patientid2").withProviderId("provider2").build();

    @Before
    public void setUp(){
        initMocks(this);
    }

    @Test
    public void shouldFetchPatientsAccordingToPageNumbersWhenEitherFilterParamIsSelected(){
        List<Patient> allPatientsList = new ArrayList<Patient>(){{
            add(patient1);
            add(patient1);
            add(patient2);
            add(patient2);
            add(patient1);
        }};

        List<Patient> patientsPerPage = new ArrayList<Patient>(){{
            add(patient2);
            add(patient2);
        }};

        filterParams = new FilterParams();
        filterParams.put(PatientFilterKeys.SelectedProvider.value(), "providerID");

        when(allPatients.getAll(anyInt(), anyInt())).thenReturn(patientsPerPage);
        when(allPatients.count()).thenReturn(String.valueOf(allPatientsList.size()));
        when(allPatients.filter((FilterParams) anyObject(), (SortParams) anyObject(), anyInt(), anyInt())).thenReturn(patientsPerPage);
        when(allPatients.getAllWithActiveTreatmentFor(anyString())).thenReturn(allPatientsList);

        PatientPagingService patientPagingService = new PatientPagingService(allPatients);
        PageResults<PatientDashboardRow> pageResults = patientPagingService.page(pageNumber, rowsPerPage, filterParams, new SortParams());

        assertThat(pageResults.getTotalRows(), is(5));
        assertThat(pageResults.getPageNo(), is(pageNumber));
        assertThat(pageResults.getResults().size(), is(2));
        assertThat(pageResults.getResults().get(0).getPatientId(), is("patientid2"));
    }

}
