package org.motechproject.whp.common.builder;

import org.motechproject.whp.common.service.AdherencePropertyValues;

import static java.util.Arrays.asList;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.motechproject.model.DayOfWeek.*;

public class DefaultAdherencePropertyValues {

    public static AdherencePropertyValues build(){
        AdherencePropertyValues adherencePropertyValues = mock(AdherencePropertyValues.class);
        when(adherencePropertyValues.validAdherenceDays()).thenReturn(asList(Sunday, Monday, Tuesday));
        return adherencePropertyValues;
    }
}
