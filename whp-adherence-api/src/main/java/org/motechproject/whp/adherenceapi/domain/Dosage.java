package org.motechproject.whp.adherenceapi.domain;

import lombok.EqualsAndHashCode;
import org.motechproject.whp.patient.domain.Patient;

import static org.motechproject.whp.adherenceapi.domain.TreatmentProvider.GOVERNMENT;
import static org.motechproject.whp.adherenceapi.domain.TreatmentProvider.PRIVATE;

@EqualsAndHashCode
public class Dosage {

    public static final String VALID_RANGE_FROM_FOR_ALL_CATEGORIES = "0";

    private Patient patient;

    public Dosage(Patient patient) {
        this.patient = patient;
    }

    public TreatmentProvider getTreatmentProvider() {
        return this.patient.getTreatmentCategory().isGovernmentCategory() ? GOVERNMENT : PRIVATE;
    }

    public String getValidRangeFrom() {
        return VALID_RANGE_FROM_FOR_ALL_CATEGORIES;
    }

    public String getValidRangeTo() {
        return this.patient.getTreatmentCategory().getDosesPerWeek().toString();
    }

    public boolean isValidInput(int doseCount) {
        return patient.isValidDose(doseCount);
    }
}
