package org.motechproject.whp.reponse;


import lombok.Data;
import org.apache.commons.collections.CollectionUtils;
import org.motechproject.whp.common.error.ErrorWithParameters;

import java.util.List;

@Data
public class WHPResponse {

    public static final String ERROR = "error";
    public static final String SUCCESS = "success";

    private String status = SUCCESS;
    private List<ErrorWithParameters> errors;

    public WHPResponse() {
    }

    public WHPResponse(List<ErrorWithParameters> errors) {
        this.errors = errors;
        if (CollectionUtils.isNotEmpty(errors))
            status = ERROR;
    }
}
