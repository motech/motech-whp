package org.motechproject.whp.treatmentcard.builder;

import org.joda.time.LocalDate;
import org.motechproject.whp.adherence.domain.Adherence;
import org.motechproject.whp.adherence.domain.PillStatus;
import org.motechproject.whp.common.ForEveryDayBetween;
import org.motechproject.whp.patient.domain.Patient;

import java.util.ArrayList;
import java.util.List;

import static org.motechproject.util.DateUtil.today;

public class AdherenceDataBuilder {

    private String forPatient;

    private String onTreatment;

    private LocalDate startDate = today();

    private LocalDate endDate = today();

    private PillStatus status = PillStatus.Taken;

    public AdherenceDataBuilder forPatient(Patient patient) {
        forPatient = patient.getPatientId();
        onTreatment = patient.getCurrentTherapy().getUid();
        return this;
    }

    public AdherenceDataBuilder from(LocalDate from) {
        startDate = from;
        return this;
    }

    public AdherenceDataBuilder till(LocalDate till) {
        endDate = till;
        return this;
    }

    public AdherenceDataBuilder withAllPills(PillStatus status) {
        this.status = status;
        return this;
    }

    public List<Adherence> build() {
        final List<Adherence> adherence = new ArrayList<>();
        new ForEveryDayBetween(startDate, endDate) {
            @Override
            public void _do_(LocalDate date) {
                Adherence log = new Adherence(date);
                log.setPatientId(forPatient);
                log.setProviderId("providerId");
                log.setTreatmentId(onTreatment);
                log.setPillStatus(status);
                adherence.add(log);
            }
        };
        return adherence;
    }
}
