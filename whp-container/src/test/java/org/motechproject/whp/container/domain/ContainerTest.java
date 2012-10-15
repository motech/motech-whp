package org.motechproject.whp.container.domain;

import org.joda.time.LocalDate;
import org.junit.Test;
import org.motechproject.whp.common.domain.ContainerStatus;
import org.motechproject.whp.common.domain.SputumTrackingInstance;

import static org.joda.time.DateTime.now;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.motechproject.whp.common.domain.Diagnosis.Pending;
import static org.motechproject.whp.common.domain.Diagnosis.Positive;

public class ContainerTest {

    @Test
    public void shouldSetDefaultsUponCreation() {
        Container container = new Container("providerId", "12345678912", SputumTrackingInstance.PreTreatment, now());
        assertEquals(ContainerStatus.Open, container.getStatus());
        assertEquals(Pending, container.getDiagnosis());
        assertNull(container.getPatientId());
        assertEquals(container.getInstance(), container.getCurrentTrackingInstance());
        assertNull(container.getTbId());
        assertNull(container.getReasonForClosure());
    }

    @Test
    public void shouldMapContainerToPatient() {
        Container container = new Container();
        String patientId = "patientid";
        SputumTrackingInstance instance = SputumTrackingInstance.ExtendedIP;
        String tbId = "tbId";

        container.mapWith(patientId, tbId, instance, new ReasonForContainerClosure("some reason", "0"));

        assertEquals(patientId, container.getPatientId());
        assertEquals(ContainerStatus.Closed, container.getStatus());
        assertEquals(instance, container.getMappingInstance());
        assertEquals(tbId, container.getTbId());
        assertEquals(Positive, container.getDiagnosis());
        assertEquals(SputumTrackingInstance.InTreatment, container.getCurrentTrackingInstance());
        assertEquals("0", container.getReasonForClosure());
    }

    @Test
    public void shouldUnMapContainer() {
        Container container = new Container();
        container.setConsultationDate(new LocalDate());
        container.mapWith("patientid", "tbId", SputumTrackingInstance.ExtendedIP, new ReasonForContainerClosure("some reason", "123"));

        container.unMap();

        assertEquals(ContainerStatus.Open, container.getStatus());
        assertNull(container.getPatientId());
        assertNull(container.getMappingInstance());
        assertNull(container.getTbId());
        assertEquals(Pending, container.getDiagnosis());
        assertEquals(container.getInstance(), container.getCurrentTrackingInstance());
        assertNull(container.getReasonForClosure());
        assertNull(container.getConsultationDate());
    }

    @Test
    public void shouldUpdateTbId_uponMapping() {

        Container container = new Container("providerId", "12345678912", SputumTrackingInstance.PreTreatment, now());
        String patientId = "patientid";
        SputumTrackingInstance instance = SputumTrackingInstance.ExtendedIP;
        String tbId = "tbId";
        container.mapWith(patientId, tbId,instance, mock(ReasonForContainerClosure.class));

        assertEquals(tbId, container.getTbId());

    }
}
