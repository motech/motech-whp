package org.motechproject.whp.refdata.seed;


import org.motechproject.deliverytools.seed.Seed;
import org.motechproject.whp.patient.domain.TreatmentCategory;
import org.motechproject.whp.patient.repository.AllTreatmentCategories;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TreatmentCategorySeed {

    @Autowired
    private AllTreatmentCategories allTreatmentCategories;

    @Seed(priority = 0)
    public void load() {
        allTreatmentCategories.add(new TreatmentCategory("RNTCP Category 1", "01", 3, 8, 18));
        allTreatmentCategories.add(new TreatmentCategory("RNTCP Category 2", "02", 3, 12, 22));
        allTreatmentCategories.add(new TreatmentCategory("Commercial/Private Category 1", "11", 7, 8, 18));
        allTreatmentCategories.add(new TreatmentCategory("Commercial/Private Category 2", "12", 7, 12, 22));
    }

}
