package org.motechproject.whp.criteria;


import org.joda.time.LocalDate;
import org.motechproject.util.DateUtil;
import org.motechproject.whp.patient.domain.Patient;
import org.motechproject.whp.patient.repository.AllPatients;
import org.motechproject.whp.refdata.domain.PatientStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class UpdateAdherenceCriteria {

    private AllPatients allPatients;

    @Autowired
    public UpdateAdherenceCriteria(AllPatients allPatients) {
        this.allPatients = allPatients;
    }

    public boolean canUpdate(String patientId) {
        return isCorrectDayOfWeek() && isPatientOpen(patientId);
    }

    private boolean isCorrectDayOfWeek() {
        LocalDate today = DateUtil.today();
        boolean isLaterThanTuesday = today.getDayOfWeek() > 2;
        boolean isEarlierThanSunday = today.getDayOfWeek() < 7;
        return !(isLaterThanTuesday && isEarlierThanSunday);
    }

    private boolean isPatientOpen(String patientId) {
        Patient patient = allPatients.findByPatientId(patientId);
        return patient.getStatus() == PatientStatus.Open;
    }
}
