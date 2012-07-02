package org.motechproject.whp.treatmentcard.domain;

import org.junit.Test;
import org.motechproject.whp.patient.builder.PatientBuilder;
import org.motechproject.whp.patient.domain.Patient;
import org.motechproject.whp.refdata.domain.PhaseName;

import static junit.framework.Assert.assertEquals;
import static org.motechproject.util.DateUtil.newDate;
import static org.motechproject.util.DateUtil.today;

public class TreatmentCardTest {

    @Test
    public void shouldReturnTodaysDateIfPatientIsOnIP() {
        Patient patient = PatientBuilder.patient();
        patient.startTherapy(today().minusMonths(5));
        assertEquals(today(), new TreatmentCard(patient).ipBoxAdherenceEndDate());
    }

    @Test
    public void shouldReturnIPEndDateIfPatientHasCompletedIPAndNextPhaseIsUnknown() {
        Patient patient = PatientBuilder.patient();
        patient.startTherapy(today().minusMonths(5));
        patient.endCurrentPhase(today().minusMonths(4));
        assertEquals(today().minusMonths(4), new TreatmentCard(patient).ipBoxAdherenceEndDate());
    }

    @Test
    public void shouldReturnIPEndDateIfPatientHasCompletedIPAndNextPhaseIsKnown() {
        Patient patient = PatientBuilder.patient();
        patient.startTherapy(today().minusMonths(5));
        patient.endCurrentPhase(today().minusMonths(4));
        patient.nextPhaseName(PhaseName.EIP);
        assertEquals(today().minusMonths(4), new TreatmentCard(patient).ipBoxAdherenceEndDate());
    }

    @Test
    public void shouldReturnTodaysDateIfPatientIsOnEIP() {
        Patient patient = PatientBuilder.patient();
        patient.startTherapy(today().minusMonths(5));
        patient.endCurrentPhase(today().minusMonths(4));
        patient.nextPhaseName(PhaseName.EIP);
        patient.startNextPhase();
        assertEquals(today(), new TreatmentCard(patient).ipBoxAdherenceEndDate());
    }

    @Test
    public void shouldReturnEIPEnDateIfPatientHasCompletedEIP() {
        Patient patient = PatientBuilder.patient();
        patient.startTherapy(today().minusMonths(5));
        patient.endCurrentPhase(today().minusMonths(4));
        patient.nextPhaseName(PhaseName.EIP);
        patient.startNextPhase();
        patient.endCurrentPhase(today().minusMonths(3));
        assertEquals(today().minusMonths(3), new TreatmentCard(patient).ipBoxAdherenceEndDate());
    }

    @Test
    public void shouldReturnTodaysDateIfPatientIsStillOnCP() {
        Patient patient = PatientBuilder.patient();
        patient.startTherapy(today().minusMonths(5));
        patient.endCurrentPhase(today().minusMonths(4));
        patient.nextPhaseName(PhaseName.CP);
        patient.startNextPhase();
        assertEquals(today(), new TreatmentCard(patient).cpBoxAdherenceEndDate());
    }

    @Test
    public void shouldReturnCPEndDateIfPatientHasCompletedCP() {
        Patient patient = PatientBuilder.patient();
        patient.startTherapy(today().minusMonths(5));
        patient.endCurrentPhase(today().minusMonths(4));
        patient.nextPhaseName(PhaseName.CP);
        patient.startNextPhase();
        patient.endCurrentPhase(today().minusMonths(2));
        assertEquals(today().minusMonths(2), new TreatmentCard(patient).cpBoxAdherenceEndDate());
    }

    @Test
    public void ipBoxLastDateShouldBe5MonthsFromStartDate() {
        Patient patient = PatientBuilder.patient();
        patient.startTherapy(today().minusMonths(5));
        assertEquals(today().minusDays(1), new TreatmentCard(patient).ipBoxLastDoseDate());
    }

    @Test
    public void cpBoxLastDateShouldBe7MonthsFromStartDate() {
        Patient patient = PatientBuilder.patient();
        patient.startTherapy(newDate(2012, 1, 1));
        patient.endCurrentPhase(newDate(2012, 2, 1));
        patient.nextPhaseName(PhaseName.CP);
        patient.startNextPhase();
        patient.endCurrentPhase(newDate(2012, 4, 1));

        assertEquals(newDate(2012, 9, 1), new TreatmentCard(patient).cpBoxLastDoseDate());
    }

}
