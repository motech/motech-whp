package org.motechproject.whp.functional.data;

import org.apache.commons.lang.StringUtils;
import org.openqa.selenium.WebElement;

import java.util.List;

import static java.util.Arrays.asList;

public class AdherenceValue {

    public static enum Value {Tick, Circle}

    public static Value getCurrentValue(WebElement cell) {
        String cssClass = cell.getAttribute("class");
        if (StringUtils.containsIgnoreCase(cssClass, "tick-icon")) {
            return Value.Tick;
        } else if (StringUtils.containsIgnoreCase(cssClass, "round-icon")) {
            return Value.Circle;
        }
        return null;
    }

    public static Integer getDistanceInNumberOfClicks(Value currentValue, Value desiredValue) {
        List<Value> values = asList(Value.values());
        if (currentValue == null) {
            return values.indexOf(desiredValue) + 1;
        }
        int indexOfCurrentValue = values.indexOf(currentValue);
        int indexOfDesiredValue = values.indexOf(desiredValue);
        return positiveModulo((indexOfDesiredValue - indexOfCurrentValue), values.size());
    }

    private static int positiveModulo(int numerator, int denominator) {
        return ((numerator % denominator) + denominator) % denominator;
    }
}
