package org.motechproject.whp.reponse;

import org.motechproject.whp.wgninbound.response.VerificationResult;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

@XmlRootElement(name = "validation_result")
public class VerificationErrorResponse extends VerificationResponse {

    @XmlElement
    private List<ValidationErrors.ValidationError> errors;

    /*Required for JAXB*/
    public VerificationErrorResponse() {
    }

    public VerificationErrorResponse(VerificationResult verificationResult) {
        super(verificationResult);
        setErrorCode(verificationResult);
    }

    private void setErrorCode(VerificationResult result) {
        if (result.isError()) {
            errors = new ValidationErrors(result.getErrors()).value();
        }
    }
}
