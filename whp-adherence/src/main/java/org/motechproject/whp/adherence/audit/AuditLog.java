package org.motechproject.whp.adherence.audit;


import org.codehaus.jackson.annotate.JsonProperty;
import org.ektorp.support.TypeDiscriminator;
import org.joda.time.DateTime;
import org.motechproject.model.MotechBaseDataObject;
import org.motechproject.util.DateUtil;

@TypeDiscriminator("doc.type == 'AuditLog'")
public class AuditLog extends MotechBaseDataObject {

    @JsonProperty
    private DateTime creationTime = DateUtil.now();
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
    @JsonProperty
    private String providerId;
    @JsonProperty
    private String user;

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

    public AuditLog providerId(String providerId) {
        this.providerId = providerId;
        return this;
    }

    public String providerId() {
        return providerId;
    }

    public AuditLog user(String user) {
        this.user = user;
        return this;
    }

    public String user() {
        return user;
    }

    public DateTime getCreationTime() {
        return creationTime;
    }

    public void setCreationTime(DateTime creationTime) {
        this.creationTime = DateUtil.setTimeZone(creationTime);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        AuditLog auditLog = (AuditLog) o;

        if (numberOfDosesTaken != auditLog.numberOfDosesTaken) return false;
        if (creationTime != null ? !creationTime.equals(auditLog.creationTime) : auditLog.creationTime != null)
            return false;
        if (patientId != null ? !patientId.equals(auditLog.patientId) : auditLog.patientId != null) return false;
        if (providerId != null ? !providerId.equals(auditLog.providerId) : auditLog.providerId != null) return false;
        if (remark != null ? !remark.equals(auditLog.remark) : auditLog.remark != null) return false;
        if (sourceOfChange != null ? !sourceOfChange.equals(auditLog.sourceOfChange) : auditLog.sourceOfChange != null)
            return false;
        if (tbId != null ? !tbId.equals(auditLog.tbId) : auditLog.tbId != null) return false;
        if (user != null ? !user.equals(auditLog.user) : auditLog.user != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = creationTime != null ? creationTime.hashCode() : 0;
        result = 31 * result + numberOfDosesTaken;
        result = 31 * result + (remark != null ? remark.hashCode() : 0);
        result = 31 * result + (sourceOfChange != null ? sourceOfChange.hashCode() : 0);
        result = 31 * result + (patientId != null ? patientId.hashCode() : 0);
        result = 31 * result + (tbId != null ? tbId.hashCode() : 0);
        result = 31 * result + (providerId != null ? providerId.hashCode() : 0);
        result = 31 * result + (user != null ? user.hashCode() : 0);
        return result;
    }
}
