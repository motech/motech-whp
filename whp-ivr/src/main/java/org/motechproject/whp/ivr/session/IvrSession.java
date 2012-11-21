package org.motechproject.whp.ivr.session;


import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.codehaus.jackson.map.ObjectMapper;
import org.joda.time.DateTime;
import org.motechproject.decisiontree.FlowSession;
import org.motechproject.server.kookoo.KookooCallServiceImpl;
import org.motechproject.whp.adherence.request.UpdateAdherenceRequest;
import org.motechproject.whp.ivr.CallStatus;
import org.motechproject.whp.ivr.IVRInput;
import org.motechproject.whp.ivr.util.SerializableList;
import org.motechproject.whp.patient.domain.Patient;
import org.springframework.util.StringUtils;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.joda.time.DateTime.parse;
import static org.motechproject.util.DateUtil.setTimeZone;

public class IvrSession {

    public static final String IVR_FLASHING_CALL_ID = "flashingCallId";
    public static final String PATIENTS_WITHOUT_ADHERENCE = "patientsWithoutAdherence";
    public static final String PATIENTS_WITH_ADHERENCE = "patientsWithAdherence";
    public static final String PATIENTS_WITH_ADHERENCE_RECORDED_IN_THIS_SESSION = "patientsWithAdherenceRecordedInThisSession";
    public static final String CURRENT_PATIENT_INDEX = "currentPatientPosition";
    public static final String PROVIDER_ID = "providerId";
    public static final String START_OF_ADHERENCE_SUBMISSION = "startOfAdherenceSubmission";
    public static final String CALL_START_TIME = "CALL_START_TIME";
    public static final String SID = "sid";
    public static final String CURRENT_PATIENT_ADHERENCE_INPUT = "curretPatientAdherenceInput";
    public static final String CURRENT_INVALID_INPUT_RETRY_COUNT = "currentInvalidInputRetryCount";
    public static final String CURRENT_NO_INPUT_RETRY_COUNT = "currentNoInputRetryCount";
    public static final String IS_FIRST_INVALID_INPUT = "firstInvalidInput";
    private static final String NATIONAL_TELEPHONE_NUMBER_PREFIX = "0";
    public static final String CALL_STATUS = "callStatus";

    private FlowSession flowSession;

    public IvrSession(FlowSession flowSession) {
        this.flowSession = flowSession;
        // Stripping the national telephone number prefix from the actual mobile number for all practical usage
        String cid = flowSession.get("cid");
        if (StringUtils.hasText(cid) && cid.startsWith(NATIONAL_TELEPHONE_NUMBER_PREFIX)) {
            this.flowSession.set("cid", cid.substring(1));
        }
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
        return flowSession.get(SID).toString();
    }

    public void callId(String callId) {
        flowSession.set(SID, callId);
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
        return patientsWithAdherence() != null ? patientsWithAdherence().size() : 0;
    }

    public int countOfCurrentPatientsWithAdherence() {
        int countOfPatientsWithAdherenceRecordedInThisSession = patientsWithAdherenceRecordedInThisSession().size();
        return countOfPatientsWithAdherence() + countOfPatientsWithAdherenceRecordedInThisSession;
    }

    public int countOfCurrentPatientsWithoutAdherence() {
        return countOfAllPatients() - countOfCurrentPatientsWithAdherence();
    }

    public int countOfPatientsWithAdherenceRecordedInThisSession() {
        return patientsWithAdherenceRecordedInThisSession().size();
    }

    public void recordAdherenceForCurrentPatient() {
        List<String> patientsWithAdherenceRecordedInThisSession = patientsWithAdherenceRecordedInThisSession();
        patientsWithAdherenceRecordedInThisSession.add(currentPatientId());
    }

    private List<String> patientsWithAdherenceRecordedInThisSession() {
        List<String> patientsWithAdherenceRecordedInThisSession = (List<String>) flowSession.get(PATIENTS_WITH_ADHERENCE_RECORDED_IN_THIS_SESSION);
        if (patientsWithAdherenceRecordedInThisSession == null) {
            patientsWithAdherenceRecordedInThisSession = new SerializableList(new ArrayList<String>());
            flowSession.set(PATIENTS_WITH_ADHERENCE_RECORDED_IN_THIS_SESSION, (SerializableList) patientsWithAdherenceRecordedInThisSession);
        }

        return patientsWithAdherenceRecordedInThisSession;
    }

    public void recordCallStartTime(DateTime dateTime) {
        flowSession.set(CALL_START_TIME, dateTime);
    }

    public DateTime callStartTime() {
        return setTimeZone(parse(flowSession.get(CALL_START_TIME).toString()));
    }

    public void currentInvalidInputRetryCount(int retryCount) {
        flowSession.set(CURRENT_INVALID_INPUT_RETRY_COUNT, retryCount);
    }

    public void currentNoInputRetryCount(int retryCount) {
        flowSession.set(CURRENT_NO_INPUT_RETRY_COUNT, retryCount);
    }

    public int currentInvalidInputRetryCount() {
        Integer count = flowSession.get(CURRENT_INVALID_INPUT_RETRY_COUNT);
        return count == null ? 0 : count;
    }

    public int currentNoInputRetryCount() {
        Integer count = flowSession.get(CURRENT_NO_INPUT_RETRY_COUNT);
        return count == null ? 0 : count;
    }

    public void firstInvalidInput(boolean first) {
        flowSession.set(IS_FIRST_INVALID_INPUT, first);
    }

    public boolean firstInvalidInput() {
        Boolean flag = flowSession.get(IS_FIRST_INVALID_INPUT);
        return flag == null ? true : flag;
    }

    public void callStatus(CallStatus callStatus) {
        flowSession.set(CALL_STATUS, callStatus);
    }

    public CallStatus callStatus() {
        return flowSession.get(CALL_STATUS) == null ? CallStatus.PROVIDER_DISCONNECT : CallStatus.valueOf(flowSession.get(CALL_STATUS).toString());
    }

    public String flashingCallId() {
        String jsonDataMap = flowSession.get(KookooCallServiceImpl.CUSTOM_DATA_KEY);
        Map map = new Gson().fromJson(jsonDataMap, new TypeToken<Map<String, String>>(){}.getType());
        return map != null ? (String) map.get(IVR_FLASHING_CALL_ID) : null;
    }
}

