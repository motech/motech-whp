package org.motechproject.whp.remedi.util;

import freemarker.template.TemplateException;
import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Test;
import org.motechproject.util.DateUtil;
import org.motechproject.whp.common.util.WHPDate;
import org.motechproject.whp.refdata.domain.SputumTrackingInstance;
import org.motechproject.whp.remedi.model.ContainerRegistrationModel;
import org.xml.sax.SAXException;

import java.io.IOException;

import static org.custommonkey.xmlunit.XMLAssert.assertXMLEqual;

public class RemediXmlRequestBuilderTest {
    public static final String REMEDI_API_KEY = "123456";
    RemediXmlRequestBuilder remediXmlRequestBuilder;
    @Before
    public void setUp() {
        remediXmlRequestBuilder = new RemediXmlRequestBuilder();
        remediXmlRequestBuilder.setApiKey(REMEDI_API_KEY);
    }

    @Test
    public void shouldBuildXmlFromTemplate_forSuppliedContainerRegistrationModel() throws IOException, TemplateException, SAXException {
        DateTime now = DateUtil.now();
        ContainerRegistrationModel containerRegistrationModel = new ContainerRegistrationModel("123456", "raj", SputumTrackingInstance.PreTreatment, now);
        assertXMLEqual(getExpectedXml("123456", "raj", SputumTrackingInstance.PreTreatment, now), remediXmlRequestBuilder.buildTemplatedXmlFor(containerRegistrationModel));
    }

    private String getExpectedXml(String containerId, String providerId, SputumTrackingInstance caseType, DateTime dateModified) {
        return "<?xml version=\"1.0\"?>\n" +
                "<case xmlns=\"http://openrosa.org/javarosa\" case_id=\"" + containerId + "\" date_modified=\"" + dateModified.toString(WHPDate.DATE_TIME_FORMAT) +"\" user_id=\"system\"\n" +
                "      api_key=\"" + REMEDI_API_KEY + "\">\n" +
                "    <create>\n" +
                "        <case_type>" + caseType.getDisplayText() + "</case_type>\n" +
                "    </create>\n" +
                "    <update>\n" +
                "        <provider_id>" + providerId + "</provider_id>\n" +
                "    </update>\n" +
                "</case>";
    }
}
