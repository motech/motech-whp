package org.motechproject.whp.container.mapping.domain;

import lombok.Data;
import org.ektorp.support.TypeDiscriminator;
import org.motechproject.model.MotechBaseDataObject;

@TypeDiscriminator("doc.type == 'AdminContainerMapping'")
@Data
public class AdminContainerMapping extends MotechBaseDataObject {
    private ContainerRanges containerRanges = new ContainerRanges();

    public AdminContainerMapping add(ContainerRange containerRange) {
        this.containerRanges.add(containerRange);
        return this;
    }

    public boolean hasContainerId(long containerId) {
        return containerRanges.hasContainerId(containerId);
    }
}
