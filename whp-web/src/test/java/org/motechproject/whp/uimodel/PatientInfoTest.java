package org.motechproject.whp.uimodel;

import org.joda.time.LocalDate;
import org.junit.Before;
import org.junit.Test;
import org.motechproject.whp.patient.builder.PatientBuilder;
import org.motechproject.whp.patient.builder.TherapyBuilder;
import org.motechproject.whp.patient.builder.TreatmentBuilder;
import org.motechproject.whp.patient.domain.*;
import org.motechproject.whp.refdata.domain.*;
import org.motechproject.whp.user.domain.Provider;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.motechproject.whp.patient.builder.ProviderBuilder.newProviderBuilder;

public class PatientInfoTest {

    String firstName = "firstName";
    String lastName = "lastName";
    String patientId = "patientid";
    String phi = "phi";
    Gender gender = Gender.M;
    int patientAge = 20;
    DiseaseClass diseaseClass = DiseaseClass.E;
    String treatmentCategory = "treatmentCategory";
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
        Therapy therapy = new TherapyBuilder()
                .withAge(patientAge)
                .withDiseaseClass(diseaseClass)
                .withTherapyUid("therapyUid")
                .withTreatmentCategory(treatmentCategory).withStartDate(startDate).withNoOfDoesTaken(PhaseName.IP, 2).build();

        SmearTestResults smearTestResults = new SmearTestResults();
        smearTestResults.add(new SmearTestRecord(SampleInstance.PreTreatment, null, null, null, null));

        WeightStatistics weightStatistics = new WeightStatistics();
        weightStatistics.add(new WeightStatisticsRecord(SampleInstance.PreTreatment, 20.0, new LocalDate(2012, 1, 1)));

        expectedTestResults = new TestResults(smearTestResults, weightStatistics);

        currentTreatment = new TreatmentBuilder()
                .withStartDate(new LocalDate(2012, 2, 2))
                .withTbId(tbId)
                .withProviderId(providerId)
                .withTbRegistrationNumber(tbRegistrationNo)
                .withTherapy(therapy)
                .withPatientType(patientType)
                .withAddress(patientAddress)
                .withSmearTestResults(smearTestResults)
                .withWeightStatistics(weightStatistics)
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
                .withCurrentTreatment(currentTreatment)
                .withCurrentTherapy(therapy)
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
        assertThat(patientInfo.getGender(), is(gender.name()));
        assertThat(patientInfo.getTbId(), is(tbId));
        assertThat(patientInfo.getProviderId(), is(providerId));
        assertThat(patientInfo.getProviderMobileNumber(), is(providerMobileNumber));
        assertThat(patientInfo.getTherapyStartDate(), is(startDate.toString("dd/MM/yyyy")));
        assertThat(patientInfo.getTbRegistrationNumber(), is(tbRegistrationNo));
        assertThat(patientInfo.getPatientType(), is(patientType.name()));
        assertThat(patientInfo.getAge(), is(patientAge));
        assertThat(patientInfo.getDiseaseClass(), is(diseaseClass.name()));
        assertThat(patientInfo.getTreatmentCategory(), is(treatmentCategory));
        assertThat(patientInfo.getAddress(), is("houseNo, landmark, block, village, district, state"));
        assertThat(patientInfo.getAddressState(), is("state"));
        assertThat(patientInfo.getTestResults(), is(expectedTestResults));
    }


}
