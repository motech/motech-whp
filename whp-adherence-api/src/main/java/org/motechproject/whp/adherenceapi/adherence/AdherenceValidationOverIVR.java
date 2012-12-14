package org.motechproject.whp.adherenceapi.adherence;

import org.motechproject.whp.adherenceapi.domain.AdherenceCaptureStatus;
import org.motechproject.whp.adherenceapi.domain.Dosage;
import org.motechproject.whp.adherenceapi.domain.ProviderId;
import org.motechproject.whp.adherenceapi.errors.AdherenceErrors;
import org.motechproject.whp.adherenceapi.errors.ValidationRequestErrors;
import org.motechproject.whp.adherenceapi.reporting.AdherenceCaptureReportRequest;
import org.motechproject.whp.adherenceapi.request.AdherenceValidationRequest;
import org.motechproject.whp.adherenceapi.response.validation.AdherenceValidationResponse;
import org.motechproject.whp.adherenceapi.service.AdherenceService;
import org.motechproject.whp.patient.domain.Patient;
import org.motechproject.whp.patient.service.PatientService;
import org.motechproject.whp.reporting.service.ReportingPublisherService;
import org.motechproject.whp.reports.contract.AdherenceCaptureRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static org.motechproject.whp.adherenceapi.domain.AdherenceCaptureStatus.INVALID;
import static org.motechproject.whp.adherenceapi.domain.AdherenceCaptureStatus.VALID;
import static org.motechproject.whp.adherenceapi.response.validation.AdherenceValidationResponse.failure;
import static org.motechproject.whp.adherenceapi.response.validation.AdherenceValidationResponse.success;

@Service
public class AdherenceValidationOverIVR {

    private AdherenceService adherenceService;
    private ReportingPublisherService reportingService;
    private PatientService patientService;

    @Autowired
    public AdherenceValidationOverIVR(AdherenceService adherenceService, ReportingPublisherService reportingService, PatientService patientService) {
        this.adherenceService = adherenceService;
        this.reportingService = reportingService;
        this.patientService = patientService;
    }

    public AdherenceValidationResponse handleValidationRequest(AdherenceValidationRequest request, ProviderId providerId) {
        AdherenceValidationResponse response = validateAdherenceInput(request, providerId);
        reportAdherenceValidation(request, response, providerId);
        return response;
    }

    protected AdherenceValidationResponse validateAdherenceInput(AdherenceValidationRequest adherenceValidationRequest, ProviderId providerId) {
        Dosage dosage = adherenceService.dosageForPatient(adherenceValidationRequest.getPatientId());
        AdherenceErrors errors = adherenceErrors(adherenceValidationRequest, providerId, dosage);
        if (errors.isNotEmpty()) {
            return failure(errors.errorMessage());
        } else if (isValidDose(adherenceValidationRequest, dosage)) {
            return success();
        } else {
            return failure(dosage);
        }
    }

    private void reportAdherenceValidation(AdherenceValidationRequest request, AdherenceValidationResponse response, ProviderId providerId) {
        AdherenceCaptureStatus status = response.failed() ? INVALID : VALID;
        AdherenceCaptureRequest reportingRequest = new AdherenceCaptureReportRequest(
                request,
                providerId,
                !response.failed(),
                status).request();
        reportingService.reportAdherenceCapture(reportingRequest);
    }

    private AdherenceErrors adherenceErrors(AdherenceValidationRequest adherenceValidationRequest, ProviderId providerId, Dosage dosage) {
        return new ValidationRequestErrors(
                !providerId.isEmpty(),
                (dosage != null),
                isValidProviderForPatient(
                        providerId, adherenceValidationRequest.getPatientId()
                )
        );
    }

    private boolean isValidProviderForPatient(ProviderId providerId, String patientId) {
        Patient patient = patientService.findByPatientId(patientId);
        return patient != null && patient.getCurrentTreatment().getProviderId().equals(providerId.value());
    }

    private boolean isValidDose(AdherenceValidationRequest adherenceValidationRequest, Dosage dosage) {
        return dosage.isValidInput(adherenceValidationRequest.doseTakenCount());
    }
}
