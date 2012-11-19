package org.motechproject.whp.remedi.service;

import freemarker.template.TemplateException;
import org.joda.time.DateTime;
import org.motechproject.casexml.domain.CaseLog;
import org.motechproject.casexml.service.CaseLogService;
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
    private CaseLogService caseLogService;
    private RemediXmlRequestBuilder remediXmlRequestBuilder;
    private String remediUrl;

    @Autowired
    public RemediService(HttpClientService httpClientService, CaseLogService caseLogService, RemediXmlRequestBuilder remediXmlRequestBuilder, RemediProperties remediProperties) {
        this.httpClientService = httpClientService;
        this.caseLogService = caseLogService;
        this.remediXmlRequestBuilder = remediXmlRequestBuilder;
        this.remediUrl = remediProperties.getUrl();
    }

    public void sendContainerRegistrationResponse(ContainerRegistrationModel containerRegistrationModel) throws IOException, TemplateException {
        String requestXml = remediXmlRequestBuilder.buildTemplatedXmlFor(containerRegistrationModel);
        httpClientService.post(remediUrl, requestXml);
        caseLogService.add(new CaseLog(requestXml, remediUrl, false, DateTime.now()));
    }
}
