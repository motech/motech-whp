package org.motechproject.whp.container.domain;

import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.junit.Test;
import org.motechproject.util.DateUtil;
import org.motechproject.whp.common.domain.ContainerStatus;
import org.motechproject.whp.common.domain.SputumTrackingInstance;
import org.motechproject.whp.container.WHPContainerConstants;

import static org.joda.time.DateTime.now;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.motechproject.whp.common.domain.Diagnosis.Pending;
import static org.motechproject.whp.common.domain.Diagnosis.Positive;

public class ContainerTest {

    @Test
    public void shouldSetDefaultsUponCreation() {
        DateTime creationTime = now();
        Container container = new Container("providerId", "12345678912", SputumTrackingInstance.PreTreatment, creationTime, "d1");
        assertEquals(ContainerStatus.Open, container.getStatus());
        assertEquals(Pending, container.getDiagnosis());
        assertNull(container.getPatientId());
        assertEquals(creationTime, container.getCreationTime());
        assertEquals(DateUtil.newDate(creationTime), container.getContainerIssuedDate());
        assertNull(container.getTbId());
        assertNull(container.getReasonForClosure());
    }

    @Test
    public void shouldMapContainerToPatient_forInTreatmentPhase() {
        Container container = new Container();
        container.setConsultationDate(LocalDate.now());
        String patientId = "patientid";
        SputumTrackingInstance instance = SputumTrackingInstance.ExtendedIP;
        String tbId = "tbId";
        LocalDate consultationDate = new LocalDate(2011, 11, 23);

        container.mapWith(patientId, tbId, instance, new ReasonForContainerClosure("some reason", "0"), consultationDate);

        assertEquals(patientId, container.getPatientId());
        assertEquals(ContainerStatus.Closed, container.getStatus());
        assertEquals(instance, container.getMappingInstance());
        assertEquals(tbId, container.getTbId());
        assertEquals(Positive, container.getDiagnosis());
        assertEquals(SputumTrackingInstance.InTreatment, container.getCurrentTrackingInstance());
        assertEquals("0", container.getReasonForClosure());
        assertNull(container.getConsultationDate());
    }

    @Test
    public void shouldMapContainerToPatient_forPreTreatmentPhase() {
        Container container = new Container();
        LocalDate now = LocalDate.now();
        container.setConsultationDate(now);
        String patientId = "patientid";
        SputumTrackingInstance instance = SputumTrackingInstance.PreTreatment;
        String tbId = "tbId";
        LocalDate consultationDate = new LocalDate(2011, 11, 23);

        container.mapWith(patientId, tbId, instance, new ReasonForContainerClosure("some reason", "0"), consultationDate);

        assertEquals(patientId, container.getPatientId());
        assertEquals(ContainerStatus.Closed, container.getStatus());
        assertEquals(instance, container.getMappingInstance());
        assertEquals(tbId, container.getTbId());
        assertEquals(Positive, container.getDiagnosis());
        assertEquals(SputumTrackingInstance.PreTreatment, container.getCurrentTrackingInstance());
        assertEquals("0", container.getReasonForClosure());
        assertEquals(consultationDate, container.getConsultationDate());
    }

    @Test
    public void shouldUnMapContainer() {
        Container container = new Container();
        container.setConsultationDate(new LocalDate());
        container.mapWith("patientid", "tbId", SputumTrackingInstance.ExtendedIP, new ReasonForContainerClosure("some reason", "123"), new LocalDate());

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
    public void shouldReturnWhetherItIsClosedDueToMappingRequest() {
        Container container = new Container();
        container.setReasonForClosure(new ReasonForContainerClosure("default mapping reason", WHPContainerConstants.CLOSURE_DUE_TO_MAPPING).getCode());

        boolean closedDueToMapping = container.isClosedDueToMapping();

        assertTrue(closedDueToMapping);
    }
}
