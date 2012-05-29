package org.motechproject.whp.importer.csv.request.importPatientRequestTests;

import org.junit.Test;
import org.motechproject.validation.constraints.NotNullOrEmpty;
import org.motechproject.whp.importer.csv.request.ImportPatientRequest;

import java.lang.reflect.Field;

import static junit.framework.Assert.assertTrue;

public class ValidationsTest {
    @Test
    public void shouldHaveNotNullOrEmptyAnnotationForMandatoryFields() throws NoSuchFieldException {
        assertNotNullAnnotationPresentForField("case_id");
        assertNotNullAnnotationPresentForField("first_name");
        assertNotNullAnnotationPresentForField("last_name");
        assertNotNullAnnotationPresentForField("gender");
        assertNotNullAnnotationPresentForField("disease_class");
        assertNotNullAnnotationPresentForField("address_location");
        assertNotNullAnnotationPresentForField("address_village");
        assertNotNullAnnotationPresentForField("address_block");
        assertNotNullAnnotationPresentForField("address_district");
        assertNotNullAnnotationPresentForField("address_state");
        assertNotNullAnnotationPresentForField("tb_id");
        assertNotNullAnnotationPresentForField("treatment_category");
        assertNotNullAnnotationPresentForField("age");
        assertNotNullAnnotationPresentForField("date_modified");
        assertNotNullAnnotationPresentForField("provider_id");
    }

    private void assertNotNullAnnotationPresentForField(String fieldName) throws NoSuchFieldException {
        Field field = ImportPatientRequest.class.getDeclaredField(fieldName);
        assertTrue(field.isAnnotationPresent(NotNullOrEmpty.class));
    }

}
