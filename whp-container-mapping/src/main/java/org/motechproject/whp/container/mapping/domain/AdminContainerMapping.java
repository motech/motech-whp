package org.motechproject.whp.container.mapping.domain;

import lombok.Data;
import org.ektorp.support.TypeDiscriminator;
import org.motechproject.model.MotechBaseDataObject;

import java.util.ArrayList;
import java.util.List;

@TypeDiscriminator("doc.type == 'AdminContainerMapping'")
@Data
public class AdminContainerMapping extends MotechBaseDataObject {
    private List<ContainerRange> containerRanges = new ArrayList<>();

    public AdminContainerMapping add(ContainerRange containerRange) {
        this.containerRanges.add(containerRange);
        return this;
    }
}
