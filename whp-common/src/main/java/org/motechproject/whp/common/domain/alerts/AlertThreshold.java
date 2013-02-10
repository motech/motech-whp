package org.motechproject.whp.common.domain.alerts;

import lombok.Data;

@Data
public class AlertThreshold {
    private Integer threshold;
    private Integer alertSeverity;
    private String alertSeverityColor;

    public AlertThreshold(Integer threshold, Integer alertSeverity, String alertSeverityColor) {
        this.threshold = threshold;
        this.alertSeverity = alertSeverity;
        this.alertSeverityColor = alertSeverityColor;
    }
}
