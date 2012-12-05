package org.motechproject.whp.adherenceapi.response;

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
                ).getPatientRemainingCount().intValue()
        );
    }

    @Test
    public void shouldCountTheNumberOfPatientsWithAdherence() {
        assertEquals(
                1,
                new AdherenceCaptureFlashingResponse(
                        asList("patientId1"),
                        asList("patientId1", "patientId2", "patientId3")

                ).getPatientGivenCount().intValue()
        );
    }

    @Test
    public void shouldAcceptNullListsAsArguments() {
        assertEquals(
                0,
                new AdherenceCaptureFlashingResponse(
                        null,
                        null

                ).getPatientGivenCount().intValue()
        );
        assertEquals(
                0,
                new AdherenceCaptureFlashingResponse(
                        null,
                        null

                ).getPatientRemainingCount().intValue()
        );
    }
}
