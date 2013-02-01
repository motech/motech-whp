package org.motechproject.whp.common.domain.alerts;

import lombok.Data;

@Data
public class AlertThreshold {
    private Integer threshold;
    private Integer alertSeverity;

    public AlertThreshold(Integer threshold, Integer alertSeverity) {
        this.threshold = threshold;
        this.alertSeverity = alertSeverity;
    }
}
