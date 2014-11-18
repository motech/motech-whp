package org.motechproject.whp.patientivralert.model;

import lombok.Data;
import org.codehaus.jackson.annotate.JsonProperty;

import java.io.Serializable;

@Data
public class PatientAdherenceRecord implements Serializable {
    private static final long serialVersionUID = 1L;
    private String patientId;
    @JsonProperty(value = "msisdn")
    private String mobileNumber;
    @JsonProperty(value = "missing_weeks")
    private int missingWeeks;

    public PatientAdherenceRecord() {
    }

    public PatientAdherenceRecord(String patientId, String mobileNumber, int missingWeeks) {
        this.patientId = patientId;
        this.mobileNumber = mobileNumber;
        this.missingWeeks = missingWeeks;
    }
    
    @Override
    public String toString(){
		return patientId+" : "+mobileNumber+"\n";
    	
    }
}