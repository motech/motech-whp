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

import java.util.Arrays;

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
    private Phases phases = new Phases(Arrays.asList(new Phase(PhaseName.IP), new Phase(PhaseName.CP), new Phase(PhaseName.EIP)));

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
        /* Not handling not null case as the only use case is in
         * Transfer In where check has already been made to see if current treatment is closed.
         *   now, whether closing a treatment sets the end date or not, that is cause for concern
         */
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

    public void start(LocalDate therapyStartDate){
        setStartDate(therapyStartDate);
        phases.setIPStartDate(therapyStartDate);
    }
}
