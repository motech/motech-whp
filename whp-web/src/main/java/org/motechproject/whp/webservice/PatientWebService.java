package org.motechproject.whp.webservice;

import org.dozer.DozerBeanMapper;
import org.dozer.MappingException;
import org.motechproject.casexml.service.CaseService;
import org.motechproject.whp.patient.contract.PatientRequest;
import org.motechproject.whp.patient.contract.TreatmentUpdateRequest;
import org.motechproject.whp.patient.exception.WHPCaseException;
import org.motechproject.whp.patient.exception.WHPRuntimeException;
import org.motechproject.whp.patient.service.PatientService;
import org.motechproject.whp.registration.service.RegistrationService;
import org.motechproject.whp.request.PatientWebRequest;
import org.motechproject.whp.validation.RequestValidator;
import org.motechproject.whp.validation.ValidationScope;
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
    DozerBeanMapper treatmentUpdateRequestMapper;

    @Autowired
    public PatientWebService(
            RegistrationService registrationService,
            PatientService patientService,
            RequestValidator validator,
            DozerBeanMapper patientRequestMapper,
            DozerBeanMapper treatmentUpdateRequestMapper) {

        super(PatientWebRequest.class);
        this.registrationService = registrationService;
        this.patientService = patientService;
        this.validator = validator;
        this.patientRequestMapper = patientRequestMapper;
        this.treatmentUpdateRequestMapper = treatmentUpdateRequestMapper;
    }

    @Override
    public void closeCase(PatientWebRequest patientWebRequest) {
    }

    @Override
    public void updateCase(PatientWebRequest patientWebRequest) {
        try {
            if (patientWebRequest.isTreatmentUpdateRequest()) {
                validator.validate(patientWebRequest, patientWebRequest.updateScenario().getScope());
                TreatmentUpdateRequest treatmentUpdateRequest = treatmentUpdateRequestMapper.map(patientWebRequest, TreatmentUpdateRequest.class);
                patientService.performTreatmentUpdate(treatmentUpdateRequest);
            } else {
                validator.validate(patientWebRequest, ValidationScope.simpleUpdate);
                PatientRequest patientRequest = patientRequestMapper.map(patientWebRequest, PatientRequest.class);
                patientService.simpleUpdate(patientRequest);
            }
        } catch (WHPRuntimeException e) {
            throw new WHPCaseException(e);
        } catch (MappingException e) {
            throw new WHPCaseException(e);
        }
    }

    @Override
    public void createCase(PatientWebRequest patientWebRequest) {
        try {
            validator.validate(patientWebRequest, ValidationScope.create);
            PatientRequest patientRequest = patientRequestMapper.map(patientWebRequest, PatientRequest.class);
            registrationService.registerPatient(patientRequest);
        } catch (WHPRuntimeException e) {
            throw new WHPCaseException(e);
        } catch (MappingException e) {
            throw new WHPCaseException(e);
        }
    }
}
