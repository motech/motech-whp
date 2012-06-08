package org.motechproject.whp.contract;

import lombok.Data;

@Data
public class DailyAdherence {
    private int day;
    private int pillStatus;
    private boolean adherenceCapturedDuringPausedPeriod;

    public DailyAdherence(int day, int pillStatus, boolean adherenceCapturedDuringPausedPeriod) {
        this.day = day;
        this.pillStatus = pillStatus;
        this.adherenceCapturedDuringPausedPeriod = adherenceCapturedDuringPausedPeriod;
    }
}
