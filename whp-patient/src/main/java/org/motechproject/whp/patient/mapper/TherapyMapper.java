package org.motechproject.whp.patient.mapper;

import org.motechproject.whp.patient.contract.PatientRequest;
import org.motechproject.whp.patient.domain.Patient;
import org.motechproject.whp.patient.domain.Therapy;
import org.motechproject.whp.patient.domain.TreatmentCategory;
import org.motechproject.whp.refdata.domain.DiseaseClass;

public class TherapyMapper {

    public static Therapy map(PatientRequest patientRequest) {
        Therapy therapy = createFirstTreatment(patientRequest);
        therapy.setCreationDate(patientRequest.getTreatmentCreationDate());
        return therapy;
    }

    public static Therapy createNewTreatment(Patient patient, PatientRequest patientRequest) {
        TreatmentCategory treatmentCategory = patientRequest.getTreatment_category();
        DiseaseClass diseaseClass = patientRequest.getDisease_class();

        return new Therapy(treatmentCategory, diseaseClass, patient.getAge());
    }

    private static Therapy createFirstTreatment(PatientRequest patientRequest) {
        TreatmentCategory treatmentCategory = patientRequest.getTreatment_category();
        DiseaseClass diseaseClass = patientRequest.getDisease_class();
        int patientAge = patientRequest.getAge();

        return new Therapy(treatmentCategory, diseaseClass, patientAge);
    }

}
