package org.motechproject.whp.functional.framework;

import org.apache.commons.lang.StringUtils;

public class TestEnvironment {
    public static String webserverName() {
        return propertyValueOfDefault("webserver.name", "localhost");
    }

    private static String propertyValueOfDefault(String propertyName, String defaultValue) {
        String property = System.getProperty(propertyName);
        if (StringUtils.isEmpty(property)) return defaultValue;
        return property;
    }

    public static String webserverPort() {
        return propertyValueOfDefault("jetty.port", "8080");
    }
}
