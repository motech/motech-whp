package org.motechproject.whp.webservice.service;

import org.motechproject.casexml.service.CaseService;
import org.motechproject.casexml.service.exception.CaseException;
import org.motechproject.whp.common.exception.WHPError;
import org.motechproject.whp.common.exception.WHPErrorCode;
import org.motechproject.whp.webservice.exception.WHPCaseException;
import org.motechproject.whp.webservice.request.ContainerPatientMappingWebRequest;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import static org.motechproject.whp.common.exception.WHPErrorCode.CONTAINER_PATIENT_MAPPING_IS_INCOMPLETE;

@Controller
@RequestMapping("/containerPatientMapping/**")
public class ContainerPatientMappingWebService extends CaseService<ContainerPatientMappingWebRequest>{

    public ContainerPatientMappingWebService() {
        super(ContainerPatientMappingWebRequest.class);
    }

    @Override
    public void closeCase(ContainerPatientMappingWebRequest containerPatientMappingWebRequest) throws CaseException {
        throw new CaseException("containerPatientMapping does not support Create Case", HttpStatus.NOT_IMPLEMENTED);
    }

    @Override
    public void updateCase(ContainerPatientMappingWebRequest containerPatientMappingWebRequest) throws CaseException {
        if(!containerPatientMappingWebRequest.isWellFormed()) {
            throw new WHPCaseException(new WHPError(CONTAINER_PATIENT_MAPPING_IS_INCOMPLETE));
        }
    }

    @Override
    public void createCase(ContainerPatientMappingWebRequest containerPatientMappingWebRequest) throws CaseException {
        throw new CaseException("containerPatientMapping does not support Create Case", HttpStatus.NOT_IMPLEMENTED);
    }
}
