package org.motechproject.whp.ivr.tree;

import org.apache.commons.codec.binary.Base64;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.hamcrest.core.Is;
import org.joda.time.LocalDate;
import org.junit.*;
import org.mockito.ArgumentCaptor;
import org.mortbay.jetty.Server;
import org.mortbay.jetty.servlet.Context;
import org.mortbay.jetty.servlet.ServletHolder;
import org.motechproject.adherence.repository.AllAdherenceLogs;
import org.motechproject.util.DateUtil;
import org.motechproject.whp.adherence.domain.WeeklyAdherenceSummary;
import org.motechproject.whp.adherence.service.WHPAdherenceService;
import org.motechproject.whp.common.util.SpringIntegrationTest;
import org.motechproject.whp.patient.builder.PatientBuilder;
import org.motechproject.whp.patient.domain.Patient;
import org.motechproject.whp.patient.repository.AllPatients;
import org.motechproject.whp.reporting.service.ReportingPublisherService;
import org.motechproject.whp.reports.contract.AdherenceCaptureRequest;
import org.motechproject.whp.reports.contract.CallLogRequest;
import org.motechproject.whp.user.domain.Provider;
import org.motechproject.whp.user.repository.AllProviders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.web.servlet.DispatcherServlet;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.util.UUID;

import static java.lang.String.format;
import static junit.framework.Assert.assertEquals;
import static org.custommonkey.xmlunit.XMLAssert.assertXMLEqual;
import static org.custommonkey.xmlunit.XMLUnit.setIgnoreWhitespace;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ContextConfiguration(locations = {"/test-applicationIVRContext.xml"})
public class AdherenceCaptureTreeIT extends SpringIntegrationTest {

    static Server server;
    static String CONTEXT_PATH = "/whp";
    public static final String TREE_PATH_START = new String(Base64.encodeBase64URLSafe("/".getBytes()));
    public static final String TREE_PATH_ADHERENCE_CAPTURE = new String(Base64.encodeBase64URLSafe("/1".getBytes()));

    String KOOKOO_CALLBACK_URL = "/kookoo/ivr";
    String SERVER_URL = "http://localhost:7080" + CONTEXT_PATH + KOOKOO_CALLBACK_URL;

    DefaultHttpClient decisionTreeController;

    @Autowired
    AdherenceCaptureTree adherenceCaptureTree;

    @Autowired
    AllProviders allProviders;

    @Autowired
    AllPatients allPatients;

    @Autowired
    WHPAdherenceService adherenceService;

    @Autowired
    AllAdherenceLogs allAdherenceLogs;

    Provider provider;
    Patient patient1;
    Patient patient2;
    Patient patient3;
    private static DispatcherServlet dispatcherServlet;
    private ReportingPublisherService reportingPublisherService;

    @BeforeClass
    public static void startServer() throws Exception {
        server = new Server(7080);
        Context context = new Context(server, CONTEXT_PATH);

        dispatcherServlet = new DispatcherServlet();
        dispatcherServlet.setContextConfigLocation("classpath:test-applicationIVRContext.xml");

        ServletHolder servletHolder = new ServletHolder(dispatcherServlet);
        context.addServlet(servletHolder, "/*");
        server.setHandler(context);
        server.start();
    }

    @Before
    public void setup() {
        setIgnoreWhitespace(true);
        decisionTreeController = new DefaultHttpClient();
        adherenceCaptureTree.load();
        reportingPublisherService = getMockedReportingPublisherService();
        setUpTestData();
    }

