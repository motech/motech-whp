package org.motechproject.whp.adherence.audit.domain;

import lombok.Data;
import org.ektorp.support.TypeDiscriminator;
import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.motechproject.model.MotechBaseDataObject;
import org.motechproject.util.DateUtil;
import org.motechproject.whp.adherence.domain.PillStatus;

@Data
@TypeDiscriminator("doc.type == 'DailyAdherenceAuditLog'")
public class DailyAdherenceAuditLog extends MotechBaseDataObject {

    public DailyAdherenceAuditLog() {

    }

    public DailyAdherenceAuditLog(String patientId, String tbId, LocalDate pillDate, PillStatus pillStatus, String user, String sourceOfChange, DateTime creationTime, String providerId) {
        this.patientId = patientId;
        this.tbId = tbId;
        this.pillDate = pillDate;
        this.pillStatus = pillStatus;
        this.user = user;
        this.sourceOfChange = sourceOfChange;
        this.creationTime = creationTime;
        this.providerId = providerId;
    }

    private String patientId;

    private String tbId;

    private LocalDate pillDate;

    private PillStatus pillStatus = PillStatus.Unknown;

    private String user;

    private String sourceOfChange;

    private DateTime creationTime = DateUtil.now();

    private String providerId;
}
