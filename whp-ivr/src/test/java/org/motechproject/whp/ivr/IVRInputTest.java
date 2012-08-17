package org.motechproject.whp.ivr;

import org.junit.Test;

import static org.junit.Assert.assertFalse;

public class IVRInputTest {

    @Test
    public void shouldTreatEmptyStringsAsNonNumeric() {
        assertFalse(new IVRInput("").isNumeric());
    }

}
