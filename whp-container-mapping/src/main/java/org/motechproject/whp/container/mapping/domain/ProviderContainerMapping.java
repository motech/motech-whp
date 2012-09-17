package org.motechproject.whp.container.mapping.domain;

import lombok.Data;
import org.ektorp.support.TypeDiscriminator;
import org.motechproject.model.MotechBaseDataObject;

import java.util.ArrayList;
import java.util.List;

@TypeDiscriminator("doc.type == 'ProviderContainerMapping'")
@Data
public class ProviderContainerMapping extends MotechBaseDataObject {
    private String providerId;
    private List<ContainerRange> containerRanges = new ArrayList<>();

    public ProviderContainerMapping add(ContainerRange containerRange) {
        containerRanges.add(containerRange);
        return this;
    }
}
