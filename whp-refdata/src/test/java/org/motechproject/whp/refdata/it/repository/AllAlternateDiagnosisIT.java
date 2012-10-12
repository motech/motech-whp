package org.motechproject.whp.refdata.it.repository;

import org.junit.After;
import org.junit.Test;
import org.motechproject.whp.common.util.SpringIntegrationTest;
import org.motechproject.whp.refdata.domain.AlternateDiagnosis;
import org.motechproject.whp.refdata.repository.AllAlternateDiagnosis;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;

import static org.junit.Assert.assertEquals;

@ContextConfiguration(locations = "classpath*:/applicationRefDataContext.xml")
public class AllAlternateDiagnosisIT extends SpringIntegrationTest {

    @Autowired
    AllAlternateDiagnosis allAlternateDiagnosis;

    @After
    public void setup() {
        allAlternateDiagnosis.removeAll();
    }

    @Test
    public void shouldStoreAndGetAllContainerClosureReasons() {
        allAlternateDiagnosis.addOrReplace(new AlternateDiagnosis("sickness number one", "code1"));
        allAlternateDiagnosis.addOrReplace(new AlternateDiagnosis("sickness number two", "code2"));
        allAlternateDiagnosis.addOrReplace(new AlternateDiagnosis("sickness number one", "code1"));
        assertEquals(2, allAlternateDiagnosis.getAll().size());
        assertEquals("code1", allAlternateDiagnosis.getAll().get(0).getCode());
        assertEquals("code2", allAlternateDiagnosis.getAll().get(1).getCode());
    }
}
