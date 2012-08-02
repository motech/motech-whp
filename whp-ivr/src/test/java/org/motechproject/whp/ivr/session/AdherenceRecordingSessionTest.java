package org.motechproject.whp.ivr.session;

import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.mockito.Mock;
import org.motechproject.decisiontree.FlowSession;
import org.motechproject.testing.utils.BaseUnitTest;
import org.motechproject.whp.adherence.domain.AdherenceSummaryByProvider;
import org.motechproject.whp.adherence.service.AdherenceDataService;
import org.motechproject.whp.ivr.util.FlowSessionStub;
import org.motechproject.whp.ivr.util.IvrSession;
import org.motechproject.whp.user.repository.AllProviders;

import java.util.List;

import static java.util.Arrays.asList;
import static org.apache.commons.collections.ListUtils.sum;
import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;
import static org.motechproject.whp.patient.builder.ProviderBuilder.newProviderBuilder;

@RunWith(Suite.class)
@Suite.SuiteClasses({
        AdherenceRecordingSessionTest.WhenProviderReportsAdherence.class,
        AdherenceRecordingSessionTest.WhenProviderReportsAdherenceForSecondPatient.class,
        AdherenceRecordingSessionTest.WhenAnotherProviderReportsAdherence.class
})
public class AdherenceRecordingSessionTest {

    public static class AdherenceRecordingSessionTestCase extends BaseUnitTest {
        @Mock
        protected AdherenceDataService adherenceDataService;
        @Mock
        protected AllProviders allProviders;

        protected FlowSession flowSession;

        protected AdherenceRecordingSession adherenceRecordingSession;

        protected AdherenceSummaryByProvider adherenceSummary(List<String> patientsWithAdherence, List<String> patientsWithoutAdherence, String providerId) {
            AdherenceSummaryByProvider summary = new AdherenceSummaryByProvider(providerId, sum(patientsWithAdherence, patientsWithoutAdherence), patientsWithAdherence);
            when(adherenceDataService.getAdherenceSummary(providerId)).thenReturn(summary);
            return summary;
        }
    }

    public static class WhenProviderReportsAdherence extends AdherenceRecordingSessionTestCase {

        protected static final String PROVIDER_ID = "providerid";
        protected static final String MOBILE_NUMBER = "mobileNumber";
        protected static final String CALL_ID = "call_id1";
        private List<String> patientsWithAdherence = asList("patient1", "patient2");
        private List<String> patientsWithoutAdherence = asList("patient3", "patient4");
        protected DateTime now = new DateTime(2011, 1, 1, 10, 0, 0, 0);

        @Before
        public void setup() {
            initMocks(this);
            setupSession();
            when(allProviders.findByMobileNumber(MOBILE_NUMBER)).thenReturn(newProviderBuilder().withProviderId(PROVIDER_ID).build());
            setupSummary();
            mockCurrentDate(now);
            adherenceRecordingSession = new AdherenceRecordingSession(allProviders, adherenceDataService);
        }

        private void setupSession() {
            flowSession = new FlowSessionStub();
            flowSession.set("cid", MOBILE_NUMBER);
            flowSession.set("sid", CALL_ID);
        }

        private void setupSummary() {
            adherenceSummary(patientsWithAdherence, patientsWithoutAdherence, PROVIDER_ID);
        }

        @Test
        public void shouldInitializeSessionWithPatientsWithAdherence() {
            assertEquals(asList("patient1", "patient2"), adherenceRecordingSession.initialize(flowSession).patientsWithAdherence());
        }

        @Test
        public void shouldInitializeSessionWithPatientsWithoutAdherence() {
            assertEquals(asList("patient3", "patient4"), adherenceRecordingSession.initialize(flowSession).patientsWithoutAdherence());
        }

        @Test
        public void shouldInitializeSessionWithProviderId() {
            assertEquals(PROVIDER_ID, adherenceRecordingSession.initialize(flowSession).providerId());
        }

        @Test
        public void shouldInitializeSessionWithMobileNumber() {
            assertEquals(MOBILE_NUMBER, adherenceRecordingSession.initialize(flowSession).getMobileNumber());
        }

        @Test
        public void shouldInitializeSessionWithCallId() {
            assertEquals(CALL_ID, adherenceRecordingSession.initialize(flowSession).callId());
        }

        @Test
        public void shouldSetCurrentTimeInSessionWhenCollectingAdherenceInput() {
            assertEquals(now, adherenceRecordingSession.collectingAdherenceInput("patientId1", flowSession).startOfAdherenceSubmission());
        }
    }

    public static class WhenProviderReportsAdherenceForSecondPatient extends AdherenceRecordingSessionTestCase {

