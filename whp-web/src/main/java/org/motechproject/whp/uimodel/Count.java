package org.motechproject.whp.uimodel;

import lombok.Data;

@Data
public class Count {

    private String name;
    private String count;

    public Count() {
    }

    public Count(String name, String count) {
        this.name = name;
        this.count = count;
    }
}
