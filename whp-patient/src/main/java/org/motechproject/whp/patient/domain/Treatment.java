package org.motechproject.whp.patient.domain;

import lombok.Data;
import org.codehaus.jackson.annotate.JsonIgnore;
import org.ektorp.support.TypeDiscriminator;
import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.motechproject.model.MotechBaseDataObject;
import org.motechproject.whp.refdata.domain.DiseaseClass;
import org.motechproject.whp.refdata.domain.TreatmentStatus;

@Data
@TypeDiscriminator("doc.type == 'Treatment'")
public class Treatment extends MotechBaseDataObject {

    private Integer patientAge;
    private DateTime creationDate;
    private LocalDate startDate;
    private LocalDate closeDate;
    private TreatmentStatus status = TreatmentStatus.Ongoing;
    private TreatmentCategory treatmentCategory;
    private DiseaseClass diseaseClass;

    // Required for ektorp
    public Treatment() {
    }

    public Treatment(TreatmentCategory treatmentCategory, DiseaseClass diseaseClass, Integer patientAge) {
        this.treatmentCategory = treatmentCategory;
        this.diseaseClass = diseaseClass;
        this.patientAge = patientAge;
    }

    public void close(DateTime dateModified) {
        closeDate = dateModified.toLocalDate();
        status = TreatmentStatus.Closed;
    }

    public void revive() {
        closeDate = null;
        status = TreatmentStatus.Ongoing;
        /*not handling not null case as the only use case is in Transfer In where check has already been made to see if current treatment is closed.
        now, whether closing a treatment sets the end date or not, that is cause for concern*/
    }

    @JsonIgnore
    public boolean isClosed() {
        return TreatmentStatus.Closed == status;
    }
}
