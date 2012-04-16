package org.motechproject.whp.adherence.repository;

import org.joda.time.LocalDate;
import org.junit.After;
import org.junit.Test;
import org.motechproject.util.DateUtil;
import org.motechproject.whp.adherence.domain.DosageLog;
import org.motechproject.whp.adherence.domain.DosageSummary;
import org.motechproject.whp.common.integration.repository.SpringIntegrationTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;

@ContextConfiguration(locations = {"classpath:applicationAdherenceContext.xml"})
public class AllDosageLogsIT extends SpringIntegrationTest {

    public static final String PATIENT_ID = "patientId";
    public static final int IDEAL_DOSE_COUNT = 10;

    @Autowired
    private AllDosageLogs allDosageLogs;

    @After
    public void tearDown() {
        markForDeletion(allDosageLogs.getAll().toArray());
    }

    @Test
    public void shouldAddDosageLogs_WhenLogDoesNotExistForGivenDateRangeAndPatientId() {
        DosageLog dosageLog = new DosageLog("patientId", "treatmentCourseId", DateUtil.today(), DateUtil.today(), 3, 3, null);
        allDosageLogs.add(dosageLog);
        assertNotNull(allDosageLogs.get(dosageLog.getId()));
    }

    @Test
    public void shouldReplaceDosageLogs_WhenLogAlreadyExistsForGivenDateRangeAndPatientId() {
        Map<String, String> metaData = new HashMap<String, String>() {{
            this.put("key1", "value1");
            this.put("key2", "value2");
        }};
        DosageLog existingDosageLog = new DosageLog("patientId", "treatmentCourseId", DateUtil.today(), DateUtil.today(), 3, 3, metaData);
        allDosageLogs.add(existingDosageLog);

        Map<String, String> updatedMetaData = new HashMap<String, String>() {{
            this.put("key2", "newValue2");
        }};
        DosageLog dosageLog = new DosageLog("patientId", "treatmentCourseId", DateUtil.today(), DateUtil.today(), 5, 10, updatedMetaData);
        allDosageLogs.add(dosageLog);

        assertEquals(existingDosageLog.getId(), dosageLog.getId());

        DosageLog updatedDosageLog = allDosageLogs.get(existingDosageLog.getId());
        assertEquals(5, updatedDosageLog.getDoseTakenCount());
        assertEquals(10, updatedDosageLog.getIdealDoseCount());
        assertEquals(2, updatedDosageLog.getMetaData().size());
        assertEquals("value1", updatedDosageLog.getMetaData().get("key1"));
        assertEquals("newValue2", updatedDosageLog.getMetaData().get("key2"));
    }

    @Test
    public void shouldFindAllDosageLogsForAPatientAndTreatmentCourseBetweenGivenDateRange() {
        LocalDate logsStartDate = DateUtil.newDate(2011, 12, 31);
        DosageLog beforeDateRange = addLog("patientId", "treatmentCourseId", 2, logsStartDate);
        DosageLog inRange_1 = addLog("patientId", "treatmentCourseId", 2, logsStartDate.plusDays(3));
        DosageLog inRange_OtherTreatmentCourse = addLog("patientId", "otherTreatmentCourseId", 2, logsStartDate.plusDays(4));
        DosageLog inRange_OtherPatient = addLog("otherPatientId", "treatmentCourseId", 2, logsStartDate.plusDays(5));
        DosageLog inRange_2 = addLog("patientId", "treatmentCourseId", 2, logsStartDate.plusDays(6));
        DosageLog afterDateRange = addLog("patientId", "treatmentCourseId", 2, logsStartDate.plusDays(7));

        List<DosageLog> dosageLogs = allDosageLogs.getAllBy("patientId", "treatmentCourseId", logsStartDate.plusDays(2), logsStartDate.plusDays(6));
        assertEquals(Arrays.asList(inRange_1, inRange_2), dosageLogs);

        List<DosageLog> otherTreatmentCourseDosageLogs = allDosageLogs.getAllBy("patientId", "otherTreatmentCourseId", logsStartDate.plusDays(2), logsStartDate.plusDays(6));
        assertEquals(Arrays.asList(inRange_OtherTreatmentCourse), otherTreatmentCourseDosageLogs);

        List<DosageLog> otherPatientDosageLogs = allDosageLogs.getAllBy("otherPatientId", "treatmentCourseId", logsStartDate.plusDays(2), logsStartDate.plusDays(6));
        assertEquals(Arrays.asList(inRange_OtherPatient), otherPatientDosageLogs);
    }

