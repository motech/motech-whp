package org.motechproject.whp.uimodel;

import org.joda.time.DateTime;
import org.junit.Test;
import org.motechproject.util.DateUtil;
import org.motechproject.whp.container.dashboard.model.ContainerDashboardRow;
import org.motechproject.whp.container.domain.Container;
import org.motechproject.whp.container.domain.LabResults;
import org.motechproject.whp.patient.builder.PatientBuilder;
import org.motechproject.whp.patient.builder.TherapyBuilder;
import org.motechproject.whp.patient.builder.TreatmentBuilder;
import org.motechproject.whp.patient.domain.Patient;
import org.motechproject.whp.patient.domain.Therapy;
import org.motechproject.whp.patient.domain.Treatment;
import org.motechproject.whp.user.builder.ProviderBuilder;
import org.motechproject.whp.user.domain.Provider;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class SputumTrackingDashboardRowTest {


    private DateTime now = DateUtil.now();

    @Test
    public void shouldExtractContainerID() {
        String containerId = "containerId";
        LabResults labResults = new LabResults();

        Container container = new Container();
        container.setContainerId(containerId);
        container.setTbId("tbid");
        container.setCreationTime(now);
        container.setLabResults(labResults);

        ContainerDashboardRow containerDashboardRow = new ContainerDashboardRow();
        containerDashboardRow.setContainer(container);

        String patientId = "patientid";
        Patient patient = new PatientBuilder().withDefaults().withPatientId(patientId).withTherapy(therapy()).build();
        containerDashboardRow.setPatient(patient);

        String providerId = "providerid";
        String district = "district";
        Provider provider = new ProviderBuilder().withDefaults().withProviderId(providerId).withDistrict(district).build();
        containerDashboardRow.setProvider(provider);

        SputumTrackingDashboardRow row = new SputumTrackingDashboardRow(containerDashboardRow);

        assertEquals(containerId, row.getContainerId());
        assertEquals(now.toLocalDate(), row.getContainerIssuedOn());
        assertEquals(now.toLocalDate(), row.getConsultation());
        assertEquals(patientId, row.getPatientId());
        assertEquals(district, row.getDistrict());
        assertEquals(providerId, row.getProviderId());
        assertEquals(labResults, row.getLabResults());
    }

    @Test
    public void shouldNotHaveProviderInformationWhenContainerNotMappedToProvider() {
        Container containerNotMappedToProvider = new Container();
        containerNotMappedToProvider.setCreationTime(now);

        ContainerDashboardRow row = new ContainerDashboardRow();
        row.setContainer(containerNotMappedToProvider);
        assertNull(new SputumTrackingDashboardRow(row).getProviderId());
    }

    @Test
    public void shouldReturnDiagnosisAsPositiveWhenContainerMappedToPatient() {
        Container containerMappedToPatient = new Container();
        containerMappedToPatient.setCreationTime(now);
        containerMappedToPatient.setTbId("tbId");
        containerMappedToPatient.setPatientId("patientId");

        ContainerDashboardRow row = new ContainerDashboardRow();
        row.setContainer(containerMappedToPatient);
        assertEquals("Positive", new SputumTrackingDashboardRow(row).getDiagnosis());
    }

    @Test
    public void shouldNotHaveConsultationDateWhenContainerNotMappedToPatient() {
        Container containerNotMappedToPatient = new Container();
        containerNotMappedToPatient.setCreationTime(now);

        ContainerDashboardRow row = new ContainerDashboardRow();
        row.setContainer(containerNotMappedToPatient);
        assertNull(new SputumTrackingDashboardRow(row).getConsultation());
    }

    private Therapy therapy() {
        Treatment treatment = new TreatmentBuilder().withDefaults().withStartDate(now.toLocalDate()).withTbId("tbid").build();
        return new TherapyBuilder().withTreatment(treatment).build();
    }
}
