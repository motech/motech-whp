package org.motechproject.whp.v0.domain;

import lombok.Data;
import org.codehaus.jackson.annotate.JsonIgnore;
import org.ektorp.support.TypeDiscriminator;
import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.motechproject.model.MotechBaseDataObject;
import org.motechproject.util.DateUtil;

@Data
@TypeDiscriminator("doc.type == 'Therapy'")
public class TherapyV0 extends MotechBaseDataObject {

    private Integer patientAge;
    private DateTime creationDate;
    private LocalDate startDate;
    private LocalDate closeDate;
    private TherapyStatusV0 status = TherapyStatusV0.Ongoing;
    private TreatmentCategoryV0 treatmentCategory;
    private DiseaseClassV0 diseaseClass;

    public TherapyV0() {
    }

    public TherapyV0(TreatmentCategoryV0 treatmentCategory, DiseaseClassV0 diseaseClass, Integer patientAge) {
        this.treatmentCategory = treatmentCategory;
        this.diseaseClass = diseaseClass;
        this.patientAge = patientAge;
    }

    public void close(DateTime dateModified) {
        closeDate = dateModified.toLocalDate();
        status = TherapyStatusV0.Closed;
    }

    public void revive() {
        /* Not handling not null case as the only use case is in
         * Transfer In where check has already been made to see if current treatment is closed.
         *   now, whether closing a treatment sets the end date or not, that is cause for concern
         */
        closeDate = null;
        status = TherapyStatusV0.Ongoing;
    }

    @JsonIgnore
    public boolean isClosed() {
        return TherapyStatusV0.Closed == status;
    }

    public DateTime getCreationDate() {
        return DateUtil.setTimeZone(creationDate);
    }

    public void start(LocalDate therapyStartDate){
        setStartDate(therapyStartDate);
    }
}
