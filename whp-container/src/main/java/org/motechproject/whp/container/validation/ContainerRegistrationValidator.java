package org.motechproject.whp.container.validation;

import org.motechproject.whp.common.error.ErrorWithParameters;

import java.util.List;

public interface ContainerRegistrationValidator<T> {
    List<ErrorWithParameters> validate(T registrationRequest);
}
