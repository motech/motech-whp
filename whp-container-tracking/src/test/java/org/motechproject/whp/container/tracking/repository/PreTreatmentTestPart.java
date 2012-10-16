package org.motechproject.whp.container.tracking.repository;

import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.motechproject.util.DateUtil;
import org.motechproject.whp.common.domain.ContainerStatus;
import org.motechproject.whp.common.domain.Diagnosis;
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
import static org.motechproject.whp.common.domain.SputumTrackingInstance.PreTreatment;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:/applicationContainerTrackingContext.xml")
public class PreTreatmentTestPart extends AllContainerTrackingRecordsTestPart {

    @Autowired
    AllPreTreatmentContainerTrackingRecordsImpl allPreTreatmentContainerTrackingRecords;

    @After
    public void tearDown() {
        allPreTreatmentContainerTrackingRecords.removeAll();
    }

    @Test
    public void shouldFilterPreTreatmentContainerRecordsByInstanceAndProviderId() {
        Properties queryParams = new Properties();
        String providerId = "providerId";
        queryParams.put("providerId", providerId);
        queryParams.put("containerInstance", "PreTreatment");
        SputumTrackingInstance preTreatment = PreTreatment;
        String districtName = "East Champaran";

        ContainerTrackingRecord expectedContainerTrackingRecord = createContainerTrackingRecord(providerId, districtName, preTreatment);

        allPreTreatmentContainerTrackingRecords.add(expectedContainerTrackingRecord);
        allPreTreatmentContainerTrackingRecords.add(createContainerTrackingRecord(providerId, districtName, SputumTrackingInstance.EndTreatment));
        allPreTreatmentContainerTrackingRecords.add(createContainerTrackingRecord("anotherProvider", districtName, PreTreatment));

        int skip = 0;
        int limit = 10;

        List<ContainerTrackingRecord> results = allPreTreatmentContainerTrackingRecords.filter(queryParams, skip, limit);

        assertThat(results.size(), is(1));
        assertEquals(results.get(0).getId(), expectedContainerTrackingRecord.getId());
    }

    @Test
    public void shouldFilterPreTreatmentContainerRecordsByAllFilterCriteria() {
        Properties queryParams = new Properties();
        String providerId = "providerId";
        String districtName = "Begusarai";
        queryParams.put("providerId", providerId);
        queryParams.put("district", districtName);
        queryParams.put("cumulativeResult", Positive.name());
        queryParams.put("containerStatus", Open.name());
        queryParams.put("containerIssuedDate<date>", "[2010-02-01 TO 2010-04-30]");
        queryParams.put("consultationDate<date>","[2010-02-04 TO 2010-05-01]");
        queryParams.put("diagnosis", Positive.name());
        queryParams.put("reasonForClosure", "reasonForClosure");

        ContainerTrackingRecord expectedContainerTrackingRecord = new ContainerTrackingRecordBuilder()
                .withProviderId(providerId)
                .withInstance(PreTreatment)
                .withProviderDistrict(districtName)
                .withCumulativeResult(Positive)
                .withStatus(Open)
                .withContainerIssuedDate(DateUtil.newDateTime(2010, 2, 5))
                .withConsultationDate(DateUtil.newDate(2010, 3, 6))
                .withDiagnosis(Diagnosis.Positive)
                .withReasonForClosure("reasonForClosure")
                .build();

        ContainerTrackingRecord containerTrackingRecord1 = new ContainerTrackingRecordBuilder()
                .withProviderId(providerId)
                .withInstance(InTreatment)
                .withProviderDistrict(districtName)
                .withCumulativeResult(Positive)
                .withStatus(ContainerStatus.Closed)
                .withContainerIssuedDate(DateUtil.newDateTime(2010, 2, 5))
                .withConsultationDate(DateUtil.newDate(2012, 3, 6))
                .withDiagnosis(Diagnosis.Negative)
                .build();

        ContainerTrackingRecord containerTrackingRecord2 = new ContainerTrackingRecordBuilder()
                .withProviderId(providerId)
                .withInstance(PreTreatment)
                .withProviderDistrict(districtName)
                .withCumulativeResult(Positive)
                .withStatus(Open)
                .withContainerIssuedDate(DateUtil.newDateTime(2009, 2, 5))
                .withNoPatientMapping()
                .build();

        allPreTreatmentContainerTrackingRecords.add(expectedContainerTrackingRecord);
        allPreTreatmentContainerTrackingRecords.add(containerTrackingRecord1);
        allPreTreatmentContainerTrackingRecords.add(containerTrackingRecord2);

        int skip = 0;
        int limit = 10;

        List<ContainerTrackingRecord> results = allPreTreatmentContainerTrackingRecords.filter(queryParams, skip, limit);

        assertThat(results.size(), is(1));
        assertThat(results.get(0).getId(), is(expectedContainerTrackingRecord.getId()));
    }

    @Test
    public void shouldCountPreTreatmentContainerTrackingRecordRows(){
        String providerId1 = "providerId";
        String providerId2 = "anotherProvider";
        String districtName1 = "East Champaran";

        allPreTreatmentContainerTrackingRecords.add(createContainerTrackingRecord(providerId1, districtName1, PreTreatment));
        allPreTreatmentContainerTrackingRecords.add(createContainerTrackingRecord(providerId1, districtName1, PreTreatment));
        allPreTreatmentContainerTrackingRecords.add(createContainerTrackingRecord(providerId1, districtName1, SputumTrackingInstance.EndTreatment));
        allPreTreatmentContainerTrackingRecords.add(createContainerTrackingRecord(providerId2, districtName1, PreTreatment));

        // Assertion 1 for provider1-Instance1
        Properties queryParams = new Properties();
        queryParams.put("providerId", providerId1);
        int recordCount = allPreTreatmentContainerTrackingRecords.count(queryParams);
        assertEquals(2, recordCount);


        // Assertion 3 for provider2-Instance1
        queryParams = new Properties();
        queryParams.put("providerId", providerId2);
        recordCount = allPreTreatmentContainerTrackingRecords.count(queryParams);
        assertEquals(1, recordCount);
    }
}
