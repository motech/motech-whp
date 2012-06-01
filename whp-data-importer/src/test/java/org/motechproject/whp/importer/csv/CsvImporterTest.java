package org.motechproject.whp.importer.csv;

import org.joda.time.DateTime;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.motechproject.whp.importer.csv.exceptions.WHPImportException;
import org.motechproject.whp.patient.domain.Patient;
import org.motechproject.whp.patient.domain.ProvidedTreatment;
import org.motechproject.whp.patient.domain.Provider;
import org.motechproject.whp.patient.domain.TreatmentCategory;
import org.motechproject.whp.patient.repository.*;
import org.motechproject.whp.refdata.domain.Gender;
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

    @Autowired
    AllTreatmentCategories allTreatmentCategories;

    @After
    public void cleanDb() {
        allPatients.removeAll();
        allTreatments.removeAll();
        allTreatmentCategories.removeAll();
        allProviders.removeAll();
    }
    @Before
    public void setUp(){
        allPatients.removeAll();
        allProviders.removeAll();
        allTreatmentCategories.add(new TreatmentCategory("test1","01",3,3,3, 9, 9, null));
        allTreatmentCategories.add(new TreatmentCategory("test2","02",3,3,3, 9, 9, null));
    }

    @Test
    public void shouldStoreProviderData() throws Exception {
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
        assertEquals(0, allPatients.getAll().size());
        assertEquals(0, allTreatments.getAll().size());
        assertEquals(PatientType.New, allPatients.findByPatientId("12345").getCurrentProvidedTreatment().getPatientType());
        Patient patient2 = allPatients.findByPatientId("234324");
        assertEquals(patient2.getLastModifiedDate().toLocalDate(), patient2.getCurrentProvidedTreatment().getWeightStatistics().get(0).getMeasuringDate());

    }

    @Test(expected = WHPImportException.class)
    public void shouldThrowExceptionForInvalidLogFile() throws Exception {
        String[] arguments = new String[3];
        arguments[0] = "provider";
        arguments[1] = getProviderCsv();
        arguments[2] = getLogDir() + "/impor\\/ter5.log";
        CsvImporter.main(arguments);
    }

    @Test(expected = WHPImportException.class)
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

    @Ignore
    @Test
    public void shouldVerifyImportedProviderData() throws Exception {
        String[] arguments = new String[3];
        arguments[0] = "provider";
        arguments[1] = getProviderCsv();
        arguments[2] = getLogFilePath();
        CsvImporter.main(arguments);

        Provider provider2 = allProviders.findByProviderId("raj");
        provider2.setDistrict(provider2.getDistrict()+"_modified");
        provider2.setPrimaryMobile(provider2.getPrimaryMobile() + "1");
        provider2.setSecondaryMobile("11111");
        provider2.setTertiaryMobile("");
        allProviders.update(provider2);
        allProviders.remove(allProviders.findByProviderId("john"));

        arguments[0] = ImportType.ProviderTest.name();
        CsvImporter.main(arguments);



    }

    @Ignore
    @Test
    public void shouldVerifyPatientDataWithDefaultValues() throws Exception {
        String[] arguments = new String[3];
        String logFile = getLogDir() + "patient.log";
        arguments[0] = "provider";
        arguments[1] = getProviderCsv();
        arguments[2] = logFile;
        CsvImporter.main(arguments);

        arguments[0] = "patient";
        arguments[1] = getPatientCsv();
        arguments[2] = logFile;
        CsvImporter.main(arguments);

        arguments[0] = ImportType.PatientTest.name();
        Patient patient1 = allPatients.findByPatientId("12345");
        Patient patient2 = allPatients.findByPatientId("234324");
        allPatients.remove(patient1);

        patient2.setGender(Gender.F);
        patient2.setFirstName("first");
        patient2.setLastName("last");
        patient2.setMigrated(false);
        patient2.setPhi("blah");
        patient2.setPhoneNumber("00");
        ProvidedTreatment treatment = patient2.getCurrentProvidedTreatment();
        treatment.setProviderId("providerId");
        treatment.setPatientType(PatientType.Chronic);
        patient2.setLastModifiedDate(patient2.getLastModifiedDate().plusSeconds(1));
        treatment.setStartDate(treatment.getStartDate().plusDays(1));
        treatment.setTbId("modified_tb_id");
        treatment.setTbRegistrationNumber("mod_tb_reg_no");
        treatment.getTreatment().setStartDate(DateTime.now().toLocalDate());
        treatment.getTreatment().setCreationDate(DateTime.now());
        allPatients.update(patient2);

        CsvImporter.main(arguments);
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
