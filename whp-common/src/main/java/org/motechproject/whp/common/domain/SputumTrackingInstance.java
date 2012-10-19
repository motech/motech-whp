package org.motechproject.whp.common.domain;

import java.util.ArrayList;
import java.util.List;

import static java.util.Arrays.asList;

public enum SputumTrackingInstance {
    PreTreatment("Pre-treatment"), InTreatment("In-treatment"), EndIP("End of Intensive Phase"), ExtendedIP("Extended IP"), TwoMonthsIntoCP("Two Months Into CP"), EndTreatment("End Treatment");;

    public static SputumTrackingInstance[] REGISTRATION_INSTANCES = {PreTreatment, InTreatment};
    public static SputumTrackingInstance[] MAPPING_INSTANCES = {PreTreatment, EndIP, ExtendedIP, TwoMonthsIntoCP, EndTreatment};
    public static SputumTrackingInstance[] IN_TREATMENT_INSTANCES = {EndIP, ExtendedIP, TwoMonthsIntoCP, EndTreatment};

    private String displayText;

    SputumTrackingInstance(String displayText) {
        this.displayText = displayText;
    }

    public String getDisplayText() {
        return displayText;
    }

    public static SputumTrackingInstance getInstanceForValue(String value) {
        for(SputumTrackingInstance instance : SputumTrackingInstance.values()) {
            if(instance.getDisplayText().equalsIgnoreCase(value) || instance.name().equalsIgnoreCase(value))
                return instance;
        }
        return null;
    }

    public static boolean isValidRegistrationInstance(String text) {
        return isValid(text, REGISTRATION_INSTANCES);
    }

    public static boolean isValidMappingInstance(String text) {
        return isValid(text, MAPPING_INSTANCES);
    }

    public static SputumTrackingInstance getInstanceByName(String name) {
        for(SputumTrackingInstance instance : SputumTrackingInstance.values()) {
            if(instance.name().equals(name))
                return instance;
        }
        return null;
    }

    public static SputumTrackingInstance getTrackingInstanceType(SputumTrackingInstance mappingInstance) {
        return asList(IN_TREATMENT_INSTANCES).contains(mappingInstance) ? InTreatment : PreTreatment;
    }

    public static List<String> allInTreatmentInstanceNames() {
        List<String> names = new ArrayList<>();
        for(SputumTrackingInstance value : IN_TREATMENT_INSTANCES) {
            names.add(value.name());
        }
        return names;
    }

    private static boolean isValid(String text, SputumTrackingInstance[] instances) {
        for(SputumTrackingInstance sputumTrackingInstance : instances)
            if(sputumTrackingInstance.getDisplayText().equalsIgnoreCase(text) || sputumTrackingInstance.name().equalsIgnoreCase(text))
                return true;
        return false;
    }
}