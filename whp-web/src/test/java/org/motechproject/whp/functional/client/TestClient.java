package org.motechproject.whp.functional.client;

import com.gargoylesoftware.htmlunit.HttpMethod;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.gargoylesoftware.htmlunit.WebResponse;

import java.io.IOException;
import java.net.URL;

public class TestClient {

    private WebClient webClient;

    public TestClient() {
        this(new WebClient());
    }

    TestClient(WebClient webClient) {
        this.webClient = webClient;
    }

    public String getResponse(URL url) {
        WebResponse webResponse = null;
        try {
            webResponse = webClient.loadWebResponse(new WebRequest(url, HttpMethod.POST));
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (webResponse != null) {
            return webResponse.getContentAsString();
        }
        return null;
    }


}
