package org.motechproject.whp.webservice;

import org.motechproject.casexml.service.CaseService;
import org.motechproject.whp.exception.WHPValidationException;
import org.motechproject.whp.mapper.PatientMapper;
import org.motechproject.whp.mapper.TreatmentMapper;
import org.motechproject.whp.patient.domain.Patient;
import org.motechproject.whp.patient.domain.Treatment;
import org.motechproject.whp.patient.repository.AllPatients;
import org.motechproject.whp.patient.repository.AllTreatments;
import org.motechproject.whp.request.PatientRequest;
import org.motechproject.whp.validation.validator.BeanValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/patient/**")
public class PatientWebService extends CaseService<PatientRequest> {

    AllPatients allPatients;
    AllTreatments allTreatments;

    private BeanValidator validator;

    @Autowired
    public PatientWebService(AllPatients allPatients, AllTreatments allTreatments, BeanValidator validator) {
        super(PatientRequest.class);
        this.allPatients = allPatients;
        this.allTreatments = allTreatments;
        this.validator = validator;
    }

    @Override
    public void closeCase(PatientRequest patientRequest) {
    }

    @Override
    public void updateCase(PatientRequest patientRequest) {
        Patient patient = mapPatient(patientRequest);
        Patient patientReturned = allPatients.findByPatientId(patient.getPatientId());
        if (patientReturned != null)
            allPatients.update(patientReturned, patient);
    }

    @Override
    public void createCase(PatientRequest patientRequest) throws WHPValidationException {
        patientRequest.validate(validator);
        Patient patient = mapPatient(patientRequest);
        Patient patientReturned = allPatients.findByPatientId(patient.getPatientId());
        if (patientReturned == null)
            allPatients.add(patient);
    }

    private Patient mapPatient(PatientRequest patientRequest) {
        Treatment treatment = new TreatmentMapper().map(patientRequest);
        allTreatments.add(treatment);

        return new PatientMapper().map(patientRequest, treatment);
    }

}
