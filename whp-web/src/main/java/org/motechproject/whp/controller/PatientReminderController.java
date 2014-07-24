package org.motechproject.whp.controller;

import static org.springframework.web.bind.annotation.RequestMethod.POST;

import org.apache.log4j.Logger;
import org.motechproject.whp.patient.domain.Patient;
import org.motechproject.whp.patient.service.PatientService;
import org.motechproject.whp.reporting.service.ReportingPublisherService;
import org.motechproject.whp.request.IvrPatientReminderCallLogRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@RequestMapping(value = "/patientReminder")
@Controller
public class PatientReminderController {
	
	private ReportingPublisherService reportingPublisherService;
    private PatientService patientService;
    private Logger logger = Logger.getLogger(PatientReminderController.class);

    @Autowired
    public PatientReminderController(ReportingPublisherService reportingPublisherService, PatientService patientService) {
        this.reportingPublisherService = reportingPublisherService;
        this.patientService = patientService;
    }

    @RequestMapping(value = "/calllog", method = POST)
    @ResponseBody
    public void recordCallLog(@RequestBody IvrPatientReminderCallLogRequest request) {
    	logger.info(request);
    	if (request.getStartTime()==null || request.getStartTime().equals("null"))
    		request.setStartTime("");
    	if (request.getEndTime()==null || request.getEndTime().equals("null"))
    		request.setEndTime("");
    	Patient patient = patientService.getPatientByPhoneNumber(request.getMsisdn());
        String patientId = patient != null ? patient.getPatientId() : null;
        reportingPublisherService.reportPatientReminderCallLog(request.mapToReportingRequest(patientId));
    }
}
