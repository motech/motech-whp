package org.motechproject.whp.it.patient.repository.allPatients;

import org.junit.Before;
import org.junit.Test;
import org.motechproject.paginator.contract.FilterParams;
import org.motechproject.paginator.contract.SortParams;
import org.motechproject.whp.patient.domain.Patient;

import java.util.List;

import static java.util.Arrays.asList;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.motechproject.whp.patient.assertUtil.PatientAssert.assertPatientEquals;

public class SearchByDistrictTestPart extends AllPatientsTestPart {


    FilterParams filterParams;

    SortParams sortParams;

    @Before
    public void setUp() {
        allPatients.removeAll();
    }

    @Test
    public void shouldReturnPatientsForADistrictForTheGivenPage() {
        int startIndex = 0;       //page starts from zero
        int rowsPerPage = 2;
        String district2Name = "district2";
        String patientId2 = "id2";

        filterParams = new FilterParams();
        filterParams.put("selectedDistrict", district2Name);

        createPatient("id1", "providerid1", "district1");
        createPatient(patientId2, "providerid2", district2Name);

        List<Patient> patients = allPatients.filter(filterParams, sortParams, startIndex, rowsPerPage);

        assertThat(patients.size(), is(1));
        assertThat(patients.get(0).getPatientId(), is(patientId2));
    }

    @Test
    public void shouldReturnNoPatientsIfNoDistrictOrProviderIdGivenForTheGivenPage() {
        int startIndex = 0;       //page starts from zero
        int rowsPerPage = 2;
        String district2Name = "district2";
        String patientId2 = "id2";

        filterParams = new FilterParams();

        createPatient("id1", "providerid1", "district1");
        createPatient(patientId2, "providerid2", district2Name);

        List<Patient> patients = allPatients.filter(filterParams, sortParams, startIndex, rowsPerPage);

        assertThat(patients.size(), is(0));
    }


    @Test
    public void shouldFetchByDistrict() {
        String provider1 = "provider1";
        String provider2 = "provider2";
        String provider3 = "provider3";

        Patient patient1 = createPatient("patientId1", provider1, PROVIDER_DISTRICT);
        Patient patient2 = createPatient("patientId2", provider2, "another-district");
        Patient patient3 = createPatient("patientId3", provider3, PROVIDER_DISTRICT);

        List<Patient> patients = allPatients.getAllUnderActiveTreatmentInDistrict(PROVIDER_DISTRICT);
        assertPatientEquals(asList(patient1, patient3), patients);
    }


    @Test
    public void shouldFetchByDistrictForGivenPageNumber(){

        String provider1 = "provider1";
        String provider2 = "provider2";
        String provider3 = "provider3";

        Patient patient1 = createPatient("patientId1", provider1, PROVIDER_DISTRICT);
        Patient patient2 = createPatient("patientId2", provider2, PROVIDER_DISTRICT);
        Patient patient3 = createPatient("patientId3", provider3, PROVIDER_DISTRICT);

        Integer startIndex = 1;
        Integer rowsPerPage = 2;
        List<Patient> patients = allPatients.getAllUnderActiveTreatmentInDistrictForAGivenPage(PROVIDER_DISTRICT, startIndex, rowsPerPage);
        assertPatientEquals(asList(patient3), patients);
        assertThat(patients.size(), is(1));
    }


}