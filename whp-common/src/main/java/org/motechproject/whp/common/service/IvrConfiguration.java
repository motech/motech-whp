package org.motechproject.whp.common.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.util.Properties;

@Component
public class IvrConfiguration {

    private Properties properties;

    @Autowired
    public IvrConfiguration(@Qualifier("ivrProperty") Properties properties) {
        this.properties = properties;
    }

    public String getProviderReminderUrl() {
        return properties.getProperty("provider.reminder.url");
    }
}
