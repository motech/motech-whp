package org.motechproject.whp.patient.domain;

import java.util.Comparator;

public class PatientComparatorByFirstName implements Comparator<Patient> {

    @Override
    public int compare(Patient patient1, Patient patient2) {
        return patient1.getFirstName().compareTo(patient2.getFirstName());
    }
}
