package org.motechproject.whp.uimodel;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import org.motechproject.whp.common.WHPDate;
import org.motechproject.whp.patient.domain.Patient;
import org.motechproject.whp.patient.domain.Phase;
import org.motechproject.whp.patient.domain.Therapy;
import org.motechproject.whp.patient.domain.Treatment;
import org.motechproject.whp.refdata.domain.PhaseName;
import org.motechproject.whp.user.domain.Provider;

import java.util.ArrayList;

@Getter
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
    private String treatmentStartDate;
    private String tbRegistrationNumber;
    private String patientType;
    private Integer age;
    private String diseaseClass;
    private String treatmentCategory;
    private String address;
    private String addressState;
    private String providerMobileNumber;

    private Treatment currentTreatment;
    private PhaseName nextPhaseName;
    private Phase currentPhase;
    private Phase lastCompletedPhase;
    private ArrayList<String> phasesNotPossibleToTransitionTo;
    private boolean nearingPhaseTransition;
    private boolean transitioning;
    private int remainingDosesInCurrentPhase;

    public PatientInfo(Patient patient, Provider provider) {
        initialize(patient, provider);
    }

    private void initialize(Patient patient, Provider provider) {
        currentTreatment = patient.getCurrentTreatment();
        Therapy latestTherapy = patient.currentTherapy();
        testResults = new TestResults(currentTreatment.getSmearTestResults(), currentTreatment.getWeightStatistics());
        patientId = patient.getPatientId();
        firstName = patient.getFirstName();
        lastName = patient.getLastName();
        phoneNumber = patient.getPhoneNumber();
        phi = patient.getPhi();
        gender = patient.getGender().name();
        tbId = currentTreatment.getTbId();
        providerId = currentTreatment.getProviderId();
        treatmentStartDate = WHPDate.date(currentTreatment.getStartDate()).value();
        tbRegistrationNumber = currentTreatment.getTbRegistrationNumber();
        patientType = currentTreatment.getPatientType().name();
        age = latestTherapy.getPatientAge();
        diseaseClass = latestTherapy.getDiseaseClass().name();
        treatmentCategory = latestTherapy.getTreatmentCategory().getName();
        address = currentTreatment.getPatientAddress().toString();
        addressState = currentTreatment.getPatientAddress().getAddress_state();
        providerMobileNumber = provider.getPrimaryMobile();
        nextPhaseName = currentTreatment.getTherapy().getNextPhaseName();
        phasesNotPossibleToTransitionTo = patient.getPhasesNotPossibleToTransitionTo();
        nearingPhaseTransition = patient.isNearingPhaseTransition();
        transitioning = patient.isTransitioning();
        currentPhase = patient.currentTherapy().getCurrentPhase();
        remainingDosesInCurrentPhase = patient.getRemainingDosesInCurrentPhase();
        lastCompletedPhase = patient.currentTherapy().getLastCompletedPhase();
    }
}

