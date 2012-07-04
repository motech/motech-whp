package org.motechproject.whp.functional.framework;

import java.io.IOException;
import java.util.Properties;

import static org.apache.commons.lang.StringUtils.isNotEmpty;

public class TestEnvironment {

    public static String webserverName() {
        return propertyValueOfDefault("webserver.name", "localhost");
    }

    public static String webserverPort() {
        return propertyValueOfDefault("jetty.port", "8080");
    }

    private static String propertyValueOfDefault(String propertyName, String defaultValue) {
        if (isNotEmpty(getSystemProperty(propertyName))) {
            return getSystemProperty(propertyName);
        } else {
            return defaultValue;
        }
    }

    private static String getSystemProperty(String propertyName) {
        return System.getProperty(propertyName);
    }

}
