package org.motechproject.whp.common.util;

import java.util.Properties;

public class URLEscape {

    public static Properties escape(Properties properties) {
        for (Object o : properties.keySet()) {
            String result = properties.get(o).toString().replace(" ", "\\ ");
            properties.setProperty(o.toString(), result);
        }
        return properties;
    }

}
