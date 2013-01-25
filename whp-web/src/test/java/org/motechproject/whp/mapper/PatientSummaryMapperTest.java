package org.motechproject.whp.mapper;

import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.junit.Test;
import org.motechproject.util.DateUtil;
import org.motechproject.whp.common.domain.Phase;
import org.motechproject.whp.common.domain.SmearTestResult;
import org.motechproject.whp.common.domain.SputumTrackingInstance;
import org.motechproject.whp.patient.builder.PatientBuilder;
import org.motechproject.whp.patient.builder.TherapyBuilder;
import org.motechproject.whp.patient.builder.TreatmentBuilder;
import org.motechproject.whp.patient.domain.*;
import org.motechproject.whp.uimodel.PatientSummary;

import java.util.List;

import static java.util.Arrays.asList;
import static junit.framework.Assert.assertEquals;
import static org.motechproject.util.DateUtil.today;
import static org.motechproject.whp.common.domain.TreatmentWeekInstance.currentAdherenceCaptureWeek;

public class PatientSummaryMapperTest {

    private final PatientSummaryMapper patientSummaryMapper = new PatientSummaryMapper();

    @Test
    public void shouldMapAllPatientFields_forPatientOnActiveTreatment() {
        Patient activePatient = createActivePatient();

        List<PatientSummary> patientSummaries = patientSummaryMapper.map(asList(activePatient));

        verifyActivePatientSummary(patientSummaries.get(0), activePatient);
    }

    @Test
    public void shouldMapAllPatientFields_forPatientOnInactiveTreatment() {
        Patient inactivePatient = createInactivePatient();

        List<PatientSummary> patientSummaries = patientSummaryMapper.map(asList(inactivePatient));

        verifyInactivePatientSummary(patientSummaries.get(0), inactivePatient);
    }

    @Test
    public void shouldHandleNullPretreatmentSputumAndWeightResult(){

        SmearTestResults smearTestResults = new SmearTestResults();
        smearTestResults.add(new SmearTestRecord(SputumTrackingInstance.EndIP, DateUtil.today(), SmearTestResult.Positive, DateUtil.today(), SmearTestResult.Positive, "labName", "labNumber"));

        WeightStatistics weightStatistics = new WeightStatistics();
        weightStatistics.add(new WeightStatisticsRecord(SputumTrackingInstance.ExtendedIP, 10.0, DateUtil.today()));
        Treatment treatment = new TreatmentBuilder().withDefaults().withSmearTestResults(smearTestResults).withWeightStatistics(weightStatistics).build();
        Patient patient = new PatientBuilder().withTherapy(new TherapyBuilder().withTreatment(treatment).build()).build();

        List<PatientSummary> patientSummaries = patientSummaryMapper.map(asList(patient));

        assertEquals(null, patientSummaries.get(0).getPreTreatmentSputumResult());
        assertEquals(null, patientSummaries.get(0).getPreTreatmentWeight());
    }



    @Test
    public void shouldReturnFormattedIPProgress() {
        Patient patient = PatientBuilder.patient();
        patient.startTherapy(currentAdherenceCaptureWeek().startDate().minusWeeks(20));

        patient.setNumberOfDosesTaken(Phase.IP, 24, currentAdherenceCaptureWeek().startDate().minusWeeks(20));

        patient.endLatestPhase(today().minusMonths(4));

        patient.nextPhaseName(Phase.EIP);
        patient.startNextPhase();

        patient.setNumberOfDosesTaken(Phase.EIP, 9, currentAdherenceCaptureWeek().startDate().minusWeeks(1));

        //(24:IP + 9:EIP) / (24:IP + 12:EIP)
        assertEquals("33/36 (91.67%)", patientSummaryMapper.getIPProgress(patient));
    }

