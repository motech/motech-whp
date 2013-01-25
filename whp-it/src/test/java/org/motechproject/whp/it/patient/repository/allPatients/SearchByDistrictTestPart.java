package org.motechproject.whp.it.patient.repository.allPatients;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.motechproject.paginator.contract.FilterParams;
import org.motechproject.paginator.contract.SortParams;
import org.motechproject.whp.patient.domain.Patient;

import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

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

    @After
    public void after() {
        markForDeletion(allPatients.getAll().toArray());
    }
}