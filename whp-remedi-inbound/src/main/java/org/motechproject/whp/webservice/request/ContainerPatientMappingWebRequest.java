package org.motechproject.whp.webservice.request;

import lombok.Data;
import org.motechproject.validation.constraints.DateTimeFormat;
import org.motechproject.validation.constraints.Enumeration;
import org.motechproject.validation.constraints.NamedConstraint;
import org.motechproject.validation.constraints.NotNullOrEmpty;
import org.motechproject.whp.common.domain.SampleInstance;
import org.motechproject.whp.webservice.validation.APIKeyValidator;

import javax.validation.constraints.Pattern;

import static org.motechproject.whp.common.util.WHPDate.DATE_TIME_FORMAT;

@Data
public class ContainerPatientMappingWebRequest {
    private static final String EMPTY_STRING = "";

    @NotNullOrEmpty
    private String case_id;

    @NamedConstraint(name = APIKeyValidator.API_KEY_VALIDATION)
    private String api_key;

    @NotNullOrEmpty
    @DateTimeFormat(pattern = DATE_TIME_FORMAT)
    private String date_modified;

    @NotNullOrEmpty
    @Pattern(regexp = "patient_mapping")
    private String update_type;

    private String patient_id;

    private String tb_id;

    @Enumeration(type = SampleInstance.class, validateEmptyString = false)
    private String smear_sample_instance;

    public boolean isMappingRequest() {
        return !(getPatient_id().equals(EMPTY_STRING)
                && getTb_id().equals(EMPTY_STRING)
                && getSmear_sample_instance().equals(EMPTY_STRING));
    }
}
