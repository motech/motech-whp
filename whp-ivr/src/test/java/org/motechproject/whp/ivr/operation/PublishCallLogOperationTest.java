package org.motechproject.whp.ivr.operation;

import org.hamcrest.core.Is;
import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.motechproject.decisiontree.FlowSession;
import org.motechproject.whp.ivr.session.IvrSession;
import org.motechproject.whp.ivr.util.FlowSessionStub;
import org.motechproject.whp.ivr.util.SerializableList;
import org.motechproject.whp.reporting.service.ReportingPublisherService;
import org.motechproject.whp.reports.contract.CallLogRequest;

import java.util.Arrays;
import java.util.List;

import static java.util.Arrays.asList;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.mockito.Mockito.verify;
import static org.mockito.MockitoAnnotations.initMocks;
import static org.motechproject.util.DateUtil.now;

public class PublishCallLogOperationTest {

    @Mock
    ReportingPublisherService reportingPublisherService;
    private List<String> patientsWithAdherence;
    private List<String> patientsWithoutAdherence;
    private List<String> patientsWithAdherenceRecordedInThisSession;

    @Before
    public void setUp()  {
        initMocks(this);
    }

    @Test
    public void shouldPublishCallLog(){
        String providerId = "providerId";
        DateTime startTime = now();
        DateTime endTime = now();

        FlowSession flowSession = setUpFlowSession(providerId, startTime);

        int totalPatients = patientsWithAdherence.size() + patientsWithoutAdherence.size();
        int adherenceCapturedInThisSession = patientsWithAdherenceRecordedInThisSession.size();
        int remainingPatientsWithoutAdherence = patientsWithoutAdherence.size() - patientsWithAdherenceRecordedInThisSession.size();

        CallLogRequest expectedCallLogRequest = createCallLogRequest(providerId, startTime, endTime, totalPatients, adherenceCapturedInThisSession, remainingPatientsWithoutAdherence);

        PublishCallLogOperation publishCallLogOperation = new PublishCallLogOperation(reportingPublisherService, endTime);

        publishCallLogOperation.perform("", flowSession);

        ArgumentCaptor<CallLogRequest> argumentCaptor = ArgumentCaptor.forClass(CallLogRequest.class);
        verify(reportingPublisherService).reportCallLog(argumentCaptor.capture());

        CallLogRequest callLogRequest = argumentCaptor.getValue();

        assertThat(callLogRequest, is(expectedCallLogRequest));
    }

    private FlowSession setUpFlowSession(String providerId, DateTime startTime) {
        FlowSession flowSession = new FlowSessionStub();
        flowSession.set(IvrSession.PROVIDER_ID, providerId);
        flowSession.set(IvrSession.CALL_START_TIME, startTime);

        patientsWithAdherence = asList("patient1");
        flowSession.set(IvrSession.PATIENTS_WITH_ADHERENCE, new SerializableList(patientsWithAdherence));
        patientsWithoutAdherence = asList("patient2", "patient3");
        flowSession.set(IvrSession.PATIENTS_WITHOUT_ADHERENCE, new SerializableList(patientsWithoutAdherence));
        patientsWithAdherenceRecordedInThisSession = asList("patient2");
        flowSession.set(IvrSession.PATIENTS_WITH_ADHERENCE_RECORDED_IN_THIS_SESSION, new SerializableList(patientsWithAdherenceRecordedInThisSession));
        return flowSession;
    }

    private CallLogRequest createCallLogRequest(String providerId, DateTime startTime, DateTime endTime, int totalPatients, int adherenceCaptured, int remainingPatientsWithoutAdherence) {
        CallLogRequest callLogRequest = new CallLogRequest();
        callLogRequest.setEndTime(endTime.toDate());
        callLogRequest.setStartTime(startTime.toDate());
        callLogRequest.setCalledBy(providerId);
        callLogRequest.setProviderId(providerId);
        callLogRequest.setTotalPatients(totalPatients);
        callLogRequest.setAdherenceCaptured(adherenceCaptured);
        callLogRequest.setAdherenceNotCaptured(remainingPatientsWithoutAdherence);
        return callLogRequest;
    }
}
