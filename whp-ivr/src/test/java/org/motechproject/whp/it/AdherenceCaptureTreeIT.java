package org.motechproject.whp.it;

import org.apache.http.client.CookieStore;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.protocol.ClientContext;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.cookie.BasicClientCookie;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import org.joda.time.LocalDate;
import org.junit.*;
import org.mortbay.jetty.Server;
import org.mortbay.jetty.servlet.Context;
import org.mortbay.jetty.servlet.ServletHolder;
import org.motechproject.adherence.repository.AllAdherenceLogs;
import org.motechproject.util.DateUtil;
import org.motechproject.whp.adherence.domain.WeeklyAdherenceSummary;
import org.motechproject.whp.adherence.service.WHPAdherenceService;
import org.motechproject.whp.common.util.SpringIntegrationTest;
import org.motechproject.whp.ivr.tree.AdherenceCaptureTree;
import org.motechproject.whp.patient.builder.PatientBuilder;
import org.motechproject.whp.patient.domain.Patient;
import org.motechproject.whp.patient.repository.AllPatients;
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

@ContextConfiguration(locations = {"/applicationIVRContext.xml"})
public class AdherenceCaptureTreeIT extends SpringIntegrationTest {

    static Server server;
    static String CONTEXT_PATH = "/whp";

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

    @BeforeClass
    public static void startServer() throws Exception {
        server = new Server(7080);
        Context context = new Context(server, CONTEXT_PATH);

        DispatcherServlet dispatcherServlet = new DispatcherServlet();
        dispatcherServlet.setContextConfigLocation("classpath:applicationIVRContext.xml");

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
        setUpTestData();
    }

    private void setUpTestData() {
        String providerId = "provider1";
        provider = new Provider(providerId, "123456","Vaishali", DateUtil.now());

        allProviders.add(provider);

        LocalDate treatmentStartDate = DateUtil.today().minusDays(10);
        patient1 = new PatientBuilder().withDefaults().withCurrentTreatmentStartDate(treatmentStartDate).withTherapyStartDate(treatmentStartDate).withPatientId("patientid1").withProviderId(providerId).build();
        patient2 = new PatientBuilder().withDefaults().withCurrentTreatmentStartDate(treatmentStartDate).withTherapyStartDate(treatmentStartDate).withPatientId("patientid2").withProviderId(providerId).build();

        allPatients.add(patient1);
        allPatients.add(patient2);
    }

    @Test
    public void shouldPlayWelcomeMessage() throws IOException, SAXException {
        String response = decisionTreeController.execute(new HttpGet(format("%s?tree=adherenceCapture&trP=Lw&ln=en", SERVER_URL)), new BasicResponseHandler());
        String expectedResponse =
                "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                        "<response>\n" +
                        "                        <playaudio>http://localhost:8080/whp/wav/stream/en/musicEnter.wav</playaudio>\n" +
                        "                                <playaudio>http://localhost:8080/whp/wav/stream/en/welcomeMessage.wav</playaudio>\n" +
                        "                        <collectdtmf l=\"1\" t=\"#\"></collectdtmf>\n" +
                        "        <gotourl>http://localhost:7080/whp/kookoo/ivr?type=kookoo&amp;ln=en&amp;tree=adherenceCapture&amp;trP=Lw</gotourl>\n" +
                        "    </response>";
        assertXMLEqual(expectedResponse, response);
    }

    @Test
    public void shouldTransitionToAdherenceSummaryNode() throws IOException, SAXException {

        String response = decisionTreeController.execute(new HttpGet(format("%s?tree=adherenceCapture&trP=Lw&ln=en&event=GotDTMF&data=1&cid=%s", SERVER_URL, provider.getPrimaryMobile())), new BasicResponseHandler());
        String expectedResponse = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                "<response>\n" +
                "                        <playaudio>http://localhost:8080/whp/wav/stream/en/instructionalMessage1.wav</playaudio>\n" +
                "                                <playaudio>http://localhost:8080/whp/wav/stream/en/0.wav</playaudio>\n" +
                "                                <playaudio>http://localhost:8080/whp/wav/stream/en/instructionalMessage2.wav</playaudio>\n" +
                "                                <playaudio>http://localhost:8080/whp/wav/stream/en/2.wav</playaudio>\n" +
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
                "        <gotourl>http://localhost:7080/whp/kookoo/ivr?type=kookoo&amp;ln=en&amp;tree=adherenceCapture&amp;trP=LzE</gotourl>\n" +
                "    </response>";
        assertXMLEqual(expectedResponse, response);
    }

