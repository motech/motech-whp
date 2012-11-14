package org.motechproject.whp.importer.csv;

import org.hamcrest.core.Is;
import org.joda.time.DateTime;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.motechproject.whp.common.domain.District;
import org.motechproject.whp.common.repository.AllDistricts;
import org.motechproject.whp.common.util.SpringIntegrationTest;
import org.motechproject.whp.containermapping.domain.ContainerRange;
import org.motechproject.whp.containermapping.domain.ProviderContainerMapping;
import org.motechproject.whp.containermapping.repository.AllProviderContainerMappings;
import org.motechproject.whp.importer.csv.exceptions.WHPImportException;
import org.motechproject.whp.patient.domain.Patient;
import org.motechproject.whp.patient.domain.Treatment;
import org.motechproject.whp.patient.repository.AllPatients;
import org.motechproject.whp.patient.domain.Gender;
import org.motechproject.whp.patient.domain.PatientType;
import org.motechproject.whp.patient.domain.TreatmentCategory;
import org.motechproject.whp.patient.repository.AllTreatmentCategories;
import org.motechproject.whp.user.domain.Provider;
import org.motechproject.whp.user.repository.AllProviders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsCollectionContaining.hasItem;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath*:/applicationDataImporterContext.xml")
public class CsvImporterTest extends SpringIntegrationTest {

    @Autowired
    AllPatients allPatients;
    @Autowired
    AllProviders allProviders;
    @Autowired
    AllTreatmentCategories allTreatmentCategories;
    @Autowired
    AllProviderContainerMappings allProviderContainerMappings;
    @Autowired
    AllDistricts allDistricts;
    private District district;

    @Before
    public void setUp() {
        district = new District("district");
        allDistricts.add(district);
        allPatients.removeAll();
        allProviders.removeAll();
        allTreatmentCategories.add(new TreatmentCategory("test1", "01", 3, 3, 9, 4, 12, 3, 9, null));
        allTreatmentCategories.add(new TreatmentCategory("test2", "02", 3, 3, 9, 4, 12, 3, 9, null));
    }

    @Test
    public void shouldStoreProviderData() throws Exception {
        String[] arguments = new String[3];
        arguments[0] = "provider";
        arguments[1] = getProviderCsv();
        CsvImporter.main(arguments);

        Provider provider1 = allProviders.findByProviderId("john");
        Provider provider2 = allProviders.findByProviderId("raj");
        assertNotNull(provider1);
        assertNotNull(provider2);
        assertEquals(0, allPatients.getAll().size());

    }

    @Test
    public void shouldStorePatientDataWithDefaultValues() throws Exception {
        String[] arguments = new String[3];
        arguments[0] = "provider";
        arguments[1] = getProviderCsv();
        CsvImporter.main(arguments);

        arguments[0] = "patient";
        arguments[1] = getPatientCsv();
        CsvImporter.main(arguments);
        assertEquals(3, allPatients.getAll().size());
        assertEquals(PatientType.New, allPatients.findByPatientId("12345").getCurrentTreatment().getPatientType());
        Patient patient2 = allPatients.findByPatientId("234324");
        assertEquals(patient2.getLastModifiedDate().toLocalDate(), patient2.getCurrentTreatment().getWeightStatistics().get(0).getMeasuringDate());

    }

    @Test
    public void shouldNotStoreAnyPatientIfThereIsInvalidData() throws Exception {
        String[] arguments = new String[3];
        arguments[0] = "provider";
        arguments[1] = getProviderCsv();
        CsvImporter.main(arguments);

        arguments[0] = "patient";
        arguments[1] = CsvImporterTest.class.getClassLoader().getResource("patientRecordsWitnInvalidData.csv").getPath();
        CsvImporter.main(arguments);
        assertEquals(0, allPatients.getAll().size());
    }

    @Test(expected = WHPImportException.class)
    public void shouldThrowExceptionForInvalidImportFile() throws Exception {
        String[] arguments = new String[3];
        arguments[0] = "provider";
        arguments[1] = "invalidFile.csv";
        CsvImporter.main(arguments);
    }

    @Ignore
    @Test
    public void shouldVerifyImportedProviderData() throws Exception {
        String[] arguments = new String[3];
        arguments[0] = "provider";
        arguments[1] = getProviderCsv();
        CsvImporter.main(arguments);

        Provider provider2 = allProviders.findByProviderId("raj");
        provider2.setDistrict(provider2.getDistrict() + "_modified");
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
        arguments[0] = "provider";
        arguments[1] = getProviderCsv();
        CsvImporter.main(arguments);

        arguments[0] = "patient";
        arguments[1] = getPatientCsv();
        CsvImporter.main(arguments);

        arguments[0] = ImportType.PatientTest.name();
        Patient patient1 = allPatients.findByPatientId("12345");
        Patient patient2 = allPatients.findByPatientId("234324");
        allPatients.remove(patient1);

        patient2.setGender(Gender.F);
        patient2.setFirstName("first");
        patient2.setLastName("last");
        patient2.setMigrated(false);
        patient2.setPhi("12");
        patient2.setPhoneNumber("00");
        Treatment treatment = patient2.getCurrentTreatment();
        treatment.setProviderId("providerId");
        treatment.setPatientType(PatientType.Chronic);
        patient2.setLastModifiedDate(patient2.getLastModifiedDate().plusSeconds(1));
        treatment.setStartDate(treatment.getStartDate().plusDays(1));
        treatment.setTbId("modified_tb_id");
        treatment.setTbRegistrationNumber("mod_tb_reg_no");
        patient2.startTherapy(DateTime.now().toLocalDate());
        patient2.getCurrentTherapy().setCreationDate(DateTime.now());
        allPatients.update(patient2);

        CsvImporter.main(arguments);
    }

    @Test
    public void shouldLoadContainerMapping() throws Exception {
        String[] arguments = new String[3];
        arguments[0] = "ContainerMapping";
        arguments[1] = getContainerMappingCsv();
        CsvImporter.main(arguments);

        List<ProviderContainerMapping> mappings = allProviderContainerMappings.getAll();
        assertThat(mappings.size(), Is.is(2));
        assertThat(mappings.get(0).getProviderId(), Is.is("john"));
        assertThat(mappings.get(0).getContainerRanges(), hasItem(new ContainerRange(20000000001L, 30000000000L)));
        assertThat(mappings.get(1).getProviderId(), Is.is("raj"));
        assertThat(mappings.get(1).getContainerRanges(), hasItem(new ContainerRange(10000000000L, 20000000000L)));
    }

    @After
    public void tearDown() {
        allPatients.removeAll();
        allTreatmentCategories.removeAll();
        allProviders.removeAll();
        allProviderContainerMappings.removeAll();
        allDistricts.remove(district);
    }

    private String getPatientCsv() {
        return CsvImporterTest.class.getClassLoader().getResource("patientRecords.csv").getPath();
    }

    private String getProviderCsv() {
        return CsvImporterTest.class.getClassLoader().getResource("providerRecords.csv").getPath();
    }

    private String getContainerMappingCsv() {
        return CsvImporterTest.class.getClassLoader().getResource("containerMapping.csv").getPath();
    }

}
