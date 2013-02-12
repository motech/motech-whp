package org.motechproject.whp.uimodel;

import org.motechproject.whp.common.domain.alerts.ColorConfiguration;
import org.motechproject.whp.common.domain.alerts.PatientAlertType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class PatientDashboardLegends {

    private List<Legend> legends = new ArrayList<>(10);

    @Autowired
    public PatientDashboardLegends(ColorConfiguration colorConfiguration) {
        createLegends(colorConfiguration);

    }

    private void createLegends(ColorConfiguration colorConfiguration) {
        legends.add(new Legend("pink", "message.current.treatment.paused"));
        legends.add(new Legend(colorConfiguration.getColorFor(PatientAlertType.AdherenceMissing, 1), "message.alert.filter.adherence.missing.severity.one.alerts"));
        legends.add(new Legend(colorConfiguration.getColorFor(PatientAlertType.AdherenceMissing, 2), "message.alert.filter.adherence.missing.severity.two.alerts"));
        legends.add(new Legend(colorConfiguration.getColorFor(PatientAlertType.AdherenceMissing, 3), "message.alert.filter.adherence.missing.severity.three.alerts"));
        legends.add(new Legend(colorConfiguration.getColorFor(PatientAlertType.CumulativeMissedDoses, 1), "message.alert.filter.cumulative.missed.dose.alerts"));
        legends.add(new Legend(colorConfiguration.getColorFor(PatientAlertType.TreatmentNotStarted, 1), "message.alert.filter.treatment.not.started.alerts"));
    }

    public List<Legend> getLegends() {
        return new ArrayList<>(legends);
    }
}
