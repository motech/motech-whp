package org.motechproject.whp.adherence.audit;

import lombok.Data;
import org.motechproject.whp.adherence.domain.AdherenceSource;

@Data
public class AuditParams {

    String user;
    AdherenceSource sourceOfChange;
    String remarks;

    public AuditParams(String user, AdherenceSource sourceOfChange, String remarks) {
        this.user = user;
        this.sourceOfChange = sourceOfChange;
        this.remarks = remarks;
    }
}
