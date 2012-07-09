package org.motechproject.whp.v1.mapper;

import org.motechproject.whp.patient.domain.Patient;
import org.motechproject.whp.v0.domain.PatientV0;

public class PatientV1Mapper {

    private PatientV0 patientV0;

    public PatientV1Mapper(PatientV0 patientV0) {

        this.patientV0 = patientV0;
    }

    public Patient map() {
        Patient patient = new Patient();
        patient.setPatientId(patientV0.getPatientId());
        return patient;
    }
}