    private void setUpTestData() {
        String providerId = "provider1";
        provider = new Provider(providerId, "123456", "Vaishali", DateUtil.now());

        allProviders.add(provider);

        LocalDate treatmentStartDate = DateUtil.today().minusDays(10);
        patient1 = new PatientBuilder().withDefaults().withCurrentTreatmentStartDate(treatmentStartDate).withTherapyStartDate(treatmentStartDate).withPatientId("patientid1").withProviderId(providerId).build();
        patient2 = new PatientBuilder().withDefaults().withCurrentTreatmentStartDate(treatmentStartDate).withTherapyStartDate(treatmentStartDate).withPatientId("patientid2").withProviderId(providerId).build();
        patient3 = new PatientBuilder().withDefaults().withCurrentTreatmentStartDate(treatmentStartDate).withTherapyStartDate(treatmentStartDate).withPatientId("patientid3").withProviderId(providerId).build();

        allPatients.add(patient1);
        allPatients.add(patient2);
        allPatients.add(patient3);
    }

    @Test
    public void shouldPlayWelcomeMessage() throws IOException, SAXException {
        String response = decisionTreeController.execute(new HttpGet(format("%s?tree=adherenceCapture&trP=Lw&ln=en", SERVER_URL)), new BasicResponseHandler());
        String expectedResponse = welcomeMessageResponse();
        assertXMLEqual(expectedResponse, response);
    }

    @Test
    public void shouldTransitionToAdherenceSummaryNode() throws IOException, SAXException {
        String sessionId = UUID.randomUUID().toString();
        String response = decisionTreeController.execute(new HttpGet(format("%s?tree=adherenceCapture&trP=%s&ln=en&event=GotDTMF&data=1&cid=%s&sid=%s", SERVER_URL, TREE_PATH_START, provider.getPrimaryMobile(), sessionId)), new BasicResponseHandler());
        String expectedResponse = adherenceSummaryResponse(TREE_PATH_ADHERENCE_CAPTURE);
        assertXMLEqual(expectedResponse, response);
    }


    @Test
    public void shouldAskForConfirmation_uponEnteringValidAdherenceValue() throws IOException, SAXException {
        String sessionId = UUID.randomUUID().toString();
        String format = format("%s?tree=adherenceCapture&trP=%s&ln=en&event=GotDTMF&data=2&cid=%s&sid=%s", SERVER_URL, TREE_PATH_ADHERENCE_CAPTURE, provider.getPrimaryMobile(), sessionId);
        String response = decisionTreeController.execute(new HttpGet(format), new BasicResponseHandler());
        WeeklyAdherenceSummary adherenceSummaryPatient1 = adherenceService.currentWeekAdherence(patient1);

        assertEquals(0, adherenceSummaryPatient1.getDosesTaken());

        String nextTreePath = new String(Base64.encodeBase64URLSafe("/1/2".getBytes()));
        String expectedResponse = confirmPatient1AdherenceResponse(nextTreePath);
        assertXMLEqual(expectedResponse, response);
    }


    @Test
    public void shouldRecordAdherenceForAPatient() throws IOException, SAXException {
        String sessionId = UUID.randomUUID().toString();

        navigateToAdherenceSummary(sessionId);

        enterAdherenceForPatient(sessionId, 2, TREE_PATH_ADHERENCE_CAPTURE);

        String confirmAdherenceTreePath = new String(Base64.encodeBase64URLSafe("/1/2".getBytes()));
        String response = confirmAdherence(sessionId, confirmAdherenceTreePath);

        String transitionPath = new String(Base64.encodeBase64URLSafe("/1/2/1".getBytes()));
        String expectedResponseForPatient1Adherence = secondPatientProvideAdherenceResponse(transitionPath);
        WeeklyAdherenceSummary adherenceSummaryPatient1 = adherenceService.currentWeekAdherence(patient1);

        assertEquals(2, adherenceSummaryPatient1.getDosesTaken());
        assertXMLEqual(expectedResponseForPatient1Adherence, response);
        verify(reportingPublisherService).reportAdherenceCapture(any(AdherenceCaptureRequest.class));
    }

