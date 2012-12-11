package org.motechproject.whp.common.error;

import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;
import java.util.List;

@XmlRootElement(name = "binding_errors")
public class BindingResultXML {

    @XmlElement(name = "request_name")
    private String objectName;
    @XmlElement(name = "errors")
    private List<BindingError> errors;

    private BindingResultXML(){}

    public BindingResultXML(BindingResult result) {
        this.objectName = result.getObjectName();
        this.errors = new ArrayList<>();
        for (FieldError fieldError : result.getFieldErrors()) {
            errors.add(
                    new BindingError(fieldError.getField(),
                            new StringBuilder().append(fieldError.getRejectedValue()).toString(), fieldError.getDefaultMessage())
            );
        }
    }
}
