package org.motechproject.whp.container.domain;

import lombok.Data;
import org.ektorp.support.TypeDiscriminator;
import org.motechproject.model.MotechBaseDataObject;

@Data
@TypeDiscriminator("doc.type == 'AlternateDiagnosis'")
public class AlternateDiagnosis extends MotechBaseDataObject {

    private String name;
    private String code;

    //Required for Ektorp
    public AlternateDiagnosis() {
    }

    public AlternateDiagnosis(String name, String code) {
        this.name = name;
        this.code = code;
    }
}
