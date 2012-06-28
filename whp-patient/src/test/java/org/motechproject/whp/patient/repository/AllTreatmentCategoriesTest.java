package org.motechproject.whp.patient.repository;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.motechproject.whp.common.utils.SpringIntegrationTest;
import org.motechproject.model.DayOfWeek;
import org.motechproject.whp.patient.domain.TreatmentCategory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;

import java.util.Arrays;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;

@ContextConfiguration(locations = "classpath*:applicationPatientContext.xml")
public class AllTreatmentCategoriesTest extends SpringIntegrationTest {

    public static final String CODE = "99";

    @Autowired
    AllTreatmentCategories allTreatmentCategories;

    private TreatmentCategory cat99;
    private TreatmentCategory cat100;

    @Before
    public void setUp() {
        cat99 = new TreatmentCategory("cat99", CODE, 9, 9, 81, 4, 12, 9, 81, Arrays.asList(DayOfWeek.Monday, DayOfWeek.Tuesday));
        allTreatmentCategories.add(cat99);
        cat100 = new TreatmentCategory("cat100", "100", 10, 10, 100, 4, 12, 10, 100, Arrays.asList(DayOfWeek.Monday, DayOfWeek.Tuesday));
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
