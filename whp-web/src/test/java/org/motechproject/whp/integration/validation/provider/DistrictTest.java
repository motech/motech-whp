package org.motechproject.whp.integration.validation.provider;

import org.junit.Test;
import org.motechproject.whp.builder.ProviderRequestBuilder;
import org.motechproject.whp.patient.exception.WHPException;
import org.motechproject.whp.request.ProviderWebRequest;
import org.motechproject.whp.validation.ValidationScope;

import static junit.framework.Assert.assertTrue;
import static junit.framework.Assert.fail;

public class DistrictTest extends BaseProviderTest {
    @Test
    public void shouldThrowAnExceptionIfDistrictIsNull() {
        expectWHPException("field:district:may not be empty");
        ProviderWebRequest providerWebRequest = new ProviderRequestBuilder().withProviderId("P00001").withDate("17/03/1990").withDistrict(null).withPrimaryMobile("9880000000").build();
        validator.validate(providerWebRequest, ValidationScope.create); //Can be any scope. None of the validation is scope dependent.
    }

    @Test
    public void shouldThrowAnExceptionIfDistrictIsEmpty() {
        expectWHPException("field:district:may not be empty");
        ProviderWebRequest providerWebRequest = new ProviderRequestBuilder().withProviderId("P00001").withDate("17/03/1990").withDistrict("").withPrimaryMobile("9880000000").build();
        validator.validate(providerWebRequest, ValidationScope.create); //Can be any scope. None of the validation is scope dependent.
    }
}
