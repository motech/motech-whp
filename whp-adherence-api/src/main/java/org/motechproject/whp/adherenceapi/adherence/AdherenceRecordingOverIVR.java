package org.motechproject.whp.adherenceapi.adherence;

import org.motechproject.whp.adherenceapi.domain.Dosage;
import org.motechproject.whp.adherenceapi.request.AdherenceValidationRequest;
import org.motechproject.whp.adherenceapi.response.validation.AdherenceValidationResponse;
import org.motechproject.whp.adherenceapi.service.AdherenceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static org.motechproject.whp.adherenceapi.response.validation.AdherenceValidationResponse.failure;
import static org.motechproject.whp.adherenceapi.response.validation.AdherenceValidationResponse.success;

@Service
public class AdherenceRecordingOverIVR {

    private AdherenceService adherenceService;

    @Autowired
    public AdherenceRecordingOverIVR(AdherenceService adherenceService) {
        this.adherenceService = adherenceService;
    }

    public AdherenceValidationResponse validateInput(AdherenceValidationRequest adherenceValidationRequest) {
        Dosage dosage = adherenceService.dosageForPatient(adherenceValidationRequest.getPatientId());
        if (null == dosage) {
            return failure();
        } else if (dosage.isValidInput(adherenceValidationRequest.doseTakenCount())) {
            return success();
        } else {
            return failure(dosage);
        }
    }
}
