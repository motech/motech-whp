package org.motechproject.whp.refdata.domain;

import lombok.Data;
import org.ektorp.support.TypeDiscriminator;
import org.motechproject.model.MotechBaseDataObject;

@Data
@TypeDiscriminator("doc.type == 'District'")
public class District extends MotechBaseDataObject implements Comparable<District> {

    private String name;

    // Required for ektorp
    public District() {
    }

    public District(String name) {
        this.name = name;
    }

    @Override
    public int compareTo(District district) {
        return getName().toLowerCase().compareTo(district.getName().toLowerCase());
    }
}
