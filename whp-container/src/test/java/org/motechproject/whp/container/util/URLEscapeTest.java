package org.motechproject.whp.container.util;

import org.junit.Test;
import org.motechproject.paginator.contract.FilterParams;

import static org.junit.Assert.assertEquals;

public class URLEscapeTest {

    @Test
    public void testEscapeSpaceInString() {
        FilterParams properties = new FilterParams();
        properties.put("key", "value with space");

        properties = URLEscape.escape(properties);
        assertEquals("value\\ with\\ space", properties.get("key"));
    }
}