    private String navigateToAdherenceSummary(String sessionId) throws IOException {
        return decisionTreeController.execute(new HttpGet(format("%s?tree=adherenceCapture&trP=%s&ln=en&event=GotDTMF&data=1&cid=%s&sid=%s", SERVER_URL, TREE_PATH_START, provider.getPrimaryMobile(), sessionId)), new BasicResponseHandler());
    }

    @Test
    public void shouldRecordAdherenceForAllPatients() throws IOException, SAXException {
        String sessionId = UUID.randomUUID().toString();

        navigateToAdherenceSummary(sessionId);

        int adherenceCapturedForFirstPatient = 2;

        String treePath = "/1";
        //enter adherence for first patient
        enterAdherenceForPatient(sessionId, adherenceCapturedForFirstPatient, encodeBase64(treePath));

        treePath = treePath + "/" + adherenceCapturedForFirstPatient;

        //confirm adherence for first patient
        confirmAdherence(sessionId, encodeBase64(treePath));

        //verify(reportingPublisherService).reportAdherenceCapture(any(AdherenceCaptureRequest.class));
        WeeklyAdherenceSummary adherenceSummaryPatient1 = adherenceService.currentWeekAdherence(patient1);
        assertEquals(adherenceCapturedForFirstPatient, adherenceSummaryPatient1.getDosesTaken());

        //enter adherence for second patient
        int adherenceCapturedForSecondPatient = 3;
        String treePathAfterConfirmingAdherenceForFirstPatient = treePath + "/1";
        enterAdherenceForPatient(sessionId, adherenceCapturedForSecondPatient, encodeBase64(treePathAfterConfirmingAdherenceForFirstPatient));

        //capture adherence for second patient
        String treePathAfterEnteringAdherenceForSecondPatient = treePathAfterConfirmingAdherenceForFirstPatient + "/" + adherenceCapturedForSecondPatient;
        confirmAdherence(sessionId, encodeBase64(treePathAfterEnteringAdherenceForSecondPatient));

        WeeklyAdherenceSummary adherenceSummaryPatient2 = adherenceService.currentWeekAdherence(patient2);
        assertEquals(adherenceCapturedForSecondPatient, adherenceSummaryPatient2.getDosesTaken());
        //verify(reportingPublisherService).reportAdherenceCapture(any(AdherenceCaptureRequest.class));


        //enter adherence for 3rd patient
        int adherenceCapturedForThirdPatient = 2;
        String treePathAfterConfirmingAdherenceForSecondPatient = treePathAfterEnteringAdherenceForSecondPatient + "/1";
        enterAdherenceForPatient(sessionId, adherenceCapturedForThirdPatient, encodeBase64(treePathAfterConfirmingAdherenceForSecondPatient));


        //confirm adherence for 3rd patient
        String treePathAfterEnteringAdherenceForThirdPatient = treePathAfterConfirmingAdherenceForSecondPatient + "/" + adherenceCapturedForThirdPatient;
        String responseAfterGivingAdherenceForLastPatient = confirmAdherence(sessionId, encodeBase64(treePathAfterEnteringAdherenceForThirdPatient));

        WeeklyAdherenceSummary adherenceSummaryPatient3 = adherenceService.currentWeekAdherence(patient3);
        assertEquals(adherenceCapturedForThirdPatient, adherenceSummaryPatient3.getDosesTaken());
        //verify(reportingPublisherService).reportAdherenceCapture(any(AdherenceCaptureRequest.class));

        String expectedResponse = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                "<response>\n" +
                "                        <playaudio>http://localhost:8080/whp/wav/stream/en/thankYou.wav</playaudio>\n" +
                "                                <playaudio>http://localhost:8080/whp/wav/stream/en/summaryMessage1.wav</playaudio>\n" +
                "                                <playaudio>http://localhost:8080/whp/wav/stream/en/3.wav</playaudio>\n" +
                "                                <playaudio>http://localhost:8080/whp/wav/stream/en/summaryMessage2.wav</playaudio>\n" +
                "                                <playaudio>http://localhost:8080/whp/wav/stream/en/3.wav</playaudio>\n" +
                "                                <playaudio>http://localhost:8080/whp/wav/stream/en/summaryMessage3.wav</playaudio>\n" +
                "                                <playaudio>http://localhost:8080/whp/wav/stream/en/completionMessage.wav</playaudio>\n" +
                "                                <playaudio>http://localhost:8080/whp/wav/stream/en/musicEnd-note.wav</playaudio>\n" +
                "                        <hangup></hangup>\n" +
                "    </response>";

        assertThat(responseAfterGivingAdherenceForLastPatient, is(expectedResponse));

        ArgumentCaptor<CallLogRequest> argumentCaptor = ArgumentCaptor.forClass(CallLogRequest.class);
        verify(reportingPublisherService, times(1)).reportCallLog(argumentCaptor.capture());
        CallLogRequest callLogRequest = argumentCaptor.getValue();

        assertThat(callLogRequest.getProviderId(), Is.is(provider.getProviderId()));
    }

