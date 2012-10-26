package org.motechproject.whp.common.mapping;

import org.joda.time.DateTime;
import org.junit.Test;

import java.util.Date;

import static org.junit.Assert.assertEquals;

public class StringToDateTimeTest {
    @Test
    public void shouldReturnSourceStringAsIsForEmptyValues() {
        assertEquals(null, new StringToDateTime().convert("", DateTime.class));
        assertEquals(null, new StringToDateTime().convert(null, DateTime.class));
    }

    @Test
    public void shouldReturnSourceStringAsIsForInvalidFormats() {
        assertEquals(null, new StringToDateTime().convert("invalid date", Date.class));
    }
}
