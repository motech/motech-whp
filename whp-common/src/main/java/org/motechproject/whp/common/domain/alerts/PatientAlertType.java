package org.motechproject.whp.common.domain.alerts;

import lombok.Getter;

import static org.motechproject.whp.common.domain.alerts.Notifiable.NO;
import static org.motechproject.whp.common.domain.alerts.Notifiable.YES;

public enum PatientAlertType {
    AdherenceMissing(YES), CumulativeMissedDoses(YES), TreatmentNotStarted(YES), IPProgress(NO), CPProgress(NO);

    @Getter
    private Notifiable notifiable;

    PatientAlertType(Notifiable notifiable) {
        this.notifiable = notifiable;
    }

    public boolean isNotifiable() {
        return getNotifiable() == Notifiable.YES;
    }
}
