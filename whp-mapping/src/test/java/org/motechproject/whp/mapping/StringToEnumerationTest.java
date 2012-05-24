package org.motechproject.whp.mapping;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class StringToEnumerationTest {
    private StringToEnumeration stringToEnumeration;

    @Before
    public void setup() {
       stringToEnumeration = new StringToEnumeration();
    }

    @Test
    public void shouldConvertValidEnumString() {
       assertEquals(Enum.enum2, stringToEnumeration.convert("enum2", Enum.class));
    }

    @Test
    public void shouldHandleCaseInsensitiveStringWithSpaces() {
       assertEquals(Enum.enum2,stringToEnumeration.convert("  EnUm2",Enum.class));
    }

    @Test
    public void shouldReturnNullForInvalidEnumString() {
        assertEquals(null,stringToEnumeration.convert("enum3",Enum.class));
        assertEquals(null,stringToEnumeration.convert("  enum3 ",Enum.class));
    }

}

enum Enum{
    enum1,enum2
}