package org.motechproject.whp.common.service;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.List;

@Data
@Component
public class AlertsPropertiesValues {

    @Value("#{alertsProperties['adherence.missing.alert.numberOfWeeks'].split(',')}")
    private List<Integer> adherenceMissingWeeks;

}
