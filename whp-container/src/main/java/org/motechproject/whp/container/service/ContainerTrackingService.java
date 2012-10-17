package org.motechproject.whp.container.service;

import org.apache.commons.collections.CollectionUtils;
import org.motechproject.whp.container.domain.Container;
import org.motechproject.whp.container.repository.AllContainerTrackingRecords;
import org.motechproject.whp.user.domain.Provider;
import org.motechproject.whp.user.repository.AllProviders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ContainerTrackingService {

    AllContainerTrackingRecords allContainerTrackingRecords;
    AllProviders allProviders;

    @Autowired
    public ContainerTrackingService(AllContainerTrackingRecords allContainerTrackingRecords,
                                    AllProviders allProviders) {
        this.allContainerTrackingRecords = allContainerTrackingRecords;
        this.allProviders = allProviders;
    }

    public List<Container> allContainerDashboardRows() {
        return allContainerTrackingRecords.getAll();
    }

    public void updateProviderInformation(Provider provider) {
        List<Container> allRowsBelongingToProvider = allContainerTrackingRecords.withProviderId(provider.getProviderId());
        if (CollectionUtils.isNotEmpty(allRowsBelongingToProvider)) {
            for (Container containerTrackingRecord : allRowsBelongingToProvider) {
                containerTrackingRecord.setDistrict(provider.getDistrict());
            }
            allContainerTrackingRecords.updateAll(allRowsBelongingToProvider);
        }
    }
}
