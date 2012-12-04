package org.motechproject.whp.it.container.repository;

import org.junit.After;
import org.junit.Test;
import org.motechproject.whp.common.util.SpringIntegrationTest;
import org.motechproject.whp.container.domain.ReasonForContainerClosure;
import org.motechproject.whp.container.repository.AllReasonForContainerClosures;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;

import java.util.List;

import static java.util.Arrays.asList;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.motechproject.whp.container.domain.ReasonForContainerClosure.ApplicableTreatmentPhase.*;

@ContextConfiguration(locations = "classpath*:/applicationITContext.xml")
public class AllReasonForContainerClosuresIT extends SpringIntegrationTest {

    @Autowired
    AllReasonForContainerClosures allReasonForContainerClosures;

    @After
    public void setup() {
        allReasonForContainerClosures.removeAll();
    }

    @Test
    public void shouldStoreAndGetAllContainerClosureReasons() {
        allReasonForContainerClosures.addOrReplace(new ReasonForContainerClosure("reason number one", "1"));
        allReasonForContainerClosures.addOrReplace(new ReasonForContainerClosure("reason number two", "2"));
        allReasonForContainerClosures.addOrReplace(new ReasonForContainerClosure("reason number one", "1"));

        List<ReasonForContainerClosure> allReasons = allReasonForContainerClosures.getAll();

        assertEquals(2, allReasons.size());
        assertEquals("reason number one", allReasons.get(0).getName());
        assertEquals("reason number two", allReasons.get(1).getName());
        assertEquals("1", allReasons.get(0).getCode());
        assertEquals("2", allReasons.get(1).getCode());
    }

    @Test
    public void shouldHaveReasonsForClosureApplicableForTreatmentPhase() {
        ReasonForContainerClosure preTreatmentReason = new ReasonForContainerClosure("reason number one", "1", PreTreatment, true);
        ReasonForContainerClosure inTreatmentReason = new ReasonForContainerClosure("reason number two", "2", InTreatment, true);

        allReasonForContainerClosures.addOrReplace(preTreatmentReason);
        allReasonForContainerClosures.addOrReplace(inTreatmentReason);

        assertEquals(asList(preTreatmentReason), allReasonForContainerClosures.withTreatmentPhase(PreTreatment));
        assertEquals(asList(inTreatmentReason), allReasonForContainerClosures.withTreatmentPhase(InTreatment));
    }

    @Test
    public void shouldHaveReasonsForClosureApplicableForAllTreatmentPhase() {
        ReasonForContainerClosure reasonApplicableToAllPhases = new ReasonForContainerClosure("reason number one", "1", All, true);

        allReasonForContainerClosures.addOrReplace(reasonApplicableToAllPhases);

        assertEquals(asList(reasonApplicableToAllPhases), allReasonForContainerClosures.withTreatmentPhase(PreTreatment));
        assertEquals(asList(reasonApplicableToAllPhases), allReasonForContainerClosures.withTreatmentPhase(InTreatment));
    }

    @Test
    public void shouldHaveReasonsForClosureNotApplicableForTreatmentPhase() {
        ReasonForContainerClosure reason = new ReasonForContainerClosure("reason number one", "1", PreTreatment, true);

        allReasonForContainerClosures.addOrReplace(reason);

        assertTrue(allReasonForContainerClosures.withTreatmentPhase(InTreatment).isEmpty());
    }

    @Test
    public void shouldHaveReasonsForClosureApplicableToAdmin() {
        ReasonForContainerClosure reasonApplicableToAdmin = new ReasonForContainerClosure("reason number one", "1", PreTreatment, true);
        ReasonForContainerClosure reasonNotApplicableToAdmin = new ReasonForContainerClosure("reason number two", "2", PreTreatment, false);

        allReasonForContainerClosures.addOrReplace(reasonApplicableToAdmin);
        allReasonForContainerClosures.addOrReplace(reasonNotApplicableToAdmin);

        assertEquals(asList(reasonApplicableToAdmin), allReasonForContainerClosures.withApplicableToAdmin(true));
        assertEquals(asList(reasonNotApplicableToAdmin), allReasonForContainerClosures.withApplicableToAdmin(false));
    }

    @Test
    public void shouldHaveReasonsForClosureNotApplicableToAdmin() {
        ReasonForContainerClosure reason = new ReasonForContainerClosure("reason number one", "1", PreTreatment, false);

        allReasonForContainerClosures.addOrReplace(reason);

        assertTrue(allReasonForContainerClosures.withApplicableToAdmin(true).isEmpty());
    }

    @Test
    public void shouldHaveReasonsForClosureApplicableToAdminUnderTreatmentPhase() {
        ReasonForContainerClosure reasonApplicableToAdmin = new ReasonForContainerClosure("reason number one", "1", PreTreatment, true);
        ReasonForContainerClosure reasonNotApplicableToAdmin = new ReasonForContainerClosure("reason number two", "2", InTreatment, false);

        allReasonForContainerClosures.addOrReplace(reasonApplicableToAdmin);
        allReasonForContainerClosures.addOrReplace(reasonNotApplicableToAdmin);

        assertEquals(asList(reasonApplicableToAdmin), allReasonForContainerClosures.withApplicableToAdminAndWithPhase(true, PreTreatment));
        assertEquals(asList(reasonNotApplicableToAdmin), allReasonForContainerClosures.withApplicableToAdminAndWithPhase(false, InTreatment));
    }

    @Test
    public void shouldHaveReasonsForClosureApplicableToAdminUnderAllPhases() {
        ReasonForContainerClosure reasonApplicableToAdmin = new ReasonForContainerClosure("reason number one", "1", All, true);

        allReasonForContainerClosures.addOrReplace(reasonApplicableToAdmin);

        assertEquals(asList(reasonApplicableToAdmin), allReasonForContainerClosures.withApplicableToAdminAndWithPhase(true, PreTreatment));
        assertEquals(asList(reasonApplicableToAdmin), allReasonForContainerClosures.withApplicableToAdminAndWithPhase(true, InTreatment));
    }

    @Test
    public void shouldHaveReasonsForClosureNotApplicableToAdminUnderTreatmentPhase() {
        ReasonForContainerClosure reasonNotApplicableToAdmin = new ReasonForContainerClosure("reason number one", "1", PreTreatment, false);
        ReasonForContainerClosure reasonNotApplicableToPhase = new ReasonForContainerClosure("reason number two", "2", InTreatment, true);

        allReasonForContainerClosures.addOrReplace(reasonNotApplicableToAdmin);
        allReasonForContainerClosures.addOrReplace(reasonNotApplicableToPhase);

        assertTrue(allReasonForContainerClosures.withApplicableToAdminAndWithPhase(true, PreTreatment).isEmpty());
        assertTrue(allReasonForContainerClosures.withApplicableToAdminAndWithPhase(false, InTreatment).isEmpty());
    }
}
