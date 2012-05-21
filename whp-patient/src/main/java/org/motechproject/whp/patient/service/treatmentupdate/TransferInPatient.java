package org.motechproject.whp.patient.service.treatmentupdate;

import org.motechproject.whp.patient.contract.TreatmentUpdateRequest;
import org.motechproject.whp.patient.domain.Patient;
import org.motechproject.whp.patient.exception.WHPDomainException;
import org.motechproject.whp.patient.exception.errorcode.WHPDomainErrorCode;
import org.motechproject.whp.patient.repository.AllPatients;
import org.motechproject.whp.patient.repository.AllTreatments;
import org.motechproject.whp.patient.service.ProviderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;

import static org.motechproject.whp.patient.domain.criteria.UpdatePatientCriteria.canTransferInPatient;

@Component
public class TransferInPatient extends TreatmentUpdate {

    ProviderService providerService;

    @Autowired
    public TransferInPatient(AllPatients allPatients, AllTreatments allTreatments, ProviderService providerService) {
        super(allPatients, allTreatments);
        this.providerService = providerService;
    }

    public void apply(TreatmentUpdateRequest treatmentUpdateRequest) {
        Patient patient = allPatients.findByPatientId(treatmentUpdateRequest.getCase_id());
         ArrayList<WHPDomainErrorCode> errorCodes = new ArrayList<WHPDomainErrorCode>();
        if (!canTransferInPatient(patient, treatmentUpdateRequest, errorCodes)) {
            throw new WHPDomainException(errorCodes);
        }
        transferInPatient(patient, treatmentUpdateRequest);
    }

    private void transferInPatient(Patient patient, TreatmentUpdateRequest treatmentUpdateRequest) {
        providerService.transferIn(treatmentUpdateRequest.getProvider_id(), patient, treatmentUpdateRequest.getTb_id(), treatmentUpdateRequest.getDate_modified());
    }
}
