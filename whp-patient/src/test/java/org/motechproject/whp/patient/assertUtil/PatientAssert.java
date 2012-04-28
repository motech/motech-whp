package org.motechproject.whp.patient.assertUtil;

import org.motechproject.whp.patient.domain.Patient;

import static org.junit.Assert.assertEquals;

public class PatientAssert {

    public static void assertPatientEquals(Patient expected, Patient actual) {
        assertEquals(expected.getId(), actual.getId());
    }

    public static void assertPatientEquals(Object[] expected, Object[] actual) {
        assertEquals(lengthNotEqualMessage(expected.length, actual.length), expected.length, actual.length);
        for (int i = 0; i < expected.length; i++) {
            assertPatientEquals((Patient) expected[i], (Patient) actual[i]);
        }
    }

    private static String lengthNotEqualMessage(int expectedSize, int actualSize) {
        return String.format("Expected %s patients but was %s patients", expectedSize, actualSize);
    }
}
