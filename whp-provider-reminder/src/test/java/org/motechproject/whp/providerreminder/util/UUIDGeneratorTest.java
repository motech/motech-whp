package org.motechproject.whp.providerreminder.util;

import org.junit.Test;

import static org.junit.Assert.assertNotSame;

public class UUIDGeneratorTest {

    @Test
    public void shouldReturnUUID() {
        UUIDGenerator UUIDGenerator = new UUIDGenerator();
        assertNotSame(UUIDGenerator.uuid(), UUIDGenerator.uuid());
    }

}
