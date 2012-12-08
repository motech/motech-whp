package org.motechproject.whp.adherenceapi.response;

import lombok.EqualsAndHashCode;

import javax.xml.bind.annotation.XmlElement;
import java.util.ArrayList;
import java.util.List;

@EqualsAndHashCode
public class ErrorCodes {

    @XmlElement(name = "error_code")
    private List<String> errorCodes = new ArrayList<>();

    public void add(String error) {
        errorCodes.add(error);
    }
}
