package org.motechproject.whp.webservice.validation;

import org.motechproject.whp.common.exception.WHPError;
import org.motechproject.whp.common.exception.WHPRuntimeException;
import org.motechproject.whp.common.validation.RequestValidator;
import org.motechproject.whp.container.service.ContainerService;
import org.motechproject.whp.patient.domain.Patient;
import org.motechproject.whp.patient.service.PatientService;
import org.motechproject.whp.refdata.domain.SputumTrackingInstance;
import org.motechproject.whp.webservice.request.ContainerPatientMappingWebRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

import static org.motechproject.whp.common.exception.WHPErrorCode.*;

@Component
public class ContainerPatientMappingRequestValidator {

    private ContainerService containerService;
    private PatientService patientService;
    private RequestValidator beanValidator;

    @Autowired
    public ContainerPatientMappingRequestValidator(ContainerService containerService, PatientService patientService, RequestValidator beanValidator) {
        this.containerService = containerService;
        this.patientService = patientService;
        this.beanValidator = beanValidator;
    }

    public List<WHPError> validate(ContainerPatientMappingWebRequest containerPatientMappingWebRequest) {
        ArrayList<WHPError> validationErrors = new ArrayList<WHPError>();
        validateCaseXml(containerPatientMappingWebRequest, validationErrors);
        validateContainer(containerPatientMappingWebRequest, validationErrors);
        validatePatient(containerPatientMappingWebRequest, validationErrors);
        validateTreatment(containerPatientMappingWebRequest, validationErrors);
        validateInstance(containerPatientMappingWebRequest, validationErrors);
        return validationErrors;
    }

    private void validateInstance(ContainerPatientMappingWebRequest containerPatientMappingWebRequest, ArrayList<WHPError> validationErrors) {
        if (!SputumTrackingInstance.isValidMappingInstance(containerPatientMappingWebRequest.getSmear_sample_instance())) {
            validationErrors.add(new WHPError(INVALID_SPUTUM_TEST_INSTANCE));
        }
    }

    private void validateTreatment(ContainerPatientMappingWebRequest containerPatientMappingWebRequest, ArrayList<WHPError> validationErrors) {
        Patient patient = patientService.findByPatientId(containerPatientMappingWebRequest.getPatient_id());
        if (patient != null && !patient.hasTreatment(containerPatientMappingWebRequest.getTb_id())) {
            validationErrors.add(new WHPError(NO_SUCH_TREATMENT_EXISTS));
        }
    }

    private void validatePatient(ContainerPatientMappingWebRequest containerPatientMappingWebRequest, ArrayList<WHPError> validationErrors) {
        Patient patient = patientService.findByPatientId(containerPatientMappingWebRequest.getPatient_id());
        if (null == patient) {
            validationErrors.add(new WHPError(PATIENT_NOT_FOUND));
        }
    }

    private void validateContainer(ContainerPatientMappingWebRequest containerPatientMappingWebRequest, ArrayList<WHPError> validationErrors) {
        if (!containerService.exists(containerPatientMappingWebRequest.getCase_id())) {
            validationErrors.add(new WHPError(INVALID_CONTAINER_ID));
        } else if (containerService.getContainer(containerPatientMappingWebRequest.getCase_id()).getLabResults() == null) {
            validationErrors.add(new WHPError(NO_LAB_RESULTS_IN_CONTAINER));
        }
    }

    private void validateCaseXml(ContainerPatientMappingWebRequest containerPatientMappingWebRequest, ArrayList<WHPError> validationErrors) {
        try {
            beanValidator.validate(containerPatientMappingWebRequest, "");
        } catch (RuntimeException e) {
            validationErrors.addAll(((WHPRuntimeException) e).getErrors());
        }
    }
}