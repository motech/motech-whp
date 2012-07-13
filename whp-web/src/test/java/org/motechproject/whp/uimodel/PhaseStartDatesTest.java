package org.motechproject.whp.uimodel;

import org.joda.time.LocalDate;
import org.junit.Test;
import org.motechproject.whp.patient.builder.PatientBuilder;
import org.motechproject.whp.patient.domain.Patient;
import org.motechproject.whp.patient.domain.Therapy;
import org.motechproject.whp.common.domain.WHPConstants;

import static junit.framework.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class PhaseStartDatesTest {

    @Test
    public void shouldSetPhaseStartDatesFromPatientObject() {
        Patient patient = new PatientBuilder().withDefaults().build();
        patient.startTherapy(new LocalDate());
        patient.getCurrentTherapy().getPhases().setEIPStartDate(new LocalDate());
        patient.getCurrentTherapy().getPhases().setCPStartDate(new LocalDate());
        PhaseStartDates phaseStartDates = new PhaseStartDates(patient);
        Therapy therapy = patient.getCurrentTherapy();

        assertEquals(therapy.getPhases().getIPStartDate().toString(WHPConstants.DATE_FORMAT), phaseStartDates.getIpStartDate());
        assertEquals(therapy.getPhases().getEIPStartDate().toString(WHPConstants.DATE_FORMAT), phaseStartDates.getEipStartDate());
        assertEquals(therapy.getPhases().getCPStartDate().toString(WHPConstants.DATE_FORMAT), phaseStartDates.getCpStartDate());
    }

    @Test
    public void shouldMapNewPhaseInfoToPatient() {
        Patient patient = new PatientBuilder().withDefaults().build();
        PhaseStartDates phaseStartDates = new PhaseStartDates(patient);
        phaseStartDates.setIpStartDate("21/05/2012");
        phaseStartDates.setEipStartDate("22/05/2012");
        phaseStartDates.setCpStartDate("23/05/2012");
        phaseStartDates.mapNewPhaseInfoToPatient(patient);

        assertEquals(new LocalDate(2012, 5, 21), patient.getCurrentTherapy().getStartDate());
        assertEquals(patient.getCurrentTherapy().getPhases().getIPStartDate(), patient.getCurrentTherapy().getStartDate());
        assertEquals(new LocalDate(2012, 5, 22), patient.getCurrentTherapy().getPhases().getEIPStartDate());
        assertEquals(new LocalDate(2012, 5, 23), patient.getCurrentTherapy().getPhases().getCPStartDate());
    }

    @Test
    public void shouldResetTreatmentStartDateIfIPStartDateIsEmpty() {
        Patient patient = new PatientBuilder().withDefaults().build();
        patient.startTherapy(new LocalDate());
        PhaseStartDates phaseStartDates = new PhaseStartDates(patient);
        phaseStartDates.setIpStartDate("");
        phaseStartDates.setEipStartDate("22/05/2012");
        phaseStartDates.setCpStartDate("23/05/2012");
        phaseStartDates.mapNewPhaseInfoToPatient(patient);

        assertNull(patient.getCurrentTherapy().getStartDate());
    }

    @Test
    public void shouldSetPhaseStartDatesToNullIfFormDataIsEmpty() {
        Patient patient = new PatientBuilder().withDefaults().build();
        patient.startTherapy(new LocalDate());
        patient.getCurrentTherapy().getPhases().setEIPStartDate(new LocalDate());
        patient.getCurrentTherapy().getPhases().setCPStartDate(new LocalDate());
        PhaseStartDates phaseStartDates = new PhaseStartDates(patient);
        phaseStartDates.setIpStartDate("");
        phaseStartDates.setEipStartDate("");
        phaseStartDates.setCpStartDate("");
        phaseStartDates.mapNewPhaseInfoToPatient(patient);

        assertNull(patient.getCurrentTherapy().getStartDate());
        assertNull(patient.getCurrentTherapy().getPhases().getIPStartDate());
        assertNull(patient.getCurrentTherapy().getPhases().getEIPStartDate());
        assertNull(patient.getCurrentTherapy().getPhases().getCPStartDate());
    }

}
