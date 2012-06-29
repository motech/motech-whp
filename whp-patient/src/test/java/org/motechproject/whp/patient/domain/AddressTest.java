package org.motechproject.whp.patient.domain;

import org.junit.Test;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class AddressTest {

    @Test
    public void shouldReturnAddressInStringFormat() {
        Address address = new Address("houseNo", "landmark", "block", "village", "district", "state");
        assertThat(address.toString(),is("houseNo, landmark, block, village, district, state"));
    }

    @Test
    public void AddressStringShouldIgnoreEmptyFields() {
        assertThat(new Address(null, "landmark", "block", "village", "district", "state").toString(),
                is("landmark, block, village, district, state"));

        assertThat(new Address("houseNo", "", "block", "village", "district", "state").toString(),
                is("houseNo, block, village, district, state"));

        assertThat(new Address("houseNo", "landmark", "block", "village", "district", "").toString(),
                is("houseNo, landmark, block, village, district"));
    }
}
