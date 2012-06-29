package org.motechproject.whp.patient.domain;

import lombok.Data;
import org.apache.commons.lang.StringUtils;
import org.codehaus.jackson.annotate.JsonIgnore;
import org.motechproject.whp.common.exception.WHPErrorCode;

import java.util.ArrayList;
import java.util.List;

import static org.apache.commons.lang.StringUtils.isNotBlank;

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

    public String toString() {
        List<String> address = new ArrayList<>();

        addIfNotNull(address, address_location);
        addIfNotNull(address, address_landmark);
        addIfNotNull(address, address_block);
        addIfNotNull(address, address_village);
        addIfNotNull(address, address_district);
        addIfNotNull(address, address_state);

        return StringUtils.join(address,", ");
    }

    private void addIfNotNull(List<String> address, String element) {
        if(isNotBlank(element))
            address.add(element);
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
