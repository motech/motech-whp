package org.motechproject.whp.container.domain;

import org.junit.Test;
import org.motechproject.whp.refdata.domain.ContainerStatus;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class ContainerTest {

    @Test
    public void shouldHaveStatusAsOpenUponCreation() {
        Container container = new Container();
        assertEquals(ContainerStatus.Open, container.getStatus());
        assertNull(container.getPatientId());
    }

    @Test
    public void shouldCloseLifecycle_uponMappingWithPatient() {
        Container container = new Container();
        String patientId = "patientid";
        container.mapWith(patientId);
        assertEquals(patientId, container.getPatientId());
        assertEquals(ContainerStatus.Closed, container.getStatus());
    }

    @Test
    public void shouldOpenLifecycle_uponUnMappingFromPatient() {
        Container container = new Container();
        container.mapWith("patientid");
        container.unMap();
        assertEquals(ContainerStatus.Open, container.getStatus());
        assertNull(container.getPatientId());
    }
}
