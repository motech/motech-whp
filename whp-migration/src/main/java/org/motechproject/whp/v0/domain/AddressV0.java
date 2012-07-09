package org.motechproject.whp.v0.domain;

import lombok.Data;

@Data
public class AddressV0 {

    private String address_location;
    private String address_landmark;
    private String address_block;
    private String address_village;
    private String address_district;
    private String address_state;

    public AddressV0() {
    }

    public AddressV0(String houseNumber, String landmark, String block, String village, String district, String state) {
        this.address_location = houseNumber;
        this.address_landmark = landmark;
        this.address_block = block;
        this.address_village = village;
        this.address_district = district;
        this.address_state = state;
    }

}