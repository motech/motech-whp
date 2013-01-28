package org.motechproject.whp.user.validation;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.motechproject.whp.common.domain.District;
import org.motechproject.whp.common.repository.AllDistricts;

import static java.util.Arrays.asList;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

public class DistrictValidatorTest {

    @Mock
    AllDistricts allDistricts;

    @Before
    public void setUp() {
        initMocks(this);
    }

    @Test
    public void shouldValidateIfDistrictIsValid() {
        DistrictValidator districtValidator = new DistrictValidator(allDistricts);
        when(allDistricts.getAll()).thenReturn(asList(new District("validDistrict1"), new District("validDistrict2")));

        assertFalse(districtValidator.isValid("invalid"));
        assertTrue(districtValidator.isValid("validDistrict1"));
    }
}
