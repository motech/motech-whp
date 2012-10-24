package org.motechproject.whp.common.util;

import org.junit.Test;

import java.util.Properties;

import static org.junit.Assert.assertEquals;

public class URLEscapeTest {

    @Test
    public void testEscapeSpaceInString() {
        Properties properties = new Properties();
        properties.setProperty("key", "value with space");

        properties = URLEscape.escape(properties);
        assertEquals("value\\ with\\ space", properties.getProperty("key"));
    }
}
