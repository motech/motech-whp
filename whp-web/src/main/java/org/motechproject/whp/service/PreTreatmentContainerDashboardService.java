package org.motechproject.whp.service;

import org.apache.commons.lang.StringUtils;
import org.motechproject.paginator.response.PageResults;
import org.motechproject.paginator.service.Paging;
import org.motechproject.whp.container.tracking.model.ContainerTrackingRecord;
import org.motechproject.whp.container.tracking.repository.AllPreTreatmentContainerTrackingRecordsImpl;
import org.motechproject.whp.mapper.ContainerTrackingDashboardRowMapper;
import org.motechproject.whp.uimodel.ContainerTrackingDashboardRow;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

@Service
public class PreTreatmentContainerDashboardService extends ContainerDashboardService {
    private AllPreTreatmentContainerTrackingRecordsImpl allPreTreatmentContainerTrackingRecords;

    @Autowired
    public PreTreatmentContainerDashboardService(AllPreTreatmentContainerTrackingRecordsImpl allPreTreatmentContainerTrackingRecords, ContainerTrackingDashboardRowMapper containerTrackingDashboardRowMapper) {
        super(containerTrackingDashboardRowMapper);
        this.allPreTreatmentContainerTrackingRecords = allPreTreatmentContainerTrackingRecords;
    }

    @Override
    public String entityName() {
        return "pre_treatment_container_tracking_dashboard_row";
    }

    @Override
    protected Integer count(Properties searchCriteria) {
        return allPreTreatmentContainerTrackingRecords.count(searchCriteria);
    }

    @Override
    protected List<ContainerTrackingRecord> filter(Integer rowsPerPage, Properties searchCriteria, int startIndex) {
        return allPreTreatmentContainerTrackingRecords.filter(searchCriteria, startIndex, rowsPerPage);
    }
}
