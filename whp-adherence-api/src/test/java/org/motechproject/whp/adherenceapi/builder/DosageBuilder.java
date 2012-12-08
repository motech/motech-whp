package org.motechproject.whp.adherenceapi.builder;


import org.motechproject.whp.adherenceapi.domain.Dosage;
import org.motechproject.whp.patient.builder.PatientBuilder;
import org.motechproject.whp.patient.builder.TherapyBuilder;
import org.motechproject.whp.patient.domain.Patient;
import org.motechproject.whp.patient.domain.Therapy;
import org.motechproject.whp.patient.domain.TreatmentCategory;

public class DosageBuilder {

    private int dosesPerWeek;

    public DosageBuilder(int dosesPerWeek) {
        this.dosesPerWeek = dosesPerWeek;
    }

    public Dosage dosage() {
        Patient patient = patient(dosesPerWeek);
        return new Dosage(patient);
    }

    private Patient patient(int dosesPerWeek) {
        Therapy therapy = new TherapyBuilder().withTreatmentCategory(treatmentCategory(dosesPerWeek)).build();
        return new PatientBuilder().withDefaults().withCurrentTherapy(therapy).build();
    }

    private TreatmentCategory treatmentCategory(int dosesPerWeek) {
        TreatmentCategory category = new TreatmentCategory();
        category.setDosesPerWeek(dosesPerWeek);
        return category;
    }
}
