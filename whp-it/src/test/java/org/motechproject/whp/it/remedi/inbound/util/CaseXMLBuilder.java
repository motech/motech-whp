package org.motechproject.whp.it.remedi.inbound.util;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;

import java.io.CharArrayWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class CaseXMLBuilder {
    private String templateFile;
    public Map<String, String> params = new HashMap<>();

    public CaseXMLBuilder(String templateFile) {
        this.templateFile = templateFile;
    }

    public CaseXMLBuilder withCaseId(String caseId){
        params.put("caseId", caseId);
        return this;
    }

    public CaseXMLBuilder withParam(String key, String value){
        params.put(key, value);
        return this;
    }

    public String build() {
        try {
            Template template = getXmlTemplate(templateFile);
            return processTemplate(params, template);
        } catch (TemplateException | IOException e) {
            throw new RuntimeException(e);
        }
    }


    private String processTemplate(Map<String, String> modelMap, Template template) throws TemplateException, IOException {
        CharArrayWriter writer = new CharArrayWriter();
        template.process(modelMap, writer);
        return writer.toString();
    }

    private Template getXmlTemplate(String fileName) throws IOException {
        Configuration cfg = new Configuration();
        cfg.setClassForTemplateLoading(this.getClass(),"/test-case-xml");
        return cfg.getTemplate(fileName);
    }

    public CaseXMLBuilder withDistrict(String districtName) {
        params.put("district", districtName);
        return this;
    }

    public CaseXMLBuilder withTreatmentData(String category, String tbId, String providerId, String diseaseClass, String patientAge, String registrationNumber) {
        params.put("category", category);
        params.put("tbId", tbId);
        params.put("providerId", providerId);
        params.put("diseaseClass", diseaseClass);
        params.put("patientAge", patientAge);
        params.put("registrationNumber", registrationNumber);
        return this;
    }
}
