package org.motechproject.whp.reponse;

import org.motechproject.whp.wgninbound.response.VerificationResult;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "validation_result")
public class VerificationResponse {

    @XmlElement
    private String result;

    /*Required for JAXB*/
    public VerificationResponse(){
    }

    public VerificationResponse(VerificationResult verificationResult) {
        setResult(verificationResult.isSuccess());
    }

    private void setResult(boolean success) {
        if (success) {
            result = "success";
        } else {
            result = "failure";
        }
    }
}
