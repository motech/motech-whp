package org.motechproject.whp.patient.reporting;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.motechproject.whp.common.domain.Phase;
import org.motechproject.whp.common.domain.alerts.PatientAlertType;
import org.motechproject.whp.common.util.WHPDateUtil;
import org.motechproject.whp.patient.alerts.processor.CumulativeMissedDosesCalculator;
import org.motechproject.whp.patient.builder.PatientBuilder;
import org.motechproject.whp.patient.builder.TherapyBuilder;
import org.motechproject.whp.patient.builder.TreatmentBuilder;
import org.motechproject.whp.patient.domain.Address;
import org.motechproject.whp.patient.domain.Patient;
import org.motechproject.whp.patient.domain.Therapy;
import org.motechproject.whp.patient.domain.Treatment;
import org.motechproject.whp.patient.domain.alerts.PatientAlerts;
import org.motechproject.whp.reports.contract.enums.YesNo;
import org.motechproject.whp.reports.contract.patient.*;

import java.util.List;

import static org.joda.time.DateTime.now;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;
import static org.motechproject.whp.common.util.WHPDateUtil.toSqlDate;


public class PatientReportingRequestMapperTest {

    private PatientReportingRequestMapper mapper;

    @Mock
    private CumulativeMissedDosesCalculator cumulativeMissedDosesCalculator;

    @Before
    public void setup() {
        initMocks(this);
        mapper = new PatientReportingRequestMapper(cumulativeMissedDosesCalculator);
    }

    @Test
    public void shouldMapPatientToReportingRequest() {
        Patient patient = new PatientBuilder().withDefaults().defaultPatientAlerts().build();
        patient.addTreatment(new TreatmentBuilder().withDefaults().build(), new TherapyBuilder().withDefaults().build(), now(), now());

        int expectedCumulativeMissedDoses = 12;
        when(cumulativeMissedDosesCalculator.getCumulativeMissedDoses(patient)).thenReturn(expectedCumulativeMissedDoses);

        PatientDTO patientDTO = mapper.mapToReportingRequest(patient);

        assertEquals(patient.getPatientId(), patientDTO.getPatientId());
        assertEquals(patient.getFirstName(), patientDTO.getFirstName());
        assertEquals(patient.getGender().name(), patientDTO.getGender());
        assertEquals(patient.getLastName(), patientDTO.getLastName());
        assertEquals(YesNo.value(patient.isOnActiveTreatment()).code(), patientDTO.getOnActiveTreatment());
        assertEquals(patient.getStatus().name(), patientDTO.getPatientStatus());
        assertEquals(patient.getPhi(), patientDTO.getPhi());
        assertEquals(patient.getPhoneNumber(), patientDTO.getPhoneNumber());


        AddressDTO addressDTO = patientDTO.getPatientAddress();
        Address patientAddress = patient.getCurrentTreatment().getPatientAddress();

        assertAddress(addressDTO, patientAddress);

        List<Therapy> allTherapies = patient.getTherapyHistory();
        allTherapies.add(patient.getCurrentTherapy());

        assertTherapies(allTherapies, patientDTO.getTherapies());

        PatientAlertsDTO patientAlertsDTO = patientDTO.getPatientAlerts();
        PatientAlerts patientAlerts = patient.getPatientAlerts();
        assertPatientAlerts(patientAlerts, patientAlertsDTO);

        TherapyDTO lastTherapyDTO = patientDTO.getTherapies().get(patientDTO.getTherapies().size() - 1);
        assertEquals((Integer) expectedCumulativeMissedDoses, lastTherapyDTO.getCumulativeMissedDoses());
    }

    private void assertAddress(AddressDTO addressDTO, Address patientAddress) {
        assertEquals(patientAddress.getAddress_block(), addressDTO.getBlock());
        assertEquals(patientAddress.getAddress_district(), addressDTO.getDistrict());
        assertEquals(patientAddress.getAddress_landmark(), addressDTO.getLandmark());
        assertEquals(patientAddress.getAddress_location(), addressDTO.getLocation());
        assertEquals(patientAddress.getAddress_state(), addressDTO.getState());
        assertEquals(patientAddress.getAddress_village(), addressDTO.getVillage());
    }

