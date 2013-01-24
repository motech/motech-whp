package org.motechproject.whp.uimodel;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.motechproject.whp.common.domain.Phase;
import org.motechproject.whp.common.util.WHPDate;
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

    public PatientInfo(Patient patient, Provider provider) {
        initializePatientInfo(patient);
        initializeProviderInfo(provider);
    }

    public PatientInfo(Patient patient) {
        initializePatientInfo(patient);
    }

    private void initializeProviderInfo(Provider provider) {
        providerMobileNumber = provider.getPrimaryMobile();
        providerDistrict = provider.getDistrict();
    }

    private void initializePatientInfo(Patient patient) {
        currentTreatment = patient.getCurrentTreatment();
        Therapy latestTherapy = patient.getCurrentTherapy();
        setTestResults(patient);
        patientId = patient.getPatientId();
        firstName = patient.getFirstName();
        lastName = patient.getLastName();
        phoneNumber = patient.getPhoneNumber();
        phi = patient.getPhi();
        gender = patient.getGender().getValue();
        tbId = currentTreatment.getTbId();
        providerId = currentTreatment.getProviderId();
        therapyStartDate = WHPDate.date(latestTherapy.getStartDate()).value();
        tbRegistrationNumber = currentTreatment.getTbRegistrationNumber();
        patientType = currentTreatment.getPatientType().value();
        age = latestTherapy.getPatientAge();
        diseaseClass = latestTherapy.getDiseaseClass().value();
        treatmentCategoryName = latestTherapy.getTreatmentCategory().getName();
        treatmentCategoryCode = latestTherapy.getTreatmentCategory().getCode();
        Address patientAddress = currentTreatment.getPatientAddress();
        address = patientAddress.getAddress_location() + COMMA + patientAddress.getAddress_landmark() + COMMA + patientAddress.getAddress_block();
        addressVillage = patientAddress.getAddress_village();
        addressDistrict = patientAddress.getAddress_district();
        addressState = patientAddress.getAddress_state();
        nextPhaseName = patient.getCurrentTherapy().getPhases().getNextPhase();
        phasesNotPossibleToTransitionTo = mapPhaseNameToString(patient);
        nearingPhaseTransition = patient.isNearingPhaseTransition();
        transitioning = patient.isTransitioning();
        currentPhase = patient.getCurrentTherapy().getCurrentPhase();
        remainingDosesInCurrentPhase = patient.getRemainingDosesInCurrentPhase();
        lastCompletedPhase = patient.getCurrentTherapy().getLastCompletedPhase();
        currentTreatmentPaused = patient.isCurrentTreatmentPaused();
        currentTreatmentClosed = patient.isCurrentTreatmentClosed();
        longestDoseInterruption = patient.getLongestDoseInterruption();
        showAlert = (patient.isNearingPhaseTransition() || patient.isTransitioning()) && !patient.isOrHasBeenOnCp();
        adherenceCapturedForThisWeek = patient.hasAdherenceForLastReportingWeekForCurrentTherapy();
    }

    private List<String> mapPhaseNameToString(Patient patient) {
        List<Phase> phases = patient.getHistoryOfPhases();
        List<String> names = new ArrayList<>();
        for (Phase phase : phases) {
            names.add(phase.name());
        }
        return names;
    }

    private void setTestResults(Patient patient) {
        WeightStatistics weightStatistics = patient.getCurrentTherapy().getAggregatedWeightStatistics();
        SmearTestResults smearTestResults = patient.getCurrentTherapy().getAggregatedSmearTestResults();
        testResults = new TestResults(smearTestResults, weightStatistics);
    }
}

