package org.motechproject.whp.adherenceapi.response.flashing;

import lombok.EqualsAndHashCode;
import org.motechproject.whp.adherenceapi.domain.AdherenceSummary;
import org.motechproject.whp.common.webservice.WebServiceResponse;
import org.motechproject.whp.patient.domain.Patient;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import static ch.lambdaj.Lambda.extract;
import static ch.lambdaj.Lambda.on;
import static org.apache.commons.collections.CollectionUtils.subtract;

@XmlRootElement(name = "adherence_capture_flashing_response")
@EqualsAndHashCode
public class AdherenceFlashingResponse implements Serializable {

    @XmlElement(name = "result")
    private WebServiceResponse result = WebServiceResponse.success;
    @XmlElement(name = "error_code")
    private String errorCode;

    private List<String> patientsWithAdherence = new ArrayList<>();
    private List<String> patientsForProvider = new ArrayList<>();
    private AdherenceStatus adherenceStatus;

    public AdherenceFlashingResponse() {
    }

    public AdherenceFlashingResponse(List<String> patientsWithAdherence, List<String> patientsForProvider) {
        if (null != patientsWithAdherence) {
            this.patientsWithAdherence = patientsWithAdherence;
        }
        if (null != patientsForProvider) {
            this.patientsForProvider = patientsForProvider;
        }
        adherenceStatus = new AdherenceStatus(getPatientRemainingCount(), getPatientGivenCount(), patientsRemaining());
    }

    public AdherenceFlashingResponse(List<String> patientsWithAdherence,
                                     List<String> patientsForProvider,
                                     WebServiceResponse result) {
        this(patientsWithAdherence, patientsForProvider);
        this.result = result;
    }

    @XmlElement(name = "adherence_status")
    public AdherenceStatus getAdherenceStatus() {
        return adherenceStatus;
    }

    public Integer getPatientRemainingCount() {
        return patientsWithoutAdherence();
    }

    public Integer getPatientGivenCount() {
        return patientsWithAdherence.size();
    }

    private Integer patientsWithoutAdherence() {
        return patientsRemaining().size();
    }

    private List<String> patientsRemaining() {
        return new ArrayList<String>(subtract(patientsForProvider, patientsWithAdherence));
    }

    public static AdherenceFlashingResponse failureResponse(String errorCode) {
        AdherenceFlashingResponse response = new AdherenceFlashingResponse();
        response.errorCode = errorCode;
        response.result = WebServiceResponse.failure;
        return response;
    }

    public static AdherenceFlashingResponse successResponse(AdherenceSummary summary) {
        List<String> patientsForProvider = extract(summary.getPatientsUnderProvider(), on(Patient.class).getPatientId());
        return new AdherenceFlashingResponse(summary.getPatientsWithAdherence(), patientsForProvider);
    }
}
