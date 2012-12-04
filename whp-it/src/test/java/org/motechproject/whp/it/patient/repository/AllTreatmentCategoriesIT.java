package org.motechproject.whp.it.patient.repository;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.motechproject.model.DayOfWeek;
import org.motechproject.whp.common.util.SpringIntegrationTest;
import org.motechproject.whp.patient.domain.TreatmentCategory;
import org.motechproject.whp.patient.repository.AllTreatmentCategories;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;

import java.util.Arrays;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;

@ContextConfiguration(locations = "classpath*:/applicationITContext.xml")
public class AllTreatmentCategoriesIT extends SpringIntegrationTest {

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
