package org.motechproject.whp.patientalerts.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.kubek2k.springockito.annotations.ReplaceWithMock;
import org.kubek2k.springockito.annotations.SpringockitoContextLoader;
import org.motechproject.whp.patient.domain.Patient;
import org.motechproject.whp.patientalerts.processor.AdherenceMissingAlertProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(loader = SpringockitoContextLoader.class, locations = "classpath*:/applicationPatientAlertsContext.xml")
public class AllAlertProcessorsIT {

    @ReplaceWithMock
    @Autowired
    private AdherenceMissingAlertProcessor adherenceMissingAlertProcessor;

    @Autowired
    private AllAlertProcessors allAlertProcessors;

    @Test
    public void shouldApplyAllProcessors() {
        Patient patient = mock(Patient.class);
        allAlertProcessors.process(patient);

        verify(adherenceMissingAlertProcessor).process(patient);
    }

}
