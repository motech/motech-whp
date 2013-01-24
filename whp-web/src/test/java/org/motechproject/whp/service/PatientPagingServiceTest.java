package org.motechproject.whp.service;

import org.junit.Test;
import org.mockito.Mock;
import org.motechproject.paginator.response.PageResults;
import org.motechproject.whp.patient.builder.PatientBuilder;
import org.motechproject.whp.patient.domain.Patient;
import org.motechproject.whp.patient.repository.AllPatients;
import org.motechproject.whp.uimodel.PatientInfo;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

public class PatientPagingServiceTest {

    @Mock
    AllPatients allPatients;

    @Mock
    PatientInfo patientInfo;

    @Test
    public void shouldFetchPatientsAccordingToPageNumbers(){

        initMocks(this);
        int pageNumber = 2;
        int rowsPerPage = 2;

        final Patient patient1 = new PatientBuilder().withDefaults().build();
        final Patient patient2 = new PatientBuilder().withDefaults().build();

        List<Patient> allPatientsList = new ArrayList<Patient>(){{
            add(patient1);
            add(patient1);
            add(patient2);
            add(patient2);
            add(patient1);
        }};

        String patientId2 = "patientid2";
        patient2.setPatientId(patientId2);
        List<Patient> patientsPerPage = new ArrayList<Patient>(){{
            add(patient2);
            add(patient2);
        }};

        when(allPatients.getAll(anyInt(), anyInt())).thenReturn(patientsPerPage);
        when(allPatients.count()).thenReturn(String.valueOf(allPatientsList.size()));

        PatientPagingService patientPagingService = new PatientPagingService(allPatients);
        PageResults<PatientInfo> pageResults = patientPagingService.page(pageNumber, rowsPerPage, null, null);

        assertThat(pageResults.getTotalRows(), is(5));
        assertThat(pageResults.getPageNo(), is(pageNumber));
        assertThat(pageResults.getResults().size(), is(2));
        assertThat(pageResults.getResults().get(0).getPatientId(), is(patientId2));
    }

}
