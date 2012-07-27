package org.motechproject.whp.ivr.util;


import org.motechproject.decisiontree.FlowSession;

import java.io.Serializable;
import java.util.List;

public class IvrSession {
    public static final String PATIENTS_WITHOUT_ADHERENCE = "patientsWithoutAdherence";
    public static final String PATIENTS_WITH_ADHERENCE = "patientsWithAdherence";
    public static final String CURRENT_PATIENT_INDEX = "currentPatientPosition";
    public static final String PROVIDER_ID = "providerId";

    private FlowSession flowSession;

    public IvrSession(FlowSession flowSession) {
        this.flowSession = flowSession;
    }

    public String getMobileNumber() {
        return flowSession.get("cid");
    }

    public void patientsWithoutAdherence(List<String> patientsWithoutAdherence) {
        flowSession.set(PATIENTS_WITHOUT_ADHERENCE, new SerializableList<>(patientsWithoutAdherence));
    }

    public List<String> patientsWithoutAdherence() {
        return (List<String>) flowSession.get(PATIENTS_WITHOUT_ADHERENCE);
    }

    public void patientsWithAdherence(List<String> patientsWithAdherence) {
        flowSession.set(PATIENTS_WITH_ADHERENCE, new SerializableList<>(patientsWithAdherence));
    }

    public List<String> patientsWithAdherence() {
        return (List<String>) flowSession.get(PATIENTS_WITH_ADHERENCE);
    }

    public Integer currentPatientNumber() {
        return currentPatientIndex() + 1;
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

    public void currentPatientIndex(int value) {
        flowSession.set(CURRENT_PATIENT_INDEX, value);
    }

    public String currentPatientId() {
        List<String> patientsWithoutAdherence = patientsWithoutAdherence();
        return patientsWithoutAdherence.get(currentPatientIndex()).toString();
    }

    public String nextPatient() {
        List<String> patientsWithoutAdherence = patientsWithoutAdherence();
        incrementCurrentPatientIndex();
        return patientsWithoutAdherence.get(currentPatientIndex()).toString();
    }

    private void incrementCurrentPatientIndex() {
        currentPatientIndex(nextPatientIndex());
    }

    public boolean hasNextPatient() {
        return (patientsWithoutAdherence().size() > nextPatientIndex());
    }

    public boolean hasPatientsWithoutAdherence() {
        return (patientsWithoutAdherence().size() > 0);
    }

    public String providerId() {
        return flowSession.get(PROVIDER_ID);
    }

    public void providerId(String providerId) {
        flowSession.set(PROVIDER_ID, providerId);
    }

}

