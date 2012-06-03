package org.motechproject.whp.patient.command;

import org.motechproject.whp.patient.contract.PatientRequest;
import org.motechproject.whp.patient.domain.Patient;
import org.motechproject.whp.patient.exception.WHPErrorCode;
import org.motechproject.whp.patient.exception.WHPRuntimeException;
import org.motechproject.whp.patient.repository.AllPatients;
import org.motechproject.whp.patient.repository.AllTherapies;
import org.motechproject.whp.patient.service.ProviderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

import static org.motechproject.whp.patient.domain.criteria.UpdatePatientCriteria.canTransferInPatient;

@Component
public class TransferInPatient extends TreatmentUpdate {

    ProviderService providerService;

    @Autowired
    public TransferInPatient(AllPatients allPatients, AllTherapies allTreatments, ProviderService providerService) {
        super(allPatients, allTreatments, UpdateScope.transferIn);
        this.providerService = providerService;
    }

    public void apply(PatientRequest patientRequest) {
        Patient patient = allPatients.findByPatientId(patientRequest.getCase_id());
        List<WHPErrorCode> errorCodes = new ArrayList<WHPErrorCode>();

        if (!canTransferInPatient(patient, patientRequest, errorCodes)) {
            throw new WHPRuntimeException(errorCodes);
        }
        transferInPatient(patient, patientRequest);
    }

    private void transferInPatient(Patient patient, PatientRequest patientRequest) {
        providerService.transferIn(patientRequest.getProvider_id(),
                patient, patientRequest.getTb_id(),
                patientRequest.getTb_registration_number(),
                patientRequest.getDate_modified());
        patient.reviveLastClosedTreatment();
        allTherapies.update(patient.latestTreatment());
    }
}
