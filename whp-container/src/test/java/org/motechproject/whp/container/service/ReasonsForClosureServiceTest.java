package org.motechproject.whp.container.service;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.motechproject.whp.container.WHPContainerConstants;
import org.motechproject.whp.container.domain.ReasonForContainerClosure;
import org.motechproject.whp.container.repository.AllReasonForContainerClosures;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;
import static org.motechproject.whp.container.domain.ReasonForContainerClosure.ApplicableTreatmentPhase.InTreatment;
import static org.motechproject.whp.container.domain.ReasonForContainerClosure.ApplicableTreatmentPhase.PreTreatment;

public class ReasonsForClosureServiceTest {

    ReasonsForClosureService reasonsForClosureService;

    @Mock
    AllReasonForContainerClosures allReasonForContainerClosures;

    @Before
    public void setUp() throws Exception {
        initMocks(this);
        reasonsForClosureService = new ReasonsForClosureService(allReasonForContainerClosures);
    }

    @Test
    public void shouldGetAllReasonsForClosureForPreTreatment() {
        ArrayList<ReasonForContainerClosure> reasonForContainerClosures = new ArrayList<>();
        reasonForContainerClosures.add(new ReasonForContainerClosure("reason number one", "0"));
        reasonForContainerClosures.add(new ReasonForContainerClosure("reason number two", "2"));
        reasonForContainerClosures.add(new ReasonForContainerClosure("reason number three", "3"));
        when(allReasonForContainerClosures.withTreatmentPhase(PreTreatment)).thenReturn(reasonForContainerClosures);

        List<ReasonForContainerClosure> allClosureReasonsForAdmin = reasonsForClosureService.getAllReasonsPreTreatmentClosureReasons();

        assertEquals(3, allClosureReasonsForAdmin.size());
        assertEquals("reason number one", allClosureReasonsForAdmin.get(0).getName());
        assertEquals("reason number two", allClosureReasonsForAdmin.get(1).getName());
        assertEquals("reason number three", allClosureReasonsForAdmin.get(2).getName());
        assertEquals("0", allClosureReasonsForAdmin.get(0).getCode());
        assertEquals("2", allClosureReasonsForAdmin.get(1).getCode());
        assertEquals("3", allClosureReasonsForAdmin.get(2).getCode());
    }

    @Test
    public void shouldGetAllReasonsForInTreatment() {
        ArrayList<ReasonForContainerClosure> reasonForContainerClosures = new ArrayList<>();
        reasonForContainerClosures.add(new ReasonForContainerClosure("reason number one", "0"));
        reasonForContainerClosures.add(new ReasonForContainerClosure("reason number three", "3"));
        when(allReasonForContainerClosures.withTreatmentPhase(InTreatment)).thenReturn(reasonForContainerClosures);

        List<ReasonForContainerClosure> allClosureReasonsForAdmin = reasonsForClosureService.getAllInTreatmentClosureReasons();

        assertEquals(2, allClosureReasonsForAdmin.size());
        assertEquals("reason number one", allClosureReasonsForAdmin.get(0).getName());
        assertEquals("reason number three", allClosureReasonsForAdmin.get(1).getName());
        assertEquals("0", allClosureReasonsForAdmin.get(0).getCode());
        assertEquals("3", allClosureReasonsForAdmin.get(1).getCode());
    }

    @Test
    public void shouldGetAllPreTreatmentContainerClosureReasonsForAdmin() {
        ArrayList<ReasonForContainerClosure> reasonForContainerClosures = new ArrayList<>();
        reasonForContainerClosures.add(new ReasonForContainerClosure("reason number two", "2"));
        reasonForContainerClosures.add(new ReasonForContainerClosure("reason number three", "3"));
        when(allReasonForContainerClosures.withApplicableToAdminAndWithPhase(true, PreTreatment)).thenReturn(reasonForContainerClosures);

        List<ReasonForContainerClosure> allClosureReasonsForAdmin = reasonsForClosureService.getAllPreTreatmentClosureReasonsForAdmin();

        assertEquals(2, allClosureReasonsForAdmin.size());
        assertEquals("reason number two", allClosureReasonsForAdmin.get(0).getName());
        assertEquals("reason number three", allClosureReasonsForAdmin.get(1).getName());
        assertEquals("2", allClosureReasonsForAdmin.get(0).getCode());
        assertEquals("3", allClosureReasonsForAdmin.get(1).getCode());
    }

    @Test
    public void shouldGetAllInTreatmentContainerClosureReasonsForAdmin() {
        ArrayList<ReasonForContainerClosure> reasonForContainerClosures = new ArrayList<>();
        reasonForContainerClosures.add(new ReasonForContainerClosure("reason number two", "2"));
        reasonForContainerClosures.add(new ReasonForContainerClosure("reason number three", "3"));
        when(allReasonForContainerClosures.withTreatmentPhase(InTreatment)).thenReturn(reasonForContainerClosures);

        List<ReasonForContainerClosure> allClosureReasonsForAdmin = reasonsForClosureService.getAllInTreatmentClosureReasons();

        assertEquals(2, allClosureReasonsForAdmin.size());
        assertEquals("reason number two", allClosureReasonsForAdmin.get(0).getName());
        assertEquals("reason number three", allClosureReasonsForAdmin.get(1).getName());
        assertEquals("2", allClosureReasonsForAdmin.get(0).getCode());
        assertEquals("3", allClosureReasonsForAdmin.get(1).getCode());
    }

    @Test
    public void shouldGetContainerClosureReasonForMapping() {
        ReasonForContainerClosure reason = new ReasonForContainerClosure("not for admin", "0");
        when(allReasonForContainerClosures.findByCode(WHPContainerConstants.CLOSURE_DUE_TO_MAPPING)).thenReturn(reason);

        ReasonForContainerClosure closureReasonForMapping = reasonsForClosureService.getClosureReasonForMapping();

        assertEquals("not for admin", closureReasonForMapping.getName());
        assertEquals("0", closureReasonForMapping.getCode());
    }

}
