package org.motechproject.whp.controller;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.motechproject.security.domain.AuthenticatedUser;
import org.motechproject.whp.adherence.domain.AdherenceSource;
import org.motechproject.whp.adherence.domain.WeeklyAdherence;
import org.motechproject.whp.adherence.report.AdherenceReportBuilder;
import org.motechproject.whp.adherence.service.WHPAdherenceService;
import org.motechproject.whp.criteria.UpdateAdherenceCriteria;
import org.motechproject.whp.patient.repository.AllPatients;
import org.motechproject.whp.uimodel.WeeklyAdherenceForm;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Controller
@RequestMapping(value = "/adherence")
public class AdherenceController extends BaseController {

    AllPatients allPatients;
    WHPAdherenceService adherenceService;
    UpdateAdherenceCriteria adherenceCriteria;
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    public AdherenceController(
            AllPatients allPatients,
            WHPAdherenceService adherenceService,
            UpdateAdherenceCriteria adherenceCriteria
    ) {
        this.allPatients = allPatients;
        this.adherenceService = adherenceService;
        this.adherenceCriteria = adherenceCriteria;
    }

    @RequestMapping(method = RequestMethod.GET, value = "/update/{patientId}")
    public String update(@PathVariable("patientId") String patientId, Model uiModel) {
        WeeklyAdherence adherence = adherenceService.currentWeekAdherence(patientId);
        prepareModel(patientId, uiModel, adherence);
        return "adherence/update";
    }

    @RequestMapping(method = RequestMethod.POST, value = "/update/{patientId}")
    public String update(@PathVariable("patientId") String patientId, WeeklyAdherenceForm weeklyAdherenceForm, HttpServletRequest httpServletRequest) {
        AuthenticatedUser authenticatedUser = loggedInUser(httpServletRequest);
        adherenceService.recordAdherence(patientId, weeklyAdherenceForm.weeklyAdherence(), authenticatedUser.getUsername(), AdherenceSource.WEB);
        return "forward:/";
    }

    @RequestMapping(value = "/reports/adherenceReport.xls", method = RequestMethod.GET)
    public void buildAdherenceExcelReport(HttpServletResponse response) {
        writeExcelToResponse(response, createExcelReport(), "AdherenceReport.xls");
    }

    private void writeExcelToResponse(HttpServletResponse response, HSSFWorkbook excelWorkbook, String fileName) {
        try {
            initializeExcelResponse(response, fileName);
            ServletOutputStream outputStream = response.getOutputStream();
            if (null != excelWorkbook)
                excelWorkbook.write(outputStream);
            outputStream.flush();
        } catch (IOException e) {
            logger.error("Error while writing excel report to response: " + e.getMessage());
        }
    }

    private void initializeExcelResponse(HttpServletResponse response, String fileName) {
        response.setHeader("Content-Disposition", "inline; filename=" + fileName);
        response.setContentType("application/vnd.ms-excel");
    }

    private HSSFWorkbook createExcelReport() {
        try {
            return new AdherenceReportBuilder(adherenceService).build();
        } catch (Exception e) {
            logger.error("Error while generating excel report: " + e.getMessage());
            return null;
        }
    }

    private void prepareModel(String patientId, Model uiModel, WeeklyAdherence adherence) {
        WeeklyAdherenceForm weeklyAdherenceForm = new WeeklyAdherenceForm(adherence);
        uiModel.addAttribute("referenceDate", weeklyAdherenceForm.getReferenceDateString());
        uiModel.addAttribute("adherence", weeklyAdherenceForm);
        uiModel.addAttribute("readOnly", !(adherenceCriteria.canUpdate(patientId)));
    }

}
