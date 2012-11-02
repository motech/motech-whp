package org.motechproject.whp.mapper;

import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.junit.Test;
import org.motechproject.util.DateUtil;
import org.motechproject.whp.common.domain.Phase;
import org.motechproject.whp.common.domain.SmearTestResult;
import org.motechproject.whp.common.domain.SputumTrackingInstance;
import org.motechproject.whp.common.util.WHPDate;
import org.motechproject.whp.patient.builder.PatientBuilder;
import org.motechproject.whp.patient.builder.TherapyBuilder;
import org.motechproject.whp.patient.builder.TreatmentBuilder;
import org.motechproject.whp.patient.domain.*;
import org.motechproject.whp.uimodel.PatientSummary;
import org.springframework.util.StringUtils;

import java.util.List;

import static java.util.Arrays.asList;
import static junit.framework.Assert.assertEquals;
import static org.motechproject.util.DateUtil.today;

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

    private void verifyInactivePatientSummary(PatientSummary patientSummary, Patient inactivePatient) {
        verifyActivePatientSummary(patientSummary, inactivePatient);
        verifyDates(patientSummary.getTreatmentClosingDate(), inactivePatient.getCurrentTreatment().getEndDate());
        assertEquals(patientSummary.getTreatmentOutcome(), inactivePatient.getCurrentTreatment().getTreatmentOutcome().getOutcome());
    }

    private void verifyActivePatientSummary(PatientSummary patientSummary, Patient patient) {
        assertEquals(patientSummary.getAge(), patient.getAge());
        assertEquals(patientSummary.getCpTreatmentProgress(), patient.getCPProgress());
        assertEquals(patientSummary.getCumulativeMissedDoses(), Integer.valueOf(patient.getCumulativeDosesNotTaken()));
        assertEquals(patientSummary.getDiseaseClass(), patient.getCurrentTherapy().getDiseaseClass().value());
        assertEquals(patientSummary.getIpTreatmentProgress(), patient.getIPProgress());
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
        verifyDates(patientSummary.getTbRegistrationDate(), patient.getCurrentTreatment().getStartDate());
        verifyDates(patientSummary.getTreatmentStartDate(), patient.getCurrentTherapy().getStartDate());
    }

    private void verifyDates(String formattedDate, LocalDate date) {
        if (StringUtils.hasText(formattedDate)) {
            if (date != null) {
                assertEquals(formattedDate, new WHPDate(date).value());
            }
        }
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