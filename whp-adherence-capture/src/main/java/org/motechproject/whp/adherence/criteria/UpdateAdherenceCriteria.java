package org.motechproject.whp.adherence.criteria;


import org.joda.time.LocalDate;
import org.motechproject.util.DateUtil;
import org.motechproject.whp.patient.domain.Patient;
import org.motechproject.whp.patient.domain.PatientStatus;

public class UpdateAdherenceCriteria {

    public static boolean canUpdate(Patient patient) {
        return isWindowOpenToday() && isPatientOpen(patient);
    }

    public static boolean isWindowOpenToday() {
        LocalDate today = DateUtil.today();
        boolean isLaterThanTuesday = today.getDayOfWeek() > 2;
        boolean isEarlierThanSunday = today.getDayOfWeek() < 7;
        return !(isLaterThanTuesday && isEarlierThanSunday);
    }

    private static boolean isPatientOpen(Patient patient) {
        return patient.getStatus() == PatientStatus.Open;
    }

    public static boolean isWindowClosedToday() {
        return !isWindowOpenToday();
    }
}
