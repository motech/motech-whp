package org.motechproject.whp.container.tracking.repository;

import org.junit.After;
import org.junit.runner.RunWith;
import org.motechproject.whp.common.domain.SputumTrackingInstance;
import org.motechproject.whp.container.tracking.builder.ContainerTrackingRecordBuilder;
import org.motechproject.whp.container.tracking.model.ContainerTrackingRecord;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

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
