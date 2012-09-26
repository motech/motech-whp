package org.motechproject.whp.webservice.service;

import org.motechproject.casexml.service.CaseService;
import org.motechproject.casexml.service.exception.CaseException;
import org.motechproject.whp.common.exception.WHPError;
import org.motechproject.whp.common.exception.WHPErrorCode;
import org.motechproject.whp.common.validation.RequestValidator;
import org.motechproject.whp.container.service.ContainerService;
import org.motechproject.whp.patient.command.UpdateScope;
import org.motechproject.whp.patient.domain.Patient;
import org.motechproject.whp.patient.domain.Treatment;
import org.motechproject.whp.patient.service.PatientService;
import org.motechproject.whp.webservice.exception.WHPCaseException;
import org.motechproject.whp.webservice.request.ContainerPatientMappingWebRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import static org.motechproject.whp.common.exception.WHPErrorCode.*;
import static org.springframework.http.HttpStatus.NOT_IMPLEMENTED;

@Controller
@RequestMapping("/containerPatientMapping/**")
public class ContainerPatientMappingWebService extends CaseService<ContainerPatientMappingWebRequest>{

    private ContainerService containerService;
    private PatientService patientService;
    private RequestValidator beanValidator;

    @Autowired
    public ContainerPatientMappingWebService(ContainerService containerService, PatientService patientService, RequestValidator beanValidator) {
        super(ContainerPatientMappingWebRequest.class);
        this.containerService = containerService;
        this.patientService = patientService;
        this.beanValidator = beanValidator;
    }

    @Override
    public void closeCase(ContainerPatientMappingWebRequest containerPatientMappingWebRequest) throws CaseException {
        throw new CaseException("containerPatientMapping does not support Create Case", NOT_IMPLEMENTED);
    }

    @Override
    public void updateCase(ContainerPatientMappingWebRequest containerPatientMappingWebRequest) throws CaseException {
        validateCaseXml(containerPatientMappingWebRequest);
        validateContainer(containerPatientMappingWebRequest);
        validatePatient(containerPatientMappingWebRequest);
        validateTreatment(containerPatientMappingWebRequest);
    }

    private void validateTreatment(ContainerPatientMappingWebRequest containerPatientMappingWebRequest) {
        Patient patient = patientService.findByPatientId(containerPatientMappingWebRequest.getPatient_id());
        Treatment currentTreatment = patient.getCurrentTreatment();
        if(patient.getCurrentTherapy().isClosed()) {
             throw new WHPCaseException(new WHPError(TREATMENT_ALREADY_CLOSED));
        }
        if(currentTreatment ==null || !currentTreatment.getTbId().equals(containerPatientMappingWebRequest.getTb_id())){
            throw new WHPCaseException(new WHPError(NO_EXISTING_TREATMENT_FOR_CASE));
        }
    }

    private void validatePatient(ContainerPatientMappingWebRequest containerPatientMappingWebRequest) {
        Patient patient = patientService.findByPatientId(containerPatientMappingWebRequest.getPatient_id());
        if(null == patient) {
            throw new WHPCaseException(new WHPError(PATIENT_NOT_FOUND));
        }
    }

    private void validateContainer(ContainerPatientMappingWebRequest containerPatientMappingWebRequest) {
        if(!containerService.exists(containerPatientMappingWebRequest.getCase_id())){
            throw new WHPCaseException(new WHPError(INVALID_CONTAINER_ID));
        } else if(containerService.getContainer(containerPatientMappingWebRequest.getCase_id()).getLabResults() == null) {
            throw new WHPCaseException(new WHPError(NO_LAB_RESULTS_IN_CONTAINER));
        }
    }

    private void validateCaseXml(ContainerPatientMappingWebRequest containerPatientMappingWebRequest) {
        try {
            beanValidator.validate(containerPatientMappingWebRequest, "");
        } catch (RuntimeException e) {
            throw new WHPCaseException(new WHPError(CONTAINER_PATIENT_MAPPING_IS_INCOMPLETE));
        }
    }

    @Override
    public void createCase(ContainerPatientMappingWebRequest containerPatientMappingWebRequest) throws CaseException {
        throw new CaseException("containerPatientMapping does not support Create Case", NOT_IMPLEMENTED);
    }
}
