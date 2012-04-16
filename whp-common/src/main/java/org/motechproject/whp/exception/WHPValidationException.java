package org.motechproject.whp.exception;

/**
 * Created by IntelliJ IDEA.
 * User: preethi
 * Date: 16/4/12
 * Time: 4:35 PM
 * To change this template use File | Settings | File Templates.
 */
public class WHPValidationException extends RuntimeException {
    public WHPValidationException(String message) {
        super(message);
    }
}
