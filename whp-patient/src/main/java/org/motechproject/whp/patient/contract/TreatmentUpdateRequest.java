package org.motechproject.whp.patient.contract;

import lombok.Data;
import org.joda.time.DateTime;
import org.motechproject.whp.patient.domain.TreatmentCategory;
import org.motechproject.whp.patient.service.treatmentupdate.TreatmentUpdateScenario;

@Data
public class TreatmentUpdateRequest {

    private String case_id;
    private DateTime date_modified;
    private String provider_id;
    private String tb_id;
    private String old_tb_id;
    private TreatmentUpdateScenario treatment_update;
    private String treatment_outcome;
    private TreatmentCategory treatment_category;

}
