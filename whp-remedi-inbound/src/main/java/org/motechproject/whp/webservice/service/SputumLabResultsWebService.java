package org.motechproject.whp.webservice.service;

import org.motechproject.casexml.service.CaseService;
import org.motechproject.casexml.service.exception.CaseException;
import org.motechproject.whp.common.exception.WHPError;
import org.motechproject.whp.common.exception.WHPErrorCode;
import org.motechproject.whp.common.exception.WHPRuntimeException;
import org.motechproject.whp.common.validation.RequestValidator;
import org.motechproject.whp.container.domain.Container;
import org.motechproject.whp.container.service.ContainerService;
import org.motechproject.whp.patient.command.UpdateScope;
import org.motechproject.whp.webservice.exception.WHPCaseException;
import org.motechproject.whp.webservice.mapper.SputumLabResultsMapper;
import org.motechproject.whp.webservice.request.SputumLabResultsWebRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import static org.motechproject.whp.common.exception.WHPErrorCode.INVALID_CONTAINER_ID;
import static org.motechproject.whp.common.exception.WHPErrorCode.SPUTUM_LAB_RESULT_IS_INCOMPLETE;

@Controller
@RequestMapping("/sputumLabResults/**")
public class SputumLabResultsWebService extends CaseService<SputumLabResultsWebRequest>{

    private ContainerService containerService;
    private SputumLabResultsMapper sputumLabResultsMapper;
    private RequestValidator validator;

    @Autowired
    public SputumLabResultsWebService(ContainerService containerService, SputumLabResultsMapper sputumLabResultsMapper, RequestValidator validator) {
        super(SputumLabResultsWebRequest.class);
        this.containerService = containerService;
        this.sputumLabResultsMapper = sputumLabResultsMapper;
        this.validator = validator;
    }

    public void updateCase(SputumLabResultsWebRequest sputumLabResultsWebRequest) {
        if(!sputumLabResultsWebRequest.hasCompleteLabResults()) {
            throwWHPCaseException(SPUTUM_LAB_RESULT_IS_INCOMPLETE);
        }

        Container container = containerService.getContainer(sputumLabResultsWebRequest.getCase_id());
        if(container ==null){
            throwWHPCaseException(INVALID_CONTAINER_ID);
        }

        try {
            validator.validate(sputumLabResultsWebRequest, UpdateScope.simpleUpdateScope);
        }catch (WHPRuntimeException e){
            throw new WHPCaseException(e);
        }

        sputumLabResultsMapper.map(sputumLabResultsWebRequest, container);
        containerService.update(container);
    }

    public void createCase(SputumLabResultsWebRequest ccCase) throws CaseException {
        throw new CaseException("sputumLabResults does not support Create Case", HttpStatus.NOT_IMPLEMENTED);
    }

    public void closeCase(SputumLabResultsWebRequest ccCase) throws CaseException {
        throw new CaseException("sputumLabResults does not support Create Case", HttpStatus.NOT_IMPLEMENTED);
    }

    private void throwWHPCaseException(WHPErrorCode sputumLabResultIsIncomplete) {
        throw new WHPCaseException(new WHPError(sputumLabResultIsIncomplete));
    }
}
