package org.motechproject.whp.uimodel;

import lombok.Data;

import java.util.List;

@Data
public class UpdateAdherenceRequest {
    private String patientId;
    private List<DailyAdherenceRequest> dailyAdherenceRequests;
    private String therapy;
}

