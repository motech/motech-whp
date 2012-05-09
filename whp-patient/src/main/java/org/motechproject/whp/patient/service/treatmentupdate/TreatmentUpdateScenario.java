package org.motechproject.whp.patient.service.treatmentupdate;

import org.motechproject.whp.patient.contract.TreatmentUpdateRequest;
import org.motechproject.whp.patient.domain.criteria.CriteriaErrors;
import org.motechproject.whp.patient.exception.WHPDomainException;
import org.motechproject.whp.patient.repository.AllPatients;
import org.motechproject.whp.patient.repository.AllTreatments;

public interface TreatmentUpdateScenario {
    public void performUpdate(AllPatients allPatients, AllTreatments allTreatments, TreatmentUpdateRequest treatmentUpdateRequest);
}
