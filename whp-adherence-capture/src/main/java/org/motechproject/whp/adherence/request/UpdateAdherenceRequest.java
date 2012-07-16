package org.motechproject.whp.adherence.request;

import lombok.Data;

import java.util.List;

@Data
public class UpdateAdherenceRequest {
    private String patientId;
    private List<DailyAdherenceRequest> dailyAdherenceRequests;
}

