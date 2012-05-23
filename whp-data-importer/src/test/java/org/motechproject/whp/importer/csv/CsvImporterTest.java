package org.motechproject.whp.importer.csv;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.motechproject.whp.patient.repository.AllPatients;
import org.motechproject.whp.patient.repository.AllProviders;
import org.motechproject.whp.patient.repository.AllTreatments;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static junit.framework.Assert.assertEquals;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath*:/applicationDataImporterContext.xml")
public class CsvImporterTest {
    @Autowired
    AllPatients allPatients;
    @Autowired
    AllProviders allProviders;
    @Autowired
    AllTreatments allTreatments;

    @Before
    @After
    public void cleanDb() {
       allProviders.removeAll();
       allPatients.removeAll();
       allTreatments.removeAll();
    }
    @Test
    public void shouldStoreproviderData(){
        String[] arguments = new String[2];
        arguments[0] = "provider";
        arguments[1] = "providerRecords.csv";
        CsvImporter.main(arguments);

        assertEquals(2, allProviders.getAll().size());
        assertEquals(0, allPatients.getAll().size());
        assertEquals(0, allTreatments.getAll().size());
    }

    @Test
    public void shouldStorePatientData() {
        String[] arguments = new String[2];
        arguments[0] = "provider";
        arguments[1] = "providerRecords.csv";
        CsvImporter.main(arguments);

        arguments[0] = "patient";
        arguments[1] = "patientRecords.csv";
        CsvImporter.main(arguments);
        assertEquals(2, allProviders.getAll().size());
        assertEquals(2, allPatients.getAll().size());
        assertEquals(2, allTreatments.getAll().size());
    }
}
