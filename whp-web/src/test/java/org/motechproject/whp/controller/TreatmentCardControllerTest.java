package org.motechproject.whp.controller;

import com.google.gson.Gson;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.motechproject.whp.adherence.audit.contract.AuditParams;
import org.motechproject.whp.adherence.domain.AdherenceSource;
import org.motechproject.whp.adherence.request.DailyAdherenceRequest;
import org.motechproject.whp.adherence.request.UpdateAdherenceRequest;
import org.motechproject.whp.adherence.service.WHPAdherenceService;
import org.motechproject.whp.applicationservice.orchestrator.TreatmentUpdateOrchestrator;
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

public class TreatmentCardControllerTest extends BaseControllerTest {

    @Mock
    TreatmentCardService treatmentCardService;
    @Mock
    AllPatients allPatients;
    @Mock
    Model uiModel;
    @Mock
    HttpServletRequest request;
    @Mock
    TreatmentUpdateOrchestrator treatmentUpdateOrchestrator;

    private Patient patient;
    private static final String USER_NAME = "username";


    private TreatmentCardController treatmentCardController;
    private final AuditParams auditParams = new AuditParams(USER_NAME, AdherenceSource.WEB, null);


    @Before
    public void setup() {
        initMocks(this);
        treatmentCardController = new TreatmentCardController(treatmentCardService, allPatients, treatmentUpdateOrchestrator);
        patient = new PatientBuilder().withDefaults().build();
        when(allPatients.findByPatientId(patient.getPatientId())).thenReturn(patient);
        setupLoggedInUser(request, USER_NAME);
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
        verify(treatmentUpdateOrchestrator).recordDailyAdherence(adherenceData.getDailyAdherenceRequests(), patient, auditParams);
    }

}
