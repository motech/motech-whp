package org.motechproject.whp.adherenceapi.response.flashing;

import org.junit.Test;
import org.motechproject.whp.adherenceapi.response.flashing.AdherenceFlashingResponse;

import static java.util.Arrays.asList;
import static org.junit.Assert.assertEquals;

public class AdherenceFlashingResponseTest {

    @Test
    public void shouldCountTheNumberOfPatientsWithoutAdherence() {
        assertEquals(
                2,
                new AdherenceFlashingResponse(
                        asList("patientId1"),
                        asList("patientId1", "patientId2", "patientId3")
                ).getPatientRemainingCount().intValue()
        );
    }

    @Test
    public void shouldCountTheNumberOfPatientsWithAdherence() {
        assertEquals(
                1,
                new AdherenceFlashingResponse(
                        asList("patientId1"),
                        asList("patientId1", "patientId2", "patientId3")

                ).getPatientGivenCount().intValue()
        );
    }

    @Test
    public void shouldAcceptNullListsAsArguments() {
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
