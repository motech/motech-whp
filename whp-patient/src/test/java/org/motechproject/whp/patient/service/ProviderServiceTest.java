package org.motechproject.whp.patient.service;

import org.joda.time.DateTime;
import org.junit.After;
import org.junit.Test;
import org.motechproject.util.DateUtil;
import org.motechproject.whp.patient.builder.PatientRequestBuilder;
import org.motechproject.whp.patient.contract.PatientRequest;
import org.motechproject.whp.patient.domain.*;
import org.motechproject.whp.patient.repository.AllPatients;
import org.motechproject.whp.patient.repository.AllProviders;
import org.motechproject.whp.patient.repository.SpringIntegrationTest;
import org.motechproject.whp.refdata.domain.PatientType;
import org.motechproject.whp.refdata.domain.SmearTestResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;

import static junit.framework.Assert.assertEquals;
import static org.motechproject.util.DateUtil.now;

@ContextConfiguration(locations = "classpath*:/applicationPatientContext.xml")
public class ProviderServiceTest extends SpringIntegrationTest {

    @Autowired
    ProviderService providerService;
    @Autowired
    PatientService patientService;
    @Autowired
    AllPatients allPatients;
    @Autowired
    AllProviders allProviders;

    @After
    public void tearDown() {
        markForDeletion(allPatients.getAll().toArray());
        markForDeletion(allProviders.getAll().toArray());
    }

    @Test
    public void shouldCreateProvider() {
        String providerId = "providerId";
        String primaryMobile = "1234567890";
        String secondaryMobile = "0987654321";
        String tertiaryMobile = "1111111111";
        String district = "Muzzafarpur";
        DateTime now = now();

        providerService.createProvider(providerId, primaryMobile, secondaryMobile, tertiaryMobile, district, now);

        Provider provider = allProviders.findByProviderId(providerId);
        
        assertEquals(providerId.toLowerCase(), provider.getProviderId());
        assertEquals(primaryMobile, provider.getPrimaryMobile());
        assertEquals(secondaryMobile, provider.getSecondaryMobile());
        assertEquals(tertiaryMobile, provider.getTertiaryMobile());
        assertEquals(district, provider.getDistrict());
        assertEquals(now, provider.getLastModifiedDate());
    }

    @Test
    public void shouldTransferInPatient_WithNewlySent_SmearTestResultsAndWeightStatistics() {

        PatientRequest patientRequest = new PatientRequestBuilder().withDefaults().build();
        patientService.createPatient(patientRequest);

        Patient patient = allPatients.findByPatientId(patientRequest.getCase_id());

        String newProviderId = "newProviderId";
        String tbId = "newTbId";
        String newTbRegistrationNumber = "newTbRegistrationNumber";
        DateTime now = now();

        providerService.transferIn(newProviderId, patient, tbId, newTbRegistrationNumber, now, patientRequest.getSmearTestResults(), patientRequest.getWeightStatistics());

        Patient updatedPatient = allPatients.findByPatientId(patient.getPatientId());

        assertEquals(newProviderId.toLowerCase(), updatedPatient.getCurrentTreatment().getProviderId());
        assertEquals(tbId.toLowerCase(), updatedPatient.getCurrentTreatment().getTbId());
        assertEquals(newTbRegistrationNumber, updatedPatient.getCurrentTreatment().getTbRegistrationNumber());
        assertEquals(now.toLocalDate(), updatedPatient.getCurrentTreatment().getStartDate());
        assertEquals(now, updatedPatient.getLastModifiedDate());
        assertEquals(PatientType.TransferredIn, updatedPatient.getCurrentTreatment().getPatientType());

        assertEquals(1, patient.getCurrentTreatment().getSmearTestResults().size());
        assertEquals(DateUtil.newDate(2010, 5, 19), patient.getCurrentTreatment().getSmearTestResults().latestResult().getSmear_test_date_1());
        assertEquals(DateUtil.newDate(2010, 5, 21), patient.getCurrentTreatment().getSmearTestResults().latestResult().getSmear_test_date_2());
        assertEquals(SmearTestResult.Positive, patient.getCurrentTreatment().getSmearTestResults().latestResult().getSmear_test_result_1());
        assertEquals(SmearTestResult.Positive, patient.getCurrentTreatment().getSmearTestResults().latestResult().getSmear_test_result_2());
        assertEquals(DateUtil.newDate(2010, 5, 19), patient.getCurrentTreatment().getWeightStatistics().latestResult().getMeasuringDate());
        assertEquals(Double.valueOf(99.7), patient.getCurrentTreatment().getWeightStatistics().latestResult().getWeight());
    }

    @Test
    public void shouldTransferInPatient_WithSmearTestResultsAndWeightStatistics_FromPreviousTreatment() {

        PatientRequest patientRequest = new PatientRequestBuilder().withDefaults().build();
        patientService.createPatient(patientRequest);

        Patient patient = allPatients.findByPatientId(patientRequest.getCase_id());

        String newProviderId = "newProviderId";
        String tbId = "newTbId";
        String newTbRegistrationNumber = "newTbRegistrationNumber";
        DateTime now = now();

        // Emulating Patient Request doesn't have new lab test results and/or weight statistics
        providerService.transferIn(newProviderId, patient, tbId, newTbRegistrationNumber, now, new SmearTestResults(), new WeightStatistics());

        Patient updatedPatient = allPatients.findByPatientId(patient.getPatientId());

        assertEquals(newProviderId.toLowerCase(), updatedPatient.getCurrentTreatment().getProviderId());
        assertEquals(tbId.toLowerCase(), updatedPatient.getCurrentTreatment().getTbId());
        assertEquals(newTbRegistrationNumber, updatedPatient.getCurrentTreatment().getTbRegistrationNumber());
        assertEquals(now.toLocalDate(), updatedPatient.getCurrentTreatment().getStartDate());
        assertEquals(now, updatedPatient.getLastModifiedDate());
        assertEquals(PatientType.TransferredIn, updatedPatient.getCurrentTreatment().getPatientType());

        Treatment previousTreatment = patient.getTreatments().get(patient.getTreatments().size() - 1);

        assertEquals(1, patient.getCurrentTreatment().getSmearTestResults().size());
        assertEquals(previousTreatment.getSmearTestResults().latestResult().getSmear_test_date_1(), patient.getCurrentTreatment().getSmearTestResults().latestResult().getSmear_test_date_1());
        assertEquals(previousTreatment.getSmearTestResults().latestResult().getSmear_test_date_2(), patient.getCurrentTreatment().getSmearTestResults().latestResult().getSmear_test_date_2());
        assertEquals(previousTreatment.getSmearTestResults().latestResult().getSmear_test_result_1(), patient.getCurrentTreatment().getSmearTestResults().latestResult().getSmear_test_result_1());
        assertEquals(previousTreatment.getSmearTestResults().latestResult().getSmear_test_result_2(), patient.getCurrentTreatment().getSmearTestResults().latestResult().getSmear_test_result_2());
        assertEquals(previousTreatment.getWeightStatistics().latestResult().getMeasuringDate(), patient.getCurrentTreatment().getWeightStatistics().latestResult().getMeasuringDate());
        assertEquals(previousTreatment.getWeightStatistics().latestResult().getWeight(), patient.getCurrentTreatment().getWeightStatistics().latestResult().getWeight());
    }

}
