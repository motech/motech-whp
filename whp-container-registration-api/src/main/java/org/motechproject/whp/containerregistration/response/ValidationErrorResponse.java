package org.motechproject.whp.containerregistration.response;

import org.motechproject.whp.containerregistration.api.response.VerificationResult;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

@XmlRootElement(name = "validation_result")
public class ValidationErrorResponse extends VerificationResponse {

    @XmlElement
    private List<ValidationErrors.ValidationError> errors;

    /*Required for JAXB*/
    public ValidationErrorResponse() {
    }

    public ValidationErrorResponse(VerificationResult verificationResult) {
        super(verificationResult);
        setErrorCode(verificationResult);
    }

    private void setErrorCode(VerificationResult result) {
        if (result.isError()) {
            errors = new ValidationErrors(result.getErrors()).value();
        }
    }
}
