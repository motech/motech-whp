package org.motechproject.whp.patient.seed;


import org.motechproject.deliverytools.seed.Seed;
import org.motechproject.model.DayOfWeek;
import org.motechproject.whp.patient.domain.TreatmentCategory;
import org.motechproject.whp.patient.repository.AllTreatmentCategories;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Service
public class TreatmentCategorySeed {

    @Autowired
    private AllTreatmentCategories allTreatmentCategories;

    @Seed(priority = 0, version = "1.0")
    public void load() {

        List<DayOfWeek> threeDaysAWeek = Arrays.asList(DayOfWeek.Monday, DayOfWeek.Wednesday, DayOfWeek.Friday);
        List<DayOfWeek> allDaysOfWeek = Arrays.asList(DayOfWeek.Monday, DayOfWeek.Tuesday, DayOfWeek.Wednesday, DayOfWeek.Thursday, DayOfWeek.Friday, DayOfWeek.Saturday, DayOfWeek.Sunday);

        allTreatmentCategories.add(new TreatmentCategory("RNTCP Category 1", "01", 3, 8, 24, 4, 12, 18, 54, threeDaysAWeek));
        allTreatmentCategories.add(new TreatmentCategory("RNTCP Category 2", "02", 3, 12, 36, 4, 12, 22, 66, threeDaysAWeek));
        allTreatmentCategories.add(new TreatmentCategory("Commercial/Private Category 1", "11", 7, 8, 56, 4, 28, 18, 126, allDaysOfWeek));
        allTreatmentCategories.add(new TreatmentCategory("Commercial/Private Category 2", "12", 7, 12, 84, 4, 28, 22, 154, allDaysOfWeek));
    }

}
