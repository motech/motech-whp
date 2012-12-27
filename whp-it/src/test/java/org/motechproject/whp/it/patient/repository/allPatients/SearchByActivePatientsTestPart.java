package org.motechproject.whp.it.patient.repository.allPatients;

import org.junit.Test;
import org.motechproject.util.DateUtil;
import org.motechproject.whp.common.domain.ProviderPatientCount;
import org.motechproject.whp.patient.builder.PatientBuilder;
import org.motechproject.whp.patient.domain.Patient;
import org.motechproject.whp.patient.domain.TreatmentOutcome;
import org.motechproject.whp.user.domain.ProviderIds;

import java.util.ArrayList;
import java.util.List;

import static java.util.Arrays.asList;
import static junit.framework.Assert.assertEquals;

public class SearchByActivePatientsTestPart extends AllPatientsTestPart {

    @Test
    public void shouldFilterProvidersByActivePatients() {
        Patient patientWithCurrentTreatment = new PatientBuilder().withDefaults().withPatientId("patientId1").build();

        Patient patientWithoutCurrentTreatment = new PatientBuilder().withDefaults().withPatientId("patientId2").build();
        patientWithoutCurrentTreatment.closeCurrentTreatment(TreatmentOutcome.Cured, DateUtil.now());

        allPatients.add(patientWithCurrentTreatment);
        allPatients.add(patientWithoutCurrentTreatment);

        ProviderIds providerIds = new ProviderIds(asList(patientWithCurrentTreatment.getCurrentTreatment().getProviderId(), "providerId"));

        assertEquals(new ProviderIds(asList(patientWithCurrentTreatment.getCurrentTreatment().getProviderId())), allPatients.providersWithActivePatients(providerIds));
    }

    @Test
    public void shouldFetchAllProvidersWithActivePatients() {
        Patient patientWithCurrentTreatment = new PatientBuilder().withDefaults().withPatientId("patientId1").build();

        Patient patientWithoutCurrentTreatment = new PatientBuilder().withDefaults().withPatientId("patientId2").build();
        patientWithoutCurrentTreatment.closeCurrentTreatment(TreatmentOutcome.Cured, DateUtil.now());

        allPatients.add(patientWithCurrentTreatment);
        allPatients.add(patientWithoutCurrentTreatment);

        assertEquals(new ProviderIds(asList(patientWithCurrentTreatment.getCurrentTreatment().getProviderId())), allPatients.providersWithActivePatients());
    }

    @Test
    public void shouldReturnListOfProviderPatientCounts() {
        Patient activeProviderOnePatient1 = new PatientBuilder().withDefaults().withProviderId("provider1").withPatientId("patientId1").build();
        Patient activeProviderOnePatient2 = new PatientBuilder().withDefaults().withProviderId("provider1").withPatientId("patientId2").build();
        Patient inactiveProviderOnePatient = new PatientBuilder().withDefaults().withProviderId("provider1").withPatientId("patientId3").build();
        inactiveProviderOnePatient.closeCurrentTreatment(TreatmentOutcome.Cured, DateUtil.now());

        Patient activeProviderTwoPatient1 = new PatientBuilder().withDefaults().withProviderId("provider2").withPatientId("patientId4").build();

        allPatients.add(activeProviderOnePatient1);
        allPatients.add(activeProviderOnePatient2);
        allPatients.add(inactiveProviderOnePatient);
        allPatients.add(activeProviderTwoPatient1);


        List<ProviderPatientCount> expectedProviderAdherenceStatuses = new ArrayList<>();
        expectedProviderAdherenceStatuses.add(new ProviderPatientCount("provider1", 2));
        expectedProviderAdherenceStatuses.add(new ProviderPatientCount("provider2", 1));

        assertEquals(expectedProviderAdherenceStatuses, allPatients.findAllProviderPatientCount());
    }

}
