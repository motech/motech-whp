package org.motechproject.whp.uimodel;

import org.joda.time.DateTime;
import org.junit.Test;
import org.motechproject.util.DateUtil;
import org.motechproject.whp.container.tracking.model.ContainerTrackingRecord;
import org.motechproject.whp.container.domain.Container;
import org.motechproject.whp.container.domain.LabResults;
import org.motechproject.whp.patient.builder.PatientBuilder;
import org.motechproject.whp.patient.builder.TherapyBuilder;
import org.motechproject.whp.patient.builder.TreatmentBuilder;
import org.motechproject.whp.patient.domain.Patient;
import org.motechproject.whp.patient.domain.Therapy;
import org.motechproject.whp.patient.domain.Treatment;
import org.motechproject.whp.refdata.domain.ContainerStatus;
import org.motechproject.whp.refdata.domain.SmearTestResult;
import org.motechproject.whp.user.builder.ProviderBuilder;
import org.motechproject.whp.user.domain.Provider;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class ContainerTrackingDashboardRowTest {


    public static final String DATE_FORMAT = "dd/MM/yyyy";
    private DateTime now = DateUtil.now();

    @Test
    public void shouldExtractRequiredDetailsFromContainerDashboardRow() {
        String containerId = "containerId";
        ContainerStatus containerStatus = ContainerStatus.Open;
        String labName = "MyOwnLab";
        LabResults labResults = new LabResults();
        labResults.setLabName(labName);
        labResults.setSmearTestResult1(SmearTestResult.Positive);
        labResults.setSmearTestDate1(now.toLocalDate());
        labResults.setSmearTestResult2(SmearTestResult.Negative);
        labResults.setSmearTestDate2(now.toLocalDate().plusDays(1));

        Container container = new Container();
        container.setContainerId(containerId);
        container.setStatus(containerStatus);
        container.setTbId("tbid");
        container.setCreationTime(now);
        container.setLabResults(labResults);

        ContainerTrackingRecord containerTrackingRecord = new ContainerTrackingRecord();
        containerTrackingRecord.setContainer(container);

        String patientId = "patientid";
        Patient patient = new PatientBuilder().withDefaults().withPatientId(patientId).withTherapy(therapy()).build();
        containerTrackingRecord.setPatient(patient);

        String providerId = "providerid";
        String district = "district";
        Provider provider = new ProviderBuilder().withDefaults().withProviderId(providerId).withDistrict(district).build();
        containerTrackingRecord.setProvider(provider);

        ContainerTrackingDashboardRow row = new ContainerTrackingDashboardRow(containerTrackingRecord);

        assertEquals(containerId, row.getContainerId());
        assertEquals(containerStatus.name(), row.getContainerStatus());
        assertEquals(now.toLocalDate().toString(DATE_FORMAT), row.getContainerIssuedOn());
        assertEquals(now.toLocalDate().toString(DATE_FORMAT), row.getConsultation());
        assertEquals(patientId, row.getPatientId());
        assertEquals(district, row.getDistrict());
        assertEquals(providerId, row.getProviderId());
        assertEquals(labName, row.getLabName());
        assertEquals(now.toLocalDate().toString(DATE_FORMAT), row.getConsultationOneDate());
        assertEquals(now.toLocalDate().plusDays(1).toString(DATE_FORMAT), row.getConsultationTwoDate());
        assertEquals(SmearTestResult.Positive.name(), row.getConsultationOneResult());
        assertEquals(SmearTestResult.Negative.name(), row.getConsultationTwoResult());
    }

    @Test
    public void shouldNotHaveProviderInformationWhenContainerNotMappedToProvider() {
        Container containerNotMappedToProvider = new Container();
        containerNotMappedToProvider.setCreationTime(now);

        ContainerTrackingRecord row = new ContainerTrackingRecord();
        row.setContainer(containerNotMappedToProvider);
        assertNull(new ContainerTrackingDashboardRow(row).getProviderId());
    }

    @Test
    public void shouldReturnDiagnosisAsPositiveWhenContainerMappedToPatient() {
        Container containerMappedToPatient = new Container();
        containerMappedToPatient.setCreationTime(now);
        containerMappedToPatient.setTbId("tbId");
        containerMappedToPatient.setPatientId("patientId");

        ContainerTrackingRecord row = new ContainerTrackingRecord();
        row.setContainer(containerMappedToPatient);
        assertEquals("Positive", new ContainerTrackingDashboardRow(row).getDiagnosis());
    }

    @Test
    public void shouldNotHaveConsultationDateWhenContainerNotMappedToPatient() {
        Container containerNotMappedToPatient = new Container();
        containerNotMappedToPatient.setCreationTime(now);

        ContainerTrackingRecord row = new ContainerTrackingRecord();
        row.setContainer(containerNotMappedToPatient);
        assertNull(new ContainerTrackingDashboardRow(row).getConsultation());
    }

    private Therapy therapy() {
        Treatment treatment = new TreatmentBuilder().withDefaults().withStartDate(now.toLocalDate()).withTbId("tbid").build();
        return new TherapyBuilder().withTreatment(treatment).build();
    }
}