    private void assertPatientAlerts(PatientAlerts patientAlerts, PatientAlertsDTO patientAlertsDTO) {
        assertEquals((int) patientAlerts.getAlert(PatientAlertType.AdherenceMissing).getValue(), patientAlertsDTO.getAdherenceMissingWeeks());
        assertEquals(patientAlerts.getAlert(PatientAlertType.AdherenceMissing).getAlertSeverity(), patientAlertsDTO.getAdherenceMissingWeeksAlertSeverity());
        assertEquals(WHPDateUtil.toSqlDate(patientAlerts.getAlert(PatientAlertType.AdherenceMissing).getAlertDate()), patientAlertsDTO.getAdherenceMissingWeeksAlertDate());

        assertEquals((int) patientAlerts.getAlert(PatientAlertType.CumulativeMissedDoses).getValue(), patientAlertsDTO.getCumulativeMissedDoses());
        assertEquals(patientAlerts.getAlert(PatientAlertType.CumulativeMissedDoses).getAlertSeverity(), patientAlertsDTO.getCumulativeMissedDosesAlertSeverity());
        assertEquals(WHPDateUtil.toSqlDate(patientAlerts.getAlert(PatientAlertType.CumulativeMissedDoses).getAlertDate()), patientAlertsDTO.getCumulativeMissedDosesAlertDate());

        assertEquals((int) patientAlerts.getAlert(PatientAlertType.TreatmentNotStarted).getValue(), patientAlertsDTO.getTreatmentNotStarted());
        assertEquals(patientAlerts.getAlert(PatientAlertType.TreatmentNotStarted).getAlertSeverity(), patientAlertsDTO.getTreatmentNotStartedAlertSeverity());
        assertEquals(WHPDateUtil.toSqlDate(patientAlerts.getAlert(PatientAlertType.TreatmentNotStarted).getAlertDate()), patientAlertsDTO.getTreatmentNotStartedAlertDate());
    }

    private void assertTherapies(List<Therapy> therapies, List<TherapyDTO> therapyDTOs) {
        int size = therapies.size();
        assertEquals(size, therapyDTOs.size());
        boolean isLastTherapy = false;

        for(int i=0; i < size; i++) {

            if(i == size - 1){
                isLastTherapy = true;
            }

            TherapyDTO therapyDTO = therapyDTOs.get(i);
            Therapy therapy = therapies.get(i);

            assertEquals(WHPDateUtil.toSqlDate(therapy.getStartDate()), therapyDTO.getStartDate());
            assertEquals(WHPDateUtil.toSqlDate(therapy.getCloseDate()), therapyDTO.getCloseDate());
            assertEquals(toSqlDate(therapy.getCreationDate()), therapyDTO.getCreationDate());
            assertEquals(therapy.getUid(), therapyDTO.getTherapyId());
            assertEquals(therapy.getPatientAge(), therapyDTO.getPatientAge());
            assertEquals(therapy.getStatus().name(), therapyDTO.getStatus());

            assertEquals(therapy.getCurrentPhaseName(), therapyDTO.getCurrentPhase());
            assertEquals(therapy.getDiseaseClass().value(), therapyDTO.getDiseaseClass());

            assertEquals(WHPDateUtil.toSqlDate(therapy.getPhaseStartDate(Phase.CP)), therapyDTO.getCpStartDate());
            assertEquals(WHPDateUtil.toSqlDate(therapy.getPhaseStartDate(Phase.IP)), therapyDTO.getIpStartDate());
            assertEquals(WHPDateUtil.toSqlDate(therapy.getPhaseStartDate(Phase.EIP)), therapyDTO.getEipStartDate());

            assertEquals(WHPDateUtil.toSqlDate(therapy.getPhaseEndDate(Phase.CP)), therapyDTO.getCpEndDate());
            assertEquals(WHPDateUtil.toSqlDate(therapy.getPhaseEndDate(Phase.IP)), therapyDTO.getIpEndDate());
            assertEquals(WHPDateUtil.toSqlDate(therapy.getPhaseEndDate(Phase.EIP)), therapyDTO.getEipEndDate());

            assertEquals(therapy.getPhase(Phase.CP).getNumberOfDosesTaken(), therapyDTO.getCpPillsTaken());
            assertEquals(therapy.getPhase(Phase.IP).getNumberOfDosesTaken(), therapyDTO.getIpPillsTaken());
            assertEquals(therapy.getPhase(Phase.EIP).getNumberOfDosesTaken(), therapyDTO.getEipPillsTaken());

            assertEquals(therapy.getPhase(Phase.CP).remainingDoses(therapy.getTreatmentCategory()), therapyDTO.getCpPillsRemaining());
            assertEquals(therapy.getPhase(Phase.IP).remainingDoses(therapy.getTreatmentCategory()), therapyDTO.getIpPillsRemaining());
            assertEquals(therapy.getPhase(Phase.EIP).remainingDoses(therapy.getTreatmentCategory()), therapyDTO.getEipPillsRemaining());

            assertEquals((Integer) therapy.getTotalDoesIn(Phase.CP), therapyDTO.getCpTotalDoses());
            assertEquals((Integer) therapy.getTotalDoesIn(Phase.IP), therapyDTO.getIpTotalDoses());
            assertEquals((Integer) therapy.getTotalDoesIn(Phase.EIP), therapyDTO.getEipTotalDoses());

            assertEquals(therapy.getTreatmentCategory().getName(), therapyDTO.getTreatmentCategory());

            assertCurrentTherapyFlag(isLastTherapy, therapyDTO);

            assertTreatments(therapy.getAllTreatments(), therapyDTO.getTreatments(), isLastTherapy);
        }
    }

