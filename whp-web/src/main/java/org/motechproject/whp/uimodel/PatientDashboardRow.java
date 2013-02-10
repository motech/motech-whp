package org.motechproject.whp.uimodel;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.motechproject.whp.common.domain.alerts.PatientAlertType;
import org.motechproject.whp.patient.domain.Address;
import org.motechproject.whp.patient.domain.Patient;
import org.motechproject.whp.patient.domain.Therapy;
import org.motechproject.whp.patient.domain.Treatment;
import org.motechproject.whp.patient.domain.alerts.PatientAlerts;

@Data
@EqualsAndHashCode
public class PatientDashboardRow {

    private String patientId;
    private String firstName;
    private String lastName;
    private String gender;
    private String tbId;
    private String therapyStartDate;
    private String currentTreatmentStartDate;
    private Integer age;
    private String treatmentCategoryName;
    private String addressVillage;
    private boolean currentTreatmentPaused;
    private boolean currentTreatmentClosed;
    private Treatment currentTreatment;
    private boolean showAlert;
    private String ipProgress;
    private String cpProgress;
    private Integer cumulativeMissedDoses;
    private Integer treatmentNotStartedSeverity;
    private String treatmentNotStartedSeverityColor;
    private Integer cumulativeMissedDosesSeverity;
    private String cumulativeMissedDosesSeverityColor;
    private Integer adherenceMissingWeeks;
    private Integer adherenceMissingWeeksSeverity;
    private String adherenceMissingSeverityColor;
    private Boolean flag;


    public PatientDashboardRow(Patient patient) {
        initializePatientDashboardRow(patient);
    }

    private void initializePatientDashboardRow(Patient patient) {
        currentTreatment = patient.getCurrentTreatment();
        Therapy latestTherapy = patient.getCurrentTherapy();
        patientId = patient.getPatientId();
        tbId = currentTreatment.getTbId();
        firstName = patient.getFirstName();
        lastName = patient.getLastName();
        gender = patient.getGender().getValue();
        therapyStartDate = latestTherapy.getStartDateAsString();
        currentTreatmentStartDate = currentTreatment.getStartDateAsString();
        age = latestTherapy.getPatientAge();
        treatmentCategoryName = latestTherapy.getTreatmentCategory().getName();
        Address patientAddress = currentTreatment.getPatientAddress();
        addressVillage = patientAddress.getAddress_village();
        currentTreatmentPaused = patient.isCurrentTreatmentPaused();
        currentTreatmentClosed = patient.isCurrentTreatmentClosed();
        showAlert = (patient.isNearingPhaseTransition() || patient.isTransitioning()) && !patient.isOrHasBeenOnCp();
        ipProgress = patient.getIPProgress();
        cpProgress = patient.getCPProgress();

        PatientAlerts patientAlerts = patient.getPatientAlerts();
        cumulativeMissedDoses = patientAlerts.getAlert(PatientAlertType.CumulativeMissedDoses).getValue();
        treatmentNotStartedSeverity = patientAlerts.getAlert(PatientAlertType.TreatmentNotStarted).getAlertSeverity();
        treatmentNotStartedSeverityColor = patientAlerts.getAlert(PatientAlertType.TreatmentNotStarted).getAlertSeverityColor();
        cumulativeMissedDosesSeverity = patientAlerts.getAlert(PatientAlertType.CumulativeMissedDoses).getAlertSeverity();
        cumulativeMissedDosesSeverityColor = patientAlerts.getAlert(PatientAlertType.CumulativeMissedDoses).getAlertSeverityColor();
        adherenceMissingWeeks = patientAlerts.getAlert(PatientAlertType.AdherenceMissing).getValue();
        adherenceMissingWeeksSeverity = patientAlerts.getAlert(PatientAlertType.AdherenceMissing).getAlertSeverity();
        adherenceMissingSeverityColor = patientAlerts.getAlert(PatientAlertType.AdherenceMissing).getAlertSeverityColor();
        flag = patient.getPatientFlag().isFlagSet();
    }

}

