package org.motechproject.whp.v1.migration;

import org.motechproject.whp.patient.domain.Patient;
import org.motechproject.whp.v0.domain.PatientV0;
import org.motechproject.whp.v0.repository.AllPatientsV0;
import org.motechproject.whp.v1.mapper.PatientV1Mapper;
import org.motechproject.whp.v1.repository.AllPatientsV1;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ToVersion0 {

    AllPatientsV0 allPatientsV0;

    AllPatientsV1 allPatientsV1;

    @Autowired
    public ToVersion0(AllPatientsV0 allPatientsV0, AllPatientsV1 allPatientsV1) {
        this.allPatientsV0 = allPatientsV0;
        this.allPatientsV1 = allPatientsV1;
    }

    public void doo() {
        List<PatientV0> patients = allPatientsV0.getAll();
        for (PatientV0 patientV0 : patients) {
            Patient patientV1 = new PatientV1Mapper(patientV0).map();
            allPatientsV0.remove(patientV0);
            allPatientsV1.add(patientV1);
        }
    }

}
