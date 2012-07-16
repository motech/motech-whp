package org.motechproject.whp.uimodel;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import org.motechproject.whp.common.domain.WHPDate;
import org.motechproject.whp.patient.domain.*;
import org.motechproject.whp.refdata.domain.Phase;
import org.motechproject.whp.user.domain.Provider;

import java.util.ArrayList;
import java.util.List;

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
    private String therapyStartDate;
    private String tbRegistrationNumber;
    private String patientType;
    private Integer age;
    private String diseaseClass;
    private String treatmentCategory;
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

    public PatientInfo(Patient patient, Provider provider) {
        initializePatientInfo(patient);
        initializeProviderInfo(provider);
    }

    public PatientInfo(Patient patient) {
        initializePatientInfo(patient);
    }

    private void initializeProviderInfo(Provider provider) {
        providerMobileNumber = provider.getPrimaryMobile();
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
        patientType = currentTreatment.getPatientType().name();
        age = latestTherapy.getPatientAge();
        diseaseClass = latestTherapy.getDiseaseClass().value();
        treatmentCategory = latestTherapy.getTreatmentCategory().getName();
        address = currentTreatment.getPatientAddress().toString();
        addressState = currentTreatment.getPatientAddress().getAddress_state();
        nextPhaseName = patient.getCurrentTherapy().getPhases().getNextPhase();
        phasesNotPossibleToTransitionTo = mapPhaseNameToString(patient);
        nearingPhaseTransition = patient.isNearingPhaseTransition();
        transitioning = patient.isTransitioning();
        currentPhase = patient.getCurrentTherapy().getCurrentPhase();
        remainingDosesInCurrentPhase = patient.getRemainingDosesInCurrentPhase();
        lastCompletedPhase = patient.getCurrentTherapy().getLastCompletedPhase();
        addressVillage = currentTreatment.getPatientAddress().getAddress_village();
        addressDistrict = currentTreatment.getPatientAddress().getAddress_district();
        currentTreatmentPaused = patient.isCurrentTreatmentPaused();
        currentTreatmentClosed = patient.isCurrentTreatmentClosed();
        longestDoseInterruption = patient.getLongestDoseInterruption();
        showAlert = (patient.isNearingPhaseTransition() || patient.isTransitioning()) && !patient.isOrHasBeenOnCp();
    }

    private List<String> mapPhaseNameToString(Patient patient) {
        List<Phase> phases = patient.getHistoryOfPhases();
        List<String> names = new ArrayList<>();
        for (Phase phase : phases) {
            names.add(phase.name());
        }
        return names;
    }

    public void setAdherenceCapturedForThisWeek(boolean adherenceCapturedForThisWeek) {
        this.adherenceCapturedForThisWeek = adherenceCapturedForThisWeek;
    }

    private void setTestResults(Patient patient) {
        List<Treatment> treatments = patient.getTreatments();
        treatments.add(patient.getCurrentTreatment());
        WeightStatistics weightStatistics = new WeightStatistics();
        SmearTestResults smearTestResults = new SmearTestResults();
        for (Treatment treatment : treatments) {
            for (WeightStatisticsRecord weightStatisticsRecord : treatment.getWeightStatistics().getAll())
                weightStatistics.add(weightStatisticsRecord);
            for (SmearTestRecord smearTestRecord : treatment.getSmearTestResults().getAll())
                smearTestResults.add(smearTestRecord);
        }

        testResults = new TestResults(smearTestResults, weightStatistics);
    }
}

