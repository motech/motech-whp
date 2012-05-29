package org.motechproject.whp.importer.csv.request;

import org.junit.Test;
import org.motechproject.validation.constraints.NotNullOrEmpty;

import javax.validation.constraints.Pattern;
import java.lang.reflect.Field;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertTrue;

public class ImportProviderRequestTest {

    public static final String MOBILE_NUMBER_REGEX = "^$|[0-9]{10}";

    @Test
    public void shouldHaveNotNullValidationOnMandatoryFields() throws NoSuchFieldException {
        assertNotNullAnnotationPresentForField("providerId");
        assertNotNullAnnotationPresentForField("district");
        assertNotNullAnnotationPresentForField("primaryMobile");
    }

    private void assertNotNullAnnotationPresentForField(String fieldName) throws NoSuchFieldException {
        Field field = ImportProviderRequest.class.getDeclaredField(fieldName);
        assertTrue(field.isAnnotationPresent(NotNullOrEmpty.class));
    }
    
    @Test
    public void shouldHaveValidationForMobileNumbers() throws NoSuchFieldException {
        assertRegexPatternPresentForField("primaryMobile");
        assertRegexPatternPresentForField("secondaryMobile");
        assertRegexPatternPresentForField("tertiaryMobile");
    }

    private void assertRegexPatternPresentForField(String fieldName) throws NoSuchFieldException {
        Field field = ImportProviderRequest.class.getDeclaredField(fieldName);
        assertTrue(field.isAnnotationPresent(Pattern.class));
        String regexp = field.getAnnotation(Pattern.class).regexp();
        assertEquals(MOBILE_NUMBER_REGEX, regexp);
    }
    
    @Test
    public void mobileNumberRegexPatternShouldAcceptOnly10DigitsOrEmptyString() {
        java.util.regex.Pattern pattern = java.util.regex.Pattern.compile(MOBILE_NUMBER_REGEX);
        assertTrue(pattern.matcher("").matches());
        assertTrue(pattern.matcher("1234567890").matches());
        assertFalse(pattern.matcher("12345678901").matches());
        assertFalse(pattern.matcher("123456789").matches());
        assertFalse(pattern.matcher("abcdefghij").matches());
    }
}
