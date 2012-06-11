package org.motechproject.whp.contract;

import lombok.Data;

@Data
public class DailyAdherence {
    private int day;
    private int pillStatus;
    private boolean adherenceCapturedDuringPausedPeriod;
    private String providerId;

    public DailyAdherence(int day, int pillStatus, String providerId,boolean adherenceCapturedDuringPausedPeriod) {
        this.day = day;
        this.pillStatus = pillStatus;
        this.providerId = providerId;
        this.adherenceCapturedDuringPausedPeriod = adherenceCapturedDuringPausedPeriod;
    }
}
