package org.motechproject.whp.importer.csv;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.motechproject.whp.patient.domain.Patient;
import org.motechproject.whp.patient.domain.Provider;
import org.motechproject.whp.patient.repository.AllPatients;
import org.motechproject.whp.patient.repository.AllProviders;
import org.motechproject.whp.patient.repository.AllTreatments;
import org.motechproject.whp.patient.repository.SpringIntegrationTest;
import org.motechproject.whp.refdata.domain.PatientType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath*:/applicationDataImporterContext.xml")
public class CsvImporterTest extends SpringIntegrationTest {
    @Autowired
    AllPatients allPatients;
    @Autowired
    AllProviders allProviders;
    @Autowired
    AllTreatments allTreatments;

    @Before
    @After
    public void cleanDb() {
        allPatients.removeAll();
        allTreatments.removeAll();

        if (allProviders.findByProviderId("john") != null)
            markForDeletion(allProviders.findByProviderId("john"));
        if (allProviders.findByProviderId("raj") != null)
            markForDeletion(allProviders.findByProviderId("raj"));
    }

    @Test
    public void shouldStoreproviderData() {
        String[] arguments = new String[2];
        arguments[0] = "provider";
        arguments[1] = "providerRecords.csv";
        CsvImporter.main(arguments);

        Provider provider1 = allProviders.findByProviderId("john");
        Provider provider2 = allProviders.findByProviderId("raj");
        assertNotNull(provider1);
        assertNotNull(provider2);
        assertEquals(0, allPatients.getAll().size());
        assertEquals(0, allTreatments.getAll().size());

    }

    @Test
    public void shouldStorePatientDataWithDefaultValues() {
        String[] arguments = new String[2];
        arguments[0] = "provider";
        arguments[1] = "providerRecords.csv";
        CsvImporter.main(arguments);

        arguments[0] = "patient";
        arguments[1] = "patientRecords.csv";
        CsvImporter.main(arguments);
        assertEquals(2, allPatients.getAll().size());
        assertEquals(2, allTreatments.getAll().size());
        assertEquals(PatientType.New, allPatients.findByPatientId("12345").getCurrentProvidedTreatment().getTreatment().getPatientType());
        Patient patient2 = allPatients.findByPatientId("234324");
        assertEquals(patient2.getLastModifiedDate().toLocalDate(), patient2.getCurrentProvidedTreatment().getTreatment().getWeightInstances().get(0).getMeasuringDate());

    }

}
