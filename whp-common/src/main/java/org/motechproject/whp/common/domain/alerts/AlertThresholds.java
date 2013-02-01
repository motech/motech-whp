package org.motechproject.whp.common.domain.alerts;

import java.util.ArrayList;
import java.util.List;

public class AlertThresholds {
    private List<AlertThreshold> alertThresholds = new ArrayList<>();
    private final AlertThreshold zeroThreshold = new AlertThreshold(0, 0);

    public AlertThresholds(int... thresholds) {
        alertThresholds.add(zeroThreshold);
        int index = 1;
        for(int threshold : thresholds){
            alertThresholds.add(new AlertThreshold(threshold, index ++));
        }
    }

    public AlertThreshold getThreshold(int value) {
        AlertThreshold threshold = null;
        for(AlertThreshold alertThreshold : alertThresholds){
            if(value < alertThreshold.getThreshold()){
                break;
            } else {
                threshold = alertThreshold;
            }
        }
        return threshold;
    }
}