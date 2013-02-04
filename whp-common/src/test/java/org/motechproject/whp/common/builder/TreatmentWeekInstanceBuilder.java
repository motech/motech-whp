package org.motechproject.whp.common.builder;

import org.motechproject.whp.common.domain.TreatmentWeekInstance;
import org.motechproject.whp.common.service.AdherenceWindow;

public class TreatmentWeekInstanceBuilder {

    public static TreatmentWeekInstance build(){
        AdherenceWindow adherenceWindow = new AdherenceWindow(DefaultAdherencePropertyValues.build());
        return new TreatmentWeekInstance(adherenceWindow);
    }
}
