package org.motechproject.whp.it.ivr.tree;

import org.apache.commons.httpclient.URIException;
import org.apache.commons.httpclient.util.URIUtil;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.joda.time.LocalDate;
import org.json.JSONObject;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.mortbay.jetty.Server;
import org.mortbay.jetty.servlet.Context;
import org.mortbay.jetty.servlet.ServletHolder;
import org.motechproject.whp.common.util.SpringIntegrationTest;
import org.motechproject.whp.it.ivr.util.KooKooIvrResponse;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.web.servlet.DispatcherServlet;

import javax.xml.bind.JAXBContext;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.*;

import static java.lang.String.format;
import static org.apache.commons.codec.binary.Base64.encodeBase64URLSafe;
import static org.custommonkey.xmlunit.XMLUnit.setIgnoreWhitespace;
import static org.motechproject.whp.common.domain.TreatmentWeekInstance.currentAdherenceCaptureWeek;
import static org.motechproject.whp.ivr.session.IvrSession.IVR_FLASHING_CALL_ID;

@ContextConfiguration(locations = {"/test-applicationIVRContext.xml"})
public abstract class SpringIvrIntegrationTest extends SpringIntegrationTest {

    static Server server;
    static String CONTEXT_PATH = "/whp";
    public static final String TREE_START_PATH = "";
    public static final String FAKETIME_URL = "http://localhost:7080/whp/motech-delivery-tools/datetime/update?date=%s&hour=0&minute=0";

    String KOOKOO_CALLBACK_URL = "/kookoo/ivr";
    String SERVER_URL = "http://localhost:7080" + CONTEXT_PATH + KOOKOO_CALLBACK_URL;

    DefaultHttpClient httpClient = new DefaultHttpClient();

    private static DispatcherServlet dispatcherServlet;
    protected final String NEW_CALL_URL_FORMAT = "%s?tree=adherenceCapture&trP=%s&ln=en&cid=%s&sid=%s&dataMap=%s";
    protected final String GOT_DTMF_URL_FORMAT = "%s?tree=adherenceCapture&trP=%s&ln=en&event=GotDTMF&data=%s&cid=%s&sid=%s&";
    protected final String HANGUP_URL_FORMAT = "%s?tree=adherenceCapture&trP=%s&ln=en&event=Disconnect&cid=%s&sid=%s&";

    private String currentPath;
    private String callerId;
    private String sessionId;
    protected String flashingCallId = "flashingCallId";

    @Before
    public void setup() throws IOException, InterruptedException {
        setIgnoreWhitespace(true);

        LocalDate lastMonday = currentAdherenceCaptureWeek().startDate();
        adjustDateTime(lastMonday);
    }

    public void adjustDateTime(LocalDate date) throws IOException {
        httpClient.execute(new HttpGet(format(FAKETIME_URL, date.toString())), new BasicResponseHandler());
    }

    @BeforeClass
    public static void startServer() throws Exception {
        server = new Server(7080);
        Context context = new Context(server, CONTEXT_PATH);

        dispatcherServlet = new DispatcherServlet();
        dispatcherServlet.setContextConfigLocation("classpath*:/applicationITContext.xml");

        ServletHolder servletHolder = new ServletHolder(dispatcherServlet);
        context.addServlet(servletHolder, "/*");
        server.setHandler(context);
        server.start();
    }


    @After
    public void tearDown() throws IOException {
        adjustDateTime(new LocalDate(new Date()));
    }

    @AfterClass
    public static void stopServer() throws Exception {
        dispatcherServlet = null;
        server.stop();
    }

    protected KooKooIvrResponse getResponse(String url) {
        String response;
        try {
            response = httpClient.execute(new HttpGet(url), new BasicResponseHandler());
            JAXBContext jaxbContext = JAXBContext.newInstance(KooKooIvrResponse.class);
            return (KooKooIvrResponse) jaxbContext.createUnmarshaller().unmarshal(new ByteArrayInputStream(response.getBytes()));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new KooKooIvrResponse();
    }

    protected KooKooIvrResponse startCall(String callerId)  {
        sessionId = UUID.randomUUID().toString();
        this.currentPath = TREE_START_PATH;
        this.callerId = callerId;
        Map<String, String> params = new HashMap<>();
        params.put(IVR_FLASHING_CALL_ID, flashingCallId);
        return getResponse(String.format(NEW_CALL_URL_FORMAT, SERVER_URL, base64(TREE_START_PATH), callerId, sessionId, encodedDataMap(params)));
    }

    private String encodedDataMap(Map<String, String> params) {
        try {
            return URIUtil.encodeQuery(new JSONObject(params).toString());
        } catch (URIException e) {
            throw new RuntimeException(e);
        }
    }

    protected KooKooIvrResponse sendDtmf(String dtmf) {
        String encodedTreePath = base64(currentPath);
        KooKooIvrResponse ivrResponse = getResponse(String.format(GOT_DTMF_URL_FORMAT, SERVER_URL, encodedTreePath, dtmf, callerId, sessionId));
        currentPath = currentPath + "/" + dtmf;
        return ivrResponse;
    }

    protected KooKooIvrResponse disconnectCall() {
        String encodedTreePath = base64(currentPath);
        return getResponse(String.format(HANGUP_URL_FORMAT, SERVER_URL, encodedTreePath, callerId, sessionId));
    }

    protected String base64(String input) {
        return new String(encodeBase64URLSafe(input.getBytes()));
    }

    protected List<String> wav(String... fileNames) {
        ArrayList<String> audioFileUrls = new ArrayList<>();
        for (String fileName : fileNames) {
            try {
                if (Integer.parseInt(fileName) > -1) {
                    audioFileUrls.addAll(alphaNumeric(fileName));
                }
            } catch (Exception e) {
                audioFileUrls.add(String.format("http://localhost:8080/whp/wav/stream/en/messages/%s.wav", fileName));
            }
        }
        return audioFileUrls;
    }

    protected List<String> alphaNumeric(String... numbers) {
        ArrayList<String> numberFileUrls = new ArrayList<>();
        for (String number : numbers) {
            numberFileUrls.add(String.format("http://localhost:8080/whp/wav/stream/en/alphanumeric/%s.wav", number));
        }
        return numberFileUrls;
    }

    protected String[] id(String patientId) {
        String[] charStrings = new String[patientId.length()];
        for (int i = 0; i < patientId.length(); i++)
            charStrings[i] = String.valueOf(patientId.charAt(i));
        return charStrings;
    }

    protected Object getBean(String name) {
        return dispatcherServlet.getWebApplicationContext().getBean(name);
    }

}
