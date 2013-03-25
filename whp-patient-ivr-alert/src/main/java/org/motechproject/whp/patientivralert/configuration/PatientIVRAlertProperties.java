package org.motechproject.whp.patientivralert.configuration;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@Getter
public class PatientIVRAlertProperties {
    @Value("#{patientIVRAlertProperty['patient.ivr.batch.size']}")
    private int batchSize;

    @Value("#{patientIVRAlertProperty['patient.ivr.url']}")
    private String patientIVRRequestURL;
}
