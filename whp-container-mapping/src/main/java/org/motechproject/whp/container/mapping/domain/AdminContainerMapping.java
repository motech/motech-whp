package org.motechproject.whp.container.mapping.domain;

import lombok.Data;
import org.ektorp.support.TypeDiscriminator;
import org.motechproject.model.MotechBaseDataObject;

@TypeDiscriminator("doc.type == 'AdminContainerMapping'")
@Data
public class AdminContainerMapping extends MotechBaseDataObject {
    private ContainerRange containerRange;

    public AdminContainerMapping add(ContainerRange containerRange) {
        this.containerRange = containerRange;
        return this;
    }
}
