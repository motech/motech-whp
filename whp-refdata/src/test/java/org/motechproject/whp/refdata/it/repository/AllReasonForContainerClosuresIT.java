package org.motechproject.whp.refdata.it.repository;

import org.junit.After;
import org.junit.Test;
import org.motechproject.whp.common.util.SpringIntegrationTest;
import org.motechproject.whp.refdata.domain.ReasonForContainerClosure;
import org.motechproject.whp.refdata.repository.AllReasonForContainerClosures;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;

import static org.junit.Assert.assertEquals;

@ContextConfiguration(locations = "classpath*:/applicationRefDataContext.xml")
public class AllReasonForContainerClosuresIT extends SpringIntegrationTest {

    @Autowired
    AllReasonForContainerClosures allReasonForContainerClosures;

    @After
    public void setup() {
        allReasonForContainerClosures.removeAll();
    }

    @Test
    public void shouldStoreAndGetAllContainerClosureReasons() {
        allReasonForContainerClosures.addOrReplace(new ReasonForContainerClosure("reason number one"));
        allReasonForContainerClosures.addOrReplace(new ReasonForContainerClosure("reason number two"));
        allReasonForContainerClosures.addOrReplace(new ReasonForContainerClosure("reason number one"));
        assertEquals(2, allReasonForContainerClosures.getAll().size());
        assertEquals("reason number one", allReasonForContainerClosures.getAll().get(0).getName());
        assertEquals("reason number two", allReasonForContainerClosures.getAll().get(1).getName());
    }
}
