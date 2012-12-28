package org.motechproject.whp.service;

import org.joda.time.LocalDate;
import org.motechproject.export.annotation.DataProvider;
import org.motechproject.export.annotation.ExcelDataSource;
import org.motechproject.export.annotation.Header;
import org.motechproject.model.DayOfWeek;
import org.motechproject.whp.common.util.WHPDate;
import org.motechproject.whp.mapper.PatientSummaryMapper;
import org.motechproject.whp.patient.domain.Patient;
import org.motechproject.whp.patient.service.PatientService;
import org.motechproject.whp.uimodel.PatientSummary;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import static java.util.Arrays.asList;
import static org.motechproject.util.DateUtil.today;
import static org.motechproject.whp.common.domain.TreatmentWeekInstance.currentAdherenceCaptureWeek;

@Service
@ExcelDataSource(name = "patients")
public class PatientDataSummary {

    PatientService patientService;

    @Autowired
    public PatientDataSummary(PatientService patientService) {
        this.patientService = patientService;
    }

    @Header(span = 3)
    public List<String> patientSummaryHeader() {
        LocalDate lastSunday = currentAdherenceCaptureWeek().dateOf(DayOfWeek.Sunday);
        return asList("Generated as on " + new WHPDate(today()).value(),
                "* Cumulative missed doses shown as of " + new WHPDate(lastSunday).value());
    }

    @DataProvider
    public List<PatientSummary> patientSummaryReport(int pageNumber) {
        List<Patient> patientList = patientService.getAll(pageNumber - 1, 10000);
        return new PatientSummaryMapper().map(patientList);
    }
}
