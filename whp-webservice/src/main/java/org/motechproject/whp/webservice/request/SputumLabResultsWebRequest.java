package org.motechproject.whp.webservice.request;

import lombok.Data;
import org.apache.commons.lang.StringUtils;
import org.motechproject.validation.constraints.DateTimeFormat;
import org.motechproject.validation.constraints.NamedConstraint;
import org.motechproject.validation.constraints.NotNullOrEmpty;
import org.motechproject.whp.common.validation.APIKeyValidator;

import javax.validation.constraints.Pattern;

import static org.motechproject.whp.common.util.WHPDate.DATE_FORMAT;
import static org.motechproject.whp.common.util.WHPDate.DATE_TIME_FORMAT;

@Data
public class SputumLabResultsWebRequest {

    @NotNullOrEmpty
    private String case_id;

    @NotNullOrEmpty
    @NamedConstraint(name = APIKeyValidator.API_KEY_VALIDATION)
    private String api_key;

    @NotNullOrEmpty
    @DateTimeFormat(pattern = DATE_TIME_FORMAT)
    private String date_modified;


    @NotNullOrEmpty
    @Pattern(regexp = "lab_results")
    private String update_type;

    @DateTimeFormat(pattern = DATE_FORMAT)
    private String smear_test_date_1;

    private String smear_test_result_1;

    @DateTimeFormat(pattern = DATE_FORMAT)
    private String smear_test_date_2;

    private String smear_test_result_2;

    private String lab_name;

    private String lab_number;

    public boolean hasCompleteLabResults() {
        if (StringUtils.isNotEmpty(getSmear_test_date_1())
                && StringUtils.isNotEmpty(getSmear_test_date_2())
                && StringUtils.isNotEmpty(getSmear_test_result_1())
                && StringUtils.isNotEmpty(getSmear_test_result_2())) {
            return true;
        }
        return false;
    }
}
