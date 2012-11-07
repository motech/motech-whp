package org.motechproject.whp.service;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.motechproject.paginator.response.PageResults;
import org.motechproject.whp.container.builder.ContainerBuilder;
import org.motechproject.whp.container.domain.Container;
import org.motechproject.whp.container.repository.AllContainerTrackingRecords;
import org.motechproject.whp.mapper.ContainerTrackingDashboardRowMapper;
import org.motechproject.whp.uimodel.ContainerTrackingDashboardRow;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;
import static org.motechproject.whp.common.domain.RegistrationInstance.PreTreatment;

public class PreTreatmentTestPart {

    PreTreatmentContainerDashboardService preTreatmentContainerDashboardService;
    @Mock
    private AllContainerTrackingRecords allContainerTrackingRecords;
    @Mock
    private ContainerTrackingDashboardRowMapper containerTrackingDashboardRowMapper;

    @Before
    public void setup() {
        initMocks(this);
        preTreatmentContainerDashboardService = new PreTreatmentContainerDashboardService(allContainerTrackingRecords, containerTrackingDashboardRowMapper);
    }

    @Test
    public void shouldReturnEntityNameToBePaginatedAsSputumTrackingDashboardRow() {
        assertEquals("pre_treatment_container_tracking_dashboard_row", preTreatmentContainerDashboardService.entityName());
    }

    @Test
    public void shouldFilterContainerTrackingRecordsForGivenFilterCriteria() {
        Container container1 = new ContainerBuilder().withDefaults().withInstance(PreTreatment).build();
        Container container2 = new ContainerBuilder().withDefaults().withInstance(PreTreatment).build();
        List<Container> results = new ArrayList<>();
        results.add(container1);
        results.add(container2);

        ContainerTrackingDashboardRow expectedPageResult1 = new ContainerTrackingDashboardRow();
        ContainerTrackingDashboardRow expectedPageResult2 = new ContainerTrackingDashboardRow();
        when(containerTrackingDashboardRowMapper.mapFrom(container1)).thenReturn(expectedPageResult1);
        when(containerTrackingDashboardRowMapper.mapFrom(container2)).thenReturn(expectedPageResult2);

        Properties filterParams = new Properties();
        Properties sortParams = new Properties();
        int skip = 0;
        int limit = 10;

        when(allContainerTrackingRecords.count(PreTreatment, filterParams)).thenReturn(2);
        when(allContainerTrackingRecords.filter(PreTreatment, filterParams, sortParams, skip, limit)).thenReturn(results);

        PageResults<ContainerTrackingDashboardRow> pageResults = preTreatmentContainerDashboardService.page(1, limit, filterParams, sortParams);

        verify(allContainerTrackingRecords).filter(PreTreatment, filterParams, sortParams, skip, limit);
        verify(allContainerTrackingRecords).count(PreTreatment, filterParams);

        assertEquals(new Integer(2), pageResults.getTotalRows());
        assertEquals(2, pageResults.getResults().size());
        assertEquals(expectedPageResult1, pageResults.getResults().get(0));
        assertEquals(expectedPageResult2, pageResults.getResults().get(1));
    }

}
