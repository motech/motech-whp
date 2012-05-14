package org.motechproject.whp.patient.service.treatmentupdate;

import org.motechproject.whp.patient.contract.TreatmentUpdateRequest;
import org.motechproject.whp.patient.repository.AllPatients;
import org.motechproject.whp.patient.repository.AllTreatments;

public class RestartTreatment implements TreatmentUpdate {

    @Override
    public void apply(AllPatients allPatients, AllTreatments allTreatments, TreatmentUpdateRequest treatmentUpdateRequest) {

    }
}
