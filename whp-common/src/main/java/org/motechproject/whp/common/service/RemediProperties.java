package org.motechproject.whp.common.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.util.Properties;

@Component
public class RemediProperties {

    private Properties properties;

    @Autowired
    public RemediProperties(@Qualifier("remediProperty") Properties properties) {
        this.properties = properties;
    }

    public String getUrl() {
        return properties.getProperty("remedi.url");
    }

    public String getApiKey() {
        return properties.getProperty("remedi.api.key");
    }
}
