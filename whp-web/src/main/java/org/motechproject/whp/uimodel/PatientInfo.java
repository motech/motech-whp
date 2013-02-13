package org.motechproject.whp.uimodel;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.motechproject.whp.common.domain.Phase;
import org.motechproject.whp.patient.domain.PhaseRecord;
import org.motechproject.whp.patient.domain.Treatment;

import java.util.List;

@Data
@EqualsAndHashCode
public class PatientInfo {

    private TestResults testResults;
    private String patientId;
    private String firstName;
    private String lastName;
    private String phoneNumber;
    private String phi;
    private String gender;
    private String tbId;
    private String providerId;
    private String therapyStartDate;
    private String tbRegistrationNumber;
    private String patientType;
    private Integer age;
    private String diseaseClass;
    private String treatmentCategoryName;
    private String treatmentCategoryCode;
    private String address;
    private String addressState;
    private String addressDistrict;
    private String providerMobileNumber;


    private Treatment currentTreatment;
    private Phase nextPhaseName;
    private PhaseRecord currentPhase;
    private PhaseRecord lastCompletedPhase;
    private List<String> phasesNotPossibleToTransitionTo;
    private boolean nearingPhaseTransition;
    private boolean transitioning;
    private int remainingDosesInCurrentPhase;

    private String addressVillage;
    private boolean adherenceCapturedForThisWeek;
    private boolean currentTreatmentPaused;
    private boolean currentTreatmentClosed;

    private String longestDoseInterruption;

    private boolean showAlert;
    private String providerDistrict;
    private Boolean flag;

    private Integer adherenceMissingWeeks;
    private Integer adherenceMissingWeeksSeverity;
    private String adherenceMissingSeverityColor;
    private String adherenceMissingMessageCode;
    private String cumulativeMissedDosesMessageCode;
    private String cumulativeMissedDosesSeverityColor;
    private String treatmentNotStartedSeverityColor;
    private String treatmentNotStartedMessageCode;


    public void setAdherenceMissingMessageCode() {
        String message = null;
        switch (adherenceMissingWeeksSeverity) {
            case 1:
                message = "message.alert.filter.adherence.missing.severity.one.alerts";
                break;
            case 2:
                message = "message.alert.filter.adherence.missing.severity.two.alerts";
                break;
            case 3:
                message = "message.alert.filter.adherence.missing.severity.three.alerts";
                break;
        }
        this.adherenceMissingMessageCode = message;
    }

    public void setCumulativeMissedDosesMessageCode() {
        this.cumulativeMissedDosesMessageCode = "message.alert.filter.cumulative.missed.dose.alerts";
    }

    public void setTreatmentNotStartedMessageCode() {
        this.treatmentNotStartedMessageCode = "message.alert.filter.treatment.not.started.alerts";
    }


}

