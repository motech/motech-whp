package org.motechproject.whp.patient.contract;

import lombok.Data;
import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.motechproject.whp.patient.domain.TreatmentCategory;
import org.motechproject.whp.patient.service.treatmentupdate.TreatmentUpdateScenario;
import org.motechproject.whp.refdata.domain.DiseaseClass;
import org.motechproject.whp.refdata.domain.SmearTestResult;
import org.motechproject.whp.refdata.domain.SmearTestSampleInstance;
import org.motechproject.whp.refdata.domain.WeightInstance;

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
    private String reason_for_pause;
    private String reason_for_restart;

    private SmearTestSampleInstance smear_sample_instance;
    private LocalDate smear_test_date_1;
    private SmearTestResult smear_test_result_1;
    private LocalDate smear_test_date_2;
    private SmearTestResult smear_test_result_2;

    private WeightInstance weight_instance;
    private Double weight;

    private DiseaseClass disease_class;

}
