package org.motechproject.whp.container.dashboard.service;

import org.motechproject.whp.container.dashboard.model.ContainerDashboardRow;
import org.motechproject.whp.container.dashboard.repository.AllContainerDashboardRows;
import org.motechproject.whp.container.domain.Container;
import org.motechproject.whp.user.domain.Provider;
import org.motechproject.whp.user.repository.AllProviders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import static org.apache.commons.lang.StringUtils.isNotBlank;

@Service
public class ContainerDashboardService {

    AllContainerDashboardRows allContainerDashboardRows;
    AllProviders allProviders;

    @Autowired
    public ContainerDashboardService(AllContainerDashboardRows allContainerDashboardRows, AllProviders allProviders) {
        this.allContainerDashboardRows = allContainerDashboardRows;
        this.allProviders = allProviders;
    }

    public List<ContainerDashboardRow> allContainerDashboardRows() {
        return allContainerDashboardRows.getAll();
    }

    public void createDashboardRow(Container container) {
        ContainerDashboardRow row = new ContainerDashboardRow();
        row.setContainer(container);
        row.setProvider(provider(container));

        allContainerDashboardRows.add(row);
    }

    private Provider provider(Container container) {
        if (isNotBlank(container.getProviderId())) {
            return allProviders.findByProviderId(container.getProviderId());
        } else {
            return null;
        }
    }
}
