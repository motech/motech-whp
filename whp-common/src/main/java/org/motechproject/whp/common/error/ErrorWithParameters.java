package org.motechproject.whp.common.error;

import lombok.Data;

import java.util.List;

import static java.util.Arrays.asList;

@Data
public class ErrorWithParameters {

    private String code;
    private List<String> parameters;

    public ErrorWithParameters(String code, String... parameters){
        this.code = code;
        this.parameters = asList(parameters);
    }
}
