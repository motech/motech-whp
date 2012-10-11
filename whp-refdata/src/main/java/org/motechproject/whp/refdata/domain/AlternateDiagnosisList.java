package org.motechproject.whp.refdata.domain;

import lombok.Data;
import org.ektorp.support.TypeDiscriminator;
import org.motechproject.model.MotechBaseDataObject;

@Data
@TypeDiscriminator("doc.type == 'AlternateDiagnosisList'")
public class AlternateDiagnosisList extends MotechBaseDataObject {

    private String name;
    private String code;

    //Required for Ektorp
    public AlternateDiagnosisList() {
    }

    public AlternateDiagnosisList(String name, String code) {
        this.name = name;
        this.code = code;
    }
}