    @Test
    public void shouldReturnFormattedCPProgress() {
        Patient patient = PatientBuilder.patient();
        patient.startTherapy(currentAdherenceCaptureWeek().startDate().minusWeeks(20));
        patient.setNumberOfDosesTaken(Phase.IP, 24, currentAdherenceCaptureWeek().startDate().minusWeeks(20));

        patient.endLatestPhase(today().minusMonths(4));

        patient.nextPhaseName(Phase.EIP);
        patient.startNextPhase();
        patient.setNumberOfDosesTaken(Phase.EIP, 9, currentAdherenceCaptureWeek().startDate().minusWeeks(11));

        patient.endLatestPhase(today().minusMonths(3));

        patient.nextPhaseName(Phase.CP);
        patient.startNextPhase();
        patient.setNumberOfDosesTaken(Phase.CP, 35, currentAdherenceCaptureWeek().startDate().minusWeeks(1));

        //(24:IP + 9:EIP) / (24:IP + 12:EIP)
        assertEquals("35/54 (64.81%)", patientSummaryMapper.getCPProgress(patient));
    }


    private void verifyInactivePatientSummary(PatientSummary patientSummary, Patient inactivePatient) {
        verifyActivePatientSummary(patientSummary, inactivePatient);
        assertEquals(patientSummary.getTreatmentClosingDate(), inactivePatient.getCurrentTreatment().getEndDate().toDate());
        assertEquals(patientSummary.getTreatmentOutcome(), inactivePatient.getCurrentTreatment().getTreatmentOutcome().getOutcome());
    }

    private void verifyActivePatientSummary(PatientSummary patientSummary, Patient patient) {
        assertEquals(patientSummary.getAge(), patient.getAge());
        assertEquals(patientSummary.getCpTreatmentProgress(), patientSummaryMapper.getCPProgress(patient));
        assertEquals(patientSummary.getCumulativeMissedDoses(), Integer.valueOf(patient.getCumulativeDosesNotTaken()));
        assertEquals(patientSummary.getDiseaseClass(), patient.getCurrentTherapy().getDiseaseClass().value());
        assertEquals(patientSummary.getIpTreatmentProgress(), patientSummaryMapper.getIPProgress(patient));
        assertEquals(patientSummary.getName(), patient.getFirstName() + " " + patient.getLastName());
        assertEquals(patientSummary.getGender(), patient.getGender());
        assertEquals(patientSummary.getPatientId(), patient.getPatientId());
        assertEquals(patientSummary.getPatientType(), patient.getCurrentTreatment().getPatientType());
        assertEquals(patientSummary.getTbId(), patient.getCurrentTreatment().getTbId());
        assertEquals(patientSummary.getTreatmentCategory(), patient.getCurrentTherapy().getTreatmentCategory().getName());
        assertEquals(patientSummary.getVillage(), patient.getCurrentTreatment().getPatientAddress().getAddress_village());
        assertEquals(patientSummary.getProviderId(), patient.getCurrentTreatment().getProviderId());
        assertEquals(patientSummary.getProviderDistrict(), patient.getCurrentTreatment().getProviderDistrict());
        assertEquals(patientSummary.getPreTreatmentSputumResult(), patient.getPreTreatmentSputumResult().name());
        assertEquals(patientSummary.getPreTreatmentWeight(), patient.getPreTreatmentWeightRecord().getWeight().toString());
        assertEquals(patientSummary.getTbRegistrationDate(), patient.getCurrentTreatment().getStartDate().toDate());
        assertEquals(patientSummary.getTreatmentStartDate(), patient.getCurrentTherapy().getStartDate().toDate());
    }


    private Patient createActivePatient() {
        Patient activePatient = new PatientBuilder().withDefaults().withAge(30).build();
        activePatient.startTherapy(new LocalDate());
        activePatient.setNumberOfDosesTaken(Phase.IP, 22, today().minusMonths(5));
        return activePatient;
    }

    private Patient createInactivePatient() {
        Patient inactivePatient = createActivePatient();
        inactivePatient.closeCurrentTreatment(TreatmentOutcome.Cured, new DateTime());
        return inactivePatient;
    }
}