    @Test
    public void shouldFindAllDosageLogsBetweenGivenDateRange() {
        LocalDate logsStartDate = DateUtil.today();
        DosageLog beforeDateRange = addLog("patientId", "treatmentCourseId", 2, logsStartDate);
        DosageLog inRange_1 = addLog("patientId", "treatmentCourseId", 2, logsStartDate.plusDays(3));
        DosageLog inRangeOtherPatient = addLog("otherPatientId", "treatmentCourseId", 2, logsStartDate.plusDays(4));
        DosageLog inRange_2 = addLog("patientId", "treatmentCourseId", 2, logsStartDate.plusDays(5));
        DosageLog afterDateRange = addLog("patientId", "treatmentCourseId", 2, logsStartDate.plusDays(7));

        List<DosageLog> dosageLogs = allDosageLogs.getAllInDateRange(logsStartDate.plusDays(2), logsStartDate.plusDays(6));
        assertEquals(Arrays.asList(inRange_1, inRangeOtherPatient, inRange_2), dosageLogs);
    }

    @Test
    public void shouldGetPatientDosageSummary() {
        LocalDate logsStartDate = DateUtil.newDate(2012, 1, 1);
        DosageLog beforeDateRange = addLog(PATIENT_ID, "treatmentCourseId", 2, logsStartDate);

        //Create DosageLog for PATIENT for 25 days in range to test ReReduce
        int averageDoseTakenCount = 3;
        for(int day = 5; day < 30; day++){
            addLog(PATIENT_ID, "treatmentCourseId", averageDoseTakenCount, logsStartDate.plusDays(day));
        }
        DosageLog inRange_EdgeCase = addLog(PATIENT_ID, "treatmentCourseId", averageDoseTakenCount, logsStartDate.plusDays(35)); //total 26 logs in range

        DosageLog inRangeOtherTreatmentCourseId = addLog(PATIENT_ID, "otherTreatmentCourseId", 2, logsStartDate.plusDays(31));
        DosageLog inRangeOtherPatient = addLog("otherPatientId", "treatmentCourseId", 2, logsStartDate.plusDays(5));
        DosageLog afterDateRange = addLog(PATIENT_ID, "treatmentCourseId", 2, logsStartDate.plusDays(40));

        DosageSummary dosageSummary = allDosageLogs.getPatientDosageSummary(PATIENT_ID, "treatmentCourseId", logsStartDate.plusDays(4), logsStartDate.plusDays(35));

        assertEquals(PATIENT_ID, dosageSummary.getPatientId());
        assertEquals("treatmentCourseId", dosageSummary.getTreatmentCourseId());
        assertEquals(averageDoseTakenCount * 26, dosageSummary.getTotalDoseTakenCount());
        assertEquals(IDEAL_DOSE_COUNT * 26, dosageSummary.getTotalIdealDoseCount());
    }

    private DosageLog addLog(String patientId, String treatmentCourseId, int doseTakenCount, LocalDate fromDate) {
        DosageLog dosageLog = new DosageLog(patientId, treatmentCourseId, fromDate, fromDate, doseTakenCount, IDEAL_DOSE_COUNT, null);
        allDosageLogs.add(dosageLog);
        return dosageLog;
    }
}
