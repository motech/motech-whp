package org.motechproject.whp.patient.assertUtil;

import junit.framework.Assert;
import org.motechproject.whp.patient.contract.PatientRequest;
import org.motechproject.whp.patient.domain.Patient;

import java.util.List;

import static org.junit.Assert.assertEquals;

public class PatientAssert {

    public static void assertPatientForRequests(List<PatientRequest> requests, List<Patient> patients) {
        Assert.assertEquals(requests.size(), patients.size());
        for (int i = 0; i < requests.size(); i++) {
            PatientRequest patientRequest = requests.get(i);
            Patient patient = patients.get(i);
            Assert.assertEquals(patientRequest.getCase_id(), patient.getPatientId());
        }
    }

    public static void assertPatientEquals(Patient expected, Patient actual) {
        assertEquals(expected.getId(), actual.getId());
    }

    public static void assertPatientEquals(List<Patient> expected, List<Patient> actual) {
        assertPatientEquals(expected.toArray(), actual.toArray());
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
