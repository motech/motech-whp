package org.motechproject.whp.patient.service;

import org.motechproject.casexml.service.CaseService;
import org.motechproject.whp.patient.service.request.PatientRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Created by IntelliJ IDEA.
 * User: pchandra
 * Date: 4/12/12
 * Time: 11:43 AM
 * To change this template use File | Settings | File Templates.
 */
@Controller
@RequestMapping("/whp/patient/**")
public class PatientService extends CaseService<PatientRequest>{


    public PatientService() {
        super(PatientRequest.class);
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
        System.out.println(patientRequest.getCase_id());
        System.out.println(patientRequest.getFirst_name());
        //To change body of implemented methods use File | Settings | File Templates.
    }
}
