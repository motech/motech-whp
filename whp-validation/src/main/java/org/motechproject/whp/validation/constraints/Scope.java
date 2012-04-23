package org.motechproject.whp.validation.constraints;

import org.motechproject.whp.validation.ValidationScope;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target({FIELD})
@Retention(RUNTIME)
public @interface Scope {
    ValidationScope[] scope();
}
