package org.motechproject.whp.webservice;

import org.dozer.DozerBeanMapper;
import org.dozer.MappingException;
import org.motechproject.casexml.service.CaseService;
import org.motechproject.whp.patient.contract.PatientRequest;
import org.motechproject.whp.patient.exception.WHPCaseException;
import org.motechproject.whp.patient.exception.WHPRuntimeException;
import org.motechproject.whp.patient.service.PatientService;
import org.motechproject.whp.patient.service.AllCommands;
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
    DozerBeanMapper patientRequestMapper;

    @Autowired
    public PatientWebService(
            RegistrationService registrationService,
            PatientService patientService,
            RequestValidator validator,
            DozerBeanMapper patientRequestMapper) {

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
            validator.validate(patientWebRequest, patientWebRequest.updateScope());
            PatientRequest patientRequest = patientRequestMapper.map(patientWebRequest, PatientRequest.class);
            patientService.update(patientWebRequest.updateScope(), patientRequest);
        } catch (WHPRuntimeException e) {
            throw new WHPCaseException(e);
        } catch (MappingException e) {
            throw new WHPCaseException(e);
        }
    }

    @Override
    public void createCase(PatientWebRequest patientWebRequest) {
        try {
            validator.validate(patientWebRequest, AllCommands.create);
            PatientRequest patientRequest = patientRequestMapper.map(patientWebRequest, PatientRequest.class);
            registrationService.registerPatient(patientRequest);
        } catch (WHPRuntimeException e) {
            throw new WHPCaseException(e);
        } catch (MappingException e) {
            throw new WHPCaseException(e);
        }
    }
}
