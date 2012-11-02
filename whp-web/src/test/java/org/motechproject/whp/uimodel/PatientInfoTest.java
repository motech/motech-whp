package org.motechproject.whp.uimodel;

import org.joda.time.LocalDate;
import org.junit.Before;
import org.junit.Test;
import org.motechproject.util.DateUtil;
import org.motechproject.whp.common.domain.Phase;
import org.motechproject.whp.common.domain.SputumTrackingInstance;
import org.motechproject.whp.patient.builder.PatientBuilder;
import org.motechproject.whp.patient.builder.TherapyBuilder;
import org.motechproject.whp.patient.builder.TreatmentBuilder;
import org.motechproject.whp.patient.domain.*;
import org.motechproject.whp.user.domain.Provider;

import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertTrue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertNull;
import static org.motechproject.util.DateUtil.now;
import static org.motechproject.util.DateUtil.today;
import static org.motechproject.whp.user.builder.ProviderBuilder.newProviderBuilder;

public class PatientInfoTest {

    String firstName = "firstName";
    String lastName = "lastName";
    String patientId = "patientid";
    String phi = "phi";
    Gender gender = Gender.M;
    int patientAge = 20;
    DiseaseClass diseaseClass = DiseaseClass.E;
    String treatmentCategory = "treatmentCategory";
    private String treatmentCategoryCode = "01";
    String patientNumber = "patientNumber";
    String providerMobileNumber = "primaryMobileNumber";
    LocalDate startDate = new LocalDate(2012, 6, 28);
    String tbId = "tbid";
    String providerId = "providerid";
    String tbRegistrationNo = "tbRegistrationNo";
    PatientType patientType = PatientType.New;
    Address patientAddress = new Address("houseNo", "landmark", "block", "village", "district", "state");

    Patient patient;
    Provider provider;
    Treatment currentTreatment;
    TestResults expectedTestResults;

    @Before
    public void setup() {

        SmearTestResults smearTestResults = new SmearTestResults();
        smearTestResults.add(new SmearTestRecord(SputumTrackingInstance.PreTreatment, null, null, null, null, null, null));

        WeightStatistics weightStatistics = new WeightStatistics();
        weightStatistics.add(new WeightStatisticsRecord(SputumTrackingInstance.PreTreatment, 20.0, new LocalDate(2012, 1, 1)));

        currentTreatment = new TreatmentBuilder()
                .withStartDate(new LocalDate(2012, 2, 2))
                .withTbId(tbId)
                .withProviderId(providerId)
                .withTbRegistrationNumber(tbRegistrationNo)
                .withPatientType(patientType)
                .withAddress(patientAddress)
                .withSmearTestResults(smearTestResults)
                .withWeightStatistics(weightStatistics)
                .build();
        expectedTestResults = new TestResults(smearTestResults, weightStatistics);

        treatmentCategoryCode = "01";
        Therapy therapy = new TherapyBuilder()
                .withAge(patientAge)
                .withDiseaseClass(diseaseClass)
                .withTherapyUid("therapyUid")
                .withTreatmentCategory(treatmentCategory, treatmentCategoryCode)
                .withStartDate(startDate)
                .withNoOfDosesTaken(Phase.IP, 2)
                .withTreatment(currentTreatment)
                .build();

        provider = newProviderBuilder()
                .withProviderId(providerId)
                .withPrimaryMobileNumber(providerMobileNumber)
                .build();


        patient = new PatientBuilder()
                .withPatientId(patientId)
                .withFirstName(firstName)
                .withLastName(lastName)
                .withPhi(phi)
                .withGender(gender)
                .withPatientMobileNumber(patientNumber)
                .withCurrentTherapy(therapy)
                .withAdherenceProvidedForLastWeek()
                .build();

        expectedTestResults = new TestResults(currentTreatment.getSmearTestResults(), currentTreatment.getWeightStatistics());
    }

