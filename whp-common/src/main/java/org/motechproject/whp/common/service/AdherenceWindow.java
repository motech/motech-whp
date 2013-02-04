package org.motechproject.whp.common.service;

import org.joda.time.LocalDate;
import org.motechproject.model.DayOfWeek;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

import static org.motechproject.model.DayOfWeek.getDayOfWeek;

@Component
public class AdherenceWindow {

    private AdherencePropertyValues adherenceProperties;

    @Autowired
    public AdherenceWindow(AdherencePropertyValues adherenceProperties) {
        this.adherenceProperties = adherenceProperties;
    }

    public boolean isValidAdherenceDay(LocalDate date) {
        return adherenceProperties.validAdherenceDays().contains(getDayOfWeek(date));
    }

    public DayOfWeek lastAdherenceDay() {
        List<DayOfWeek> adherenceDays = adherenceProperties.validAdherenceDays();
        return adherenceDays.get(adherenceDays.size() - 1);
    }
}
