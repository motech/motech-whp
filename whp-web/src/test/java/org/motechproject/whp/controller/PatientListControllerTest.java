package org.motechproject.whp.controller;


import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.motechproject.whp.common.domain.District;
import org.motechproject.whp.common.domain.TreatmentWeekInstance;
import org.motechproject.whp.common.repository.AllDistricts;
import org.motechproject.whp.patient.domain.TreatmentCategory;
import org.motechproject.whp.patient.model.AlertDateFilters;
import org.motechproject.whp.patient.model.AlertTypeFilters;
import org.motechproject.whp.patient.model.FlagFilters;
import org.motechproject.whp.patient.model.ProgressFilters;
import org.motechproject.whp.patient.repository.AllTreatmentCategories;
import org.motechproject.whp.uimodel.PatientDashboardLegends;

import java.util.List;

import static java.util.Arrays.asList;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;
import static org.springframework.test.web.server.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.server.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.server.setup.MockMvcBuilders.standaloneSetup;

public class PatientListControllerTest extends BaseControllerTest {
    @Mock
    AllDistricts allDistrictsCache;
    @Mock
    AllTreatmentCategories allTreatmentCategories;
    @Mock
    List<TreatmentCategory> treatmentCategories;
    @Mock
    PatientDashboardLegends patientDashboardLegends;
    @Mock
    AlertTypeFilters alertTypes;
    @Mock
    AlertDateFilters alertDates;
    @Mock
    ProgressFilters progressFilters;
    @Mock
    private TreatmentWeekInstance treatmentWeekInstance;
    private FlagFilters flags = new FlagFilters();

    List<District> districts = asList(new District("Vaishali"), new District("Begusarai"));

    PatientListController patientListController;

    @Before
    public void setup() {
        initMocks(this);
        when(allDistrictsCache.getAll()).thenReturn(districts);
        when(allTreatmentCategories.getAll()).thenReturn(treatmentCategories);
        patientListController = new PatientListController(allDistrictsCache, allTreatmentCategories, treatmentWeekInstance, patientDashboardLegends, alertTypes, alertDates, progressFilters);
    }

    @Test
    public void shouldSetUpUiModelForListAllPatients() throws Exception {
        standaloneSetup(patientListController).build()
                .perform(get("/patients/list"))
                .andExpect(status().isOk())
                .andExpect(model().attribute("districts", districts))
                .andExpect(model().attribute("alertTypes", alertTypes))
                .andExpect(model().attribute("alertDates", alertDates))
                .andExpect(model().attribute("flags", flags))
                .andExpect(model().attribute("treatmentCategories", treatmentCategories))
                .andExpect(model().attribute("legends", patientDashboardLegends.getLegends()))
                .andExpect(model().attribute("progressFilters", progressFilters))
                .andExpect(view().name("patient/list"));
    }
}
