package org.motechproject.whp.it.container.repository;

import org.motechproject.whp.common.domain.RegistrationInstance;
import org.motechproject.whp.container.builder.ContainerBuilder;
import org.motechproject.whp.container.domain.Container;

import static org.motechproject.whp.common.domain.ContainerStatus.Open;
import static org.motechproject.whp.common.domain.Diagnosis.Pending;

public class AllContainerTrackingRecordsTestPart {

    protected Container createContainerTrackingRecord(String providerId, String districtName, RegistrationInstance instance) {
        return new ContainerBuilder()
                .withProviderId(providerId)
                .withInstance(instance)
                .withProviderDistrict(districtName)
                .withDiagnosis(Pending)
                .withStatus(Open)
                .build();
    }
}
