package org.motechproject.whp.refdata.domain;

import lombok.Data;
import org.codehaus.jackson.annotate.JsonIgnore;
import org.ektorp.support.TypeDiscriminator;
import org.motechproject.model.MotechBaseDataObject;

import static org.motechproject.whp.container.WHPContainerConstants.TB_NEGATIVE_CODE;

@Data
@TypeDiscriminator("doc.type == 'ReasonForContainerClosure'")
public class ReasonForContainerClosure extends MotechBaseDataObject {

    private String name;
    private String code;

    //Required for Ektorp
    public ReasonForContainerClosure() {
    }

    public ReasonForContainerClosure(String name, String code) {
        this.name = name;
        this.code = code;
    }

    @JsonIgnore
    public boolean isTbNegative() {
        return code.equals(TB_NEGATIVE_CODE);
    }
}
