package org.motechproject.whp.patient.domain;

import lombok.Data;
import org.ektorp.support.TypeDiscriminator;
import org.motechproject.model.MotechBaseDataObject;

@Data
@TypeDiscriminator("doc.type == 'CmfLocation'")
public class CmfLocation extends MotechBaseDataObject {

    private String location;

    // Required for ektorp
    public CmfLocation() {
    }

    public CmfLocation(String location) {
        this.location = location;
    }

}
