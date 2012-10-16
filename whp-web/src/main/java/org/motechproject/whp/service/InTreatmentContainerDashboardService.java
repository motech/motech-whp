package org.motechproject.whp.service;

import org.motechproject.whp.container.tracking.model.ContainerTrackingRecord;
import org.motechproject.whp.container.tracking.repository.AllInTreatmentContainerTrackingRecordsImpl;
import org.motechproject.whp.mapper.ContainerTrackingDashboardRowMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Properties;

@Service
public class InTreatmentContainerDashboardService extends ContainerDashboardService {

    protected AllInTreatmentContainerTrackingRecordsImpl allInTreatmentContainerTrackingRecords;

    @Autowired
    public InTreatmentContainerDashboardService(AllInTreatmentContainerTrackingRecordsImpl allInTreatmentContainerTrackingRecords, ContainerTrackingDashboardRowMapper containerTrackingDashboardRowMapper) {
        super(containerTrackingDashboardRowMapper);
        this.allInTreatmentContainerTrackingRecords = allInTreatmentContainerTrackingRecords;
    }

    @Override
    public String entityName() {
        return "in_treatment_container_tracking_dashboard_row";
    }

    @Override
    protected List<ContainerTrackingRecord> filter(Integer rowsPerPage, Properties searchCriteria, int startIndex) {
        return allInTreatmentContainerTrackingRecords.filter(searchCriteria, startIndex, rowsPerPage);
    }

    @Override
    protected Integer count(Properties searchCriteria) {
        return allInTreatmentContainerTrackingRecords.count(searchCriteria);
    }
}
