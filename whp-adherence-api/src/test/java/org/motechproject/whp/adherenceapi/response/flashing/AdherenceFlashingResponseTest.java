package org.motechproject.whp.adherenceapi.response.flashing;

import org.junit.Test;

import static java.util.Arrays.asList;
import static org.junit.Assert.assertEquals;

public class AdherenceFlashingResponseTest {

    @Test
    public void testRemainingCountWhenOnePatientHasAdherence() {
        assertEquals(
                2,
                new AdherenceFlashingResponse(
                        asList("patientId1"),
                        asList("patientId1", "patientId2", "patientId3")
                ).getPatientRemainingCount().intValue()
        );
    }

    @Test
    public void testRemainingCountWhenNoPatientHasAdherence() {
        assertEquals(
                3,
                new AdherenceFlashingResponse(
                        asList("patientId4"),
                        asList("patientId1", "patientId2", "patientId3")
                ).getPatientRemainingCount().intValue()
        );
    }

    @Test
    public void testRemainingCountWhenAllPatientsHaveAdherence() {
        assertEquals(
                0,
                new AdherenceFlashingResponse(
                        asList("patientId1", "patientId2", "patientId3"),
                        asList("patientId1", "patientId2", "patientId3")
                ).getPatientRemainingCount().intValue()
        );
    }

    @Test
    public void testRemainingCountPatientsUnderProviderIsSubsetOfPatientsWithAdherence() {
        assertEquals(
                0,
                new AdherenceFlashingResponse(
                        asList("patientId1", "patientId2", "patientId3", "patientId4"),
                        asList("patientId1", "patientId2", "patientId3")
                ).getPatientRemainingCount().intValue()
        );
    }

    @Test
    public void testAdherenceCountWhenNoPatientHasAdherence() {
        assertEquals(
                0,
                new AdherenceFlashingResponse(
                        asList("patientId2", "patientId3"),
                        asList("patientId1")

                ).getPatientGivenCount().intValue()
        );
    }

    @Test
    public void testAdherenceCountWhenOnePatientHasAdherence() {
        assertEquals(
                1,
                new AdherenceFlashingResponse(
                        asList("patientId1"),
                        asList("patientId1", "patientId2", "patientId3")

                ).getPatientGivenCount().intValue()
        );
    }

    @Test
    public void testAdherenceCountWhenAllPatientsHaveAdherence() {
        assertEquals(
                3,
                new AdherenceFlashingResponse(
                        asList("patientId1", "patientId2", "patientId3"),
                        asList("patientId1", "patientId2", "patientId3")

                ).getPatientGivenCount().intValue()
        );
    }

    @Test
    public void testAdherenceCountWhenPatientsUnderProviderIsSubsetOfPatientsWithAdherence() {
        assertEquals(
                3,
                new AdherenceFlashingResponse(
                        asList("patientId1", "patientId2", "patientId3", "patientId4"),
                        asList("patientId1", "patientId2", "patientId3")

                ).getPatientGivenCount().intValue()
        );
    }

    @Test
    public void testAdherenceCounts() {
        assertEquals(
                0,
                new AdherenceFlashingResponse(
                        null,
                        null

                ).getPatientGivenCount().intValue()
        );
        assertEquals(
                0,
                new AdherenceFlashingResponse(
                        null,
                        null

                ).getPatientRemainingCount().intValue()
        );
    }
}
