package org.motechproject.whp.patient.service.treatmentupdate;

import org.motechproject.whp.patient.contract.TreatmentUpdateRequest;
import org.motechproject.whp.patient.domain.Patient;
import org.motechproject.whp.patient.exception.WHPDomainException;
import org.motechproject.whp.patient.repository.AllPatients;
import org.motechproject.whp.patient.repository.AllTreatments;

public class TransferInPatient implements TreatmentUpdateScenario {
    @Override
    public void performUpdate(AllPatients allPatients, AllTreatments allTreatments, TreatmentUpdateRequest treatmentUpdateRequest) {
        Patient patient = allPatients.findByPatientId(treatmentUpdateRequest.getCase_id());
    }
}
