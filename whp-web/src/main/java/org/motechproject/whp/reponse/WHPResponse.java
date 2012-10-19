package org.motechproject.whp.reponse;


import lombok.Data;

import java.util.List;

@Data
public class WHPResponse {

    public static final String ERROR = "error";
    public static final String SUCCESS = "success";

    private String status = SUCCESS;
    private List<String> errors;

    public WHPResponse() {
    }

    public WHPResponse(List<String> errors) {
        this.errors = errors;
        if (null != errors && !errors.isEmpty())
            status = ERROR;
    }
}
