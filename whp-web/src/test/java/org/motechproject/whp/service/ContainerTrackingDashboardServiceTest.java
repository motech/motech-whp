package org.motechproject.whp.service;

import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.motechproject.paginator.response.PageResults;
import org.motechproject.whp.container.domain.Container;
import org.motechproject.whp.container.tracking.model.ContainerTrackingRecord;
import org.motechproject.whp.container.tracking.repository.AllContainerTrackingRecords;
import org.motechproject.whp.refdata.domain.SputumTrackingInstance;
import org.motechproject.whp.uimodel.ContainerTrackingDashboardRow;

import static java.util.Arrays.asList;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

public class ContainerTrackingDashboardServiceTest {

    ContainerTrackingDashboardService containerTrackingDashboardService;
    @Mock
    private AllContainerTrackingRecords allContainerTrackingRecords;

    @Before
    public void setup() {
        initMocks(this);
        containerTrackingDashboardService = new ContainerTrackingDashboardService(allContainerTrackingRecords);
    }

    @Test
    public void shouldReturnEntityNameToBePaginatedAsSputumTrackingDashboardRow() {
        assertEquals("container_tracking_dashboard_row", containerTrackingDashboardService.entityName());
    }

    @Test
    public void shouldReturnDashboardRowsForGivenPage() {
        ContainerTrackingRecord row1 = new ContainerTrackingRecord();
        row1.setContainer(new Container("provider1", "containerId1", SputumTrackingInstance.PreTreatment, DateTime.now()));
        ContainerTrackingRecord row2 = new ContainerTrackingRecord();
        row2.setContainer(new Container("provider2", "containerId2", SputumTrackingInstance.PreTreatment, DateTime.now()));
        when(allContainerTrackingRecords.numberOfPreTreatmentRows()).thenReturn(2);
        when(allContainerTrackingRecords.getAllPretreatmentContainerDashboardRows(0, 1)).thenReturn(asList(new ContainerTrackingRecord[]{row1}));
        when(allContainerTrackingRecords.getAllPretreatmentContainerDashboardRows(1, 1)).thenReturn(asList(new ContainerTrackingRecord[]{row2}));

        PageResults<ContainerTrackingDashboardRow> sputumTrackingDashboardRowPageResults1 = containerTrackingDashboardService.page(1, 1, null);
        PageResults<ContainerTrackingDashboardRow> sputumTrackingDashboardRowPageResults2 = containerTrackingDashboardService.page(2, 1, null);

        verify(allContainerTrackingRecords).getAllPretreatmentContainerDashboardRows(0, 1);
        verify(allContainerTrackingRecords).getAllPretreatmentContainerDashboardRows(0, 1);
        assertEquals(new Integer(2), sputumTrackingDashboardRowPageResults1.getTotalRows());
        assertEquals(new Integer(2), sputumTrackingDashboardRowPageResults2.getTotalRows());
        assertEquals(1, sputumTrackingDashboardRowPageResults1.getResults().size());
        assertEquals(1, sputumTrackingDashboardRowPageResults2.getResults().size());
        assertEquals("containerId1", sputumTrackingDashboardRowPageResults1.getResults().get(0).getContainerId());
        assertEquals("containerId2", sputumTrackingDashboardRowPageResults2.getResults().get(0).getContainerId());
    }

}
