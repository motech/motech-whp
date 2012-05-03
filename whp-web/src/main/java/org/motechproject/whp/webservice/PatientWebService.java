package org.motechproject.whp.webservice;

import org.apache.velocity.app.VelocityEngine;
import org.dozer.DozerBeanMapper;
import org.motechproject.casexml.service.CaseService;
import org.motechproject.whp.application.service.RegistrationService;
import org.motechproject.whp.mapper.PatientRequestMapper;
import org.motechproject.whp.patient.contract.PatientRequest;
import org.motechproject.whp.patient.exception.WHPDomainException;
import org.motechproject.whp.patient.exception.WHPException;
import org.motechproject.whp.patient.repository.AllTreatmentCategories;
import org.motechproject.whp.patient.service.PatientService;
import org.motechproject.whp.request.PatientWebRequest;
import org.motechproject.whp.validation.RequestValidator;
import org.motechproject.whp.validation.ValidationScope;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/patient/**")
public class PatientWebService extends CaseService<PatientWebRequest> {

    AllTreatmentCategories allTreatmentCategories;
    RegistrationService registrationService;
    PatientService patientService;

    RequestValidator validator;
    DozerBeanMapper mapper;

    @Autowired
    public PatientWebService(
            RegistrationService registrationService,
            PatientService patientService,
            RequestValidator validator,
            VelocityEngine velocityEngine,
            AllTreatmentCategories allTreatmentCategories,
            DozerBeanMapper mapper
    ) {
        super(PatientWebRequest.class, velocityEngine);
        this.registrationService = registrationService;
        this.patientService = patientService;
        this.validator = validator;
        this.allTreatmentCategories = allTreatmentCategories;
        this.mapper = mapper;
    }

    @Override
    public void closeCase(PatientWebRequest patientWebRequest) {
    }

    @Override
    public void updateCase(PatientWebRequest patientWebRequest) {
        validator.validate(patientWebRequest, ValidationScope.simpleUpdate);
        PatientRequest patientRequest = new PatientRequestMapper(allTreatmentCategories, mapper).map(patientWebRequest);
        try {
            patientService.simpleUpdate(patientRequest);
        } catch (WHPDomainException e) {
            throw new WHPException(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @Override
    public void createCase(PatientWebRequest patientWebRequest) {
        validator.validate(patientWebRequest, ValidationScope.create);
        PatientRequest patientRequest = new PatientRequestMapper(allTreatmentCategories, mapper).map(patientWebRequest);
        try {
            registrationService.registerPatient(patientRequest);
        } catch (WHPDomainException e) {
            throw new WHPException(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
}
