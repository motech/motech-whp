package org.motechproject.whp.patient.alerts.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.kubek2k.springockito.annotations.ReplaceWithMock;
import org.kubek2k.springockito.annotations.SpringockitoContextLoader;
import org.motechproject.whp.common.domain.alerts.AllAlertConfigurations;
import org.motechproject.whp.common.domain.alerts.PatientAlertType;
import org.motechproject.whp.patient.alerts.processor.AdherenceMissingAlertProcessor;
import org.motechproject.whp.patient.domain.Patient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(loader = SpringockitoContextLoader.class, locations = "classpath*:/applicationPatientContext.xml")
public class AllAlertProcessorsIT {
    @ReplaceWithMock
    @Autowired
    private AdherenceMissingAlertProcessor adherenceMissingAlertProcessor;
    @ReplaceWithMock
    @Autowired
    private AllAlertConfigurations allAlertConfigurations;

    @Autowired
    private AllAlertProcessors allAlertProcessors;

    @Test
    public void shouldApplyAllProcessors() {
        Patient patient = mock(Patient.class);
        when(allAlertConfigurations.shouldRunToday(any(PatientAlertType.class))).thenReturn(true);

        allAlertProcessors.processAll(patient);

        verify(adherenceMissingAlertProcessor).process(patient);
    }

}
