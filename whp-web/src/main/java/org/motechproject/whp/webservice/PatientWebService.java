package org.motechproject.whp.webservice;

import org.motechproject.casexml.service.CaseService;
import org.motechproject.whp.application.service.RegistrationService;
import org.motechproject.whp.patient.domain.Patient;
import org.motechproject.whp.patient.domain.Treatment;
import org.motechproject.whp.mapper.PatientMapper;
import org.motechproject.whp.mapper.TreatmentMapper;
import org.motechproject.whp.patient.repository.AllTreatments;
import org.motechproject.whp.request.PatientRequest;
import org.motechproject.whp.validation.RequestValidator;
import org.motechproject.whp.validation.ValidationScope;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

@Controller
@RequestMapping("/patient/**")
public class PatientWebService extends CaseService<PatientRequest> {

    RegistrationService patientRegistrationService;
    AllTreatments allTreatments;
    RequestValidator validator;

    @Autowired
    public PatientWebService(RegistrationService patientRegistrationService, AllTreatments allTreatments, RequestValidator validator) {
        super(PatientRequest.class);
        this.patientRegistrationService = patientRegistrationService;
        this.allTreatments = allTreatments;
        this.validator = validator;
    }

    @Override
    public void closeCase(PatientRequest patientRequest) {
    }

    @Override
    public void updateCase(PatientRequest patientRequest) {
        throw new NotImplementedException();
    }

    @Override
    public void createCase(PatientRequest patientRequest) {
        validator.validate(patientRequest, ValidationScope.create, "patient");
        Patient patient = mapPatient(patientRequest);
        patientRegistrationService.registerPatient(patient);
    }

    private Patient mapPatient(PatientRequest patientRequest) {
        Treatment treatment = new TreatmentMapper().map(patientRequest);
        allTreatments.add(treatment);
        return new PatientMapper().map(patientRequest, treatment);
    }
}
