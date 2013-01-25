package org.motechproject.whp.common.domain.alerts;

import lombok.Data;
import org.motechproject.model.DayOfWeek;
import org.motechproject.util.DateUtil;

import java.util.List;

@Data
public class AlertConfiguration {
    private PatientAlertType alertType;
    private AlertThresholds alertThresholds;
    private List<DayOfWeek> daysOfWeek;

    public AlertConfiguration(PatientAlertType alertType, AlertThresholds alertThresholds, List<DayOfWeek> daysOfWeek) {
        this.alertType = alertType;
        this.alertThresholds = alertThresholds;
        this.daysOfWeek = daysOfWeek;
    }

    public boolean shouldRunToday() {
        return daysOfWeek.contains(DayOfWeek.getDayOfWeek(DateUtil.today()));
    }
}
