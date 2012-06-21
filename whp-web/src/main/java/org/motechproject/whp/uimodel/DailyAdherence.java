package org.motechproject.whp.uimodel;

import lombok.Data;

@Data
public class DailyAdherence {

    private int day;
    private int pillStatus;
    private boolean adherenceCapturedDuringPausedPeriod;
    private boolean futureAdherence;
    private String providerId;

    public DailyAdherence(int day, int pillStatus, String providerId, boolean adherenceCapturedDuringPausedPeriod, boolean futureAdherence) {
        this.day = day;
        this.pillStatus = pillStatus;
        this.providerId = providerId;
        this.adherenceCapturedDuringPausedPeriod = adherenceCapturedDuringPausedPeriod;
        this.futureAdherence = futureAdherence;
    }
}
