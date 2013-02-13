package org.motechproject.whp.uimodel;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.motechproject.whp.common.domain.Phase;
import org.motechproject.whp.common.util.WHPDate;
import org.motechproject.whp.mapper.PatientInfoMapper;
import org.motechproject.whp.patient.domain.*;
import org.motechproject.whp.user.domain.Provider;

import java.util.ArrayList;
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

    private Integer adherenceMissingWeeks;
    private Integer adherenceMissingWeeksSeverity;
    private String adherenceMissingSeverityColor;

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
    private static final String COMMA = ", ";
    private Boolean flag;

}

