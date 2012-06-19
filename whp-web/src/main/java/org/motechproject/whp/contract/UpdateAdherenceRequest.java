package org.motechproject.whp.contract;

import lombok.Data;

import java.util.List;

@Data
public class UpdateAdherenceRequest {
    private String patientId;
    private List<DailyAdherenceRequest> dailyAdherenceRequests;
    private String therapy;
}

