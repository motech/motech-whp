package org.motechproject.whp.container.domain;

import lombok.Data;
import org.codehaus.jackson.annotate.JsonIgnore;
import org.ektorp.support.TypeDiscriminator;
import org.motechproject.model.MotechBaseDataObject;

import static org.motechproject.whp.container.WHPContainerConstants.TB_NEGATIVE_CODE;

@Data
@TypeDiscriminator("doc.type == 'ReasonForContainerClosure'")
public class ReasonForContainerClosure extends MotechBaseDataObject {

    public static enum ApplicableTreatmentPhase {PreTreatment, InTreatment, All}

    private String name;
    private String code;
    private ApplicableTreatmentPhase phase;
    private boolean canFilterOn;

    //Required for Ektorp
    public ReasonForContainerClosure() {
    }

    public ReasonForContainerClosure(String name, String code) {
        this.name = name;
        this.code = code;
    }

    public ReasonForContainerClosure(String name, String code, ApplicableTreatmentPhase phase, boolean canFilterOn) {
        this(name, code);
        this.phase = phase;
        this.canFilterOn = canFilterOn;
    }

    @JsonIgnore
    public boolean isTbNegative() {
        return code.equals(TB_NEGATIVE_CODE);
    }
}
