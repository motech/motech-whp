package org.motechproject.whp.integration.validation.patient;

import org.junit.Test;
import org.motechproject.util.DateUtil;
import org.motechproject.whp.builder.PatientWebRequestBuilder;
import org.motechproject.whp.patient.domain.Provider;
import org.motechproject.whp.patient.exception.WHPException;
import org.motechproject.whp.request.PatientWebRequest;
import org.motechproject.whp.validation.ValidationScope;

import static junit.framework.Assert.assertTrue;
import static junit.framework.Assert.fail;

public class ProviderIdTest extends BasePatientTest {
    @Test
    public void shouldThrowExceptionWhenProviderIdIsNotFound() {
        expectWHPException("No provider is found with id:providerId");
        PatientWebRequest webRequest = new PatientWebRequestBuilder().withDefaults().withProviderId("providerId").build();
        validator.validate(webRequest, ValidationScope.create);
    }

    @Test
    public void shouldThrowSingleExceptionWhenProviderIdIsNull() {
        String errorMessage = "";
        try {
            PatientWebRequest webRequest = new PatientWebRequestBuilder().withDefaults().withProviderId(null).build();
            validator.validate(webRequest, ValidationScope.create);
        } catch (WHPException e) {
            if (e.getMessage().contains("field:provider_id:may not be null")) {
                fail("Not Null validation is not required.");
            }
            errorMessage = e.getMessage();
        }
        assertTrue(errorMessage.contains("field:provider_id:Provider Id cannot be null"));
    }

    @Test
    public void shouldNotThrowExceptionWhenProviderIdIsFound() {
        Provider defaultProvider = new Provider("providerId", "1231231231", "chambal", DateUtil.now());
        allProviders.add(defaultProvider);
        PatientWebRequest webRequest = new PatientWebRequestBuilder().withDefaults().withProviderId("providerId").build();
        validator.validate(webRequest, ValidationScope.create);
    }
}
