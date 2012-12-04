package org.motechproject.whp.adherenceapi.request;

import org.junit.Test;

import static java.util.Arrays.asList;
import static org.junit.Assert.assertEquals;

public class AdherenceCaptureFlashingResponseTest {

    @Test
    public void shouldCountTheNumberOfPatientsWithoutAdherence() {
        assertEquals(
                2,
                new AdherenceCaptureFlashingResponse(
                        asList("patientId1"),
                        asList("patientId1", "patientId2", "patientId3")
                ).getPatientRemainingCount()
        );
    }

    @Test
    public void shouldCountTheNumberOfPatientsWithAdherence() {
        assertEquals(
                1,
                new AdherenceCaptureFlashingResponse(
                        asList("patientId1"),
                        asList("patientId1", "patientId2", "patientId3")

                ).getPatientGivenCount()
        );
    }

    @Test
    public void shouldAcceptNullListsAsArguments() {
        assertEquals(
                0,
                new AdherenceCaptureFlashingResponse(
                        null,
                        null

                ).getPatientGivenCount()
        );
        assertEquals(
                0,
                new AdherenceCaptureFlashingResponse(
                        null,
                        null

                ).getPatientRemainingCount()
        );
    }
}
