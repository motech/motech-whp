package org.motechproject.whp.request;

import lombok.Data;
import org.motechproject.whp.request.DailyAdherenceRequest;

import java.util.List;

@Data
public class UpdateAdherenceRequest {
    private String patientId;
    private List<DailyAdherenceRequest> dailyAdherenceRequests;
    private String therapy;
}

