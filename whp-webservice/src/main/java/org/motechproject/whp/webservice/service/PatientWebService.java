package org.motechproject.whp.webservice.service;

import org.motechproject.casexml.service.CaseService;
import org.motechproject.whp.common.exception.WHPRuntimeException;
import org.motechproject.whp.patient.command.UpdateScope;
import org.motechproject.whp.patient.contract.PatientRequest;
import org.motechproject.whp.patient.service.PatientService;
import org.motechproject.whp.validation.RequestValidator;
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
            UpdateScope updateScope = patientWebRequest.updateScope();
            validator.validate(patientWebRequest, updateScope.name());
            patientService.update(patientRequestMapper.map(patientWebRequest));
        } catch (WHPRuntimeException e) {
            throw new WHPCaseException(e);
        }
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
