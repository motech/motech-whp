package org.motechproject.whp.ivr.util;


import org.motechproject.decisiontree.FlowSession;
import org.motechproject.whp.adherence.domain.AdherenceSummaryByProvider;

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

    public void setPatientsWithoutAdherence(List<String> patientsWithoutAdherence) {
        flowSession.set(PATIENTS_WITHOUT_ADHERENCE, new SerializableList<>(patientsWithoutAdherence));
    }

    public SerializableList getPatientsWithoutAdherence() {
        return flowSession.get(PATIENTS_WITHOUT_ADHERENCE);
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

    private void currentPatientIndex(int value) {
        flowSession.set(CURRENT_PATIENT_INDEX, value);
    }

    public String currentPatient() {
        SerializableList patientsWithoutAdherence = getPatientsWithoutAdherence();
        return patientsWithoutAdherence.get(currentPatientIndex()).toString();
    }

    public String nextPatient() {
        SerializableList patientsWithoutAdherence = getPatientsWithoutAdherence();
        incrementCurrentPatientIndex();
        return patientsWithoutAdherence.get(currentPatientIndex()).toString();
    }

    private void incrementCurrentPatientIndex() {
        currentPatientIndex(nextPatientIndex());
    }

    public boolean hasNextPatient() {
        return (getPatientsWithoutAdherence().size() > nextPatientIndex());
    }

    public String getProviderId() {
        return flowSession.get(PROVIDER_ID);
    }

    public void setProviderId(String providerId) {
        flowSession.set(PROVIDER_ID, providerId);
    }
}

