package org.motechproject.whp.container.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.util.Properties;

@Component
public class SputumTrackingProperties {

    private Properties properties;

    @Autowired
    public SputumTrackingProperties(@Qualifier("sputumTracking") Properties properties) {
        this.properties = properties;
    }

    public int getContainerIdMaxLength() {
        return Integer.parseInt(properties.getProperty("containerIdMaxLength"));
    }
}
