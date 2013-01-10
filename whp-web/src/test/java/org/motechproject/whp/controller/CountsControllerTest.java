package org.motechproject.whp.controller;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.motechproject.whp.common.repository.Countable;
import org.motechproject.whp.uimodel.Count;

import static java.util.Arrays.asList;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;
import static org.springframework.test.web.server.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.server.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.server.setup.MockMvcBuilders.standaloneSetup;

public class CountsControllerTest {

    @Mock
    private Countable allPatients;
    private CountsController countsController;
    private String reportsURL = "reportsURL";

    @Before
    public void setup() {
        initMocks(this);
        countsController = new CountsController(asList(allPatients), reportsURL);
    }

    @Test
    public void shouldAddAllCounts() throws Exception {
        when(allPatients.count()).thenReturn("1");
        standaloneSetup(countsController)
                .build()
                .perform(get("/count/all"))
                .andExpect(model().attribute("counts", asList(new Count(allPatients.getClass().getSimpleName(), "1"))))
                .andExpect(model().attribute("reportsURL", reportsURL));
    }
}
