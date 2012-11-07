package org.motechproject.whp.container.util;

import org.motechproject.paginator.contract.FilterParams;

import java.util.Properties;

public class URLEscape {

    public static FilterParams escape(FilterParams properties) {
        for (Object o : properties.keySet()) {
            String result = properties.get(o).toString().replace(" ", "\\ ");
            properties.put(o.toString(), result);
        }
        return properties;
    }

}
