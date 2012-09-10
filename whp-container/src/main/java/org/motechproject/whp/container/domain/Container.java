package org.motechproject.whp.container.domain;

import lombok.Data;
import org.ektorp.support.TypeDiscriminator;
import org.motechproject.model.MotechBaseDataObject;

@Data
@TypeDiscriminator("doc.type == 'Container'")
public class Container extends MotechBaseDataObject {

    private String containerId;

    private String instance;

    private String providerId;

    // Required for ektorp
    public Container() {
    }

    public Container(String providerId, String containerId, String instance) {
        this.providerId = providerId;
        this.containerId = containerId;
        this.instance = instance;
    }


}
