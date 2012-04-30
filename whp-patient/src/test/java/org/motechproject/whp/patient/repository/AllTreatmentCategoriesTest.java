package org.motechproject.whp.patient.repository;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.motechproject.whp.patient.domain.TreatmentCategory;
import org.springframework.beans.factory.annotation.Autowired;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;

public class AllTreatmentCategoriesTest extends SpringIntegrationTest {

    public static final String CODE = "99";

    @Autowired
    AllTreatmentCategories allTreatmentCategories;

    private TreatmentCategory cat99;
    private TreatmentCategory cat100;

    @Before
    public void setUp() {
        cat99 = new TreatmentCategory("cat99", CODE, 9, 9, 9);
        allTreatmentCategories.add(cat99);
        cat100 = new TreatmentCategory("cat100", "100", 10, 10, 10);
        allTreatmentCategories.add(cat100);
    }

    @After
    public void tearDown() {
        allTreatmentCategories.remove(cat99);
        allTreatmentCategories.remove(cat100);
    }

    @Test
    public void shouldFindTreatmentCategoryByCode() {
        TreatmentCategory treatmentCategory = allTreatmentCategories.findByCode(CODE);
        assertNotNull(treatmentCategory);
        assertEquals(CODE, treatmentCategory.getCode());
    }

}