    @Test
    public void shouldPlayAdherenceSummaryWhenProviderHasProvidedAdherenceForAllPatients() throws IOException, SAXException {
        shouldRecordAdherenceForAllPatients();
        String sessionId = UUID.randomUUID().toString();

        String response = navigateToAdherenceSummary(sessionId);

        String expectedResponse = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                "<response>\n" +
                "                        <playaudio>http://localhost:8080/whp/wav/stream/en/summaryMessage1.wav</playaudio>\n" +
                "                                <playaudio>http://localhost:8080/whp/wav/stream/en/3.wav</playaudio>\n" +
                "                                <playaudio>http://localhost:8080/whp/wav/stream/en/summaryMessage2.wav</playaudio>\n" +
                "                                <playaudio>http://localhost:8080/whp/wav/stream/en/3.wav</playaudio>\n" +
                "                                <playaudio>http://localhost:8080/whp/wav/stream/en/summaryMessage3.wav</playaudio>\n" +
                "                                <playaudio>http://localhost:8080/whp/wav/stream/en/completionMessage.wav</playaudio>\n" +
                "                                <playaudio>http://localhost:8080/whp/wav/stream/en/musicEnd-note.wav</playaudio>\n" +
                "                        <hangup></hangup>\n" +
                "    </response>";

        assertThat(response, is(expectedResponse));
    }

    private String confirmAdherence(String sessionId, String confirmAdherenceTreePath) throws IOException {
        String confirmAdherenceUrl = format("%s?tree=adherenceCapture&trP=%s&ln=en&event=GotDTMF&data=1&cid=%s&sid=%s", SERVER_URL, confirmAdherenceTreePath, provider.getPrimaryMobile(), sessionId);
        return decisionTreeController.execute(new HttpGet(confirmAdherenceUrl), new BasicResponseHandler());
    }

    private String enterAdherenceForPatient(String sessionId, int adherence, String treePath) throws IOException {
        String adherenceCaptureUrl = format("%s?tree=adherenceCapture&trP=%s&ln=en&event=GotDTMF&data=%s&cid=%s&sid=%s", SERVER_URL, treePath, adherence, provider.getPrimaryMobile(), sessionId);
        return decisionTreeController.execute(new HttpGet(adherenceCaptureUrl), new BasicResponseHandler());
    }

    private String encodeBase64(String treePath) {
        return new String(Base64.encodeBase64URLSafe(treePath.getBytes()));
    }

