package org.motechproject.whp.webservice.service;

import org.apache.commons.lang.StringUtils;
import org.motechproject.casexml.service.CaseService;
import org.motechproject.whp.common.exception.WHPRuntimeException;
import org.motechproject.whp.common.validation.RequestValidator;
import org.motechproject.whp.patient.command.UpdateScope;
import org.motechproject.whp.patient.contract.PatientRequest;
import org.motechproject.whp.patient.service.PatientService;
import org.motechproject.whp.webservice.exception.WHPCaseException;
import org.motechproject.whp.webservice.mapper.PatientRequestMapper;
import org.motechproject.whp.webservice.request.PatientWebRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/patient/**")
public class PatientWebService extends CaseService<PatientWebRequest> {

    PatientService patientService;
    RequestValidator validator;
    PatientRequestMapper patientRequestMapper;

    @Autowired
    public PatientWebService(
            PatientService patientService,
            RequestValidator validator,
            PatientRequestMapper patientRequestMapper) {

        super(PatientWebRequest.class);
        this.patientService = patientService;
        this.validator = validator;
        this.patientRequestMapper = patientRequestMapper;
    }

    @Override
    public void closeCase(PatientWebRequest patientWebRequest) {
    }

    @Override
    public void updateCase(PatientWebRequest patientWebRequest) {
        try {
            formatEmptySmearTestResults(patientWebRequest);
            UpdateScope updateScope = patientWebRequest.updateScope();
            validator.validate(patientWebRequest, updateScope.name());
            patientService.update(patientRequestMapper.map(patientWebRequest));
        } catch (WHPRuntimeException e) {
            throw new WHPCaseException(e);
        }
    }

    private void formatEmptySmearTestResults(PatientWebRequest patientWebRequest) {
        patientWebRequest.setSmear_test_date_1(replaceEmptyStringWithNull(patientWebRequest.getSmear_test_date_1()));
        patientWebRequest.setSmear_test_date_2(replaceEmptyStringWithNull(patientWebRequest.getSmear_test_date_2()));
        patientWebRequest.setSmear_test_result_1(replaceEmptyStringWithNull(patientWebRequest.getSmear_test_result_1()));
        patientWebRequest.setSmear_test_result_2(replaceEmptyStringWithNull(patientWebRequest.getSmear_test_result_2()));
        patientWebRequest.setLab_number(replaceEmptyStringWithNull(patientWebRequest.getLab_number()));
        patientWebRequest.setLab_name(replaceEmptyStringWithNull(patientWebRequest.getLab_name()));
    }

    private String replaceEmptyStringWithNull(String fieldValue) {
        return StringUtils.isEmpty(fieldValue) ? null : fieldValue;
    }

    @Override
    public void createCase(PatientWebRequest patientWebRequest) {
        try {
            validator.validate(patientWebRequest, UpdateScope.createScope);
            PatientRequest patientRequest = patientRequestMapper.map(patientWebRequest);
            patientService.createPatient(patientRequest);
        } catch (WHPRuntimeException e) {
            throw new WHPCaseException(e);
        }
    }
}
