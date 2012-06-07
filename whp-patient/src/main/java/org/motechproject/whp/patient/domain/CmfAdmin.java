package org.motechproject.whp.patient.domain;

import lombok.Data;
import org.ektorp.support.TypeDiscriminator;
import org.motechproject.model.MotechBaseDataObject;

@Data
@TypeDiscriminator("doc.type == 'CmfAdmin'")
public class CmfAdmin extends MotechBaseDataObject {

    private String userId;

    private String email;

    private String department;

    private String locationId;

    private String staffName;

    // Required for ektorp
    public CmfAdmin() {
    }

    public CmfAdmin(String userId, String email, String department, String locationId, String staffName) {
        setUserId(userId);
        this.email = email;
        this.department = department;
        this.locationId = locationId;
        this.staffName = staffName;
    }
    private void setUserId(String userId) {
        if(userId==null)
            this.userId = null;
        this.userId = userId.toLowerCase();
    }

}
