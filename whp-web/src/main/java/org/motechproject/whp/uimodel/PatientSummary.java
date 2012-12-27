package org.motechproject.whp.uimodel;

import lombok.Setter;
import org.motechproject.export.annotation.ExportValue;
import org.motechproject.whp.common.util.WHPDate;
import org.motechproject.whp.patient.domain.Gender;
import org.motechproject.whp.patient.domain.PatientType;

import java.util.Date;


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
    private Date tbRegistrationDate;
    private Date treatmentStartDate;
    private String diseaseClass;
    private PatientType patientType;
    private String ipTreatmentProgress;
    private String cpTreatmentProgress;
    private Integer cumulativeMissedDoses;
    private String treatmentOutcome;
    private Date treatmentClosingDate;
    private String preTreatmentSputumResult;
    private String preTreatmentWeight;

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

    @ExportValue(index = 9)
    public String getPreTreatmentSputumResult() {
        return preTreatmentSputumResult;
    }

    @ExportValue(index = 10, column = "Pre Treatment Weight (kgs)")
    public String getPreTreatmentWeight() {
        return preTreatmentWeight;
    }

    @ExportValue(index = 11, column = "TB Registration Date", format = WHPDate.DATE_FORMAT)
    public Date getTbRegistrationDate() {
        return tbRegistrationDate;
    }

    @ExportValue(index = 12, format = WHPDate.DATE_FORMAT)
    public Date getTreatmentStartDate() {
        return treatmentStartDate;
    }

    @ExportValue(index = 13)
    public String getDiseaseClass() {
        return diseaseClass;
    }

    @ExportValue(index = 14)
    public PatientType getPatientType() {
        return patientType;
    }

    @ExportValue(index = 15, column = "IP Treatment Progress")
    public String getIpTreatmentProgress() {
        return ipTreatmentProgress;
    }

    @ExportValue(index = 16, column = "CP Treatment Progress")
    public String getCpTreatmentProgress() {
        return cpTreatmentProgress;
    }

    @ExportValue(index = 17, column = "Cumulative Missed Doses *")
    public Integer getCumulativeMissedDoses() {
        return cumulativeMissedDoses;
    }

    @ExportValue(index = 18)
    public String getTreatmentOutcome() {
        return treatmentOutcome;
    }

    @ExportValue(index = 19, format = WHPDate.DATE_FORMAT)
    public Date getTreatmentClosingDate() {
        return treatmentClosingDate;
    }
}
