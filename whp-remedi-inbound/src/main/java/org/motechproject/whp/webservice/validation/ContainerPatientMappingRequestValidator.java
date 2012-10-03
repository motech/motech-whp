package org.motechproject.whp.webservice.validation;

import org.motechproject.whp.common.exception.WHPError;
import org.motechproject.whp.common.validation.RequestValidator;
import org.motechproject.whp.container.service.ContainerService;
import org.motechproject.whp.patient.domain.Patient;
import org.motechproject.whp.patient.domain.Treatment;
import org.motechproject.whp.patient.service.PatientService;
import org.motechproject.whp.refdata.domain.SputumTrackingInstance;
import org.motechproject.whp.webservice.request.ContainerPatientMappingWebRequest;

import static org.motechproject.whp.common.exception.WHPErrorCode.*;

public class ContainerPatientMappingRequestValidator {

    private ContainerService containerService;
    private PatientService patientService;
    private RequestValidator beanValidator;

    public ContainerPatientMappingRequestValidator(ContainerService containerService, PatientService patientService, RequestValidator beanValidator) {
        this.containerService = containerService;
        this.patientService = patientService;
        this.beanValidator = beanValidator;
    }

    public WHPError validate(ContainerPatientMappingWebRequest containerPatientMappingWebRequest) {
        WHPError validationError = validateCaseXml(containerPatientMappingWebRequest);
        if (null == validationError) {
            validationError = validateContainer(containerPatientMappingWebRequest);
        }
        if (null == validationError) {
            validationError = validatePatient(containerPatientMappingWebRequest);
        }
        if (null == validationError) {
            validationError = validateTreatment(containerPatientMappingWebRequest);
        }
        if(null == validationError) {
            validationError = validateInstance(containerPatientMappingWebRequest);
        }
        return validationError;
    }

    private WHPError validateInstance(ContainerPatientMappingWebRequest containerPatientMappingWebRequest) {
        if(!SputumTrackingInstance.isValidMappingInstance(containerPatientMappingWebRequest.getSmear_sample_instance())) {
            return new WHPError(INVALID_SPUTUM_TEST_INSTANCE);
        }
        return null;
    }

    private WHPError validateTreatment(ContainerPatientMappingWebRequest containerPatientMappingWebRequest) {
        Patient patient = patientService.findByPatientId(containerPatientMappingWebRequest.getPatient_id());
        if (!patient.hasTreatment(containerPatientMappingWebRequest.getTb_id())) {
            return new WHPError(NO_SUCH_TREATMENT_EXISTS);
        }
        return null;
    }

    private WHPError validatePatient(ContainerPatientMappingWebRequest containerPatientMappingWebRequest) {
        Patient patient = patientService.findByPatientId(containerPatientMappingWebRequest.getPatient_id());
        if (null == patient) {
            return new WHPError(PATIENT_NOT_FOUND);
        }
        return null;
    }

    private WHPError validateContainer(ContainerPatientMappingWebRequest containerPatientMappingWebRequest) {
        if (!containerService.exists(containerPatientMappingWebRequest.getCase_id())) {
            return new WHPError(INVALID_CONTAINER_ID);
        } else if (containerService.getContainer(containerPatientMappingWebRequest.getCase_id()).getLabResults() == null) {
            return new WHPError(NO_LAB_RESULTS_IN_CONTAINER);
        }
        return null;
    }

    private WHPError validateCaseXml(ContainerPatientMappingWebRequest containerPatientMappingWebRequest) {
        try {
            beanValidator.validate(containerPatientMappingWebRequest, "");
        } catch (RuntimeException e) {
            return new WHPError(CONTAINER_PATIENT_MAPPING_IS_INCOMPLETE);
        }
        return null;
    }
}