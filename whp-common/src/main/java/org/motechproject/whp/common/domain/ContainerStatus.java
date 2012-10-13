package org.motechproject.whp.common.domain;
import java.util.ArrayList;
import java.util.List;

public enum ContainerStatus {
    Open, Closed;

    public static List<String> allNames() {
        List<String> names = new ArrayList<>();
        for(ContainerStatus containerStatus : values()) {
            names.add(containerStatus.name());
        }
        return names;
    }
}