    @Test
    public void shouldRecordAdherenceForAPatient()  throws IOException, SAXException{
        String response = decisionTreeController.execute(new HttpGet(format("%s?tree=adherenceCapture&trP=LzE&ln=en&event=GotDTMF&data=2&cid=%s" , SERVER_URL, provider.getPrimaryMobile())), new BasicResponseHandler());
        String expectedResponseForPatient1Adherence = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
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
                "                                <playaudio>http://localhost:8080/whp/wav/stream/en/patientList.wav</playaudio>\n" +
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
                "        <gotourl>http://localhost:7080/whp/kookoo/ivr?type=kookoo&amp;ln=en&amp;tree=adherenceCapture&amp;trP=LzEvMg</gotourl>\n" +
                "    </response>";
        WeeklyAdherenceSummary adherenceSummaryPatient1 = adherenceService.currentWeekAdherence(patient1);
        assertEquals(2, adherenceSummaryPatient1.getDosesTaken());

        assertXMLEqual(expectedResponseForPatient1Adherence, response);

        response = decisionTreeController.execute(new HttpGet(format("%s?tree=adherenceCapture&trP=LzE&ln=en&event=GotDTMF&data=3&cid=%s", SERVER_URL, provider.getPrimaryMobile())), new BasicResponseHandler());

        String expectedResponseForPatient2Adherence = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
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
                "                                <playaudio>http://localhost:8080/whp/wav/stream/en/2.wav</playaudio>\n" +
                "                                <playaudio>http://localhost:8080/whp/wav/stream/en/confirmMessage1a.wav</playaudio>\n" +
                "                                <playaudio>http://localhost:8080/whp/wav/stream/en/3.wav</playaudio>\n" +
                "                                <playaudio>http://localhost:8080/whp/wav/stream/en/confirmMessage2.wav</playaudio>\n" +
                "                                <playaudio>http://localhost:8080/whp/wav/stream/en/3.wav</playaudio>\n" +
                "                                <playaudio>http://localhost:8080/whp/wav/stream/en/confirmMessage3.wav</playaudio>\n" +
                "                        <gotourl>http://localhost:7080/whp/kookoo/ivr?type=kookoo&amp;ln=en</gotourl>\n" +
                "        <hangup></hangup>\n" +
                "    </response>";


        WeeklyAdherenceSummary adherenceSummaryForPatient2 = adherenceService.currentWeekAdherence(patient2);
        assertEquals(3, adherenceSummaryForPatient2.getDosesTaken());

        assertXMLEqual(expectedResponseForPatient2Adherence, response);
    }

    @Test
    public void shouldSkipForInvalidInputs() throws IOException, SAXException{
        String sessionId = UUID.randomUUID().toString();
        HttpGet request = new HttpGet(format("%s?tree=adherenceCapture&trP=LzE&ln=en&event=GotDTMF&data=8&flowSessionId=%s&cid=%s" , SERVER_URL, sessionId, provider.getPrimaryMobile()));
        String response = decisionTreeController.execute(request, new BasicResponseHandler());
        String expectedResponseForPatient1Adherence = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
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
                "        <gotourl>http://localhost:7080/whp/kookoo/ivr?type=kookoo&amp;ln=en&amp;tree=adherenceCapture&amp;trP=LzEvOA</gotourl>\n" +
                "    </response>";

        WeeklyAdherenceSummary adherenceSummaryPatient1 = adherenceService.currentWeekAdherence(patient1);
        assertEquals(0, adherenceSummaryPatient1.getDosesTaken());
        assertXMLEqual(expectedResponseForPatient1Adherence, response);


        response = decisionTreeController.execute(new HttpGet(format("%s?tree=adherenceCapture&trP=LzE&ln=en&event=GotDTMF&data=9&flowSessionId=%s&cid=%s", SERVER_URL,sessionId, provider.getPrimaryMobile())), new BasicResponseHandler());
        String expectedResponseForPatient2Adherence = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                "<vxml version=\"2.1\" xmlns=\"http://www.w3.org/2001/vxml\">\n" +
                "    <form>\n" +
                "        <exit/>\n" +
                "    </form>\n" +
                "</vxml>";

        WeeklyAdherenceSummary adherenceSummaryPatient2 = adherenceService.currentWeekAdherence(patient2);
        assertEquals(0, adherenceSummaryPatient2.getDosesTaken());

        assertXMLEqual(expectedResponseForPatient2Adherence, response);

    }

    @After
    public void tearDown() {
       allPatients.remove(allPatients.findByPatientId(patient1.getPatientId()));
       allPatients.remove(allPatients.findByPatientId(patient2.getPatientId()));

       allProviders.remove(allProviders.findByPrimaryMobileNumber("123456"));

       allAdherenceLogs.removeAll();
    }

    @AfterClass
    public static void stopServer() throws Exception {
        server.stop();
    }

}
