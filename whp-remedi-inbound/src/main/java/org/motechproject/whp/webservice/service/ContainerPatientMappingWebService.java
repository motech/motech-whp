package org.motechproject.whp.webservice.service;

import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.joda.time.format.DateTimeFormat;
import org.motechproject.casexml.service.CaseService;
import org.motechproject.casexml.service.exception.CaseException;
import org.motechproject.whp.common.domain.SputumTrackingInstance;
import org.motechproject.whp.common.exception.WHPError;
import org.motechproject.whp.common.exception.WHPErrorCode;
import org.motechproject.whp.common.exception.WHPRuntimeException;
import org.motechproject.whp.common.util.WHPDate;
import org.motechproject.whp.container.domain.Container;
import org.motechproject.whp.container.domain.ReasonForContainerClosure;
import org.motechproject.whp.container.service.ContainerService;
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
    private ContainerPatientMappingRequestValidator containerPatientMappingRequestValidator;

    @Autowired
    public ContainerPatientMappingWebService(ContainerService containerService, ContainerPatientMappingRequestValidator validator) {
        super(ContainerPatientMappingWebRequest.class);
        this.containerService = containerService;
        this.containerPatientMappingRequestValidator = validator;
    }

    @Override
    public void updateCase(ContainerPatientMappingWebRequest request) throws CaseException {
        List<WHPError> validationErrors = containerPatientMappingRequestValidator.validate(request);
        if (validationErrors != null && !validationErrors.isEmpty()) {
            throw new WHPCaseException(new WHPRuntimeException(validationErrors, WHPErrorCode.FIELD_VALIDATION_FAILED.getMessage()));
        }
        Container container = containerService.getContainer(request.getCase_id());
        if (!request.isMappingRequest()) {
            container.unMap();
        } else {
            ReasonForContainerClosure closureReasonForMapping = containerService.getClosureReasonForMapping();
            container.mapWith(request.getPatient_id(), request.getTb_id(), SputumTrackingInstance.getInstanceByName(request.getSmear_sample_instance()), closureReasonForMapping, getTbRegistrationDate(request), getDateTimeFor(request.getDate_modified()));
        }
        containerService.updatePatientMapping(container);
    }

    @Override
    public void closeCase(ContainerPatientMappingWebRequest request) throws CaseException {
        throw new CaseException("containerPatientMapping does not support Close Case", NOT_IMPLEMENTED);
    }

    @Override
    public void createCase(ContainerPatientMappingWebRequest request) throws CaseException {
        throw new CaseException("containerPatientMapping does not support Create Case", NOT_IMPLEMENTED);
    }

    private LocalDate getTbRegistrationDate(ContainerPatientMappingWebRequest request) {
        if (SputumTrackingInstance.getInstanceByName(request.getSmear_sample_instance()) == SputumTrackingInstance.PreTreatment)
            return LocalDate.parse(request.getTb_registration_date(), DateTimeFormat.forPattern(WHPDate.DATE_FORMAT));
        return null;
    }

    private DateTime getDateTimeFor(String dateTime) {
        return DateTime.parse(dateTime, DateTimeFormat.forPattern(WHPDate.DATE_TIME_FORMAT));
    }
}
