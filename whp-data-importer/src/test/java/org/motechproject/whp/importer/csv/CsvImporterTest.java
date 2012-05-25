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

import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import static junit.framework.Assert.*;

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
    public void shouldStoreproviderData() throws Exception {
        String[] arguments = new String[3];
        arguments[0] = "provider";
        arguments[1] = getProviderCsv();
        arguments[2] = getLogFilePath();
        CsvImporter.main(arguments);

        Provider provider1 = allProviders.findByProviderId("john");
        Provider provider2 = allProviders.findByProviderId("raj");
        assertNotNull(provider1);
        assertNotNull(provider2);
        assertEquals(0, allPatients.getAll().size());
        assertEquals(0, allTreatments.getAll().size());

    }

    @Test
    public void shouldStorePatientDataWithDefaultValues() throws Exception {
        String[] arguments = new String[3];
        arguments[0] = "provider";
        arguments[1] = getProviderCsv();
        arguments[2] = getLogFilePath();
        CsvImporter.main(arguments);

        arguments[0] = "patient";
        arguments[1] = getPatientCsv();
        arguments[2] = getLogFilePath();
        CsvImporter.main(arguments);
        assertEquals(2, allPatients.getAll().size());
        assertEquals(2, allTreatments.getAll().size());
        assertEquals(PatientType.New, allPatients.findByPatientId("12345").getCurrentProvidedTreatment().getTreatment().getPatientType());
        Patient patient2 = allPatients.findByPatientId("234324");
        assertEquals(patient2.getLastModifiedDate().toLocalDate(), patient2.getCurrentProvidedTreatment().getTreatment().getWeightInstances().get(0).getMeasuringDate());

    }

    @Test(expected = RuntimeException.class)
    public void shouldThrowExceptionForInvalidLogFile() throws Exception {
        String[] arguments = new String[3];
        arguments[0] = "provider";
        arguments[1] = getProviderCsv();
        arguments[2] = getLogDir() + "/impor\\/ter5.log";
        CsvImporter.main(arguments);
    }

    @Test(expected = RuntimeException.class)
    public void shouldThrowExceptionForInvalidImportFile() throws Exception {
        String[] arguments = new String[3];
        arguments[0] = "provider";
        arguments[1] = getLogDir() + "invalidFile.csv";
        arguments[2] = getLogFilePath();
        CsvImporter.main(arguments);
    }

    @Test
    public void shouldLogInGivenPath() throws Exception {
        File logFile = new File(getLogDir() + "logCheck.log");
        logFile.delete();
        assertFalse(logFile.exists());

        String[] arguments = new String[3];
        arguments[0] = "provider";
        arguments[1] = getProviderCsv();
        arguments[2] = logFile.getAbsolutePath();
        CsvImporter.main(arguments);
        assertTrue(logFile.exists());
        FileReader reader = new FileReader(logFile.getAbsoluteFile());
        assertNotSame(-1, reader.read());
    }
    @Test
    public void shouldLogInSpecifiedLogPathEvenIfExceptionOccurs() throws IOException {
        File logFile = new File(getLogDir() + "logCheck1.log");
        logFile.delete();
        assertFalse(logFile.exists());

        String[] arguments = new String[3];
        arguments[0] = "provider";
        arguments[1] = getLogDir() + "invalidFile.csv";
        arguments[2] = logFile.getAbsolutePath();

        try{
            CsvImporter.main(arguments);
        }
        catch (Exception e){}

        assertTrue(logFile.exists());
        FileReader reader = new FileReader(logFile.getAbsoluteFile());
        assertNotSame(-1, reader.read());
    }

    private String getPatientCsv(){
        return CsvImporterTest.class.getClassLoader().getResource("patientRecords.csv").getPath();
    }
    private String getProviderCsv(){
        return CsvImporterTest.class.getClassLoader().getResource("providerRecords.csv").getPath();
    }
    private String getLogFilePath(){
        return  getLogDir() + "importer.log";
    }
    private String getLogDir(){
        return System.getProperty("user.dir") +"/logs/";
    }
}
