package org.motechproject.whp.common.domain;

import static java.util.Arrays.asList;

public enum RegistrationInstance {
    PreTreatment("Pre-treatment"), InTreatment("In-treatment");

    private String displayText;

    RegistrationInstance(String displayText) {
        this.displayText = displayText;
    }

    public String getDisplayText() {
        return displayText;
    }

    public static boolean isValidRegistrationInstance(String text) {
        return isValid(text);
    }

    public static boolean isValidRegistrationInstanceName(String instance) {
        return isValidInstanceName(instance);
    }

    public static RegistrationInstance getTrackingInstanceType(SputumTrackingInstance mappingInstance) {
        return asList(SputumTrackingInstance.IN_TREATMENT_INSTANCES).contains(mappingInstance) ? InTreatment : PreTreatment;
    }

    public static RegistrationInstance getInstanceForValue(String value) {
        for(RegistrationInstance instance : RegistrationInstance.values()) {
            if(instance.getDisplayText().equalsIgnoreCase(value) || instance.name().equalsIgnoreCase(value))
                return instance;
        }
        return null;
    }

    private static boolean isValidInstanceName(String text) {
        for(RegistrationInstance registrationInstance : values())
            if(registrationInstance.name().equalsIgnoreCase(text))
                return true;
        return false;
    }

    private static boolean isValid(String text) {
        for(RegistrationInstance registrationInstance : values())
            if(registrationInstance.getDisplayText().equalsIgnoreCase(text) || registrationInstance.name().equalsIgnoreCase(text))
                return true;
        return false;
    }
}
