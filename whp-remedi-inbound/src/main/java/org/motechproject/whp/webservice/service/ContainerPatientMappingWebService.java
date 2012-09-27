package org.motechproject.whp.webservice.service;

import org.motechproject.casexml.service.CaseService;
import org.motechproject.casexml.service.exception.CaseException;
import org.motechproject.whp.common.exception.WHPError;
import org.motechproject.whp.common.validation.RequestValidator;
import org.motechproject.whp.container.domain.Container;
import org.motechproject.whp.container.service.ContainerService;
import org.motechproject.whp.patient.service.PatientService;
import org.motechproject.whp.webservice.exception.WHPCaseException;
import org.motechproject.whp.webservice.request.ContainerPatientMappingWebRequest;
import org.motechproject.whp.webservice.validation.ContainerPatientMappingRequestValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import static org.springframework.http.HttpStatus.NOT_IMPLEMENTED;

@Controller
@RequestMapping("/containerPatientMapping/**")
public class ContainerPatientMappingWebService extends CaseService<ContainerPatientMappingWebRequest> {

    private ContainerService containerService;
    private PatientService patientService;
    private RequestValidator beanValidator;
    private final ContainerPatientMappingRequestValidator containerPatientMappingRequestValidator;

    @Autowired
    public ContainerPatientMappingWebService(ContainerService containerService, PatientService patientService, RequestValidator beanValidator) {
        super(ContainerPatientMappingWebRequest.class);
        this.containerService = containerService;
        this.patientService = patientService;
        this.beanValidator = beanValidator;
        containerPatientMappingRequestValidator = new ContainerPatientMappingRequestValidator(containerService, patientService, beanValidator);
    }

    @Override
    public void updateCase(ContainerPatientMappingWebRequest request) throws CaseException {
        WHPError validationError = containerPatientMappingRequestValidator.validate(request);
        if (null != validationError) {
            throw new WHPCaseException(validationError);
        }
        map(request.getCase_id(), request.getPatient_id());
    }

    private void map(String containerId, String patient_id) {
        Container container = containerService.getContainer(containerId);
        container.mapWith(patient_id);
    }

    @Override
    public void closeCase(ContainerPatientMappingWebRequest containerPatientMappingWebRequest) throws CaseException {
        throw new CaseException("containerPatientMapping does not support Create Case", NOT_IMPLEMENTED);
    }

    @Override
    public void createCase(ContainerPatientMappingWebRequest containerPatientMappingWebRequest) throws CaseException {
        throw new CaseException("containerPatientMapping does not support Create Case", NOT_IMPLEMENTED);
    }
}
