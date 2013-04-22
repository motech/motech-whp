package org.motechproject.whp.patientivralert.model;

import lombok.Data;
import org.codehaus.jackson.annotate.JsonProperty;

import java.io.Serializable;
import java.util.List;

@Data
public class PatientAlertRequest implements Serializable {
    private static final long serialVersionUID = 1L;

    private String requestId;
    private String batchId;
    private String messageId;
    @JsonProperty(value = "call_type")
    private String callType;
    private List<PatientAdherenceRecord> data;
}
