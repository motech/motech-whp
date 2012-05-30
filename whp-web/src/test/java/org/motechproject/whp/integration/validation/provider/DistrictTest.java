package org.motechproject.whp.integration.validation.provider;

import org.junit.Test;
import org.motechproject.whp.builder.ProviderRequestBuilder;
import org.motechproject.whp.patient.command.UpdateScope;
import org.motechproject.whp.contract.ProviderWebRequest;

public class DistrictTest extends BaseProviderTest {

    @Test()
    public void shouldThrowAnExceptionIfDistrictIsNull() {
        expectFieldValidationRuntimeException("field:district:may not be empty");
        ProviderWebRequest providerWebRequest = new ProviderRequestBuilder().withProviderId("P00001").withDate("17/03/1990").withDistrict(null).withPrimaryMobile("9880000000").build();
        validator.validate(providerWebRequest, UpdateScope.createScope); //Can be any scope. None of the validation is scope dependent.
    }

    @Test
    public void shouldThrowAnExceptionIfDistrictIsEmpty() {
        expectFieldValidationRuntimeException("field:district:may not be empty");
        ProviderWebRequest providerWebRequest = new ProviderRequestBuilder().withProviderId("P00001").withDate("17/03/1990").withDistrict("").withPrimaryMobile("9880000000").build();
        validator.validate(providerWebRequest, UpdateScope.createScope); //Can be any scope. None of the validation is scope dependent.
    }

}
