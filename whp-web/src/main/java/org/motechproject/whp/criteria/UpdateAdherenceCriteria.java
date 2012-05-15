package org.motechproject.whp.criteria;


import org.joda.time.LocalDate;
import org.motechproject.util.DateUtil;
import org.motechproject.whp.patient.domain.Patient;
import org.motechproject.whp.patient.repository.AllPatients;
import org.motechproject.whp.refdata.domain.PatientStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

public class UpdateAdherenceCriteria {

    public static boolean canUpdate(Patient patient) {
        return isCorrectDayOfWeek() && isPatientOpen(patient);
    }

    private static boolean isCorrectDayOfWeek() {
        LocalDate today = DateUtil.today();
        boolean isLaterThanTuesday = today.getDayOfWeek() > 2;
        boolean isEarlierThanSunday = today.getDayOfWeek() < 7;
        return !(isLaterThanTuesday && isEarlierThanSunday);
    }

    private static boolean isPatientOpen(Patient patient) {
        return patient.getStatus() == PatientStatus.Open;
    }
}
