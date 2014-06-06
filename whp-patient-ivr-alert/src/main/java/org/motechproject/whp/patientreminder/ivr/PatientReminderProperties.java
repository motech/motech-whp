package org.motechproject.whp.patientreminder.ivr;

import lombok.Getter;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
@Component
@Getter
public class PatientReminderProperties {
	@Value("#{patientReminderProperty['patient.reminder.batch.size']}")
    private int batchSize;

    @Value("#{patientReminderProperty['patient.reminder.url']}")
    private String providerReminderUrl;
}
