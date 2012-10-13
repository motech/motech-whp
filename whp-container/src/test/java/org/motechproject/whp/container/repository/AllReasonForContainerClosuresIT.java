package org.motechproject.whp.container.repository;

import org.junit.After;
import org.junit.Test;
import org.motechproject.whp.common.util.SpringIntegrationTest;
import org.motechproject.whp.container.domain.ReasonForContainerClosure;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;

import java.util.List;

import static org.junit.Assert.assertEquals;

@ContextConfiguration(locations = "classpath*:/applicationContainerContext.xml")
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
}
