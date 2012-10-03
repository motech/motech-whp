package org.motechproject.whp.container.dashboard.service;

import org.motechproject.whp.container.dashboard.model.ContainerDashboardRow;
import org.motechproject.whp.container.dashboard.repository.AllContainerDashboardRows;
import org.motechproject.whp.container.domain.Container;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ContainerDashboardService {

    AllContainerDashboardRows allContainerDashboardRows;

    @Autowired
    public ContainerDashboardService(AllContainerDashboardRows allContainerDashboardRows) {
        this.allContainerDashboardRows = allContainerDashboardRows;
    }

    public List<ContainerDashboardRow> allContainerDashboardRows() {
        return allContainerDashboardRows.getAll();
    }

    public void createDashboardRow(Container container) {
        ContainerDashboardRow row = new ContainerDashboardRow();
        row.setContainer(container);
        allContainerDashboardRows.add(row);
    }
}
