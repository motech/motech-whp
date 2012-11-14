package org.motechproject.whp.ivr.session;

import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.mockito.Mock;
import org.motechproject.decisiontree.core.FlowSession;
import org.motechproject.testing.utils.BaseUnitTest;
import org.motechproject.whp.adherence.domain.AdherenceSummaryByProvider;
import org.motechproject.whp.adherence.service.AdherenceDataService;
import org.motechproject.whp.common.domain.TreatmentWeekInstance;
import org.motechproject.whp.ivr.util.FlowSessionStub;
import org.motechproject.whp.patient.builder.PatientBuilder;
import org.motechproject.whp.patient.domain.Patient;
import org.motechproject.whp.user.repository.AllProviders;

import java.util.List;

import static java.util.Arrays.asList;
import static org.apache.commons.collections.ListUtils.sum;
import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;
import static org.motechproject.whp.user.builder.ProviderBuilder.newProviderBuilder;

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

        protected AdherenceSummaryByProvider adherenceSummary(List<Patient> patientsWithAdherence, List<Patient> patientsWithoutAdherence, String providerId) {
            AdherenceSummaryByProvider summary = new AdherenceSummaryByProvider(providerId, sum(patientsWithAdherence, patientsWithoutAdherence));
            when(adherenceDataService.getAdherenceSummary(providerId)).thenReturn(summary);
            return summary;
        }
    }

    public static class WhenProviderReportsAdherence extends AdherenceRecordingSessionTestCase {

        protected static final String PROVIDER_ID = "providerid";
        protected static final String MOBILE_NUMBER = "mobileNumber";
        protected static final String CALL_ID = "call_id1";
        private List<Patient> patientsWithAdherence;
        private List<Patient> patientsWithoutAdherence;
        protected DateTime now = new DateTime(2011, 1, 1, 10, 0, 0, 0);

        @Before
        public void setup() {
            initMocks(this);
            setupSession();

            LocalDate lastWeekStartDate = TreatmentWeekInstance.currentAdherenceCaptureWeek().startDate();
            patientsWithAdherence = asList(
                    new PatientBuilder().withDefaults().withTherapyStartDate(new LocalDate(2012, 7, 7)).withPatientId("patient1").withAdherenceProvidedForLastWeek().build(),
                    new PatientBuilder().withDefaults().withTherapyStartDate(new LocalDate(2012, 7, 7)).withPatientId("patient2").withAdherenceProvidedForLastWeek().build());

            patientsWithoutAdherence = asList(
                    new PatientBuilder().withPatientId("patient3").build(),
                    new PatientBuilder().withPatientId("patient4").build());

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
            IvrSession session = new IvrSession(adherenceRecordingSession.initialize(flowSession));
            assertEquals(asList("patient1", "patient2"), session.patientsWithAdherence());
        }

        @Test
        public void shouldInitializeSessionWithPatientsWithoutAdherence() {
            IvrSession session = new IvrSession(adherenceRecordingSession.initialize(flowSession));
            assertEquals(asList("patient3", "patient4"), session.patientsWithoutAdherence());
        }

        @Test
        public void shouldInitializeSessionWithProviderId() {
            IvrSession session = new IvrSession(adherenceRecordingSession.initialize(flowSession));
            assertEquals(PROVIDER_ID, session.providerId());
        }

        @Test
        public void shouldInitializeSessionWithMobileNumber() {
            IvrSession session = new IvrSession(adherenceRecordingSession.initialize(flowSession));
            assertEquals(MOBILE_NUMBER, session.mobileNumber());
        }

        @Test
        public void shouldInitializeSessionWithCallId() {
            IvrSession session = new IvrSession(adherenceRecordingSession.initialize(flowSession));
            assertEquals(CALL_ID, session.callId());
        }
    }

    public static class WhenProviderReportsAdherenceForSecondPatient extends AdherenceRecordingSessionTestCase {

        protected static final String PROVIDER_ID = "providerid";
        protected static final String MOBILE_NUMBER = "mobileNumber";
        private LocalDate lastWeekStartDate = TreatmentWeekInstance.currentAdherenceCaptureWeek().startDate();
        private List<Patient> patientsWithAdherence;
        private List<Patient> patientsWithoutAdherence;
        private List<Patient> patientsWithAdherenceAfterFirstInput;
        private List<Patient> patientsWithoutAdherenceAfterFirstInput;

        @Before
        public void setup() {
            initMocks(this);
            Patient patient1WithAdherence = new PatientBuilder().withDefaults().withPatientId("patient1").withTherapyStartDate(new LocalDate(2012, 7, 7)).withAdherenceProvidedForLastWeek().build();

            patientsWithAdherence = asList(patient1WithAdherence);
            patientsWithoutAdherence = asList(
                    new PatientBuilder().withPatientId("patient2").build(),
                    new PatientBuilder().withPatientId("patient3").build());

            patientsWithAdherenceAfterFirstInput = asList(
                    patient1WithAdherence,
                    new PatientBuilder().withPatientId("patient2").withDefaults().withTherapyStartDate(new LocalDate(2012, 7, 7)).withAdherenceProvidedForLastWeek().build());

            patientsWithoutAdherenceAfterFirstInput = asList(new PatientBuilder().withPatientId("patient3").build());

            setupSession();
            when(allProviders.findByMobileNumber(MOBILE_NUMBER)).thenReturn(newProviderBuilder().withProviderId(PROVIDER_ID).build());
            setupSummary();
            adherenceRecordingSession = new AdherenceRecordingSession(allProviders, adherenceDataService);
        }

        private void setupSummary() {
            AdherenceSummaryByProvider summary = new AdherenceSummaryByProvider(
                    PROVIDER_ID,
                    sum(patientsWithAdherence, patientsWithoutAdherence)
            );
            AdherenceSummaryByProvider summaryAfterFirstInput = new AdherenceSummaryByProvider(
                    PROVIDER_ID,
                    sum(patientsWithAdherenceAfterFirstInput, patientsWithoutAdherenceAfterFirstInput)
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
            IvrSession session = new IvrSession(adherenceRecordingSession.initialize(flowSession));
            assertEquals(asList("patient1"), session.patientsWithAdherence());
        }

        @Test
        public void shouldNotReinitializeSessionWithPatientsWithoutAdherence() {
            adherenceRecordingSession.initialize(flowSession);
            IvrSession session = new IvrSession(adherenceRecordingSession.initialize(flowSession));
            assertEquals(asList("patient2", "patient3"), session.patientsWithoutAdherence());
        }
    }

    public static class WhenAnotherProviderReportsAdherence extends AdherenceRecordingSessionTestCase {

        public static final String PROVIDER_ID = "providerid2";
        public static final String MOBILE_NUMBER = "mobileNumber2";
        private List<Patient> patientsWithAdherence;
        private List<Patient> patientsWithoutAdherence;
        private static final String CALL_ID = "call_id2";
        protected DateTime now = new DateTime(2011, 1, 1, 10, 10, 0, 0);

        @Before
        public void setup() {
            initMocks(this);
            patientsWithAdherence = asList(new PatientBuilder().withDefaults().withTherapyStartDate(new LocalDate(2012, 7, 7)).withPatientId("patient4").withAdherenceProvidedForLastWeek().build());
            patientsWithoutAdherence = asList(new PatientBuilder().withPatientId("patient5").build(), new PatientBuilder().withPatientId("patient6").build());
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
        public void shouldInitializeSessionWithPatientsWithAdherence() {
            IvrSession session = new IvrSession(adherenceRecordingSession.initialize(flowSession));
            assertEquals(asList("patient4"), session.patientsWithAdherence());
        }

        @Test
        public void shouldInitializeSessionWithPatientsWithoutAdherence() {
            IvrSession session = new IvrSession(adherenceRecordingSession.initialize(flowSession));
            assertEquals(asList("patient5", "patient6"), session.patientsWithoutAdherence());
        }

        @Test
        public void shouldInitializeSessionWithProviderId() {
            IvrSession session = new IvrSession(adherenceRecordingSession.initialize(flowSession));
            assertEquals(PROVIDER_ID, session.providerId());
        }

        @Test
        public void shouldInitializeSessionWithMobileNumber() {
            IvrSession session = new IvrSession(adherenceRecordingSession.initialize(flowSession));
            assertEquals(MOBILE_NUMBER, session.mobileNumber());
        }

        @Test
        public void shouldInitializeSessionWithCallId() {
            IvrSession session = new IvrSession(adherenceRecordingSession.initialize(flowSession));
            assertEquals(CALL_ID, session.callId());
        }
    }
}
