package org.motechproject.whp.common.util;

import org.junit.Test;

import static org.junit.Assert.assertNotSame;

public class UUIDGeneratorTest {

    @Test
    public void shouldReturnUUID() {
        UUIDGenerator uuidGenerator = new UUIDGenerator();
        assertNotSame(uuidGenerator.uuid(), uuidGenerator.uuid());
    }

}
