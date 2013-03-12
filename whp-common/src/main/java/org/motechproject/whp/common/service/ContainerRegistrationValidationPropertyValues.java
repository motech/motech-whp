package org.motechproject.whp.common.service;

import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.List;

@Setter
@Component
public class ContainerRegistrationValidationPropertyValues {

    @Value("#{containerRegistrationValidationProperties[mandatory].split(',')}")
    private List<String> mandatoryFileds;

    public ContainerRegistrationValidationPropertyValues() {
    }

    public List<String> getMandatoryFields() {
        return mandatoryFileds;
    }

    public boolean isMandatory(String fieldName) {
        return getMandatoryFields().contains(fieldName);
    }
}