    @Test
    public void shouldSkipForInvalidInputs() throws IOException, SAXException {
        String sessionId = UUID.randomUUID().toString();
        HttpGet request = new HttpGet(format("%s?tree=adherenceCapture&trP=%s&ln=en&event=GotDTMF&data=8&sid=%s&cid=%s", SERVER_URL, TREE_PATH_ADHERENCE_CAPTURE, sessionId, provider.getPrimaryMobile()));
        String response = decisionTreeController.execute(request, new BasicResponseHandler());
        String expectedResponseForPatient1Adherence = secondPatientProvideAdherenceResponse("LzEvOA");

        WeeklyAdherenceSummary adherenceSummaryPatient1 = adherenceService.currentWeekAdherence(patient1);
        assertEquals(0, adherenceSummaryPatient1.getDosesTaken());
        assertXMLEqual(expectedResponseForPatient1Adherence, response);

        String treePathForSecondPatient = new String(Base64.encodeBase64URLSafe("/1/8".getBytes()));
        response = decisionTreeController.execute(new HttpGet(format("%s?tree=adherenceCapture&trP=%s&ln=en&event=GotDTMF&data=9&sid=%s&cid=%s", SERVER_URL, treePathForSecondPatient, sessionId, provider.getPrimaryMobile())), new BasicResponseHandler());

        String nextTreePath = new String(Base64.encodeBase64URLSafe("/1/8/9".getBytes()));
        String expectedResponseForPatient2Adherence = thirdPatientProvideAdherenceResponse(nextTreePath);

        WeeklyAdherenceSummary adherenceSummaryPatient2 = adherenceService.currentWeekAdherence(patient2);
        assertEquals(0, adherenceSummaryPatient2.getDosesTaken());

        assertXMLEqual(expectedResponseForPatient2Adherence, response);

    }

    private String thirdPatientProvideAdherenceResponse(String nextTreePath) {
        return "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                "<response>\n" +
                "                        <playaudio>http://localhost:8080/whp/wav/stream/en/patientList.wav</playaudio>\n" +
                "                                <playaudio>http://localhost:8080/whp/wav/stream/en/3.wav</playaudio>\n" +
                "                                <playaudio>http://localhost:8080/whp/wav/stream/en/p.wav</playaudio>\n" +
                "                                <playaudio>http://localhost:8080/whp/wav/stream/en/a.wav</playaudio>\n" +
                "                                <playaudio>http://localhost:8080/whp/wav/stream/en/t.wav</playaudio>\n" +
                "                                <playaudio>http://localhost:8080/whp/wav/stream/en/i.wav</playaudio>\n" +
                "                                <playaudio>http://localhost:8080/whp/wav/stream/en/e.wav</playaudio>\n" +
                "                                <playaudio>http://localhost:8080/whp/wav/stream/en/n.wav</playaudio>\n" +
                "                                <playaudio>http://localhost:8080/whp/wav/stream/en/t.wav</playaudio>\n" +
                "                                <playaudio>http://localhost:8080/whp/wav/stream/en/i.wav</playaudio>\n" +
                "                                <playaudio>http://localhost:8080/whp/wav/stream/en/d.wav</playaudio>\n" +
                "                                <playaudio>http://localhost:8080/whp/wav/stream/en/3.wav</playaudio>\n" +
                "                                <playaudio>http://localhost:8080/whp/wav/stream/en/enterAdherence.wav</playaudio>\n" +
                "                        <collectdtmf l=\"50\" t=\"#\"></collectdtmf>\n" +
                "        <gotourl>http://localhost:7080/whp/kookoo/ivr?type=kookoo&amp;ln=en&amp;tree=adherenceCapture&amp;trP=" + nextTreePath + "</gotourl>\n" +
                "    </response>";
    }

