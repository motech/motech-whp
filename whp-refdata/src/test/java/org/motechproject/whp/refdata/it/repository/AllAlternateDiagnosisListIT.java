package org.motechproject.whp.refdata.it.repository;

import org.junit.After;
import org.junit.Test;
import org.motechproject.whp.common.util.SpringIntegrationTest;
import org.motechproject.whp.refdata.domain.AlternateDiagnosisList;
import org.motechproject.whp.refdata.repository.AllAlternateDiagnosisList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;

import static org.junit.Assert.assertEquals;

@ContextConfiguration(locations = "classpath*:/applicationRefDataContext.xml")
public class AllAlternateDiagnosisListIT extends SpringIntegrationTest {

    @Autowired
    AllAlternateDiagnosisList allAlternateDiagnosisList;

    @After
    public void setup() {
        allAlternateDiagnosisList.removeAll();
    }

    @Test
    public void shouldStoreAndGetAllContainerClosureReasons() {
        allAlternateDiagnosisList.addOrReplace(new AlternateDiagnosisList("sickness number one", "code1"));
        allAlternateDiagnosisList.addOrReplace(new AlternateDiagnosisList("sickness number two", "code2"));
        allAlternateDiagnosisList.addOrReplace(new AlternateDiagnosisList("sickness number one", "code1"));
        assertEquals(2, allAlternateDiagnosisList.getAll().size());
        assertEquals("code1", allAlternateDiagnosisList.getAll().get(0).getCode());
        assertEquals("code2", allAlternateDiagnosisList.getAll().get(1).getCode());
    }
}
