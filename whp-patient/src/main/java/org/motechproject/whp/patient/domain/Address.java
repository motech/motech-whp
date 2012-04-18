package org.motechproject.whp.patient.domain;

import lombok.Data;

@Data
public class Address {

    private String houseNumber;
    private String landmark;
    private String block;
    private String village;
    private String district;
    private String state;
    private String postalCode;

    public Address() {
    }

    public Address(String houseNumber, String landmark, String block, String village, String district, String state, String postalCode) {
        this.houseNumber = houseNumber;
        this.landmark = landmark;
        this.block = block;
        this.village = village;
        this.district = district;
        this.state = state;
        this.postalCode = postalCode;
    }

}
