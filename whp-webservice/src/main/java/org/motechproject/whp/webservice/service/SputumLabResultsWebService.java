package org.motechproject.whp.webservice.service;

import org.motechproject.casexml.service.CaseService;
import org.motechproject.casexml.service.exception.CaseException;
import org.motechproject.whp.common.exception.WHPErrorCode;
import org.motechproject.whp.common.exception.WHPRuntimeException;
import org.motechproject.whp.container.domain.Container;
import org.motechproject.whp.container.service.ContainerService;
import org.motechproject.whp.webservice.mapper.SputumLabResultsMapper;
import org.motechproject.whp.webservice.request.SputumLabResultsWebRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/sputumLabResults/**")
public class SputumLabResultsWebService extends CaseService<SputumLabResultsWebRequest>{

    private ContainerService containerService;
    private SputumLabResultsMapper sputumLabResultsMapper;

    @Autowired
    public SputumLabResultsWebService(ContainerService containerService, SputumLabResultsMapper sputumLabResultsMapper) {
        super(SputumLabResultsWebRequest.class);
        this.containerService = containerService;
        this.sputumLabResultsMapper = sputumLabResultsMapper;
    }

    public void updateCase(SputumLabResultsWebRequest sputumLabResultsWebRequest) {
        if(!sputumLabResultsWebRequest.hasCompleteLabResults()) {
            throw new WHPRuntimeException(WHPErrorCode.SPUTUM_LAB_RESULT_IS_INCOMPLETE);
        }

        Container container = containerService.getContainer(sputumLabResultsWebRequest.getCase_id());
        if(container ==null){
            throw new WHPRuntimeException(WHPErrorCode.INVALID_CONTAINER_ID);
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

}
