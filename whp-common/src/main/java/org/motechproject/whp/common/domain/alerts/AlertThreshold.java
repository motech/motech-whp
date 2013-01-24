package org.motechproject.whp.common.domain.alerts;

import lombok.Data;

@Data
public class AlertThreshold {
    private int threshold;
    private int alertSeverity;

    public AlertThreshold(int threshold, int alertSeverity) {
        this.threshold = threshold;
        this.alertSeverity = alertSeverity;
    }
}
