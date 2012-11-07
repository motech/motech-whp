package org.motechproject.whp.container.repository;

import org.joda.time.LocalDate;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.motechproject.util.DateUtil;
import org.motechproject.whp.common.domain.ContainerStatus;
import org.motechproject.whp.common.domain.Diagnosis;
import org.motechproject.whp.common.domain.RegistrationInstance;
import org.motechproject.whp.container.builder.ContainerBuilder;
import org.motechproject.whp.container.domain.Container;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;
import java.util.Properties;

import static junit.framework.Assert.assertEquals;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.motechproject.whp.common.domain.ContainerStatus.Open;
import static org.motechproject.whp.common.domain.RegistrationInstance.InTreatment;
import static org.motechproject.whp.common.domain.RegistrationInstance.PreTreatment;
import static org.motechproject.whp.common.domain.SmearTestResult.Positive;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:/applicationContainerContext.xml")
public class PreTreatmentTestPart extends AllContainerTrackingRecordsTestPart {

    @Autowired
    AllContainerTrackingRecords allContainerTrackingRecords;

    @After
    public void tearDown() {
        allContainerTrackingRecords.removeAll();
    }

    @Test
    public void shouldFilterPreTreatmentContainerRecordsByInstanceAndProviderId() {
        Properties sortParams = new Properties();
        Properties queryParams = new Properties();
        String providerId = "providerid";
        queryParams.put("providerId", providerId);
        queryParams.put("containerInstance", "PreTreatment");
        RegistrationInstance preTreatment = PreTreatment;
        String districtName = "East Champaran";

        Container expectedContainerTrackingRecord = createContainerTrackingRecord(providerId, districtName, preTreatment);

        allContainerTrackingRecords.add(expectedContainerTrackingRecord);
        allContainerTrackingRecords.add(createContainerTrackingRecord(providerId, districtName, RegistrationInstance.InTreatment));
        allContainerTrackingRecords.add(createContainerTrackingRecord("anotherProvider", districtName, PreTreatment));

        int skip = 0;
        int limit = 10;

        List<Container> results = allContainerTrackingRecords.filter(PreTreatment, queryParams, sortParams, skip, limit);

        assertThat(results.size(), is(1));
        assertEquals(results.get(0).getId(), expectedContainerTrackingRecord.getId());
    }

    @Test
    public void shouldFilterPreTreatmentContainerRecordsByAllFilterCriteria() {
        Properties queryParams = new Properties();
        Properties sortParams = new Properties();
        String providerId = "providerid";
        String districtName = "Begusarai";
        queryParams.put("providerId", providerId);
        queryParams.put("district", districtName);
        queryParams.put("cumulativeResult", Positive.name());
        queryParams.put("containerStatus", Open.name());
        queryParams.put("containerIssuedDateFrom", "01/02/2010");
        queryParams.put("containerIssuedDateTo", "30/04/2010");
        queryParams.put("consultationDateFrom","04/02/2010");
        queryParams.put("consultationDateTo","01/05/2010");
        queryParams.put("diagnosis", Positive.name());
        queryParams.put("reasonForClosure", "reasonForClosure");

        Container expectedContainerTrackingRecord = new ContainerBuilder()
                .withProviderId(providerId)
                .withInstance(PreTreatment)
                .withProviderDistrict(districtName)
                .withCumulativeResult(Positive)
                .withStatus(Open)
                .withContainerIssuedDate(DateUtil.newDate(2010, 2, 5))
                .withConsultationDate(DateUtil.newDate(2010, 3, 6))
                .withDiagnosis(Diagnosis.Positive)
                .withReasonForClosure("reasonForClosure")
                .build();

        Container containerTrackingRecord1 = new ContainerBuilder()
                .withProviderId(providerId)
                .withInstance(InTreatment)
                .withProviderDistrict(districtName)
                .withCumulativeResult(Positive)
                .withStatus(ContainerStatus.Closed)
                .withContainerIssuedDate(DateUtil.newDate(2010, 2, 5))
                .withConsultationDate(DateUtil.newDate(2012, 3, 6))
                .withDiagnosis(Diagnosis.Negative)
                .build();

        Container containerTrackingRecord2 = new ContainerBuilder()
                .withProviderId(providerId)
                .withInstance(PreTreatment)
                .withProviderDistrict(districtName)
                .withCumulativeResult(Positive)
                .withStatus(Open)
                .withContainerIssuedDate(DateUtil.newDate(2009, 2, 5))
                .build();

        allContainerTrackingRecords.add(expectedContainerTrackingRecord);
        allContainerTrackingRecords.add(containerTrackingRecord1);
        allContainerTrackingRecords.add(containerTrackingRecord2);

        int skip = 0;
        int limit = 10;

        List<Container> results = allContainerTrackingRecords.filter(PreTreatment, queryParams, sortParams, skip, limit);

        assertThat(results.size(), is(1));
        assertThat(results.get(0).getId(), is(expectedContainerTrackingRecord.getId()));
    }

