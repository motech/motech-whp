package org.motechproject.whp.patient.domain;

import lombok.Data;
import org.codehaus.jackson.annotate.JsonIgnore;
import org.motechproject.whp.common.exception.WHPErrorCode;

import java.util.List;

@Data
public class Address {

    private String address_location;
    private String address_landmark;
    private String address_block;
    private String address_village;
    private String address_district;
    private String address_state;

    public Address() {
    }

    public Address(String houseNumber, String landmark, String block, String village, String district, String state) {
        this.address_location = houseNumber;
        this.address_landmark = landmark;
        this.address_block = block;
        this.address_village = village;
        this.address_district = district;
        this.address_state = state;
    }

    @JsonIgnore
    public boolean isValid(List<WHPErrorCode> validationErrors) {
        boolean isFilled = address_location != null
                && address_block != null
                && address_village != null
                && address_district != null
                && address_state != null;
        if (!isFilled) {
            validationErrors.add(WHPErrorCode.NULL_VALUE_IN_ADDRESS);
        }
        return isFilled;
    }

    @JsonIgnore
    public boolean isEmpty() {
        return address_location == null
                && address_block == null
                && address_village == null
                && address_district == null
                && address_state == null;
    }
}
