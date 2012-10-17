package org.motechproject.whp.containertracking.repository;

import org.motechproject.whp.common.domain.SputumTrackingInstance;
import org.motechproject.whp.containertracking.builder.ContainerTrackingRecordBuilder;
import org.motechproject.whp.containertracking.model.ContainerTrackingRecord;

import static org.motechproject.whp.common.domain.ContainerStatus.Open;
import static org.motechproject.whp.common.domain.Diagnosis.Pending;

public class AllContainerTrackingRecordsTestPart {

    protected ContainerTrackingRecord createContainerTrackingRecord(String providerId, String districtName, SputumTrackingInstance instance) {
        return new ContainerTrackingRecordBuilder()
                .withProviderId(providerId)
                .withInstance(instance)
                .withProviderDistrict(districtName)
                .withDiagnosis(Pending)
                .withStatus(Open)
                .build();
    }
}