    private String secondPatientProvideAdherenceResponse(String nextTreePath) {
        return "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                "<response>\n" +
                "                        <playaudio>http://localhost:8080/whp/wav/stream/en/patientList.wav</playaudio>\n" +
                "                                <playaudio>http://localhost:8080/whp/wav/stream/en/2.wav</playaudio>\n" +
                "                                <playaudio>http://localhost:8080/whp/wav/stream/en/p.wav</playaudio>\n" +
                "                                <playaudio>http://localhost:8080/whp/wav/stream/en/a.wav</playaudio>\n" +
                "                                <playaudio>http://localhost:8080/whp/wav/stream/en/t.wav</playaudio>\n" +
                "                                <playaudio>http://localhost:8080/whp/wav/stream/en/i.wav</playaudio>\n" +
                "                                <playaudio>http://localhost:8080/whp/wav/stream/en/e.wav</playaudio>\n" +
                "                                <playaudio>http://localhost:8080/whp/wav/stream/en/n.wav</playaudio>\n" +
                "                                <playaudio>http://localhost:8080/whp/wav/stream/en/t.wav</playaudio>\n" +
                "                                <playaudio>http://localhost:8080/whp/wav/stream/en/i.wav</playaudio>\n" +
                "                                <playaudio>http://localhost:8080/whp/wav/stream/en/d.wav</playaudio>\n" +
                "                                <playaudio>http://localhost:8080/whp/wav/stream/en/2.wav</playaudio>\n" +
                "                                <playaudio>http://localhost:8080/whp/wav/stream/en/enterAdherence.wav</playaudio>\n" +
                "                        <collectdtmf l=\"50\" t=\"#\"></collectdtmf>\n" +
                "        <gotourl>http://localhost:7080/whp/kookoo/ivr?type=kookoo&amp;ln=en&amp;tree=adherenceCapture&amp;trP=" + nextTreePath + "</gotourl>\n" +
                "    </response>";
    }

    private String confirmPatient1AdherenceResponse(String nextTreePath) {
        return "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                "<response>\n" +
                "                        <playaudio>http://localhost:8080/whp/wav/stream/en/confirmMessage1.wav</playaudio>\n" +
                "                                <playaudio>http://localhost:8080/whp/wav/stream/en/p.wav</playaudio>\n" +
                "                                <playaudio>http://localhost:8080/whp/wav/stream/en/a.wav</playaudio>\n" +
                "                                <playaudio>http://localhost:8080/whp/wav/stream/en/t.wav</playaudio>\n" +
                "                                <playaudio>http://localhost:8080/whp/wav/stream/en/i.wav</playaudio>\n" +
                "                                <playaudio>http://localhost:8080/whp/wav/stream/en/e.wav</playaudio>\n" +
                "                                <playaudio>http://localhost:8080/whp/wav/stream/en/n.wav</playaudio>\n" +
                "                                <playaudio>http://localhost:8080/whp/wav/stream/en/t.wav</playaudio>\n" +
                "                                <playaudio>http://localhost:8080/whp/wav/stream/en/i.wav</playaudio>\n" +
                "                                <playaudio>http://localhost:8080/whp/wav/stream/en/d.wav</playaudio>\n" +
                "                                <playaudio>http://localhost:8080/whp/wav/stream/en/1.wav</playaudio>\n" +
                "                                <playaudio>http://localhost:8080/whp/wav/stream/en/confirmMessage1a.wav</playaudio>\n" +
                "                                <playaudio>http://localhost:8080/whp/wav/stream/en/2.wav</playaudio>\n" +
                "                                <playaudio>http://localhost:8080/whp/wav/stream/en/confirmMessage2.wav</playaudio>\n" +
                "                                <playaudio>http://localhost:8080/whp/wav/stream/en/3.wav</playaudio>\n" +
                "                                <playaudio>http://localhost:8080/whp/wav/stream/en/confirmMessage3.wav</playaudio>\n" +
                "                                <playaudio>http://localhost:8080/whp/wav/stream/en/confirmMessage4.wav</playaudio>\n" +
                "                        <collectdtmf l=\"50\" t=\"#\"></collectdtmf>\n" +
                "        <gotourl>http://localhost:7080/whp/kookoo/ivr?type=kookoo&amp;ln=en&amp;tree=adherenceCapture&amp;trP=" + nextTreePath + "</gotourl>\n" +
                "    </response>";
    }

