package org.motechproject.whp.providerreminder.model;

import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.List;

@EqualsAndHashCode
public class ProviderReminderRequest implements Serializable {

    private String reminderType;
    private List<String> msisdns;

    public ProviderReminderRequest(String reminderType, List<String> msisdns) {
        this.reminderType = reminderType;
        this.msisdns = msisdns;
    }
}
