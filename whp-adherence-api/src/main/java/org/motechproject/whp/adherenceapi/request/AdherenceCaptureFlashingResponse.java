package org.motechproject.whp.adherenceapi.request;

import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import static org.apache.commons.collections.CollectionUtils.subtract;

@EqualsAndHashCode
public class AdherenceCaptureFlashingResponse implements Serializable {

    private List<String> patientsWithAdherence = new ArrayList<>();
    private List<String> patientsForProvider = new ArrayList<>();

    public AdherenceCaptureFlashingResponse(List<String> patientsWithAdherence, List<String> patientsForProvider) {
        if (null != patientsWithAdherence) {
            this.patientsWithAdherence = patientsWithAdherence;
        }
        if (null != patientsForProvider) {
            this.patientsForProvider = patientsForProvider;
        }
    }

    public int getPatientRemainingCount() {
        return patientsWithoutAdherence();
    }

    public int getPatientGivenCount() {
        return patientsWithAdherence.size();
    }

    private int patientsWithoutAdherence() {
        return subtract(patientsForProvider, patientsWithAdherence).size();
    }
}
