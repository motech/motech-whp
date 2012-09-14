package org.motechproject.whp.remedi.service;

import freemarker.template.TemplateException;
import org.motechproject.http.client.service.HttpClientService;
import org.motechproject.whp.remedi.model.ContainerRegistrationModel;
import org.motechproject.whp.remedi.util.RemediXmlRequestBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class RemediService {
    private HttpClientService httpClientService;
    private RemediXmlRequestBuilder remediXmlRequestBuilder;
    private String remediUrl;

    @Autowired
    public RemediService(HttpClientService httpClientService, RemediXmlRequestBuilder remediXmlRequestBuilder, @Value("${remedi.url}") String remediUrl) {
        this.httpClientService = httpClientService;
        this.remediXmlRequestBuilder = remediXmlRequestBuilder;
        this.remediUrl = remediUrl;
    }

    public void sendContainerRegistrationResponse(ContainerRegistrationModel containerRegistrationModel) throws IOException, TemplateException {
        httpClientService.post(remediUrl, remediXmlRequestBuilder.buildTemplatedXmlFor(containerRegistrationModel));

    }
}
