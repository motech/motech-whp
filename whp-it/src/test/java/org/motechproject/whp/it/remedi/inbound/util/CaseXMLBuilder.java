package org.motechproject.whp.it.remedi.inbound.util;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import org.apache.commons.beanutils.PropertyUtils;
import org.motechproject.whp.webservice.request.PatientWebRequest;

import java.io.CharArrayWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class CaseXMLBuilder {
    private String templateFile;
    public Map<String, Object> params = new HashMap<>();

    public CaseXMLBuilder(String templateFile) {
        this.templateFile = templateFile;
    }

    public static CaseXMLBuilder createPatientRequest(){
        return new CaseXMLBuilder("create_patient.xml");
    }

    public static CaseXMLBuilder createPatientRequestWithNoMandatoryFields() {
        return new CaseXMLBuilder("create_patient_with_no_mandatory_fields.xml");
    }

    public static CaseXMLBuilder createPatientRequestWithOnlyMandatoryFields() {
        return new CaseXMLBuilder("create_patient_with_only_mandatory_fields.xml");
    }

    public static  CaseXMLBuilder updatePatientRequestWithOnlyMandatoryFields() {
        return new CaseXMLBuilder("update_patient_with_only_mandatory_fields.xml");
    }

    public static  CaseXMLBuilder updatePatientRequestWithNoMandatoryTreatmentDetails() {
        return new CaseXMLBuilder("update_patient_no_mandatory_treatment_fields.xml");
    }

    public static  CaseXMLBuilder simpleUpdatePatientXML() {
        return new CaseXMLBuilder("patient_simple_update.xml");
    }

    public static  CaseXMLBuilder simpleUpdatePatientRequestWithNoTreatmentDetails() {
        return new CaseXMLBuilder("patient_simple_update_with_no_treatment_details.xml");
    }

    public static  CaseXMLBuilder simpleUpdatePatientRequestWithEmptyTags() {
        return new CaseXMLBuilder("patient_simple_update_with_empty_treatment_details_tags.xml");
    }

    public static CaseXMLBuilder updatePatientRequest(){
        return new CaseXMLBuilder("update_patient.xml");
    }

    public static CaseXMLBuilder pauseTreatmentRequest() {
        return new CaseXMLBuilder("pause_treatment.xml");
    }

    public static CaseXMLBuilder restartTreatmentRequest() {
        return new CaseXMLBuilder("restart_treatment.xml");
    }

    public static CaseXMLBuilder closeTreatmentRequest() {
        return new CaseXMLBuilder("close_treatment.xml");
    }

    public static CaseXMLBuilder openNewTreatmentRequest() {
        return new CaseXMLBuilder("open_new_treatment.xml");
    }

    public String build() {
        try {
            Template template = getXmlTemplate(templateFile);
            CharArrayWriter writer = new CharArrayWriter();
            template.process(params, writer);
            return writer.toString();
        } catch (TemplateException | IOException e) {
            throw new RuntimeException(e);
        }
    }


    private Template getXmlTemplate(String fileName) throws IOException {
        Configuration cfg = new Configuration();
        cfg.setClassForTemplateLoading(this.getClass(),"/test-case-xml");
        return cfg.getTemplate(fileName);
    }


    private CaseXMLBuilder withParams(Map params){
        this.params.putAll(params);
        return this;
    }

    public CaseXMLBuilder withRequest(PatientWebRequest patientWebRequest){
        try {
            return this.withParams(PropertyUtils.describe(patientWebRequest));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
