package org.motechproject.whp.patient.domain;

import lombok.Data;
import org.codehaus.jackson.annotate.JsonIgnore;

@Data
public class Address {

    private String houseNumber;
    private String landmark;
    private String block;
    private String village;
    private String district;
    private String state;

    public Address() {
    }

    public Address(String houseNumber, String landmark, String block, String village, String district, String state) {
        this.houseNumber = houseNumber;
        this.landmark = landmark;
        this.block = block;
        this.village = village;
        this.district = district;
        this.state = state;
    }

    @JsonIgnore
    public boolean isValid() {
        return houseNumber != null && block != null && village != null && district != null && state != null;
    }

    @JsonIgnore
    public boolean isEmpty() {
        return houseNumber == null && block == null && village == null && district == null && state == null;
    }

}