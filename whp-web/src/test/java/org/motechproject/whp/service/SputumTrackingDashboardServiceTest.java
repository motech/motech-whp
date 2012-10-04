package org.motechproject.whp.service;

import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.motechproject.paginator.response.PageResults;
import org.motechproject.whp.container.dashboard.model.ContainerDashboardRow;
import org.motechproject.whp.container.dashboard.repository.AllContainerDashboardRows;
import org.motechproject.whp.container.domain.Container;
import org.motechproject.whp.refdata.domain.SputumTrackingInstance;
import org.motechproject.whp.uimodel.SputumTrackingDashboardRow;

import static java.util.Arrays.asList;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

public class SputumTrackingDashboardServiceTest {

    SputumTrackingDashboardService sputumTrackingDashboardService;
    @Mock
    private AllContainerDashboardRows allContainerDashboardRows;

    @Before
    public void setup() {
        initMocks(this);
        sputumTrackingDashboardService = new SputumTrackingDashboardService(allContainerDashboardRows);
    }

    @Test
    public void shouldReturnEntityNameToBePaginatedAsSputumTrackingDashboardRow() {
        assertEquals("sputum_tracking_dashboard_row", sputumTrackingDashboardService.entityName());
    }

    @Test
    public void shouldReturnDashboardRowsForGivenPage() {
        ContainerDashboardRow row1 = new ContainerDashboardRow();
        row1.setContainer(new Container("provider1", "containerId1", SputumTrackingInstance.PreTreatment, DateTime.now()));
        ContainerDashboardRow row2 = new ContainerDashboardRow();
        row2.setContainer(new Container("provider2", "containerId2", SputumTrackingInstance.PreTreatment, DateTime.now()));
        when(allContainerDashboardRows.getAllPretreatmentContainerDashboardRows(0, 1)).thenReturn(asList(new ContainerDashboardRow[]{row1}));
        when(allContainerDashboardRows.getAllPretreatmentContainerDashboardRows(1, 1)).thenReturn(asList(new ContainerDashboardRow[]{row2}));

        PageResults<SputumTrackingDashboardRow> sputumTrackingDashboardRowPageResults1 = sputumTrackingDashboardService.page(1, 1, null);
        PageResults<SputumTrackingDashboardRow> sputumTrackingDashboardRowPageResults2 = sputumTrackingDashboardService.page(2, 1, null);

        verify(allContainerDashboardRows).getAllPretreatmentContainerDashboardRows(0, 1);
        verify(allContainerDashboardRows).getAllPretreatmentContainerDashboardRows(0, 1);
        assertEquals(1, sputumTrackingDashboardRowPageResults1.getResults().size());
        assertEquals("containerId1", sputumTrackingDashboardRowPageResults1.getResults().get(0).getContainerId());
        assertEquals("containerId2", sputumTrackingDashboardRowPageResults2.getResults().get(0).getContainerId());
    }
}
