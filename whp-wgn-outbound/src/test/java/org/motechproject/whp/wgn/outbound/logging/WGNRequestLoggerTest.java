package org.motechproject.whp.wgn.outbound.logging;

import lombok.Setter;
import org.apache.log4j.Logger;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.motechproject.whp.wgn.outbound.WGNRequest;

import javax.xml.bind.JAXB;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.StringWriter;

import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.MockitoAnnotations.initMocks;

public class WGNRequestLoggerTest {

    @Mock
    private Logger logger;

    private ArgumentCaptor<String> captor;

    private WGNRequestLogger requestLogger;

    @Before
    public void setup() {
        initMocks(this);
        captor = ArgumentCaptor.forClass(String.class);
        requestLogger = new WGNRequestLogger(logger);
    }

    @Test
    public void shouldLogURL() {
        requestLogger.log("url", new TestRequest("value"));

        verify(logger).info(captor.capture());
        assertTrue(captor.getValue().contains("url"));
    }

    @Test(expected = RuntimeException.class)
    public void shouldNotLogOnException() {
        requestLogger.log("url", null);

        verify(logger, never()).info(anyString());
    }

    @Test
    public void shouldLogSerializedRequest() {
        requestLogger.log("url", new TestRequest("value"));

        verify(logger).info(captor.capture());
        assertTrue(captor.getValue().contains("value"));
    }

    @XmlRootElement(name = "test_request")
    @Setter
    private static class TestRequest implements WGNRequest {

        private String data;

        public TestRequest() {
        }

        public TestRequest(String data) {
            this.data = data;
        }

        @XmlElement(name = "data")
        public String getData() {
            return data;
        }

        @Override
        public String toXML() {
            StringWriter writer = new StringWriter();
            JAXB.marshal(this, writer);
            return writer.toString();
        }
    }
}
