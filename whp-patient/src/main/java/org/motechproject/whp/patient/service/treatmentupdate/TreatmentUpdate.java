package org.motechproject.whp.patient.service.treatmentupdate;

import org.motechproject.whp.patient.contract.PatientRequest;
import org.motechproject.whp.patient.repository.AllPatients;
import org.motechproject.whp.patient.repository.AllTreatments;

public abstract class TreatmentUpdate {

    protected AllPatients allPatients;
    protected AllTreatments allTreatments;

    protected TreatmentUpdate(AllPatients allPatients, AllTreatments allTreatments) {
        this.allPatients = allPatients;
        this.allTreatments = allTreatments;
    }

    public abstract void apply(PatientRequest patientRequest);

}
