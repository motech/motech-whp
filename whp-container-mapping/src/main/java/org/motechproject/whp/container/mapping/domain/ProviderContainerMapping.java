package org.motechproject.whp.container.mapping.domain;

import lombok.Data;
import org.ektorp.support.TypeDiscriminator;
import org.motechproject.model.MotechBaseDataObject;

@TypeDiscriminator("doc.type == 'ProviderContainerMapping'")
@Data
public class ProviderContainerMapping extends MotechBaseDataObject {
    private String providerId;
    private ContainerRanges containerRanges = new ContainerRanges();

    public ProviderContainerMapping add(ContainerRange containerRange) {
        containerRanges.add(containerRange);
        return this;
    }

    public boolean hasContainerId(long containerId) {
        return containerRanges.hasContainerId(containerId);
    }

    public void setProviderId(String providerId) {
        if (providerId != null)
            this.providerId = providerId.toLowerCase();
    }
}
