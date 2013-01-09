package org.motechproject.whp.adherenceapi.response.validation;

import lombok.EqualsAndHashCode;
import org.motechproject.whp.common.webservice.WebServiceResponse;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "adherence_call_status_response")
@EqualsAndHashCode
public class AdherenceCallStatusValidationResponse {

    @XmlElement(name = "result")
    private WebServiceResponse result = WebServiceResponse.success;

    @XmlElement(name = "error")
    private AdherenceResponseError error;

    public static AdherenceCallStatusValidationResponse success(){
        return new AdherenceCallStatusValidationResponse();
    }

    public static AdherenceCallStatusValidationResponse failure(String errorCode){
        AdherenceCallStatusValidationResponse response = new AdherenceCallStatusValidationResponse();
        response.result = WebServiceResponse.failure;
        response.error = new AdherenceResponseError(errorCode);
        return response;
    }

    public boolean failed() {
        return (null != error) || (result == WebServiceResponse.failure);
    }
}
