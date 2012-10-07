package org.motechproject.whp.remedi.service;

import freemarker.template.TemplateException;
import org.motechproject.http.client.service.HttpClientService;
import org.motechproject.whp.common.service.RemediProperties;
import org.motechproject.whp.remedi.model.ContainerRegistrationModel;
import org.motechproject.whp.remedi.util.RemediXmlRequestBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class RemediService {
    private HttpClientService httpClientService;
    private RemediXmlRequestBuilder remediXmlRequestBuilder;
    private String remediUrl;

    @Autowired
    public RemediService(HttpClientService httpClientService, RemediXmlRequestBuilder remediXmlRequestBuilder, RemediProperties remediProperties) {
        this.httpClientService = httpClientService;
        this.remediXmlRequestBuilder = remediXmlRequestBuilder;
        this.remediUrl = remediProperties.getUrl();
    }

    public void sendContainerRegistrationResponse(ContainerRegistrationModel containerRegistrationModel) throws IOException, TemplateException {
        httpClientService.post(remediUrl, remediXmlRequestBuilder.buildTemplatedXmlFor(containerRegistrationModel));

    }
}
