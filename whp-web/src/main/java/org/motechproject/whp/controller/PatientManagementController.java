package org.motechproject.whp.controller;

import static org.apache.commons.lang.StringUtils.isNotBlank;
import static org.motechproject.flash.Flash.in;
import static org.motechproject.flash.Flash.out;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.io.FileUtils;
import org.joda.time.DateTime;
import org.motechproject.whp.adherence.audit.service.AdherenceAuditService;
import org.motechproject.whp.adherence.service.AdherenceLogService;
import org.motechproject.whp.adherenceapi.adherence.AdherenceConfirmationOverIVR;
import org.motechproject.whp.common.domain.WHPConstants;
import org.motechproject.whp.common.exception.WHPDomainException;
import org.motechproject.whp.container.domain.Container;
import org.motechproject.whp.container.service.ContainerService;
import org.motechproject.whp.container.service.ContainerTrackingService;
import org.motechproject.whp.mapper.PatientInfoMapper;
import org.motechproject.whp.patient.domain.Patient;
import org.motechproject.whp.patient.domain.Therapy;
import org.motechproject.whp.patient.domain.Treatment;
import org.motechproject.whp.patient.service.PatientService;
import org.motechproject.whp.uimodel.PatientInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping(value = "/managepatients")
public class PatientManagementController extends BaseWebController {

	private PatientService patientService;
	private AdherenceLogService adherenceLogService;
	private AdherenceAuditService adherenceAuditService;
	private ContainerTrackingService containerTrackingService;
	private ContainerService containerService;
	private AdherenceConfirmationOverIVR adherenceConfirmationOverIVR;
	private static final String DELIMITER = ",";

	@Autowired
	public PatientManagementController(PatientService patientService,
			AdherenceLogService adherenceLogService,
			AdherenceAuditService adherenceAuditService,
			ContainerTrackingService containerTrackingService,
			ContainerService containerService,
			AdherenceConfirmationOverIVR adherenceConfirmationOverIVR) {
		this.patientService = patientService;
		this.adherenceLogService = adherenceLogService;
		this.adherenceAuditService = adherenceAuditService;
		this.containerTrackingService = containerTrackingService;
		this.containerService = containerService;
		this.adherenceConfirmationOverIVR = adherenceConfirmationOverIVR;
	}

	@RequestMapping
	public String view(Model uiModel, HttpServletRequest request) {
		String messages = in(WHPConstants.NOTIFICATION_MESSAGE, request);
		if (isNotBlank(messages)) {
			uiModel.addAttribute(WHPConstants.NOTIFICATION_MESSAGE, messages);
		}
		return "patient/manage";
	}

	@RequestMapping(value = "treatment", method = RequestMethod.GET)
	public String findGivenTreatment(
			@RequestParam("patientId") String patientId,
			@RequestParam("tbId") String tbId, Model uiModel,
			HttpServletRequest request) {

		Patient patient = patientService.findByPatientId(patientId);
		if (patient != null)
			uiModel.addAttribute("treatment", patient.getTreatmentBy(tbId));
		return "patient/treatmentDetails";
	}

	@RequestMapping(value = "removeTreatment", method = RequestMethod.POST)
	public String removeTreatment(@RequestParam("patientId") String patientId,
			@RequestParam("tbId") String tbId, HttpServletRequest request) {
		Patient patient = patientService.findByPatientId(patientId);

		if (patient.canRemoveTreatment(tbId)) {
			patient.removeTreatmentForTbId(tbId);
			patientService.update(patient);
			out(WHPConstants.NOTIFICATION_MESSAGE, String.format(
					"Treatment with id %s removed successfully.", tbId),
					request);
		} else
			out(WHPConstants.NOTIFICATION_MESSAGE, String.format(
					"Treatment with id %s cannot be removed.", tbId), request);

		return "redirect:/managepatients";
	}

	@RequestMapping(value = "deletePatient", method = RequestMethod.GET)
	public String deletePatients(Model uiModel, HttpServletRequest request) {

		String messages = in(WHPConstants.NOTIFICATION_MESSAGE, request);
		if (isNotBlank(messages)) {
			uiModel.addAttribute(WHPConstants.NOTIFICATION_MESSAGE, messages);
		}
		return "patient/deletePatients";
	}

