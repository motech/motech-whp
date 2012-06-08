package org.motechproject.whp.contract;

import lombok.Data;

@Data
public class DailyAdherence {
    private int day;
    private int pillStatus;

    public DailyAdherence(int day, int pillStatus) {
        this.day = day;
        this.pillStatus = pillStatus;
    }
}
