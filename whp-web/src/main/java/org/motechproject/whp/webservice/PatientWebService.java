package org.motechproject.whp.webservice;

import org.motechproject.casexml.service.CaseService;
import org.motechproject.whp.application.service.PatientRegistrationService;
import org.motechproject.whp.domain.Patient;
import org.motechproject.whp.domain.Treatment;
import org.motechproject.whp.exception.WHPDomainException;
import org.motechproject.whp.exception.WHPException;
import org.motechproject.whp.mapper.PatientMapper;
import org.motechproject.whp.mapper.TreatmentMapper;
import org.motechproject.whp.repository.AllTreatments;
import org.motechproject.whp.request.PatientRequest;
import org.motechproject.whp.validation.validator.BeanValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

@Controller
@RequestMapping("/patient/**")
public class PatientWebService extends CaseService<PatientRequest> {

    PatientRegistrationService patientRegistrationService;
    AllTreatments allTreatments;

    private BeanValidator validator;

    @Autowired
    public PatientWebService(PatientRegistrationService patientRegistrationService, AllTreatments allTreatments, BeanValidator validator) {
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
    public void createCase(PatientRequest patientRequest) throws WHPException {
        try {
            patientRequest.validate(validator);
            Patient patient = mapPatient(patientRequest);
            patientRegistrationService.register(patient);
        } catch (WHPException e) {
            throw new WHPException(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (WHPDomainException e) {
            throw new WHPException(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    private Patient mapPatient(PatientRequest patientRequest) {
        Treatment treatment = new TreatmentMapper().map(patientRequest);
        allTreatments.add(treatment);

        return new PatientMapper().map(patientRequest, treatment);
    }

}
