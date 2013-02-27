package org.motechproject.whp.mapper;

import org.motechproject.whp.common.domain.Phase;
import org.motechproject.whp.common.domain.alerts.AlertColorConfiguration;
import org.motechproject.whp.common.domain.alerts.PatientAlertType;
import org.motechproject.whp.common.util.WHPDate;
import org.motechproject.whp.patient.domain.*;
import org.motechproject.whp.patient.domain.alerts.PatientAlert;
import org.motechproject.whp.patient.domain.alerts.PatientAlerts;
import org.motechproject.whp.uimodel.PatientInfo;
import org.motechproject.whp.uimodel.TestResults;
import org.motechproject.whp.user.domain.Provider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class PatientInfoMapper {

    private static final String COMMA = ", ";
    AlertColorConfiguration alertColorConfiguration;

    @Autowired
    public PatientInfoMapper(AlertColorConfiguration alertColorConfiguration) {
        this.alertColorConfiguration = alertColorConfiguration;
    }

    public PatientInfoMapper() {

    }

    public PatientInfo map(Patient patient, Provider provider) {
        PatientInfo patientInfo = new PatientInfo();
        Treatment currentTreatment = patient.getCurrentTreatment();
        Address patientAddress = currentTreatment.getPatientAddress();
        Therapy latestTherapy = patient.getCurrentTherapy();
        PatientAlerts patientAlerts = patient.getPatientAlerts();

        if (provider != null) {
            mapProviderDetails(provider, patientInfo);
        }

        patientInfo.setCurrentTreatment(currentTreatment);
        patientInfo.setPatientId(patient.getPatientId());
        patientInfo.setFirstName(patient.getFirstName());
        patientInfo.setLastName(patient.getLastName());
        patientInfo.setPhoneNumber(patient.getPhoneNumber());
        patientInfo.setPhi(patient.getPhi());
        patientInfo.setGender(patient.getGender().getValue());
        patientInfo.setTbId(currentTreatment.getTbId());
        patientInfo.setProviderId(currentTreatment.getProviderId());
        patientInfo.setTherapyStartDate(WHPDate.date(latestTherapy.getStartDate()).value());
        patientInfo.setTbRegistrationNumber(currentTreatment.getTbRegistrationNumber());
        patientInfo.setPatientType(currentTreatment.getPatientType().value());
        patientInfo.setAge(latestTherapy.getPatientAge());
        patientInfo.setDiseaseClass(latestTherapy.getDiseaseClass().value());
        patientInfo.setTreatmentCategoryName(latestTherapy.getTreatmentCategory().getName());
        patientInfo.setTreatmentCategoryCode(latestTherapy.getTreatmentCategory().getCode());
        patientInfo.setAddress(patientAddress.getAddress_location() + COMMA + patientAddress.getAddress_landmark() + COMMA + patientAddress.getAddress_block());
        patientInfo.setAddressVillage(patientAddress.getAddress_village());
        patientInfo.setAddressDistrict(patientAddress.getAddress_district());
        patientInfo.setAddressState(patientAddress.getAddress_state());
        patientInfo.setNextPhaseName(patient.getCurrentTherapy().getPhases().getNextPhase());
        patientInfo.setPhasesNotPossibleToTransitionTo(mapPhaseNameToString(patient));
        patientInfo.setNearingPhaseTransition(patient.isNearingPhaseTransition());
        patientInfo.setTransitioning(patient.isTransitioning());
        patientInfo.setCurrentPhase(patient.getCurrentTherapy().getCurrentPhase());
        patientInfo.setRemainingDosesInCurrentPhase(patient.getRemainingDosesInCurrentPhase());
        patientInfo.setLastCompletedPhase(patient.getCurrentTherapy().getLastCompletedPhase());
        patientInfo.setCurrentTreatmentPaused(patient.isCurrentTreatmentPaused());
        patientInfo.setCurrentTreatmentClosed(patient.isCurrentTreatmentClosed());
        patientInfo.setLongestDoseInterruption(patient.getLongestDoseInterruption());
        patientInfo.setShowAlert((patient.isNearingPhaseTransition() || patient.isTransitioning()) && !patient.isOrHasBeenOnCp());
        patientInfo.setAdherenceCapturedForThisWeek(patient.hasAdherenceForLastReportingWeekForCurrentTherapy());
        patientInfo.setFlag(patient.getPatientFlag().isFlagSet());
        patientInfo.setTestResults(setTestResults(patient));


        if (alertColorConfiguration != null) {
                       setPatientAlertInfo(patientInfo, patientAlerts);
        }
        return patientInfo;
    }

    private void setPatientAlertInfo(PatientInfo patientInfo, PatientAlerts patientAlerts) {
        setAdherenceMissingWeekAlertInfo(patientInfo, patientAlerts);
        setCumulativeMissedDosesAlertInfo(patientInfo, patientAlerts);
        setTreatmentNotStartedAlertInfo(patientInfo, patientAlerts);
    }

    private void setTreatmentNotStartedAlertInfo(PatientInfo patientInfo, PatientAlerts patientAlerts) {
        PatientAlert treatmentNotStartedAlert = patientAlerts.getAlert(PatientAlertType.TreatmentNotStarted);
        patientInfo.setTreatmentNotStartedSeverityColor(alertColorConfiguration.getColorFor(PatientAlertType.TreatmentNotStarted, treatmentNotStartedAlert.getAlertSeverity()));
        patientInfo.setTreatmentNotStartedMessageCode();
    }

    private void setCumulativeMissedDosesAlertInfo(PatientInfo patientInfo, PatientAlerts patientAlerts) {
        PatientAlert cumulativeMissedDoseAlert = patientAlerts.getAlert(PatientAlertType.CumulativeMissedDoses);
        patientInfo.setCumulativeMissedDosesSeverityColor(alertColorConfiguration.getColorFor(PatientAlertType.CumulativeMissedDoses, cumulativeMissedDoseAlert.getAlertSeverity()));
        patientInfo.setCumulativeMissedDosesMessageCode();
    }

    private void setAdherenceMissingWeekAlertInfo(PatientInfo patientInfo, PatientAlerts patientAlerts) {
        PatientAlert adherenceAlert = patientAlerts.getAlert(PatientAlertType.AdherenceMissing);
        int adherenceAlertSeverity = adherenceAlert.getAlertSeverity();
        patientInfo.setAdherenceMissingWeeksSeverity(adherenceAlertSeverity);
        patientInfo.setAdherenceMissingWeeks((int) adherenceAlert.getValue());
        patientInfo.setAdherenceMissingSeverityColor(alertColorConfiguration.getColorFor(PatientAlertType.AdherenceMissing, adherenceAlertSeverity));
        patientInfo.setAdherenceMissingMessageCode();
    }

    public PatientInfo map(Patient patient) {
        return map(patient, null);
    }

    private void mapProviderDetails(Provider provider, PatientInfo patientInfo) {
        patientInfo.setProviderMobileNumber(provider.getPrimaryMobile());
        patientInfo.setProviderDistrict(provider.getDistrict());
    }


    private List<String> mapPhaseNameToString(Patient patient) {
        List<Phase> phases = patient.getHistoryOfPhases();
        List<String> names = new ArrayList<>();
        for (Phase phase : phases) {
            names.add(phase.name());
        }
        return names;
    }

    private TestResults setTestResults(Patient patient) {
        WeightStatistics weightStatistics = patient.getCurrentTherapy().getAggregatedWeightStatistics();
        SmearTestResults smearTestResults = patient.getCurrentTherapy().getAggregatedSmearTestResults();
        return new TestResults(smearTestResults, weightStatistics);
    }
}
