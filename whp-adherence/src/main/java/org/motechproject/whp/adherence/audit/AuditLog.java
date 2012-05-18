package org.motechproject.whp.adherence.audit;


import org.codehaus.jackson.annotate.JsonProperty;
import org.ektorp.support.TypeDiscriminator;
import org.motechproject.model.MotechBaseDataObject;

@TypeDiscriminator("doc.type == 'AuditLog'")
public class AuditLog extends MotechBaseDataObject {

    @JsonProperty
    private int numberOfDosesTaken;
    @JsonProperty
    private String remark;
    @JsonProperty
    private String sourceOfChange;
    @JsonProperty
    private String patientId;
    @JsonProperty
    private String tbId;

    public int numberOfDosesTaken() {
        return numberOfDosesTaken;
    }

    public AuditLog numberOfDosesTaken(int doses) {
        numberOfDosesTaken = doses;
        return this;
    }

    public String remark() {
        return remark;
    }

    public AuditLog remark(String remark) {
        this.remark = remark;
        return this;
    }

    public String sourceOfChange() {
        return this.sourceOfChange;
    }

    public AuditLog sourceOfChange(String sourceOfChange) {
        this.sourceOfChange = sourceOfChange;
        return this;
    }

    public String patientId() {
        return this.patientId;
    }

    public AuditLog patientId(String patientId) {
        this.patientId = patientId;
        return this;
    }

    public String tbId() {
        return this.tbId;
    }

    public AuditLog tbId(String tbId) {
        this.tbId = tbId;
        return this;
    }
}