    private void assertCurrentTherapyFlag(boolean lastTherapy, TherapyDTO therapyDTO) {
        String expected = lastTherapy?  YesNo.Yes.code() :  YesNo.No.code();
        assertEquals(expected, therapyDTO.getCurrentTherapy());
    }

    private void assertTreatments(List<Treatment> treatments, List<TreatmentDTO> treatmentDTOs, boolean isLastTherapy) {
        int size = treatments.size();
        assertEquals(size, treatmentDTOs.size());

        boolean isLastTreatment = false;
        for(int i=0; i < size; i++) {

            if(i == size - 1){
                isLastTreatment = true;
            }

            Treatment treatment = treatments.get(i);
            TreatmentDTO treatmentDTO = treatmentDTOs.get(i);
            assertEquals(WHPDateUtil.toSqlDate(treatment.getStartDate()), treatmentDTO.getStartDate());
            assertEquals(WHPDateUtil.toSqlDate(treatment.getEndDate()), treatmentDTO.getEndDate());
            assertEquals(treatment.getProviderId(), treatmentDTO.getProviderId());
            assertEquals(treatment.getTbId(), treatmentDTO.getTbId());
            assertEquals(treatment.getTbRegistrationNumber(), treatmentDTO.getTbRegistrationNumber());
            assertEquals(treatment.getTreatmentOutcome(), treatmentDTO.getTreatmentOutcome());
            assertEquals(treatment.getPatientType().name(), treatmentDTO.getPatientType());
            assertEquals(treatment.getProviderDistrict(), treatmentDTO.getProviderDistrict());
            assertEquals(YesNo.value(treatment.isPaused()).code(), treatmentDTO.getIsPaused());
            assertEquals(WHPDateUtil.toSqlDate(treatment.getInterruptions().getPauseDateForOngoingInterruption()), treatmentDTO.getPausedDate());
            assertEquals(treatment.getInterruptions().totalPausedDuration(), treatmentDTO.getTotalPausedDuration());
            assertEquals(treatment.getInterruptions().getPauseReasonForOngoingInterruption(), treatmentDTO.getReasonsForPause());
            assertEquals(treatment.getPreTreatmentSmearTestResult().value(), treatmentDTO.getPreTreatmentSmearTestResult());
            assertEquals(treatment.getPreTreatmentWeightRecord().getWeight(), treatmentDTO.getPreTreatmentWeight());

            assertCurrentTreatmentFlag(isLastTherapy, isLastTreatment, treatmentDTO);
        }
    }

    private void assertCurrentTreatmentFlag(boolean isLastTherapy, boolean lastTreatment, TreatmentDTO treatmentDTO) {
        String expected = isLastTherapy && lastTreatment ? YesNo.Yes.code() : YesNo.No.code();
        assertEquals(expected, treatmentDTO.getCurrentTreatment());
    }
}
