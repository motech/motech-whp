package org.motechproject.whp.service;

import org.motechproject.casexml.service.CaseService;
import org.motechproject.whp.mapper.PatientMapper;
import org.motechproject.whp.patient.domain.Patient;
import org.motechproject.whp.patient.repository.AllPatients;
import org.motechproject.whp.request.PatientRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/whp/patient/**")
public class PatientService extends CaseService<PatientRequest>{

    AllPatients allPatients;


    public PatientService(AllPatients allPatients) {
        super(PatientRequest.class);
        this.allPatients = allPatients;
    }

    @Override
    public void closeCase(PatientRequest patientRequest) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void updateCase(PatientRequest patientRequest) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void createCase(PatientRequest patientRequest) {
        Patient patient = new PatientMapper().map(patientRequest);
        Patient patientReturned = allPatients.findByPatientId(patient.getPatientId());
        if(patientReturned == null)
            allPatients.add(patient);
        else
            allPatients.update(patient);
    }
}
