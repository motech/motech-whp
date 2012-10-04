package org.motechproject.whp.container.dashboard.service;

import org.apache.commons.collections.CollectionUtils;
import org.motechproject.whp.container.dashboard.model.ContainerDashboardRow;
import org.motechproject.whp.container.dashboard.repository.AllContainerDashboardRows;
import org.motechproject.whp.container.domain.Container;
import org.motechproject.whp.patient.domain.Patient;
import org.motechproject.whp.patient.repository.AllPatients;
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
    AllPatients allPatients;

    @Autowired
    public ContainerDashboardService(AllContainerDashboardRows allContainerDashboardRows, AllProviders allProviders, AllPatients allPatients) {
        this.allContainerDashboardRows = allContainerDashboardRows;
        this.allProviders = allProviders;
        this.allPatients = allPatients;
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

    public void updateDashboardRow(Container container) {
        ContainerDashboardRow dashboardRow = allContainerDashboardRows.findByContainerId(container.getContainerId());
        dashboardRow.setProvider(provider(container));
        dashboardRow.setPatient(patient(container));

        allContainerDashboardRows.update(dashboardRow);
    }

    public void updateProviderInformation(Provider provider) {
        List<ContainerDashboardRow> allRowsBelongingToProvider = allContainerDashboardRows.withProviderId(provider.getProviderId());
        if (CollectionUtils.isNotEmpty(allRowsBelongingToProvider)) {
            for (ContainerDashboardRow containerDashboardRow : allRowsBelongingToProvider) {
                containerDashboardRow.setProvider(provider);
            }
            allContainerDashboardRows.updateAll(allRowsBelongingToProvider);
        }
    }

    public void updatePatientInformation(Patient patient) {
        List<ContainerDashboardRow> allRowsBelongingToPatient = allContainerDashboardRows.withPatientId(patient.getPatientId());
        if (CollectionUtils.isNotEmpty(allRowsBelongingToPatient)) {
            for (ContainerDashboardRow containerDashboardRow : allRowsBelongingToPatient) {
                containerDashboardRow.setPatient(patient);
            }
            allContainerDashboardRows.updateAll(allRowsBelongingToPatient);
        }
    }

    private Provider provider(Container container) {
        if (isNotBlank(container.getProviderId())) {
            return allProviders.findByProviderId(container.getProviderId());
        } else {
            return null;
        }
    }

    private Patient patient(Container container) {
        if (isNotBlank(container.getPatientId())) {
            return allPatients.findByPatientId(container.getPatientId());
        } else {
            return null;
        }
    }
}
