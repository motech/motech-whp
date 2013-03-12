package org.motechproject.whp.container.validation;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang.StringUtils;
import org.motechproject.whp.common.domain.RegistrationInstance;
import org.motechproject.whp.common.error.ErrorWithParameters;
import org.motechproject.whp.common.exception.WHPErrorCode;
import org.motechproject.whp.common.exception.WHPRuntimeException;
import org.motechproject.whp.common.service.ContainerRegistrationValidationPropertyValues;
import org.motechproject.whp.container.contract.ContainerRegistrationRequest;
import org.motechproject.whp.container.domain.ContainerId;
import org.motechproject.whp.container.service.ContainerService;
import org.motechproject.whp.user.service.ProviderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

import static java.lang.String.valueOf;
import static org.apache.commons.lang.StringUtils.length;


@Component
public class CommonContainerRegistrationValidator {

    public static final String PATIENT_FIELD_NAME = "patientName";
    private ContainerService containerService;
    private ProviderService providerService;
    private ContainerRegistrationValidationPropertyValues containerRegistrationValidationPropertyValues;

    @Autowired
    public CommonContainerRegistrationValidator(ContainerService containerService, ProviderService providerService, ContainerRegistrationValidationPropertyValues containerRegistrationValidationPropertyValues) {
        this.containerService = containerService;
        this.providerService = providerService;
        this.containerRegistrationValidationPropertyValues = containerRegistrationValidationPropertyValues;
    }

    public List<ErrorWithParameters> validate(ContainerRegistrationRequest registrationRequest) {
        String containerIdSequence = registrationRequest.getContainerId();
        String instance = registrationRequest.getInstance();
        String providerId = registrationRequest.getProviderId();

        ArrayList<ErrorWithParameters> errors = new ArrayList<>();

        if (StringUtils.isBlank(providerId))
            errors.add(new ErrorWithParameters("provider.id.invalid.error", providerId));

        if (!RegistrationInstance.isValidRegistrationInstance(instance))
            errors.add(new ErrorWithParameters("invalid.instance.error", instance));

        if (!isProviderExists(providerId))
            errors.add(new ErrorWithParameters("provider.not.registered.error", providerId));

        validateContainerId(registrationRequest, containerIdSequence, providerId, errors);

        return errors;
    }

    public List<ErrorWithParameters> validatePatientDetails(ContainerRegistrationRequest registrationRequest) {
        ArrayList<ErrorWithParameters> errors = new ArrayList();
        List<String> mandatoryFieldNames = containerRegistrationValidationPropertyValues.getMandatoryFields();

        for (String fieldName : mandatoryFieldNames) {
            try {
                String value = BeanUtils.getProperty(registrationRequest, fieldName);
                if (StringUtils.isBlank(value))
                    errors.add(new ErrorWithParameters("invalid." + fieldName + ".error", value));
            } catch (Exception e) {
                throw new WHPRuntimeException(WHPErrorCode.INVALID_FIELD_FOR_CONTAINER_REGISTRATION, "Invalid field name : " + fieldName);
            }
        }
        return errors;
    }

    private boolean validateContainerId(ContainerRegistrationRequest registrationRequest, String containerIdSequence, String providerId, ArrayList<ErrorWithParameters> errors) {
        if (isContainerLengthIsInvalid(registrationRequest)) {
            errors.add(new ErrorWithParameters("container.id.length.error", valueOf(registrationRequest.getContainerRegistrationMode().getValidContainerIdLength())));
            return true;
        }

        ContainerId containerId = new ContainerId(providerId, containerIdSequence, registrationRequest.getContainerRegistrationMode());
        if (containerService.exists(containerId.value()))
            errors.add(new ErrorWithParameters("container.already.registered.error"));
        return false;
    }

    private boolean isContainerLengthIsInvalid(ContainerRegistrationRequest registrationRequest) {
        return length(registrationRequest.getContainerId()) !=
                registrationRequest.getContainerRegistrationMode().getValidContainerIdLength();
    }

    private boolean isProviderExists(String providerId) {
        return providerService.findByProviderId(providerId) != null;
    }
}
