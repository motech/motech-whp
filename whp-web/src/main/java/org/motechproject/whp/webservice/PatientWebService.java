package org.motechproject.whp.webservice;

import org.apache.velocity.app.VelocityEngine;
import org.motechproject.casexml.service.CaseService;
import org.motechproject.whp.application.service.RegistrationService;
import org.motechproject.whp.mapper.PatientRequestMapper;
import org.motechproject.whp.patient.contract.CreatePatientRequest;
import org.motechproject.whp.patient.exception.WHPDomainException;
import org.motechproject.whp.patient.exception.WHPException;
import org.motechproject.whp.patient.repository.AllTreatments;
import org.motechproject.whp.patient.service.PatientService;
import org.motechproject.whp.request.PatientRequest;
import org.motechproject.whp.validation.RequestValidator;
import org.motechproject.whp.validation.ValidationScope;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/patient/**")
public class PatientWebService extends CaseService<PatientRequest> {

    RegistrationService registrationService;
    PatientService patientService;

    AllTreatments allTreatments;
    RequestValidator validator;

    @Autowired
    public PatientWebService(RegistrationService registrationService, PatientService patientService, AllTreatments allTreatments, RequestValidator validator, VelocityEngine velocityEngine) {
        super(PatientRequest.class, velocityEngine);
        this.registrationService = registrationService;
        this.patientService = patientService;
        this.allTreatments = allTreatments;
        this.validator = validator;
    }

    @Override
    public void closeCase(PatientRequest patientRequest) {
    }

    @Override
    public void updateCase(PatientRequest patientRequest) {
        validator.validate(patientRequest, ValidationScope.simpleUpdate);
        CreatePatientRequest createPatientRequest = new PatientRequestMapper().map(patientRequest);
        try {
            patientService.simpleUpdate(createPatientRequest);
        } catch (WHPDomainException e) {
            throw new WHPException(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @Override
    public void createCase(PatientRequest patientRequest) {
        validator.validate(patientRequest, ValidationScope.create);
        CreatePatientRequest createPatientRequest = new PatientRequestMapper().map(patientRequest);
        try {
            registrationService.registerPatient(createPatientRequest);
        } catch (WHPDomainException e) {
            throw new WHPException(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

}
