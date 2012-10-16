package org.motechproject.whp.service;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.motechproject.paginator.response.PageResults;
import org.motechproject.whp.container.tracking.builder.ContainerTrackingRecordBuilder;
import org.motechproject.whp.container.tracking.model.ContainerTrackingRecord;
import org.motechproject.whp.container.tracking.repository.AllPreTreatmentContainerTrackingRecordsImpl;
import org.motechproject.whp.mapper.ContainerTrackingDashboardRowMapper;
import org.motechproject.whp.uimodel.ContainerTrackingDashboardRow;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

public class PreTreatmentContainerDashboardServiceTest {

    PreTreatmentContainerDashboardService preTreatmentContainerDashboardService;
    @Mock
    private AllPreTreatmentContainerTrackingRecordsImpl allPreTreatmentContainerTrackingRecords;
    @Mock
    private ContainerTrackingDashboardRowMapper containerTrackingDashboardRowMapper;

    @Before
    public void setup() {
        initMocks(this);
        preTreatmentContainerDashboardService = new PreTreatmentContainerDashboardService(allPreTreatmentContainerTrackingRecords, containerTrackingDashboardRowMapper);
    }

    @Test
    public void shouldReturnEntityNameToBePaginatedAsSputumTrackingDashboardRow() {
        assertEquals("container_tracking_dashboard_row", preTreatmentContainerDashboardService.entityName());
    }

    @Test
    public void shouldFilterContainerTrackingRecordsForGivenFilterCriteria() {
        ContainerTrackingRecord containerTrackingRecord1 = new ContainerTrackingRecordBuilder().withDefaults().build();
        ContainerTrackingRecord containerTrackingRecord2 = new ContainerTrackingRecordBuilder().withDefaults().build();
        List<ContainerTrackingRecord> results = new ArrayList<>();
        results.add(containerTrackingRecord1);
        results.add(containerTrackingRecord2);

        ContainerTrackingDashboardRow expectedPageResult1 = new ContainerTrackingDashboardRow();
        ContainerTrackingDashboardRow expectedPageResult2 = new ContainerTrackingDashboardRow();
        when(containerTrackingDashboardRowMapper.mapFrom(containerTrackingRecord1)).thenReturn(expectedPageResult1);
        when(containerTrackingDashboardRowMapper.mapFrom(containerTrackingRecord2)).thenReturn(expectedPageResult2);

        Properties filterParams = new Properties();
        int skip = 0;
        int limit = 10;

        when(allPreTreatmentContainerTrackingRecords.countPreTreatmentRecords(filterParams)).thenReturn(2);
        when(allPreTreatmentContainerTrackingRecords.filterPreTreatmentRecords(filterParams, skip, limit)).thenReturn(results);

        PageResults<ContainerTrackingDashboardRow> pageResults = preTreatmentContainerDashboardService.page(1, limit, filterParams);

        verify(allPreTreatmentContainerTrackingRecords).filterPreTreatmentRecords(filterParams, skip, limit);
        verify(allPreTreatmentContainerTrackingRecords).countPreTreatmentRecords(filterParams);

        assertEquals(new Integer(2), pageResults.getTotalRows());
        assertEquals(2, pageResults.getResults().size());
        assertEquals(expectedPageResult1, pageResults.getResults().get(0));
        assertEquals(expectedPageResult2, pageResults.getResults().get(1));
    }

}
