package org.motechproject.whp.webservice.request;

import lombok.Data;
import org.apache.commons.lang.StringUtils;
import org.motechproject.validation.constraints.DateTimeFormat;
import org.motechproject.validation.constraints.Enumeration;
import org.motechproject.validation.constraints.NamedConstraint;
import org.motechproject.validation.constraints.NotNullOrEmpty;
import org.motechproject.whp.refdata.domain.SampleInstance;
import org.motechproject.whp.webservice.validation.APIKeyValidator;

import javax.validation.constraints.Pattern;

import static org.motechproject.whp.common.util.WHPDate.DATE_TIME_FORMAT;

@Data
public class ContainerPatientMappingWebRequest {

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

    @NotNullOrEmpty
    private String patient_id;

    @NotNullOrEmpty
    private String tb_id;

    @Enumeration(type = SampleInstance.class)
    private String smear_sample_instance;

    public boolean isWellFormed() {
        return StringUtils.isNotEmpty(case_id)
                && StringUtils.isNotEmpty(api_key)
                && StringUtils.isNotEmpty(date_modified)
                && StringUtils.isNotEmpty(update_type)
                && StringUtils.isNotEmpty(patient_id)
                && StringUtils.isNotEmpty(tb_id)
                && smear_sample_instance != null;
    }
}
