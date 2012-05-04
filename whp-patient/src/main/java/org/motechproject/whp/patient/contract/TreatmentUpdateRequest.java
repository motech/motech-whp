package org.motechproject.whp.patient.contract;

import lombok.Data;
import lombok.Getter;
import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.motechproject.whp.patient.domain.*;

@Data
public class TreatmentUpdateRequest {

    private String case_id;
    private DateTime date_modified;
    private String provider_id;
    private String tb_id;
    private TreatmentUpdate treatment_update;
    private String reason_for_closure;
    private String treatment_complete;
    private TreatmentCategory treatment_category;
    private String old_tb_id;

}
