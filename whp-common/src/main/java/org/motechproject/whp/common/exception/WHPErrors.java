package org.motechproject.whp.common.exception;

import java.util.ArrayList;

public class WHPErrors extends ArrayList<WHPError> {

    public WHPErrors() {
    }

    @Override
    public boolean add(WHPError whpError) {
        if (!this.contains(whpError))
            super.add(whpError);
        return false;
    }
}
