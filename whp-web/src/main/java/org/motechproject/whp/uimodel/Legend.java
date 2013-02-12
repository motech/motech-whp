package org.motechproject.whp.uimodel;

import lombok.Data;

@Data
public class Legend {
    String color;
    String displayText;

    public Legend(String color, String displayText) {
        this.color = color;
        this.displayText = displayText;
    }
}
