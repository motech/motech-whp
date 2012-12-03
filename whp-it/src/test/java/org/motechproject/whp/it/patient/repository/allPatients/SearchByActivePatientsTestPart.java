package org.motechproject.whp.it.patient.repository.allPatients;

import org.junit.Test;
import org.motechproject.util.DateUtil;
import org.motechproject.whp.patient.builder.PatientBuilder;
import org.motechproject.whp.patient.domain.Patient;
import org.motechproject.whp.patient.domain.TreatmentOutcome;
import org.motechproject.whp.user.domain.ProviderIds;

import static java.util.Arrays.asList;
import static junit.framework.Assert.assertEquals;

public class SearchByActivePatientsTestPart extends AllPatientsTestPart {

    @Test
    public void shouldFetchProvidersOfActivePatients() {
        Patient patientWithCurrentTreatment = new PatientBuilder().withDefaults().withPatientId("patientId1").build();

        Patient patientWithoutCurrentTreatment = new PatientBuilder().withDefaults().withPatientId("patientId2").build();
        patientWithoutCurrentTreatment.closeCurrentTreatment(TreatmentOutcome.Cured, DateUtil.now());

        allPatients.add(patientWithCurrentTreatment);
        allPatients.add(patientWithoutCurrentTreatment);

        ProviderIds providerIds = new ProviderIds(asList(patientWithCurrentTreatment.getCurrentTreatment().getProviderId(), "providerId"));

        assertEquals(new ProviderIds(asList(patientWithCurrentTreatment.getCurrentTreatment().getProviderId())), allPatients.providersWithActivePatients(providerIds));
    }
}
