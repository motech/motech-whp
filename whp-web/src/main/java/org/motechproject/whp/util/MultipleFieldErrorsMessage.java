package org.motechproject.whp.util;

import org.springframework.validation.Errors;
import org.springframework.validation.FieldError;

public class MultipleFieldErrorsMessage {

    public static String getMessage(Errors error) {
        StringBuilder errorMessage = new StringBuilder();
        errorMessage.append("Following errors were found : \n");
        for (FieldError fieldError : error.getFieldErrors()) {
            errorMessage.append(String.format("field:%s:%s\n", fieldError.getField(), fieldError.getDefaultMessage()));
        }
        return errorMessage.toString();
    }
}
