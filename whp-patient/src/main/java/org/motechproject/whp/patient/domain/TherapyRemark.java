package org.motechproject.whp.patient.domain;

import lombok.Data;
import org.ektorp.support.TypeDiscriminator;
import org.joda.time.DateTime;
import org.motechproject.model.MotechBaseDataObject;
import org.motechproject.util.DateUtil;

@Data
@TypeDiscriminator("doc.type == 'TherapyRemark'")
public class TherapyRemark extends MotechBaseDataObject {

    private DateTime creationTime = DateUtil.now();
    private String patientId;
    private String therapyUid;
    private String remark;
    private String user;

    public TherapyRemark() {
    }

    public TherapyRemark(String patientId, String therapyUid, String remark, String user) {
        super();
        this.patientId = patientId;
        this.therapyUid = therapyUid;
        this.remark = remark;
        this.user = user;
    }
}
