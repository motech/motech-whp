package org.motechproject.whp.common.util;

public class MathUtil {

    public static float roundToFirstDecimal(float number){
        double temp = number * 10.0;
        long round = Math.round(temp);
        temp = round / 10.0;
        return (float) temp;
    }

}
