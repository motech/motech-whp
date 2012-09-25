package org.motechproject.whp.container.mapping.domain;

import java.util.ArrayList;

public class ContainerRanges extends ArrayList<ContainerRange> {

    public boolean hasContainerId(long containerId) {
        for(ContainerRange containerRange: this){
            if(containerRange.includes(containerId)) {
                return true;
            }
        }
        return false;
    }
}
