package org.motechproject.whp.interceptor;

import org.junit.Before;
import org.junit.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertTrue;

public class APIKeyInterceptorTest {

    private APIKeyInterceptor apiKeyInterceptor;
    private String validAPIKey;

    @Before
    public void setUp() throws Exception {
        validAPIKey = "valid-api-key";
        apiKeyInterceptor = new APIKeyInterceptor(validAPIKey);
    }

    @Test
    public void shouldAcceptValidApiKeyInRequestHeader() throws Exception {
        APIKeyInterceptor apiKeyInterceptor = new APIKeyInterceptor(validAPIKey);
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addHeader("api-key", validAPIKey);

        assertTrue(apiKeyInterceptor.preHandle(request, new MockHttpServletResponse(), null));
    }

    @Test
    public void shouldNotAcceptInvalidApiKeyInRequestHeader() throws Exception {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addHeader("api-key", "invalid-api-key");

        assertFalse(apiKeyInterceptor.preHandle(request, new MockHttpServletResponse(), null));
    }

}
