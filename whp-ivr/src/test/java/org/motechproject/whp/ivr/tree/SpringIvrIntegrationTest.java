package org.motechproject.whp.ivr.tree;

import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.joda.time.LocalDate;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.mortbay.jetty.Server;
import org.mortbay.jetty.servlet.Context;
import org.mortbay.jetty.servlet.ServletHolder;
import org.motechproject.whp.common.util.SpringIntegrationTest;
import org.motechproject.whp.ivr.util.KooKooIvrResponse;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.web.servlet.DispatcherServlet;

import javax.xml.bind.JAXBContext;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static java.lang.String.format;
import static org.apache.commons.codec.binary.Base64.encodeBase64URLSafe;
import static org.custommonkey.xmlunit.XMLUnit.setIgnoreWhitespace;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.motechproject.whp.common.domain.TreatmentWeekInstance.currentAdherenceCaptureWeek;

@ContextConfiguration(locations = {"/test-applicationIVRContext.xml"})
public abstract class SpringIvrIntegrationTest extends SpringIntegrationTest {

    static Server server;
    static String CONTEXT_PATH = "/whp";
    public static final String TREE_START_PATH = "/";
    public static final String FAKETIME_URL = "http://localhost:7080/whp/motech-delivery-tools/datetime/update?date=%s&hour=0&minute=0";

    String KOOKOO_CALLBACK_URL = "/kookoo/ivr";
    String SERVER_URL = "http://localhost:7080" + CONTEXT_PATH + KOOKOO_CALLBACK_URL;

    DefaultHttpClient httpClient;

    private static DispatcherServlet dispatcherServlet;
    protected final String NEW_CALL_URL_FORMAT = "%s?tree=adherenceCapture&trP=%s&ln=en&cid=%s&sid=%s";
    protected final String GOT_DTMF_URL_FORMAT = "%s?tree=adherenceCapture&trP=%s&ln=en&event=GotDTMF&data=%s&sid=%s";

    private String currentPath;

    @Before
    public void setup() throws IOException, InterruptedException {
        setIgnoreWhitespace(true);
        currentPath = base64(TREE_START_PATH);

        httpClient = new DefaultHttpClient();
        LocalDate lastMonday = currentAdherenceCaptureWeek().startDate();
        httpClient.execute(new HttpGet(format(FAKETIME_URL, lastMonday.toString())), new BasicResponseHandler());
    }

    public void adjustDateTime(LocalDate date) throws IOException {
        httpClient.execute(new HttpGet(format(FAKETIME_URL, date.toString())), new BasicResponseHandler());
    }

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

    @AfterClass
    public static void stopServer() throws Exception {
        dispatcherServlet = null;
        server.stop();
    }

    protected KooKooIvrResponse getResponse(String url) {
        String response = null;
        try {
            response = httpClient.execute(new HttpGet(url), new BasicResponseHandler());
            JAXBContext jaxbContext = JAXBContext.newInstance(KooKooIvrResponse.class);
            return (KooKooIvrResponse) jaxbContext.createUnmarshaller().unmarshal(new ByteArrayInputStream(response.getBytes()));
        } catch (Exception e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        return new KooKooIvrResponse();
    }

    protected KooKooIvrResponse startCall(String sessionId, String callerId) {
        return getResponse(String.format(NEW_CALL_URL_FORMAT, SERVER_URL, base64(TREE_START_PATH), callerId, sessionId));
    }

    protected KooKooIvrResponse sendDtmf(String sessionId, String dtmf) {
        String encodedTreePath = base64(currentPath);
        KooKooIvrResponse ivrResponse = getResponse(String.format(GOT_DTMF_URL_FORMAT, SERVER_URL, encodedTreePath, dtmf, sessionId));
        currentPath = currentPath + "/" + dtmf;
        return ivrResponse;
    }

    protected String base64(String input) {
        return new String(encodeBase64URLSafe(input.getBytes()));
    }

    protected List<String> wav(String... fileNames) {
        ArrayList<String> audioFileUrls = new ArrayList<>();
        for (String fileName : fileNames) {
            audioFileUrls.add(String.format("http://localhost:8080/whp/wav/stream/en/messages/%s.wav", fileName));
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

    protected Object getBean(String name) {
        return dispatcherServlet.getWebApplicationContext().getBean(name);
    }

    protected void assertPrompts(List<String> expectedPrompts, List<String> actualPrompts) {
        for (int i = 0; i < expectedPrompts.size(); i++) {
            assertThat(actualPrompts.get(i), is(expectedPrompts.get(i)));
        }
    }

    protected String[] id(String patientId) {
        String[] charStrings = new String[patientId.length()];
        for(int i=0; i<patientId.length(); i++)
            charStrings[i] = String.valueOf(patientId.charAt(i));
        return charStrings;
    }

}