    @Test
    public void shouldFilterAndSortPreTreatmentContainerRecordsByAllFilterCriteria() {
        Properties queryParams = new Properties();
        Properties sortParams = new Properties();
        sortParams.put("containerIssuedDate", "asc");
        String providerId = "providerid";
        queryParams.put("providerId", providerId);

        Container containerTrackingRecord1 = new ContainerBuilder()
                .withDefaults()
                .withProviderId(providerId)
                .withInstance(PreTreatment)
                .withContainerIssuedDate(new LocalDate(2012, 10, 9))
                .build();

        Container containerTrackingRecord2 = new ContainerBuilder()
                .withDefaults()
                .withProviderId(providerId)
                .withInstance(PreTreatment)
                .withContainerIssuedDate(new LocalDate(2012, 10, 15))
                .build();

        Container containerTrackingRecord3 = new ContainerBuilder()
                .withDefaults()
                .withProviderId(providerId)
                .withInstance(PreTreatment)
                .withContainerIssuedDate(new LocalDate(2012, 10, 10))
                .build();


        allContainerTrackingRecords.add(containerTrackingRecord1);
        allContainerTrackingRecords.add(containerTrackingRecord2);
        allContainerTrackingRecords.add(containerTrackingRecord3);

        int skip = 0;
        int limit = 10;

        List<Container> results = allContainerTrackingRecords.filter(PreTreatment, queryParams, sortParams, skip, limit);

        assertThat(results.size(), is(3));
        assertThat(results.get(0).getId(), is(containerTrackingRecord1.getId()));
        assertThat(results.get(1).getId(), is(containerTrackingRecord3.getId()));
        assertThat(results.get(2).getId(), is(containerTrackingRecord2.getId()));
    }

    @Test
    public void shouldCountPreTreatmentContainerTrackingRecordRows(){
        String providerId1 = "providerid";
        String providerId2 = "anotherprovider";
        String districtName1 = "East Champaran";

        allContainerTrackingRecords.add(createContainerTrackingRecord(providerId1, districtName1, PreTreatment));
        allContainerTrackingRecords.add(createContainerTrackingRecord(providerId1, districtName1, PreTreatment));
        allContainerTrackingRecords.add(createContainerTrackingRecord(providerId1, districtName1, RegistrationInstance.InTreatment));
        allContainerTrackingRecords.add(createContainerTrackingRecord(providerId2, districtName1, PreTreatment));

        // Assertion 1 for provider1-Instance1
        Properties queryParams = new Properties();
        queryParams.put("providerId", providerId1);
        int recordCount = allContainerTrackingRecords.count(PreTreatment, queryParams);
        assertEquals(2, recordCount);


        // Assertion 3 for provider2-Instance1
        queryParams = new Properties();
        queryParams.put("providerId", providerId2);
        recordCount = allContainerTrackingRecords.count(PreTreatment, queryParams);
        assertEquals(1, recordCount);
    }
}
