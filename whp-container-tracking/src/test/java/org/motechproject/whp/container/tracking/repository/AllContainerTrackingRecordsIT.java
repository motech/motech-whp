package org.motechproject.whp.container.tracking.repository;

import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.motechproject.util.DateUtil;
import org.motechproject.whp.common.domain.ContainerStatus;
import org.motechproject.whp.common.domain.Diagnosis;
import org.motechproject.whp.common.domain.SputumTrackingInstance;
import org.motechproject.whp.container.builder.ContainerBuilder;
import org.motechproject.whp.container.domain.Container;
import org.motechproject.whp.container.tracking.builder.ContainerTrackingRecordBuilder;
import org.motechproject.whp.container.tracking.model.ContainerTrackingRecord;
import org.motechproject.whp.patient.builder.PatientBuilder;
import org.motechproject.whp.user.builder.ProviderBuilder;
import org.motechproject.whp.user.domain.Provider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;
import java.util.Properties;

import static java.util.Arrays.asList;
import static junit.framework.Assert.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.motechproject.whp.common.domain.SputumTrackingInstance.InTreatment;
import static org.motechproject.whp.common.domain.SputumTrackingInstance.PreTreatment;
import static org.motechproject.whp.common.domain.SmearTestResult.Positive;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:/applicationContainerTrackingContext.xml")
public class AllContainerTrackingRecordsIT {

    @Autowired
    AllContainerTrackingRecords allContainerTrackingRecords;

    @After
    public void tearDown() {
        allContainerTrackingRecords.removeAll();
    }

    @Test
    public void shouldFetchAllContainerDashboardRows() {
        ContainerTrackingRecord containerTrackingRecord = new ContainerTrackingRecord();
        allContainerTrackingRecords.add(containerTrackingRecord);
        assertEquals(containerTrackingRecord.getId(), allContainerTrackingRecords.get(containerTrackingRecord.getId()).getId());
    }

    @Test
    public void shouldFetchContainerDashboardRowByContainerId() {
        ContainerTrackingRecord containerTrackingRecord = new ContainerTrackingRecord();
        Container container = ContainerBuilder.newContainer().withDefaults().build();
        containerTrackingRecord.setContainer(container);
        allContainerTrackingRecords.add(containerTrackingRecord);

        ContainerTrackingRecord byContainerId = allContainerTrackingRecords.findByContainerId(container.getContainerId());
        assertEquals(container, byContainerId.getContainer());
    }

    @Test
    public void shouldFetchContainerDashboardRowByProviderId() {
        ContainerTrackingRecord containerTrackingRecord = new ContainerTrackingRecord();
        Container container = ContainerBuilder.newContainer().withDefaults().build();
        containerTrackingRecord.setContainer(container);
        containerTrackingRecord.setProvider(new ProviderBuilder().withDefaults().withProviderId("providerId").build());

        allContainerTrackingRecords.add(containerTrackingRecord);

        List<ContainerTrackingRecord> byProviderId = allContainerTrackingRecords.withProviderId("providerId");
        assertEquals(container, byProviderId.get(0).getContainer());
    }

    @Test
    public void shouldFetchContainerDashboardRowByPatientId() {
        ContainerTrackingRecord containerTrackingRecord = new ContainerTrackingRecord();
        Container container = ContainerBuilder.newContainer().withDefaults().build();
        containerTrackingRecord.setContainer(container);
        containerTrackingRecord.setPatient(new PatientBuilder().withDefaults().withPatientId("patientId").build());

        allContainerTrackingRecords.add(containerTrackingRecord);

        List<ContainerTrackingRecord> byPatientId = allContainerTrackingRecords.withPatientId("patientId");
        assertEquals(container, byPatientId.get(0).getContainer());
    }

    @Test
    public void shouldUpdateAllContainerDashboardRows() {
        ContainerTrackingRecord containerTrackingRecord1 = new ContainerTrackingRecord();
        Container container1 = ContainerBuilder.newContainer().withDefaults().build();
        containerTrackingRecord1.setContainer(container1);
        allContainerTrackingRecords.add(containerTrackingRecord1);

        ContainerTrackingRecord containerTrackingRecord2 = new ContainerTrackingRecord();
        Container container2 = ContainerBuilder.newContainer().withDefaults().build();
        containerTrackingRecord2.setContainer(container2);
        allContainerTrackingRecords.add(containerTrackingRecord2);

        containerTrackingRecord1.setProvider(new Provider());
        containerTrackingRecord2.setProvider(new Provider());

        allContainerTrackingRecords.updateAll(asList(containerTrackingRecord1, containerTrackingRecord2));

        assertNotNull(allContainerTrackingRecords.get(containerTrackingRecord1.getId()).getProvider());
        assertNotNull(allContainerTrackingRecords.get(containerTrackingRecord2.getId()).getProvider());
    }

    @Test
    public void shouldReturnEmptyListWhenNoDashboardRowsPresent() {
        assertTrue(allContainerTrackingRecords.getAllPretreatmentContainerDashboardRows(1, 1).isEmpty());
    }

    @Test
    public void shouldReturnOnlyPretreatmentContainerDashboardRows() {
        ContainerTrackingRecord containerTrackingRecord = new ContainerTrackingRecord();
        Container container = ContainerBuilder.newContainer().withDefaults().build();
        container.setCurrentTrackingInstance(SputumTrackingInstance.InTreatment);
        containerTrackingRecord.setContainer(container);

        allContainerTrackingRecords.add(containerTrackingRecord);
        assertTrue(allContainerTrackingRecords.getAllPretreatmentContainerDashboardRows(0, 1).isEmpty());
    }

