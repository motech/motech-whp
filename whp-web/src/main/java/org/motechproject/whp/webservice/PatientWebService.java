package org.motechproject.whp.webservice;

import org.motechproject.casexml.service.CaseService;
import org.motechproject.whp.mapper.PatientRequestMapper;
import org.motechproject.whp.patient.command.UpdateScope;
import org.motechproject.whp.patient.contract.PatientRequest;
import org.motechproject.whp.patient.exception.WHPCaseException;
import org.motechproject.whp.patient.exception.WHPRuntimeException;
import org.motechproject.whp.patient.service.PatientService;
import org.motechproject.whp.registration.service.RegistrationService;
import org.motechproject.whp.request.PatientWebRequest;
import org.motechproject.whp.validation.RequestValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/patient/**")
public class PatientWebService extends CaseService<PatientWebRequest> {

    RegistrationService registrationService;
    PatientService patientService;

    RequestValidator validator;
    PatientRequestMapper patientRequestMapper;

    @Autowired
    public PatientWebService(
            RegistrationService registrationService,
            PatientService patientService,
            RequestValidator validator,
            PatientRequestMapper patientRequestMapper) {

        super(PatientWebRequest.class);
        this.registrationService = registrationService;
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
            validator.validate(patientWebRequest, patientWebRequest.updateScope().name());
            PatientRequest patientRequest = patientRequestMapper.map(patientWebRequest);
            patientService.update(patientWebRequest.updateScope(), patientRequest);
        } catch (WHPRuntimeException e) {
            throw new WHPCaseException(e);
        }
    }

    @Override
    public void createCase(PatientWebRequest patientWebRequest) {
        try {
            validator.validate(patientWebRequest, UpdateScope.createScope);
            PatientRequest patientRequest = patientRequestMapper.map(patientWebRequest);
            registrationService.registerPatient(patientRequest);
        } catch (WHPRuntimeException e) {
            throw new WHPCaseException(e);
        }
    }
}
