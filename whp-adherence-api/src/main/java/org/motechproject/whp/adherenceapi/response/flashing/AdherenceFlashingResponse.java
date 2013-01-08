package org.motechproject.whp.adherenceapi.response.flashing;

import lombok.EqualsAndHashCode;
import org.motechproject.whp.adherenceapi.domain.AdherenceSummary;
import org.motechproject.whp.common.webservice.WebServiceResponse;
import org.motechproject.whp.patient.domain.Patient;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.util.List;

import static ch.lambdaj.Lambda.extract;
import static ch.lambdaj.Lambda.on;

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

    public AdherenceFlashingResponse(String providerId, List<String> patientsWithAdherence, List<String> patientsForProvider) {
        adherenceStatus = new AdherenceStatus(providerId, patientsForProvider, patientsWithAdherence);
    }

    @XmlElement(name = "adherence_status")
    public AdherenceStatus getAdherenceStatus() {
        return adherenceStatus;
    }

    public Integer getPatientRemainingCount() {
        return adherenceStatus.getPatientRemainingCount();
    }

    public Integer getPatientGivenCount() {
        return adherenceStatus.getPatientGivenCount();
    }

    public static AdherenceFlashingResponse failureResponse(String errorCode) {
        return failureResponse(errorCode, new AdherenceFlashingResponse());
    }

    public static AdherenceFlashingResponse failureResponse(AdherenceSummary summary, String errorCode) {
        return failureResponse(errorCode, buildResponse(summary));
    }

    public static AdherenceFlashingResponse successResponse(AdherenceSummary summary) {
        return buildResponse(summary);
    }

    public static AdherenceFlashingResponse failureResponse(String errorCode, AdherenceFlashingResponse response) {
        response.errorCode = errorCode;
        response.result = WebServiceResponse.failure;
        return response;
    }

    private static AdherenceFlashingResponse buildResponse(AdherenceSummary summary) {
        List<String> patientsForProvider = extract(summary.getPatientsUnderProvider(), on(Patient.class).getPatientId());
        return new AdherenceFlashingResponse(summary.getProviderId(), summary.getPatientsWithAdherence(), patientsForProvider);
    }
}
