package org.motechproject.whp.patient.service.treatmentupdate;

import org.motechproject.whp.patient.contract.TreatmentUpdateRequest;
import org.motechproject.whp.patient.domain.Patient;
import org.motechproject.whp.patient.domain.ProvidedTreatment;
import org.motechproject.whp.patient.domain.criteria.CriteriaErrors;
import org.motechproject.whp.patient.exception.WHPDomainException;
import org.motechproject.whp.patient.mapper.PatientMapper;
import org.motechproject.whp.patient.repository.AllPatients;
import org.motechproject.whp.patient.repository.AllTreatments;

import static org.motechproject.whp.patient.domain.criteria.UpdatePatientCriteria.canTransferInPatient;

public class TransferInPatient implements TreatmentUpdateScenario {

    private final String CANNOT_TRANSFER_IN_PATIENT = "Cannot TransferIn patient: ";

    @Override
    public void performUpdate(AllPatients allPatients, AllTreatments allTreatments, TreatmentUpdateRequest treatmentUpdateRequest) {
        Patient patient = allPatients.findByPatientId(treatmentUpdateRequest.getCase_id());
        CriteriaErrors criteriaErrors = new CriteriaErrors();
        if (!canTransferInPatient(patient, treatmentUpdateRequest, criteriaErrors)){
            throw new WHPDomainException(CANNOT_TRANSFER_IN_PATIENT + criteriaErrors);
        }
        transferInPatient(patient, treatmentUpdateRequest, allPatients, allTreatments);
    }

    private void transferInPatient(Patient patient, TreatmentUpdateRequest treatmentUpdateRequest, AllPatients allPatients, AllTreatments allTreatments) {
        ProvidedTreatment currentProvidedTreatment = patient.getCurrentProvidedTreatment();
        ProvidedTreatment newProvidedTreatment = new ProvidedTreatment(currentProvidedTreatment).updateForTransferIn(treatmentUpdateRequest.getTb_id(), treatmentUpdateRequest.getProvider_id(), treatmentUpdateRequest.getDate_modified().toLocalDate());

        patient.addProvidedTreatment(newProvidedTreatment);

        allPatients.update(patient);
    }
}
