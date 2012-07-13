package org.motechproject.whp.common.util;

import org.junit.Test;

import static junit.framework.Assert.assertEquals;

public class MathUtilTest {

    @Test
    public void shouldRoundFirstDecimalValue() {

        float number1 = (float) 2.34;
        float roundedNumber1 = MathUtil.roundToFirstDecimal(number1);


        float number2 = (float) 2.35;
        float roundedNumber2 = MathUtil.roundToFirstDecimal(number2);


        float number3 = (float) 2.36;
        float roundedNumber3 = MathUtil.roundToFirstDecimal(number3);

        float number4 = (float) 2.96;
        float roundedNumber4 = MathUtil.roundToFirstDecimal(number4);

        float number5 = (float) 2.04;
        float roundedNumber5 = MathUtil.roundToFirstDecimal(number5);

        float roundedNumber6 = MathUtil.roundToFirstDecimal(0);

        assertEquals((float)2.3, roundedNumber1);
        assertEquals((float)2.3, roundedNumber2);
        assertEquals((float)2.4, roundedNumber3);
        assertEquals((float)3.0, roundedNumber4);
        assertEquals((float)2.0, roundedNumber5);
        assertEquals((float)0.0, roundedNumber6);
    }

}
