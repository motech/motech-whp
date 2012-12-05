package org.motechproject.whp.adherence.service;

import org.motechproject.model.DayOfWeek;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

import static org.motechproject.model.DayOfWeek.getDayOfWeek;

@Component
public class AdherencePropertyValues {

    @Value("#{adherenceProperties['valid.adherence.days.of.week'].split(',')}")
    private List<String> adherenceDays;

    public AdherencePropertyValues() {
    }

    public AdherencePropertyValues(List<String> adherenceDays) {
        this.adherenceDays = adherenceDays;
    }

    public List<DayOfWeek> validAdherenceDays() {
        List<DayOfWeek> result = new ArrayList<>();
        for (String adherenceDay : adherenceDays) {
            result.add(getDayOfWeek(new Integer(adherenceDay)));
        }
        return result;
    }
}
