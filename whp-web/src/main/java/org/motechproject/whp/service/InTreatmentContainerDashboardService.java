package org.motechproject.whp.service;

import org.motechproject.whp.common.domain.RegistrationInstance;
import org.motechproject.whp.container.repository.AllContainerTrackingRecords;
import org.motechproject.whp.mapper.ContainerTrackingDashboardRowMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static org.motechproject.whp.common.domain.RegistrationInstance.InTreatment;

@Service
public class InTreatmentContainerDashboardService extends ContainerDashboardService {

    @Override
    protected RegistrationInstance getSupportedInstance() {
        return InTreatment;
    }

    @Autowired
    public InTreatmentContainerDashboardService(AllContainerTrackingRecords allContainerTrackingRecords, ContainerTrackingDashboardRowMapper containerTrackingDashboardRowMapper) {
        super(allContainerTrackingRecords, containerTrackingDashboardRowMapper);
    }

    @Override
    public String entityName() {
        return "in_treatment_container_tracking_dashboard_row";
    }
}
