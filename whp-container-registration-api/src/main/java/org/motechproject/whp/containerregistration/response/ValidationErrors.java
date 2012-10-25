package org.motechproject.whp.containerregistration.response;

import org.motechproject.whp.common.exception.WHPError;

import javax.xml.bind.annotation.XmlElement;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ValidationErrors {

    private List<ValidationError> validationErrors;

    /*Required for JAXB*/
    public ValidationErrors() {
        validationErrors = Collections.emptyList();
    }

    public ValidationErrors(List<WHPError> errors) {
        validationErrors = new ArrayList<>();
        for (WHPError error : errors) {
            validationErrors.add(new ValidationError(error));
        }
    }

    public List<ValidationError> value() {
        return validationErrors;
    }

    public static class ValidationError {

        @XmlElement(name = "error_code")
        private String errorCode;
        @XmlElement(name = "error_description")
        private String errorDescription;

        /*Required for JAXB*/
        public ValidationError() {
        }

        public ValidationError(WHPError error) {
            errorCode = error.getErrorCode().name();
            errorDescription = error.getMessage();
        }

        public String errorCode() {
            return errorCode;
        }

        public String errorDescription() {
            return errorDescription;
        }
    }
}
