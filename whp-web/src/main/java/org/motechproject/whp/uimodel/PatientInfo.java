package org.motechproject.whp.uimodel;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import org.motechproject.whp.common.WHPDate;
import org.motechproject.whp.patient.domain.*;
import org.motechproject.whp.refdata.domain.PhaseName;
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
        Therapy latestTherapy = patient.getCurrentTherapy();
        setTestResults(patient);
        patientId = patient.getPatientId();
        firstName = patient.getFirstName();
        lastName = patient.getLastName();
        phoneNumber = patient.getPhoneNumber();
        phi = patient.getPhi();
        gender = patient.getGender().name();
        tbId = currentTreatment.getTbId();
        providerId = currentTreatment.getProviderId();
        therapyStartDate = WHPDate.date(latestTherapy.getStartDate()).value();
        tbRegistrationNumber = currentTreatment.getTbRegistrationNumber();
        patientType = currentTreatment.getPatientType().name();
        age = latestTherapy.getPatientAge();
        diseaseClass = latestTherapy.getDiseaseClass().name();
        treatmentCategory = latestTherapy.getTreatmentCategory().getName();
        address = currentTreatment.getPatientAddress().toString();
        addressState = currentTreatment.getPatientAddress().getAddress_state();
        providerMobileNumber = provider.getPrimaryMobile();
        nextPhaseName = currentTreatment.getTherapy().getPhases().getNextPhaseName();
        phasesNotPossibleToTransitionTo = patient.getPhasesNotPossibleToTransitionTo();
        nearingPhaseTransition = patient.isNearingPhaseTransition();
        transitioning = patient.isTransitioning();
        currentPhase = patient.getCurrentTherapy().getCurrentPhase();
        remainingDosesInCurrentPhase = patient.getRemainingDosesInCurrentPhase();
        lastCompletedPhase = patient.getCurrentTherapy().getLastCompletedPhase();
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

