package org.motechproject.whp.refdata.domain;

import lombok.Data;
import org.ektorp.support.TypeDiscriminator;
import org.motechproject.model.MotechBaseDataObject;

@Data
@TypeDiscriminator("doc.type == 'ReasonForContainerClosure'")
public class ReasonForContainerClosure extends MotechBaseDataObject {

    private String name;

    //Required for Ektorp
    public ReasonForContainerClosure() {
    }

    public ReasonForContainerClosure(String name) {
        this.name = name;
    }
}
