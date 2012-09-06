package org.motechproject.whp.uimodel;

import lombok.Setter;
import org.joda.time.LocalDate;
import org.motechproject.export.annotation.ExportValue;
import org.motechproject.whp.refdata.domain.*;


@Setter
public class PatientSummary {

    private String name;
    private Integer age;
    private Gender gender;
    private String patientId;
    private String tbId;
    private String providerId;
    private String village;
    private String providerDistrict;
    private String treatmentCategory;
    private String tbRegistrationDate;
    private String treatmentStartDate;
    private String diseaseClass;
    private PatientType patientType;
    private String ipTreatmentProgress;
    private String cpTreatmentProgress;
    private Integer cumulativeMissedDoses;
    private TreatmentOutcome treatmentOutcome;
    private String treatmentClosingDate;

    @ExportValue(index = 0)
    public String getName() {
        return name;
    }

    @ExportValue(index = 1)
    public Integer getAge() {
        return age;
    }

    @ExportValue(index = 2)
    public Gender getGender() {
        return gender;
    }

    @ExportValue(index = 3, column = "Patient ID")
    public String getPatientId() {
        return patientId;
    }

    @ExportValue(index = 4, column = "TB ID")
    public String getTbId() {
        return tbId;
    }

    @ExportValue(index = 5, column = "Provider ID")
    public String getProviderId() {
        return providerId;
    }

    @ExportValue(index = 6)
    public String getVillage() {
        return village;
    }

    @ExportValue(index = 7)
    public String getProviderDistrict() {
        return providerDistrict;
    }

    @ExportValue(index = 8)
    public String getTreatmentCategory() {
        return treatmentCategory;
    }

    @ExportValue(index = 9, column = "TB Registration Date")
    public String getTbRegistrationDate() {
        return tbRegistrationDate;
    }

    @ExportValue(index = 10)
    public String getTreatmentStartDate() {
        return treatmentStartDate;
    }

    @ExportValue(index = 11)
    public String getDiseaseClass() {
        return diseaseClass;
    }

    @ExportValue(index = 12)
    public PatientType getPatientType() {
        return patientType;
    }

    @ExportValue(index = 13, column = "IP Treatment Progress")
    public String getIpTreatmentProgress() {
        return ipTreatmentProgress;
    }

    @ExportValue(index = 14, column = "CP Treatment Progress")
    public String getCpTreatmentProgress() {
        return cpTreatmentProgress;
    }

    @ExportValue(index = 15, column = "Cumulative Missed Doses *")
    public Integer getCumulativeMissedDoses() {
        return cumulativeMissedDoses;
    }

    @ExportValue(index = 16)
    public TreatmentOutcome getTreatmentOutcome() {
        return treatmentOutcome;
    }

    @ExportValue(index = 17)
    public String getTreatmentClosingDate() {
        return treatmentClosingDate;
    }
}