	@RequestMapping(value = "patient", method = RequestMethod.GET)
	public String findPatientByPatientId(
			@RequestParam("patientId") String patientId, Model uiModel,
			HttpServletRequest request) {

		Patient patient = patientService.findByPatientId(patientId);
		if (patient != null) {
			PatientInfoMapper patientInfoMapper = new PatientInfoMapper();
			PatientInfo patientInfo = patientInfoMapper.map(patient);
			uiModel.addAttribute("patient", patientInfo);
			List<String> previousTreatmentDetails = new ArrayList<>();
			for (Therapy therapy : patient.getTherapyHistory()) {
				for (Treatment treatment : therapy.getAllTreatments()) {
					previousTreatmentDetails.add(treatment.getTbId());
				}
			}
			for (Treatment treatment : patient.getTreatmentHistory()) {
				if (!patientInfo.getTbId().equals(treatment.getTbId())) {
					previousTreatmentDetails.add(treatment.getTbId());
				}
			}
			uiModel.addAttribute("previousTreatmentDetails",
					previousTreatmentDetails.toArray(new String[] {}));
			uiModel.addAttribute("currentTreatmentDetail",
					patientInfo.getTbId());
		}

		return "patient/patient";
	}

	@RequestMapping(value = "delete", method = RequestMethod.POST)
	public String deletePatientInfo(
			@RequestParam("patientId") String patientId,
			HttpServletRequest request) {

		Patient patient = patientService.findByPatientId(patientId);
		if (patient != null) {
			if (validateIfCurrentTreatmentIsOnGoing(patient)) {
				out(WHPConstants.NOTIFICATION_MESSAGE,
						String.format(
								"Patient with id %s has an open treatment. Cannot Delete patient details! Please close the treatment and retry!",
								patientId), request);
			} else {
				deleteRecords(patientId);
				out(WHPConstants.NOTIFICATION_MESSAGE, String.format(
						"Patient with id %s removed successfully.", patientId),
						request);
			}
		} else
			out(WHPConstants.NOTIFICATION_MESSAGE,
					String.format("Patient with id %s not found.", patientId),
					request);

		return "redirect:/managepatients/deletePatient";

	}

	private boolean validateIfCurrentTreatmentIsOnGoing(Patient patient) {
		return patient.getCurrentTherapy().isOngoing();
	}

	private void deleteRecords(String patientId) {
		adherenceLogService.removeAdherenceLogs(patientId);
		adherenceAuditService.removeAuditLogs(patientId);
		adherenceAuditService.removeDailyAdherenceAuditLogs(patientId);
		adherenceConfirmationOverIVR.deleteAdherenceOfInvalidPatient(patientId);
		List<Container> containers = containerTrackingService
				.findByPatientId(patientId);
		containerService.removeContainers(containers);
		patientService.removePatient(patientId);
	}

	/**
	 * implemented for the fix Missing Sputum Test Results(MS-233)
	 * Reads the file and delete the Smear Test Results for the tbId in couchDb
	 * @param filePath (absolute path of the file containing the patientId,tbId)
	 * @param request
	 * @return
	 * @author mohit
	 */
	@RequestMapping(value = "removeSmearTest", method = RequestMethod.GET)
	public String removeSmearTest(@RequestParam("filePath") String filePath,
			HttpServletRequest request) {
		File patientIdFile = FileUtils.getFile(filePath);
		try {
			List<String> fileContents = FileUtils.readLines(patientIdFile);
			for (String row : fileContents) {
				String[] rowContents = StringUtils.split(row, DELIMITER);
				String patientId = rowContents[0];
				String tbId = rowContents[1];
				Patient patient = patientService.findByPatientId(patientId);
				Treatment treatment = patient.getTreatmentBy(tbId);
				treatment.clearSmearTestResult();
				treatment.clearWeightInstance();
				patient.updateTreatment(treatment);
				patientService.update(patient);
			}
		} catch (IOException e) {
			throw new WHPDomainException("Patient not found");
		}
		return "redirect:/managepatients";
	}

}
