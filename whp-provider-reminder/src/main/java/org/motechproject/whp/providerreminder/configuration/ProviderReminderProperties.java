package org.motechproject.whp.providerreminder.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.util.Properties;

@Component
public class ProviderReminderProperties {

    private Properties properties;

    @Autowired
    public ProviderReminderProperties(@Qualifier("providerReminderProperty") Properties properties) {
        this.properties = properties;
    }

    public String getWeekDay() {
        return properties.getProperty("weekday");
    }

    public String getHour() {
        return properties.getProperty("hour");
    }

    public String getMinutes() {
        return properties.getProperty("minutes");
    }
}
