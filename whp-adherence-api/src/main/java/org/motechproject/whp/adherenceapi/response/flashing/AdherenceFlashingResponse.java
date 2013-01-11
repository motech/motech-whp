package org.motechproject.whp.adherenceapi.response.flashing;

import lombok.EqualsAndHashCode;
import org.motechproject.whp.adherence.domain.AdherenceSummaryByProvider;
import org.motechproject.whp.common.webservice.WebServiceResponse;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;

@XmlRootElement(name = "adherence_capture_flashing_response")
@EqualsAndHashCode
public class AdherenceFlashingResponse implements Serializable {

    @XmlElement(name = "result")
    private WebServiceResponse result = WebServiceResponse.success;

    @XmlElement(name = "error_code")
    private String errorCode;

    private AdherenceStatus adherenceStatus;

    public AdherenceFlashingResponse() {
    }

    public AdherenceFlashingResponse(AdherenceSummaryByProvider adherenceSummary) {
        adherenceStatus = new AdherenceStatus(adherenceSummary);
    }

    @XmlElement(name = "adherence_status")
    public AdherenceStatus getAdherenceStatus() {
        return adherenceStatus;
    }


    public static AdherenceFlashingResponse failureResponse(String errorCode) {
        return failureResponse(errorCode, new AdherenceFlashingResponse());
    }

    public static AdherenceFlashingResponse failureResponse(AdherenceSummaryByProvider summary, String errorCode) {
        return failureResponse(errorCode, buildResponse(summary));
    }

    public static AdherenceFlashingResponse successResponse(AdherenceSummaryByProvider summary) {
        return buildResponse(summary);
    }

    public static AdherenceFlashingResponse failureResponse(String errorCode, AdherenceFlashingResponse response) {
        response.errorCode = errorCode;
        response.result = WebServiceResponse.failure;
        return response;
    }

    private static AdherenceFlashingResponse buildResponse(AdherenceSummaryByProvider summary) {
        return new AdherenceFlashingResponse(summary);
    }
}
