package org.motechproject.whp.patient.domain;

import lombok.Data;
import org.codehaus.jackson.annotate.JsonIgnore;
import org.ektorp.support.TypeDiscriminator;
import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.motechproject.model.MotechBaseDataObject;
import org.motechproject.util.DateUtil;
import org.motechproject.whp.refdata.domain.DiseaseClass;
import org.motechproject.whp.refdata.domain.TherapyStatus;
import org.motechproject.whp.refdata.domain.TreatmentCategory;

import static java.util.Arrays.asList;

@Data
@TypeDiscriminator("doc.type == 'Therapy'")
public class Therapy extends MotechBaseDataObject {

    private Integer patientAge;
    private DateTime creationDate;
    private LocalDate startDate;
    private LocalDate closeDate;
    private TherapyStatus status = TherapyStatus.Ongoing;
    private TreatmentCategory treatmentCategory;
    private DiseaseClass diseaseClass;
    private Phases phases = new Phases(asList(new Phase(PhaseName.IP), new Phase(PhaseName.CP), new Phase(PhaseName.EIP)));

    public Therapy() {
    }

    public Therapy(TreatmentCategory treatmentCategory, DiseaseClass diseaseClass, Integer patientAge) {
        this.treatmentCategory = treatmentCategory;
        this.diseaseClass = diseaseClass;
        this.patientAge = patientAge;
    }

    public void close(DateTime dateModified) {
        closeDate = dateModified.toLocalDate();
        status = TherapyStatus.Closed;
    }

    public void revive() {
        closeDate = null;
        status = TherapyStatus.Ongoing;
    }

    @JsonIgnore
    public boolean isClosed() {
        return TherapyStatus.Closed == status;
    }

    public DateTime getCreationDate() {
        return DateUtil.setTimeZone(creationDate);
    }

    public void start(LocalDate therapyStartDate) {
        setStartDate(therapyStartDate);
        phases.setIPStartDate(therapyStartDate);
    }

    void setStartDate(LocalDate therapyStartDate) {
        startDate = therapyStartDate;
    }

    public Phase getPhase(PhaseName phaseName) {
        return phases.getByPhaseName(phaseName);
    }
}
