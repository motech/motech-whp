package org.motechproject.whp.service;

import org.motechproject.casexml.service.CaseService;
import org.motechproject.whp.mapper.PatientMapper;
import org.motechproject.whp.mapper.TreatmentMapper;
import org.motechproject.whp.patient.domain.Patient;
import org.motechproject.whp.patient.domain.Treatment;
import org.motechproject.whp.patient.repository.AllPatients;
import org.motechproject.whp.patient.repository.AllTreatments;
import org.motechproject.whp.request.PatientRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/patient/**")
public class PatientService extends CaseService<PatientRequest> {

    AllPatients allPatients;
    AllTreatments allTreatments;

    @Autowired
    public PatientService( AllPatients allPatients, AllTreatments allTreatments) {
        super(PatientRequest.class);
        this.allPatients = allPatients;
        this.allTreatments = allTreatments;
    }

    @Override
    public void closeCase(PatientRequest patientRequest) {
    }

    @Override
    public void updateCase(PatientRequest patientRequest) {
    }

    @Override
    public void createCase(PatientRequest patientRequest) {

        Treatment treatment = new TreatmentMapper().map(patientRequest);
        allTreatments.add(treatment);

        Patient patient = new PatientMapper().map(patientRequest, treatment);
        Patient patientReturned = allPatients.findByPatientId(patient.getPatientId());
        if (patientReturned == null)
            allPatients.add(patient);
        else
            allPatients.update(patient); // TODO
    }

}
