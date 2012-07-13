package org.motechproject.whp.controller;

import com.google.gson.Gson;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.motechproject.whp.adherence.request.DailyAdherenceRequest;
import org.motechproject.whp.adherence.request.UpdateAdherenceRequest;
import org.motechproject.whp.adherence.service.WHPAdherenceService;
import org.motechproject.whp.applicationservice.orchestrator.PhaseUpdateOrchestrator;
import org.motechproject.whp.patient.builder.PatientBuilder;
import org.motechproject.whp.patient.domain.Patient;
import org.motechproject.whp.patient.repository.AllPatients;
import org.motechproject.whp.treatmentcard.domain.TreatmentCard;
import org.motechproject.whp.treatmentcard.service.TreatmentCardService;
import org.springframework.ui.Model;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

import static java.util.Arrays.asList;
import static junit.framework.Assert.assertEquals;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.initMocks;

public class TreatmentCardControllerTest {

    @Mock
    TreatmentCardService treatmentCardService;
    @Mock
    WHPAdherenceService adherenceService;

    @Mock
    AllPatients allPatients;
    @Mock
    Model uiModel;
    @Mock
    HttpServletRequest request;
    @Mock
    PhaseUpdateOrchestrator phaseUpdateOrchestrator;

    Patient patient;

    TreatmentCardController treatmentCardController;

    @Before
    public void setup() {
        initMocks(this);
        treatmentCardController = new TreatmentCardController(adherenceService, treatmentCardService, allPatients, phaseUpdateOrchestrator);
        patient = new PatientBuilder().withDefaults().build();
        when(allPatients.findByPatientId(patient.getPatientId())).thenReturn(patient);
    }

    @Test
    public void shouldReturnTreatmentCardModelToView() {
        TreatmentCard treatmentCard = new TreatmentCard(patient);
        when(treatmentCardService.treatmentCard(patient)).thenReturn(treatmentCard);

        String view = treatmentCardController.show(patient.getPatientId(), uiModel);

        verify(uiModel).addAttribute("patientId", patient.getPatientId());
        verify(uiModel).addAttribute("treatmentCard", treatmentCard);
        assertEquals("treatmentcard/show", view);
    }

    @Test
    public void shouldSaveAdherenceData() throws IOException {
        UpdateAdherenceRequest adherenceData = new UpdateAdherenceRequest();
        adherenceData.setPatientId("test");
        DailyAdherenceRequest dailyAdherenceRequest1 = new DailyAdherenceRequest(6, 7, 2012, 1);
        DailyAdherenceRequest dailyAdherenceRequest2 = new DailyAdherenceRequest(13, 8, 2012, 2);
        adherenceData.setDailyAdherenceRequests(asList(dailyAdherenceRequest1, dailyAdherenceRequest2));

        when(allPatients.findByPatientId(adherenceData.getPatientId())).thenReturn(patient);

        String view = treatmentCardController.update(new Gson().toJson(adherenceData), request);

        assertEquals("redirect:/patients/show?patientId=patientid", view);
        verify(adherenceService, times(1)).addLogsForPatient(adherenceData, patient);
    }

    @Test
    public void shouldRecomputePillCountAndAttemptTransitionWhenSavingAdherenceData() {
        UpdateAdherenceRequest adherenceData = new UpdateAdherenceRequest();
        adherenceData.setPatientId("test");

        when(allPatients.findByPatientId(adherenceData.getPatientId())).thenReturn(patient);

        treatmentCardController.update(new Gson().toJson(adherenceData), request);

        verify(phaseUpdateOrchestrator, times(1)).recomputePillStatus(adherenceData.getPatientId());
        verify(phaseUpdateOrchestrator, times(1)).attemptPhaseTransition(adherenceData.getPatientId());
    }

}
