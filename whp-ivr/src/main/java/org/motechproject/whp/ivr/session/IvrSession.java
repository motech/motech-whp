package org.motechproject.whp.ivr.session;


import org.joda.time.DateTime;
import org.motechproject.decisiontree.FlowSession;
import org.motechproject.whp.ivr.IVRInput;
import org.motechproject.whp.ivr.util.SerializableList;
import org.motechproject.whp.patient.domain.Patient;

import java.util.ArrayList;
import java.util.List;

import static org.joda.time.DateTime.parse;
import static org.motechproject.util.DateUtil.setTimeZone;

public class IvrSession {
    public static final String PATIENTS_WITHOUT_ADHERENCE = "patientsWithoutAdherence";
    public static final String PATIENTS_WITH_ADHERENCE = "patientsWithAdherence";
    public static final String CURRENT_PATIENT_INDEX = "currentPatientPosition";
    public static final String PROVIDER_ID = "providerId";
    public static final String CURRENT_PATIENT_ADHERENCE_INPUT = "curretPatientAdherenceInput";
    public static final String START_OF_ADHERENCE_SUBMISSION = "startOfAdherenceSubmission";

    private FlowSession flowSession;

    public IvrSession(FlowSession flowSession) {
        this.flowSession = flowSession;
    }

    public String mobileNumber() {
        return flowSession.get("cid");
    }


    private List<String> getPatientIds(List<Patient> patients) {
        List<String> patientIds = new ArrayList<>();
        for (Patient patient : patients)
            patientIds.add(patient.getPatientId());
        return patientIds;
    }

    public void patientsWithoutAdherence(List<Patient> patientsWithoutAdherence) {
        List<String> patientIds = getPatientIds(patientsWithoutAdherence);
        flowSession.set(PATIENTS_WITHOUT_ADHERENCE, new SerializableList<>(patientIds));
    }

    public List<String> patientsWithoutAdherence() {
        return (List<String>) flowSession.get(PATIENTS_WITHOUT_ADHERENCE);
    }

    public void patientsWithAdherence(List<Patient> patientsWithAdherence) {
        List<String> patientIds = getPatientIds(patientsWithAdherence);
        flowSession.set(PATIENTS_WITH_ADHERENCE, new SerializableList<>(patientIds));
    }

    public List<String> patientsWithAdherence() {
        return (List<String>) flowSession.get(PATIENTS_WITH_ADHERENCE);
    }

    public Integer currentPatientNumber() {
        return currentPatientIndex() + 1;
    }

    public String callId() {
        return flowSession.get("sid").toString();
    }

    public void callId(String callId) {
        flowSession.set("sid", callId);
    }

    public void currentPatientIndex(int value) {
        flowSession.set(CURRENT_PATIENT_INDEX, value);
    }

    public String currentPatientId() {
        List<String> patientsWithoutAdherence = patientsWithoutAdherence();
        return patientsWithoutAdherence.get(currentPatientIndex());
    }

    public String nextPatient() {
        List<String> patientsWithoutAdherence = patientsWithoutAdherence();
        incrementCurrentPatientIndex();
        return patientsWithoutAdherence.get(currentPatientIndex());
    }

    public boolean hasNextPatient() {
        return (countOfPatientsWithoutAdherence() > nextPatientIndex());
    }

    public String providerId() {
        return flowSession.get(PROVIDER_ID);
    }

    public void providerId(String providerId) {
        flowSession.set(PROVIDER_ID, providerId);
    }

    public boolean hasPatientsWithoutAdherence() {
        return (countOfPatientsWithoutAdherence() > 0);
    }

    public IVRInput adherenceInputForCurrentPatient() {
        return new IVRInput(flowSession.get(CURRENT_PATIENT_ADHERENCE_INPUT).toString());
    }

    public void resetCurrentPatientIndex() {
        flowSession.set(CURRENT_PATIENT_INDEX, null);
    }

    public void adherenceInputForCurrentPatient(String input) {
        flowSession.set(CURRENT_PATIENT_ADHERENCE_INPUT, input);
    }

    public void startOfAdherenceSubmission(DateTime time) {
        flowSession.set(START_OF_ADHERENCE_SUBMISSION, time);
    }

    public DateTime startOfAdherenceSubmission() {
        return setTimeZone(parse(flowSession.get(START_OF_ADHERENCE_SUBMISSION).toString()));
    }

    private Integer currentPatientIndex() {
        Integer currentNodePosition = (Integer) flowSession.get(CURRENT_PATIENT_INDEX);
        if (currentNodePosition == null) {
            currentNodePosition = 0;
            flowSession.set(CURRENT_PATIENT_INDEX, currentNodePosition);
        }
        return currentNodePosition;
    }

    private Integer nextPatientIndex() {
        return currentPatientIndex() + 1;
    }

    private void incrementCurrentPatientIndex() {
        currentPatientIndex(nextPatientIndex());
    }

    public Integer countOfAllPatients() {
        return countOfPatientsWithAdherence() + countOfPatientsWithoutAdherence();
    }

    private int countOfPatientsWithoutAdherence() {
        return patientsWithoutAdherence().size();
    }

    public int countOfPatientsWithAdherence() {
        return patientsWithAdherence().size();
    }
}

