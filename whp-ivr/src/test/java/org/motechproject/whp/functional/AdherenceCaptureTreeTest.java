package org.motechproject.whp.functional;

import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.ektorp.CouchDbConnector;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mortbay.jetty.Server;
import org.mortbay.jetty.servlet.Context;
import org.mortbay.jetty.servlet.ServletHolder;
import org.motechproject.testing.utils.SpringIntegrationTest;
import org.motechproject.whp.ivr.tree.AdherenceCaptureTree;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.web.servlet.DispatcherServlet;
import org.xml.sax.SAXException;

import java.io.IOException;

import static java.lang.String.format;
import static org.custommonkey.xmlunit.XMLAssert.assertXMLEqual;
import static org.custommonkey.xmlunit.XMLUnit.setIgnoreWhitespace;

@ContextConfiguration(locations = {"/applicationIVRContext.xml"})
public class AdherenceCaptureTreeTest extends SpringIntegrationTest {

    static Server server;
    static String CONTEXT_PATH = "/whp";

    String KOOKOO_CALLBACK_URL = "/kookoo/ivr";
    String SERVER_URL = "http://localhost:7080" + CONTEXT_PATH + KOOKOO_CALLBACK_URL;

    DefaultHttpClient decisionTreeController;

    @Autowired
    AdherenceCaptureTree adherenceCaptureTree;

    @Autowired
    @Qualifier("whpDbConnector")
    CouchDbConnector connector;

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

    @AfterClass
    public static void stopServer() throws Exception {
        server.stop();
    }

    @Override
    public CouchDbConnector getDBConnector() {
        return connector;
    }
}