    @Test
    public void shouldCreatePatientInfoModelFromPatient() {
        PatientInfo patientInfo = new PatientInfo(patient, provider);

        assertThat(patientInfo.getPatientId(), is(patientId));
        assertThat(patientInfo.getFirstName(), is(firstName));
        assertThat(patientInfo.getLastName(), is(lastName));
        assertThat(patientInfo.getPhoneNumber(), is(patientNumber));
        assertThat(patientInfo.getPhi(), is(phi));
        assertThat(patientInfo.getGender(), is(gender.getValue()));
        assertThat(patientInfo.getTbId(), is(tbId));
        assertThat(patientInfo.getProviderId(), is(providerId));
        assertThat(patientInfo.getProviderMobileNumber(), is(providerMobileNumber));
        assertThat(patientInfo.getTherapyStartDate(), is(startDate.toString("dd/MM/yyyy")));
        assertThat(patientInfo.getTbRegistrationNumber(), is(tbRegistrationNo));
        assertThat(patientInfo.getPatientType(), is(patientType.value()));
        assertThat(patientInfo.getAge(), is(patientAge));
        assertThat(patientInfo.getDiseaseClass(), is(diseaseClass.value()));
        assertThat(patientInfo.getTreatmentCategoryName(), is(treatmentCategory));
        assertThat(patientInfo.getTreatmentCategoryCode(), is(treatmentCategoryCode));
        assertThat(patientInfo.getAddress(), is("houseNo, landmark, block"));
        assertThat(patientInfo.getAddressState(), is("state"));
        assertThat(patientInfo.getAddressDistrict(), is("district"));
        assertThat(patientInfo.getTestResults(), is(expectedTestResults));
        assertTrue(patientInfo.isAdherenceCapturedForThisWeek());
    }

    @Test
    public void shouldMapTestResultsAcrossTreatments() {
        patient.closeCurrentTreatment(TreatmentOutcome.TransferredOut, DateUtil.now());

        SmearTestResults smearTestResults = new SmearTestResults();
        SputumTrackingInstance newSputumTrackingInstance = SputumTrackingInstance.ExtendedIP;
        smearTestResults.add(new SmearTestRecord(newSputumTrackingInstance, null, null, null, null, null, null));

        WeightStatistics weightStatistics = new WeightStatistics();
        weightStatistics.add(new WeightStatisticsRecord(SputumTrackingInstance.ExtendedIP, 20.0, new LocalDate(2012, 1, 1)));

        Treatment treatment = new TreatmentBuilder()
                .withStartDate(new LocalDate(2012, 2, 2))
                .withTbId(tbId)
                .withProviderId(providerId)
                .withTbRegistrationNumber(tbRegistrationNo)
                .withPatientType(patientType)
                .withAddress(patientAddress)
                .withSmearTestResults(smearTestResults)
                .withWeightStatistics(weightStatistics)
                .build();

        patient.addTreatment(treatment, now(), now());

        PatientInfo patientInfo = new PatientInfo(patient, provider);
        assertThat(patientInfo.getTestResults().size(),is(2));
        assertThat(patientInfo.getTestResults().get(0).getSampleInstance(),is(SputumTrackingInstance.PreTreatment.getDisplayText()));
        assertThat(patientInfo.getTestResults().get(1).getSampleInstance(),is(newSputumTrackingInstance.getDisplayText()));
    }

    @Test
    public void shouldSetProviderMobileNumberToNullIfProviderNotGiven() {
        PatientInfo patientInfo = new PatientInfo(patient);

        assertNull(patientInfo.getProviderMobileNumber());
    }


    @Test
    public void shouldNotShowTransitionAlertWhenPatientIsOnCP() {
        Patient patient = new PatientBuilder().withDefaults().build();
        patient.startTherapy(today());
        patient.endLatestPhase(today().plusMonths(1));
        patient.nextPhaseName(Phase.CP);
        patient.startNextPhase();
        patient.setNumberOfDosesTaken(patient.getCurrentPhase().getName(), 51, today());

        PatientInfo patientInfo = new PatientInfo(patient);
        assertFalse(patientInfo.isShowAlert());
    }
}
