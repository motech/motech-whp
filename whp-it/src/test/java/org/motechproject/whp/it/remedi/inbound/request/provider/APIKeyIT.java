package org.motechproject.whp.it.remedi.inbound.request.provider;

import org.junit.Test;
import org.motechproject.whp.patient.command.UpdateScope;
import org.motechproject.whp.webservice.builder.ProviderRequestBuilder;
import org.motechproject.whp.webservice.request.ProviderWebRequest;

public class APIKeyIT extends BaseProviderIT {
    @Test
    public void shouldBeValidWhenAPIKeyIsValid() {
        ProviderWebRequest providerWebRequest = new ProviderRequestBuilder()
                .withDefaults()
                .build();
        validator.validate(providerWebRequest, UpdateScope.createScope); //Can be any scope. None of the validation is scope dependent.
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
        validator.validate(providerWebRequest, UpdateScope.createScope); //Can be any scope. None of the validation is scope dependent.
    }
}
