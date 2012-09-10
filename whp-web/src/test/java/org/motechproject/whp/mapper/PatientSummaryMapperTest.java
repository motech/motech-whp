package org.motechproject.whp.mapper;

import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.junit.Test;
import org.motechproject.whp.common.util.WHPDate;
import org.motechproject.whp.patient.builder.PatientBuilder;
import org.motechproject.whp.patient.domain.Patient;
import org.motechproject.whp.refdata.domain.Phase;
import org.motechproject.whp.refdata.domain.TreatmentOutcome;
import org.motechproject.whp.uimodel.PatientSummary;
import org.springframework.util.StringUtils;

import java.util.List;

import static java.util.Arrays.asList;
import static junit.framework.Assert.assertEquals;
import static org.motechproject.util.DateUtil.today;

public class PatientSummaryMapperTest {

    @Test
    public void shouldMapAllPatientFields_forPatientOnActiveTreatment() {
        Patient activePatient = createActivePatient();
        PatientSummaryMapper patientSummaryMapper = new PatientSummaryMapper();

        List<PatientSummary> patientSummaries = patientSummaryMapper.map(asList(activePatient));

        verifyActivePatientSummary(patientSummaries.get(0), activePatient, patientSummaryMapper);
    }

    @Test
    public void shouldMapAllPatientFields_forPatientOnInactiveTreatment() {
        Patient inactivePatient = createInactivePatient();
        PatientSummaryMapper patientSummaryMapper = new PatientSummaryMapper();

        List<PatientSummary> patientSummaries = patientSummaryMapper.map(asList(inactivePatient));

        verifyInactivePatientSummary(patientSummaries.get(0), inactivePatient, patientSummaryMapper);
    }

    private void verifyInactivePatientSummary(PatientSummary patientSummary, Patient inactivePatient, PatientSummaryMapper patientSummaryMapper) {
        verifyActivePatientSummary(patientSummary, inactivePatient, patientSummaryMapper);
        verifyDates(patientSummary.getTreatmentClosingDate(), inactivePatient.getCurrentTreatment().getEndDate());
        assertEquals(patientSummary.getTreatmentOutcome(), inactivePatient.getCurrentTreatment().getTreatmentOutcome());
    }

    private void verifyActivePatientSummary(PatientSummary patientSummary, Patient patient, PatientSummaryMapper patientSummaryMapper) {
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
        verifyDates(patientSummary.getTbRegistrationDate(), patient.getCurrentTreatment().getStartDate());
        assertEquals(patientSummary.getTreatmentCategory(), patient.getCurrentTherapy().getTreatmentCategory().getName());
        verifyDates(patientSummary.getTreatmentStartDate(), patient.getCurrentTherapy().getStartDate());
        assertEquals(patientSummary.getVillage(), patient.getCurrentTreatment().getPatientAddress().getAddress_village());
        assertEquals(patientSummary.getProviderId(), patient.getCurrentTreatment().getProviderId());
        assertEquals(patientSummary.getProviderDistrict(), "");
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