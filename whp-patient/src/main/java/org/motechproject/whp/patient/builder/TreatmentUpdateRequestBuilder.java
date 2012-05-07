package org.motechproject.whp.patient.builder;

import org.joda.time.DateTime;
import org.motechproject.model.DayOfWeek;
import org.motechproject.whp.patient.contract.TreatmentUpdateRequest;
import org.motechproject.whp.patient.domain.TreatmentCategory;
import org.motechproject.whp.refdata.domain.TreatmentUpdate;

import java.util.Arrays;

import static org.motechproject.util.DateUtil.now;

public class TreatmentUpdateRequestBuilder {

    private TreatmentUpdateRequest treatmentUpdateRequest;

    private TreatmentUpdateRequestBuilder() {
        treatmentUpdateRequest = new TreatmentUpdateRequest();
    }

    public static TreatmentUpdateRequestBuilder startRecording() {
        return new TreatmentUpdateRequestBuilder();
    }

    public TreatmentUpdateRequest build() {
        return treatmentUpdateRequest;
    }

    public TreatmentUpdateRequestBuilder withDefaults() {
        TreatmentCategory category = new TreatmentCategory("RNTCP Category 1", "01", 3, 8, 18, Arrays.asList(DayOfWeek.Monday));
        treatmentUpdateRequest.setCase_id("caseId");
        treatmentUpdateRequest.setDate_modified(now());
        treatmentUpdateRequest.setProvider_id("providerId");
        treatmentUpdateRequest.setTb_id("tbId");
        treatmentUpdateRequest.setTreatment_update(TreatmentUpdate.NewTreatment);
        treatmentUpdateRequest.setReason_for_closure("Cured");
        treatmentUpdateRequest.setTreatment_complete("Yes");
        treatmentUpdateRequest.setTreatment_category(category);
        treatmentUpdateRequest.setOld_tb_id("oldTbId");
        return this;
    }

    public TreatmentUpdateRequestBuilder withMandatoryFieldsForCloseTreatment() {
        treatmentUpdateRequest.setCase_id("caseId");
        treatmentUpdateRequest.setDate_modified(now());
        treatmentUpdateRequest.setTb_id("tbId");
        treatmentUpdateRequest.setTreatment_update(TreatmentUpdate.CloseTreatment);
        treatmentUpdateRequest.setReason_for_closure("Cured");
        treatmentUpdateRequest.setTreatment_complete("Yes");
        return this;
    }

    public TreatmentUpdateRequestBuilder withMandatoryFieldsForOpenNewTreatment() {
        TreatmentCategory category = new TreatmentCategory("RNTCP Category 1", "10", 3, 8, 18, Arrays.asList(DayOfWeek.Monday));
        treatmentUpdateRequest.setCase_id("caseId");
        treatmentUpdateRequest.setDate_modified(now());
        treatmentUpdateRequest.setTb_id("tbId");
        treatmentUpdateRequest.setTreatment_update(TreatmentUpdate.NewTreatment);
        treatmentUpdateRequest.setTreatment_category(category);
        return this;
    }

    public TreatmentUpdateRequestBuilder withDateModified(DateTime dateModified) {
        treatmentUpdateRequest.setDate_modified(dateModified);
        return this;
    }

    public TreatmentUpdateRequestBuilder withTbId(String tbId) {
        treatmentUpdateRequest.setTb_id(tbId);
        return this;
    }

    public TreatmentUpdateRequestBuilder withCaseId(String caseId) {
        treatmentUpdateRequest.setCase_id(caseId);
        return this;
    }

    public TreatmentUpdateRequestBuilder withTreatmentCategory(TreatmentCategory treatmentCategory) {
        treatmentUpdateRequest.setTreatment_category(treatmentCategory);
        return this;
    }
}
