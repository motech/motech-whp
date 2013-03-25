package org.motechproject.whp.wgn.outbound;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.motechproject.http.client.domain.EventCallBack;
import org.motechproject.http.client.service.HttpClientService;
import org.motechproject.whp.wgn.outbound.properties.WGNGatewayProperties;
import org.motechproject.whp.wgn.outbound.service.WGNGateway;

import java.io.Serializable;
import java.util.HashMap;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.initMocks;

public class WGNGatewayTest {

    public static final String API_KEY_NAME = "api_key";
    public static final String API_KEY_VALUE = "123456";
    @Mock
    private HttpClientService httpClientService;
    @Captor
    private ArgumentCaptor<HashMap<String, String>> headers;
    @Mock
    private WGNGatewayProperties wgnGatewayProperties;

    private WGNGateway gateway;

    @Before
    public void setup() {
        initMocks(this);
        when(wgnGatewayProperties.getApiKeyName()).thenReturn(API_KEY_NAME);
        when(wgnGatewayProperties.getApiKeyValue()).thenReturn(API_KEY_VALUE);
        gateway = new WGNGateway(httpClientService, wgnGatewayProperties);
    }

    @Test
    public void shouldSendAPIKeyAsHeaderForWGNRequest() {
        WGNRequest request = new TestRequest();

        gateway.post("url", request);
        verify(httpClientService).post(eq("url"), eq(request.toXML()), headers.capture());
        assertEquals(API_KEY_VALUE, headers.getValue().get(API_KEY_NAME));
    }

    @Test
    public void shouldSendAPIKeyAsHeaderForAnyRequest() {
        Serializable request = mock(Serializable.class);
        EventCallBack eventCallBack = mock(EventCallBack.class);
        gateway.post("url", request, eventCallBack);
        verify(httpClientService).post(eq("url"), eq(request), headers.capture(), eq(eventCallBack));
        assertEquals(API_KEY_VALUE, headers.getValue().get(API_KEY_NAME));
    }



    private static class TestRequest implements WGNRequest {

        @Override
        public String toXML() {
            return "";
        }
    }
}
