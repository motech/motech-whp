package org.motechproject.whp.service;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.motechproject.paginator.response.PageResults;
import org.motechproject.whp.container.domain.Container;
import org.motechproject.whp.container.tracking.model.ContainerTrackingRecord;
import org.motechproject.whp.container.tracking.repository.AllContainerTrackingRecords;
import org.motechproject.whp.mapper.ContainerTrackingDashboardRowMapper;
import org.motechproject.whp.uimodel.ContainerTrackingDashboardRow;

import static java.util.Arrays.asList;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.initMocks;

public class ContainerTrackingDashboardServiceTest {

    ContainerTrackingDashboardService containerTrackingDashboardService;
    @Mock
    private AllContainerTrackingRecords allContainerTrackingRecords;
    @Mock
    private ContainerTrackingDashboardRowMapper containerTrackingDashboardRowMapper;

    @Before
    public void setup() {
        initMocks(this);
        containerTrackingDashboardService = new ContainerTrackingDashboardService(allContainerTrackingRecords, containerTrackingDashboardRowMapper);
    }

    @Test
    public void shouldReturnEntityNameToBePaginatedAsSputumTrackingDashboardRow() {
        assertEquals("container_tracking_dashboard_row", containerTrackingDashboardService.entityName());
    }

    @Test
    public void shouldReturnDashboardRowsForGivenPage() {
        ContainerTrackingRecord record1 = new ContainerTrackingRecord();
        record1.setContainer(new Container());
        ContainerTrackingDashboardRow dashboardRow1 = mock(ContainerTrackingDashboardRow.class);
        when(containerTrackingDashboardRowMapper.mapFrom(record1)).thenReturn(dashboardRow1);
        ContainerTrackingRecord record2 = new ContainerTrackingRecord();
        ContainerTrackingDashboardRow dashboardRow2 = mock(ContainerTrackingDashboardRow.class);
        when(containerTrackingDashboardRowMapper.mapFrom(record2)).thenReturn(dashboardRow2);

        when(allContainerTrackingRecords.numberOfPreTreatmentRows()).thenReturn(2);
        when(allContainerTrackingRecords.getAllPretreatmentContainerDashboardRows(0, 1)).thenReturn(asList(new ContainerTrackingRecord[]{record1}));
        when(allContainerTrackingRecords.getAllPretreatmentContainerDashboardRows(1, 1)).thenReturn(asList(new ContainerTrackingRecord[]{record2}));

        PageResults<ContainerTrackingDashboardRow> sputumTrackingDashboardRowPageResults1 = containerTrackingDashboardService.page(1, 1, null);
        PageResults<ContainerTrackingDashboardRow> sputumTrackingDashboardRowPageResults2 = containerTrackingDashboardService.page(2, 1, null);

        verify(allContainerTrackingRecords).getAllPretreatmentContainerDashboardRows(0, 1);
        verify(allContainerTrackingRecords).getAllPretreatmentContainerDashboardRows(1, 1);
        assertEquals(new Integer(2), sputumTrackingDashboardRowPageResults1.getTotalRows());
        assertEquals(new Integer(2), sputumTrackingDashboardRowPageResults2.getTotalRows());
        assertEquals(1, sputumTrackingDashboardRowPageResults1.getResults().size());
        assertEquals(1, sputumTrackingDashboardRowPageResults2.getResults().size());
        assertEquals(dashboardRow1, sputumTrackingDashboardRowPageResults1.getResults().get(0));
        assertEquals(dashboardRow2, sputumTrackingDashboardRowPageResults2.getResults().get(0));
    }

}