    @Test
    public void shouldReturnCountOfAllPretreatmenContainerDashboardRows() {
        ContainerTrackingRecord containerTrackingRecord = new ContainerTrackingRecord();
        Container container = ContainerBuilder.newContainer().withDefaults().build();
        container.setCurrentTrackingInstance(PreTreatment);
        containerTrackingRecord.setContainer(container);

        ContainerTrackingRecord containerTrackingRecord2 = new ContainerTrackingRecord();
        Container container2 = ContainerBuilder.newContainer().withDefaults().build();
        container2.setCurrentTrackingInstance(SputumTrackingInstance.InTreatment);
        containerTrackingRecord2.setContainer(container2);

        allContainerTrackingRecords.add(containerTrackingRecord);
        allContainerTrackingRecords.add(containerTrackingRecord2);

        assertEquals(1, allContainerTrackingRecords.numberOfPreTreatmentRows());
    }

    @Test
    public void shouldReturnPretreatmentContainerDashboardRowsInAPagedFashion() {
        ContainerTrackingRecord containerTrackingRecord1 = new ContainerTrackingRecord();
        Container container1 = ContainerBuilder.newContainer().withDefaults().build();
        container1.setCurrentTrackingInstance(PreTreatment);
        containerTrackingRecord1.setContainer(container1);
        allContainerTrackingRecords.add(containerTrackingRecord1);

        ContainerTrackingRecord containerTrackingRecord2 = new ContainerTrackingRecord();
        Container container2 = ContainerBuilder.newContainer().withDefaults().build();
        container2.setCurrentTrackingInstance(PreTreatment);
        containerTrackingRecord2.setContainer(container2);
        allContainerTrackingRecords.add(containerTrackingRecord2);

        assertEquals(containerTrackingRecord1, allContainerTrackingRecords.getAllPretreatmentContainerDashboardRows(0, 1).get(0));
        assertEquals(containerTrackingRecord2, allContainerTrackingRecords.getAllPretreatmentContainerDashboardRows(1, 1).get(0));
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

        allContainerTrackingRecords.add(expectedContainerTrackingRecord);
        allContainerTrackingRecords.add(createContainerTrackingRecord(providerId, districtName, SputumTrackingInstance.EndTreatment));
        allContainerTrackingRecords.add(createContainerTrackingRecord("anotherProvider", districtName, PreTreatment));

        int skip = 0;
        int limit = 10;

        List<ContainerTrackingRecord> results = allContainerTrackingRecords.filterPreTreatmentRecords(queryParams, skip, limit);

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
        queryParams.put("containerStatus", ContainerStatus.Open.name());
        queryParams.put("containerIssuedDate<date>", "[2010-02-01 TO 2010-04-30]");
        queryParams.put("consultationDate<date>","[2010-02-04 TO 2010-05-01]");
        queryParams.put("diagnosis", Positive.name());
        queryParams.put("reasonForClosure", "reasonForClosure");

        ContainerTrackingRecord expectedContainerTrackingRecord = new ContainerTrackingRecordBuilder()
                .withProviderId(providerId)
                .withInstance(PreTreatment)
                .withProviderDistrict(districtName)
                .withCumulativeResult(Positive)
                .withStatus(ContainerStatus.Open)
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
                .withStatus(ContainerStatus.Open)
                .withContainerIssuedDate(DateUtil.newDateTime(2009, 2, 5))
                .withNoPatientMapping()
                .build();

        allContainerTrackingRecords.add(expectedContainerTrackingRecord);
        allContainerTrackingRecords.add(containerTrackingRecord1);
        allContainerTrackingRecords.add(containerTrackingRecord2);

        int skip = 0;
        int limit = 10;

        List<ContainerTrackingRecord> results = allContainerTrackingRecords.filterPreTreatmentRecords(queryParams, skip, limit);

        assertThat(results.size(), is(1));
        assertThat(results.get(0).getId(), is(expectedContainerTrackingRecord.getId()));
    }
    
    @Test
    public void shouldCountPreTreatmentContainerTrackingRecordRows(){
        String providerId1 = "providerId";
        String providerId2 = "anotherProvider";
        String districtName1 = "East Champaran";

        allContainerTrackingRecords.add(createContainerTrackingRecord(providerId1, districtName1, PreTreatment));
        allContainerTrackingRecords.add(createContainerTrackingRecord(providerId1, districtName1, PreTreatment));
        allContainerTrackingRecords.add(createContainerTrackingRecord(providerId1, districtName1, SputumTrackingInstance.EndTreatment));
        allContainerTrackingRecords.add(createContainerTrackingRecord(providerId2, districtName1, PreTreatment));

        // Assertion 1 for provider1-Instance1
        Properties queryParams = new Properties();
        queryParams.put("providerId", providerId1);
        int recordCount = allContainerTrackingRecords.countPreTreatmentRecords(queryParams);
        assertEquals(2, recordCount);


        // Assertion 3 for provider2-Instance1
        queryParams = new Properties();
        queryParams.put("providerId", providerId2);
        recordCount = allContainerTrackingRecords.countPreTreatmentRecords(queryParams);
        assertEquals(1, recordCount);
    }

    private ContainerTrackingRecord createContainerTrackingRecord(String providerId, String districtName, SputumTrackingInstance instance) {
        return new ContainerTrackingRecordBuilder()
                .withProviderId(providerId)
                .withInstance(instance)
                .withProviderDistrict(districtName)
                .build();
    }
}