    private String adherenceSummaryResponse(String nextTreePath) {
        return "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                "<response>\n" +
                "                        <playaudio>http://localhost:8080/whp/wav/stream/en/instructionalMessage1.wav</playaudio>\n" +
                "                                <playaudio>http://localhost:8080/whp/wav/stream/en/0.wav</playaudio>\n" +
                "                                <playaudio>http://localhost:8080/whp/wav/stream/en/instructionalMessage2.wav</playaudio>\n" +
                "                                <playaudio>http://localhost:8080/whp/wav/stream/en/3.wav</playaudio>\n" +
                "                                <playaudio>http://localhost:8080/whp/wav/stream/en/instructionalMessage3.wav</playaudio>\n" +
                "                                <playaudio>http://localhost:8080/whp/wav/stream/en/patientList.wav</playaudio>\n" +
                "                                <playaudio>http://localhost:8080/whp/wav/stream/en/1.wav</playaudio>\n" +
                "                                <playaudio>http://localhost:8080/whp/wav/stream/en/p.wav</playaudio>\n" +
                "                                <playaudio>http://localhost:8080/whp/wav/stream/en/a.wav</playaudio>\n" +
                "                                <playaudio>http://localhost:8080/whp/wav/stream/en/t.wav</playaudio>\n" +
                "                                <playaudio>http://localhost:8080/whp/wav/stream/en/i.wav</playaudio>\n" +
                "                                <playaudio>http://localhost:8080/whp/wav/stream/en/e.wav</playaudio>\n" +
                "                                <playaudio>http://localhost:8080/whp/wav/stream/en/n.wav</playaudio>\n" +
                "                                <playaudio>http://localhost:8080/whp/wav/stream/en/t.wav</playaudio>\n" +
                "                                <playaudio>http://localhost:8080/whp/wav/stream/en/i.wav</playaudio>\n" +
                "                                <playaudio>http://localhost:8080/whp/wav/stream/en/d.wav</playaudio>\n" +
                "                                <playaudio>http://localhost:8080/whp/wav/stream/en/1.wav</playaudio>\n" +
                "                                <playaudio>http://localhost:8080/whp/wav/stream/en/enterAdherence.wav</playaudio>\n" +
                "                        <collectdtmf l=\"50\" t=\"#\"></collectdtmf>\n" +
                "        <gotourl>http://localhost:7080/whp/kookoo/ivr?type=kookoo&amp;ln=en&amp;tree=adherenceCapture&amp;trP=" + nextTreePath + "</gotourl>\n" +
                "    </response>";
    }

    private String welcomeMessageResponse() {
        return "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                "<response>\n" +
                "                        <playaudio>http://localhost:8080/whp/wav/stream/en/musicEnter.wav</playaudio>\n" +
                "                                <playaudio>http://localhost:8080/whp/wav/stream/en/welcomeMessage.wav</playaudio>\n" +
                "                        <collectdtmf l=\"1\" t=\"#\"></collectdtmf>\n" +
                "        <gotourl>http://localhost:7080/whp/kookoo/ivr?type=kookoo&amp;ln=en&amp;tree=adherenceCapture&amp;trP=" + TREE_PATH_START + "</gotourl>\n" +
                "    </response>";
    }

    private ReportingPublisherService getMockedReportingPublisherService() {
        return (ReportingPublisherService) dispatcherServlet.getWebApplicationContext().getBean("reportingPublisherService");
    }

    @After
    public void tearDown() {
        allPatients.remove(allPatients.findByPatientId(patient1.getPatientId()));
        allPatients.remove(allPatients.findByPatientId(patient2.getPatientId()));
        allPatients.remove(allPatients.findByPatientId(patient3.getPatientId()));
        allProviders.remove(allProviders.findByMobileNumber("123456"));
        allAdherenceLogs.removeAll();
    }

    @AfterClass
    public static void stopServer() throws Exception {
        dispatcherServlet = null;
        server.stop();
    }
}