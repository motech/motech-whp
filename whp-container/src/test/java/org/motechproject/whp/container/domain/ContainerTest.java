package org.motechproject.whp.container.domain;

import org.junit.Test;
import org.motechproject.whp.refdata.domain.ContainerStatus;
import org.motechproject.whp.refdata.domain.SputumTrackingInstance;

import static org.joda.time.DateTime.now;
import static org.junit.Assert.*;

public class ContainerTest {

    @Test
    public void shouldHaveStatusAsOpenUponCreation() {
        Container container = new Container("providerId", "12345678912", SputumTrackingInstance.PreTreatment, now());
        assertEquals(ContainerStatus.Open, container.getStatus());
        assertNull(container.getPatientId());
    }

    @Test
    public void shouldCloseLifecycle_uponMappingWithPatient() {
        Container container = new Container();
        String patientId = "patientid";
        SputumTrackingInstance instance = SputumTrackingInstance.ExtendedIP;
        String tbId = "tbId";
        container.mapWith(patientId, tbId, instance);
        assertEquals(patientId, container.getPatientId());
        assertEquals(ContainerStatus.Closed, container.getStatus());
        assertEquals(instance, container.getMappingInstance());
        assertEquals(tbId, container.getTbId());
    }

    @Test
    public void shouldOpenLifecycle_uponUnMappingFromPatient() {
        Container container = new Container();
        container.mapWith("patientid", "tbId", SputumTrackingInstance.ExtendedIP);
        container.unMap();
        assertEquals(ContainerStatus.Open, container.getStatus());
        assertNull(container.getPatientId());
        assertNull(container.getMappingInstance());
        assertNull(container.getTbId());
    }

    @Test
    public void shouldUpdateCurrentTrackingInstance_uponMappingWithPatient() {
        Container container = new Container("providerId", "12345678912", SputumTrackingInstance.PreTreatment, now());
        String patientId = "patientid";
        SputumTrackingInstance instance = SputumTrackingInstance.ExtendedIP;
        container.mapWith(patientId, "", instance);

        assertEquals(SputumTrackingInstance.InTreatment, container.getCurrentTrackingInstance());
    }

    @Test
    public void shouldRestoreRegistrationInstance_uponUnMappingFromPatient() {
        Container container = new Container("providerId", "12345678921", SputumTrackingInstance.PreTreatment, now());
        String patientId = "patientid";
        SputumTrackingInstance instance = SputumTrackingInstance.ExtendedIP;
        container.mapWith(patientId, "", instance);
        container.unMap();
        assertEquals(container.getInstance(), container.getCurrentTrackingInstance());
    }

    @Test
    public void currentTrackingStatusShouldBeInitializedToInstance_uponCreation() {
        Container container = new Container("providerId", "12345678921", SputumTrackingInstance.PreTreatment, now());
        assertEquals(container.getInstance(), container.getCurrentTrackingInstance());
    }

    @Test
    public void shouldUpadteTbId_uponMapping() {

        Container container = new Container("providerId", "12345678912", SputumTrackingInstance.PreTreatment, now());
        String patientId = "patientid";
        SputumTrackingInstance instance = SputumTrackingInstance.ExtendedIP;
        String tbId = "tbId";
        container.mapWith(patientId, tbId,instance);

        assertEquals(tbId, container.getTbId());

    }
}
