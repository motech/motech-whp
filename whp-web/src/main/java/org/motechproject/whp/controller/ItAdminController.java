package org.motechproject.whp.controller;

import org.motechproject.whp.patient.repository.AllPatients;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping(value = "/itadmin")
public class ItAdminController {

    public static final String PATIENT_LIST = "patientList";
    public static final String MESSAGE = "message";


    @Autowired
    public ItAdminController(AllPatients allPatients) {
    }


}
