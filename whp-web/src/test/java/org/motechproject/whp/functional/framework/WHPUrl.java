package org.motechproject.whp.functional.framework;


import org.motechproject.whp.functional.framework.testdata.TestEntity;

import static org.motechproject.whp.functional.framework.TestEnvironment.webserverName;
import static org.motechproject.whp.functional.framework.TestEnvironment.webserverPort;

public class WHPUrl {
    public static String base() {
        return String.format("http://%s:%s/whp/", webserverName(), webserverPort());
    }

    public static String baseFor(String resource) {
        return base() + resource;
    }

    public static String viewPageUrlFor(TestEntity testEntity) {
        return String.format("%s%s/%s", base(), testEntity.resourceName(), testEntity.id());
    }

}
