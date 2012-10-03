package org.motechproject.whp.webservice.service;

import org.motechproject.casexml.service.CaseService;
import org.motechproject.casexml.service.exception.CaseException;
import org.motechproject.whp.common.exception.WHPError;
import org.motechproject.whp.common.validation.RequestValidator;
import org.motechproject.whp.container.domain.Container;
import org.motechproject.whp.container.service.ContainerService;
import org.motechproject.whp.patient.service.PatientService;
import org.motechproject.whp.refdata.domain.SputumTrackingInstance;
import org.motechproject.whp.webservice.exception.WHPCaseException;
import org.motechproject.whp.webservice.request.ContainerPatientMappingWebRequest;
import org.motechproject.whp.webservice.validation.ContainerPatientMappingRequestValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

import static org.springframework.http.HttpStatus.NOT_IMPLEMENTED;

@Controller
@RequestMapping("/containerPatientMapping/**")
public class ContainerPatientMappingWebService extends CaseService<ContainerPatientMappingWebRequest> {

    private ContainerService containerService;
    private RequestValidator beanValidator;
    private final ContainerPatientMappingRequestValidator containerPatientMappingRequestValidator;

    @Autowired
    public ContainerPatientMappingWebService(ContainerService containerService, PatientService patientService, RequestValidator beanValidator) {
        super(ContainerPatientMappingWebRequest.class);
        this.containerService = containerService;
        this.beanValidator = beanValidator;
        containerPatientMappingRequestValidator = new ContainerPatientMappingRequestValidator(containerService, patientService, beanValidator);
    }

    @Override
    public void updateCase(ContainerPatientMappingWebRequest request) throws CaseException {
        WHPError validationError = containerPatientMappingRequestValidator.validate(request);
        if (null != validationError) {
            throw new WHPCaseException(validationError);
        }
        map(request);
    }

    private void map(ContainerPatientMappingWebRequest request) {
        List<Container> mappedToPatient = containerService.findByPatientId(request.getPatient_id());

        if (mappedToPatient != null && !mappedToPatient.isEmpty()) {
            for (Container container : mappedToPatient) {
                if (isDuplicateRequest(request, container)) {
                    return;
                } else if (!isContainerInstanceMappingChanged(request, container)) {
                    container.unMap();
                    break;
                }
                containerService.update(container);
            }
        }
        Container container = containerService.getContainer(request.getCase_id());
        container.mapWith(request.getPatient_id(), request.getTb_id(), SputumTrackingInstance.getInstanceByName(request.getSmear_sample_instance()));
        containerService.update(container);
    }

    private boolean isContainerInstanceMappingChanged(ContainerPatientMappingWebRequest request, Container container) {
        return !container.getContainerId().equals(request.getCase_id())
                && !container.getMappingInstance().name().equals(request.getSmear_sample_instance());
    }

    private boolean isDuplicateRequest(ContainerPatientMappingWebRequest request, Container container) {
        return container.getContainerId().equals(request.getCase_id())
                && container.getPatientId().equals(request.getPatient_id())
                && container.getTbId().equals(request.getTb_id())
                && container.getMappingInstance().name().equals(request.getSmear_sample_instance());
    }

    @Override
    public void closeCase(ContainerPatientMappingWebRequest request) throws CaseException {
        throw new CaseException("containerPatientMapping does not support Close Case", NOT_IMPLEMENTED);
    }

    @Override
    public void createCase(ContainerPatientMappingWebRequest request) throws CaseException {
        throw new CaseException("containerPatientMapping does not support Create Case", NOT_IMPLEMENTED);
    }
}
