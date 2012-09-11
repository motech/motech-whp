package org.motechproject.whp.container.contract;

import lombok.Data;
import org.apache.commons.lang.StringUtils;
import org.motechproject.whp.container.domain.Instance;

import java.util.ArrayList;
import java.util.List;

@Data
public class RegistrationRequest {
    private String containerId;

    private String instance;

    private String providerId;

    public RegistrationRequest() {
    }

    public RegistrationRequest(String providerId, String containerId, String instance) {
        this.providerId = providerId;
        this.containerId = containerId;
        this.instance = instance;
    }

    public List<String> validate() {
        ArrayList<String> errors = new ArrayList<>();
        if(!StringUtils.isNumeric(containerId) || containerId.length() != 10)
            errors.add("Container Id must be of 10 digits in length");
        if(!Instance.isValid(instance))
            errors.add(String.format("Invalid instance : %s", instance));
        return errors;
    }
}
