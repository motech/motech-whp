package org.motechproject.whp.container.tracking.repository;

import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.motechproject.util.DateUtil;
import org.motechproject.whp.common.domain.ContainerStatus;
import org.motechproject.whp.common.domain.SputumTrackingInstance;
import org.motechproject.whp.container.tracking.builder.ContainerTrackingRecordBuilder;
import org.motechproject.whp.container.tracking.model.ContainerTrackingRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;
import java.util.Properties;

import static junit.framework.Assert.assertEquals;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.motechproject.whp.common.domain.ContainerStatus.Open;
import static org.motechproject.whp.common.domain.SmearTestResult.Positive;
import static org.motechproject.whp.common.domain.SputumTrackingInstance.InTreatment;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:/applicationContainerTrackingContext.xml")
public class InTreatmentTestPart extends AllContainerTrackingRecordsTestPart {

    @Autowired
    AllInTreatmentContainerTrackingRecordsImpl allInTreatmentContainerTrackingRecords;

    @After
    public void tearDown() {
        allInTreatmentContainerTrackingRecords.removeAll();
    }

    @Test
    public void shouldFilterInTreatmentContainerRecordsByInstanceAndProviderId() {
        Properties queryParams = new Properties();
        String providerId = "providerId";
        queryParams.put("providerId", providerId);
        queryParams.put("containerInstance", "InTreatment");
        SputumTrackingInstance inTreatment = InTreatment;
        String districtName = "East Champaran";

        ContainerTrackingRecord expectedContainerTrackingRecord = createContainerTrackingRecord(providerId, districtName, inTreatment);

        allInTreatmentContainerTrackingRecords.add(expectedContainerTrackingRecord);
        allInTreatmentContainerTrackingRecords.add(createContainerTrackingRecord(providerId, districtName, SputumTrackingInstance.EndTreatment));
        allInTreatmentContainerTrackingRecords.add(createContainerTrackingRecord("anotherProvider", districtName, InTreatment));

        int skip = 0;
        int limit = 10;

        List<ContainerTrackingRecord> results = allInTreatmentContainerTrackingRecords.filter(queryParams, skip, limit);

        assertThat(results.size(), is(1));
        assertEquals(results.get(0).getId(), expectedContainerTrackingRecord.getId());
    }

    @Test
    public void shouldFilterInTreatmentContainerRecordsByAllFilterCriteria() {
        Properties queryParams = new Properties();
        String providerId = "providerId";
        String districtName = "Begusarai";
        queryParams.put("providerId", providerId);
        queryParams.put("district", districtName);
        queryParams.put("cumulativeResult", Positive.name());
        queryParams.put("containerStatus", Open.name());
        queryParams.put("containerIssuedDate<date>", "[2010-02-01 TO 2010-04-30]");
        queryParams.put("reasonForClosure", "reasonForClosure");

        ContainerTrackingRecord expectedContainerTrackingRecord = new ContainerTrackingRecordBuilder()
                .withProviderId(providerId)
                .withInstance(InTreatment)
                .withProviderDistrict(districtName)
                .withCumulativeResult(Positive)
                .withStatus(Open)
                .withContainerIssuedDate(DateUtil.newDateTime(2010, 2, 5))
                .withReasonForClosure("reasonForClosure")
                .build();

        ContainerTrackingRecord containerTrackingRecord1 = new ContainerTrackingRecordBuilder()
                .withProviderId(providerId)
                .withInstance(InTreatment)
                .withProviderDistrict(districtName)
                .withCumulativeResult(Positive)
                .withStatus(ContainerStatus.Closed)
                .withContainerIssuedDate(DateUtil.newDateTime(2010, 2, 5))
                .build();

        ContainerTrackingRecord containerTrackingRecord2 = new ContainerTrackingRecordBuilder()
                .withProviderId(providerId)
                .withInstance(InTreatment)
                .withProviderDistrict(districtName)
                .withCumulativeResult(Positive)
                .withStatus(Open)
                .withContainerIssuedDate(DateUtil.newDateTime(2009, 2, 5))
                .withNoPatientMapping()
                .build();

        allInTreatmentContainerTrackingRecords.add(expectedContainerTrackingRecord);
        allInTreatmentContainerTrackingRecords.add(containerTrackingRecord1);
        allInTreatmentContainerTrackingRecords.add(containerTrackingRecord2);

        int skip = 0;
        int limit = 10;

        List<ContainerTrackingRecord> results = allInTreatmentContainerTrackingRecords.filter(queryParams, skip, limit);

        assertThat(results.size(), is(1));
        assertThat(results.get(0).getId(), is(expectedContainerTrackingRecord.getId()));
    }

    @Test
    public void shouldCountInTreatmentContainerTrackingRecordRows() {
        String providerId1 = "providerId";
        String providerId2 = "anotherProvider";
        String districtName1 = "East Champaran";

        allInTreatmentContainerTrackingRecords.add(createContainerTrackingRecord(providerId1, districtName1, InTreatment));
        allInTreatmentContainerTrackingRecords.add(createContainerTrackingRecord(providerId1, districtName1, InTreatment));
        allInTreatmentContainerTrackingRecords.add(createContainerTrackingRecord(providerId1, districtName1, SputumTrackingInstance.PreTreatment));
        allInTreatmentContainerTrackingRecords.add(createContainerTrackingRecord(providerId2, districtName1, InTreatment));

        // Assertion 1 for provider1-Instance1
        Properties queryParams = new Properties();
        queryParams.put("providerId", providerId1);
        int recordCount = allInTreatmentContainerTrackingRecords.count(queryParams);
        assertEquals(2, recordCount);


        // Assertion 3 for provider2-Instance1
        queryParams = new Properties();
        queryParams.put("providerId", providerId2);
        recordCount = allInTreatmentContainerTrackingRecords.count(queryParams);
        assertEquals(1, recordCount);
    }
}