        protected static final String PROVIDER_ID = "providerid";
        protected static final String MOBILE_NUMBER = "mobileNumber";

        private List<String> patientsWithAdherence = asList("patient1");
        private List<String> patientsWithoutAdherence = asList("patient2", "patient3");
        private List<String> patientsWithAdherenceAfterFirstInput = asList("patient1", "patient2");
        private List<String> patientsWithoutAdherenceAfterFirstInput = asList("patient3");

        @Before
        public void setup() {
            initMocks(this);
            setupSession();
            when(allProviders.findByMobileNumber(MOBILE_NUMBER)).thenReturn(newProviderBuilder().withProviderId(PROVIDER_ID).build());
            setupSummary();
            adherenceRecordingSession = new AdherenceRecordingSession(allProviders, adherenceDataService);
        }

        private void setupSummary() {
            AdherenceSummaryByProvider summary = new AdherenceSummaryByProvider(
                    PROVIDER_ID,
                    sum(patientsWithAdherence, patientsWithoutAdherence),
                    patientsWithAdherence
            );
            AdherenceSummaryByProvider summaryAfterFirstInput = new AdherenceSummaryByProvider(
                    PROVIDER_ID,
                    sum(patientsWithAdherenceAfterFirstInput, patientsWithoutAdherenceAfterFirstInput),
                    patientsWithAdherenceAfterFirstInput
            );
            when(adherenceDataService.getAdherenceSummary(PROVIDER_ID))
                    .thenReturn(summary)
                    .thenReturn(summaryAfterFirstInput);
        }

        private void setupSession() {
            flowSession = new FlowSessionStub();
            flowSession.set("cid", MOBILE_NUMBER);
        }

        @Test
        public void shouldNotReinitializeSessionWithPatientsWithAdherence() {
            adherenceRecordingSession.initialize(flowSession);
            IvrSession session = adherenceRecordingSession.initialize(flowSession);
            assertEquals(asList("patient1"), session.patientsWithAdherence());
        }

        @Test
        public void shouldNotReinitializeSessionWithPatientsWithoutAdherence() {
            adherenceRecordingSession.initialize(flowSession);
            IvrSession session = adherenceRecordingSession.initialize(flowSession);
            assertEquals(asList("patient2", "patient3"), session.patientsWithoutAdherence());
        }
    }

    public static class WhenAnotherProviderReportsAdherence extends AdherenceRecordingSessionTestCase {

        public static final String PROVIDER_ID = "providerid2";
        public static final String MOBILE_NUMBER = "mobileNumber2";
        private List<String> patientsWithAdherence = asList("patient4");
        private List<String> patientsWithoutAdherence = asList("patient5", "patient6");
        private static final String CALL_ID = "call_id2";
        protected DateTime now = new DateTime(2011, 1, 1, 10, 10, 0, 0);

        @Before
        public void setup() {
            initMocks(this);
            setupSession();
            when(allProviders.findByMobileNumber(anyString())).thenReturn(newProviderBuilder().withProviderId(PROVIDER_ID).build());
            setupSummary();
            mockCurrentDate(now);
            adherenceRecordingSession = new AdherenceRecordingSession(allProviders, adherenceDataService);
        }

        private void setupSession() {
            flowSession = new FlowSessionStub();
            flowSession.set("cid", MOBILE_NUMBER);
            flowSession.set("sid", CALL_ID);
        }

        private void setupSummary() {
            adherenceSummary(patientsWithAdherence, patientsWithoutAdherence, PROVIDER_ID);
        }

        @Test
        public void shouldSetPWhenPreparingSession() {
            assertEquals(asList("patient4"), adherenceRecordingSession.initialize(flowSession).patientsWithAdherence());
        }

        @Test
        public void shouldSetAdherenceSummaryWhenPreparingSession() {
            assertEquals(asList("patient5", "patient6"), adherenceRecordingSession.initialize(flowSession).patientsWithoutAdherence());
        }

        @Test
        public void shouldInitializeSessionWithProviderId() {
            assertEquals(PROVIDER_ID, adherenceRecordingSession.initialize(flowSession).providerId());
        }

        @Test
        public void shouldInitializeSessionWithMobileNumber() {
            assertEquals(MOBILE_NUMBER, adherenceRecordingSession.initialize(flowSession).getMobileNumber());
        }

        @Test
        public void shouldInitializeSessionWithCallId() {
            assertEquals(CALL_ID, adherenceRecordingSession.initialize(flowSession).callId());
        }

        @Test
        public void shouldSetCurrentTimeInSessionWhenCollectingAdherenceInput() {
            assertEquals(now, adherenceRecordingSession.collectingAdherenceInput("patientId4", flowSession).startOfAdherenceSubmission());
        }
    }
}
