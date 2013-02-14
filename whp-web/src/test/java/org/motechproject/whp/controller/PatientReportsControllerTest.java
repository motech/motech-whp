package org.motechproject.whp.controller;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.motechproject.whp.common.domain.District;
import org.motechproject.whp.common.repository.AllDistricts;

import java.util.List;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;
import static org.springframework.test.web.server.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.server.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.server.setup.MockMvcBuilders.standaloneSetup;

public class PatientReportsControllerTest {

    PatientReportsController patientReportsController;

    @Mock
    private AllDistricts allDistricts;

    @Before
    public void setUp() {
        initMocks(this);
        patientReportsController = new PatientReportsController(allDistricts);
    }

    @Test
    public void shouldForwardToPatientSummaryFilterView() throws Exception {
        List<District> districtList = mock(List.class);
        when(allDistricts.getAll()).thenReturn(districtList);

        standaloneSetup(patientReportsController).build()
                .perform(get("/patientreports/filter"))
                .andExpect(status().isOk())
                .andExpect(model().size(1))
                .andExpect(model().attribute("districts", districtList))
                .andExpect(forwardedUrl("patientreports/filter"));
    }

}
