package org.motechproject.whp.integration.validation.provider;

import org.junit.Test;
import org.motechproject.whp.builder.ProviderRequestBuilder;
import org.motechproject.whp.request.ProviderWebRequest;
import org.motechproject.whp.validation.ValidationScope;

public class APIKeyTest extends BaseProviderTest {
    @Test
    public void shouldBeValidWhenAPIKeyIsValid() {
        ProviderWebRequest providerWebRequest = new ProviderRequestBuilder()
                .withDefaults()
                .build();
        validator.validate(providerWebRequest, ValidationScope.create); //Can be any scope. None of the validation is scope dependent.
    }

    @Test
    public void shouldBeInvalidWhenAPIIsInvalid() {
        expectFieldValidationRuntimeException("field:api_key:api_key:is invalid.");
        ProviderWebRequest providerWebRequest = new ProviderRequestBuilder()
                .withProviderId("P00001")
                .withDate("17/03/1990")
                .withDistrict("district")
                .withPrimaryMobile("9880000000")
                .withAPIKey("invalid_api_key")
                .build();
        validator.validate(providerWebRequest, ValidationScope.create); //Can be any scope. None of the validation is scope dependent.
    }
}
