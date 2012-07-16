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

    private String patientId;

    private String tbId;

    private LocalDate pillDate;

    private PillStatus pillStatus = PillStatus.Unknown;

    private String user;

    private String sourceOfChange;

    private DateTime creationTime = DateUtil.now();


}
