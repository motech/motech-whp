package org.motechproject.whp.adherenceapi.response;

import lombok.EqualsAndHashCode;

import javax.xml.bind.annotation.XmlElement;
import java.util.ArrayList;
import java.util.List;

@EqualsAndHashCode
class PatientsRemaining {

    @XmlElement(name = "patient")
    private List<PatientId> patient;

    PatientsRemaining() {
    }

    PatientsRemaining(List<String> patient) {
        this.patient = new ArrayList<>();
        for (String patientId : patient) {
            this.patient.add(new PatientId(patientId));
        }
    }
}
