package org.motechproject.whp.migration.v1.mapper;

import org.joda.time.LocalDate;
import org.motechproject.model.DayOfWeek;
import org.motechproject.util.DateUtil;
import org.motechproject.whp.adherence.domain.Adherence;
import org.motechproject.whp.adherence.domain.AdherenceList;
import org.motechproject.whp.adherence.service.WHPAdherenceService;
import org.motechproject.whp.migration.v0.domain.*;
import org.motechproject.whp.patient.domain.*;
import org.motechproject.whp.refdata.domain.*;

import java.util.ArrayList;
import java.util.List;

import static org.motechproject.util.DateUtil.today;
import static org.motechproject.whp.common.domain.TreatmentWeekInstance.week;
import static org.motechproject.whp.common.util.WHPDateUtil.isOnOrBefore;

public class PatientV1Mapper {

    private WHPAdherenceService whpAdherenceService;

    private PatientV0 patientV0;
    private Patient patient;

    public PatientV1Mapper(PatientV0 patientV0, WHPAdherenceService whpAdherenceService) {
        this.patientV0 = patientV0;
        this.whpAdherenceService = whpAdherenceService;
    }

    public Patient map() {
        patient = new Patient();
        mapBasicInfo();
        mapCurrentTherapy();
        mapTherapyHistory();

        return patient;
    }

    private void mapTherapyHistory() {
        ArrayList<Therapy> therapies = new ArrayList<>();
        List<TherapyV0> therapyV0List = patientV0.getTherapyHistory();
        for (TherapyV0 therapyV0 : therapyV0List) {
            therapies.add(mapTherapy(therapyV0));
        }
        patient.setTherapyHistory(therapies);
    }

    private void mapCurrentTherapy() {
        TherapyV0 therapyV0 = patientV0.getCurrentTreatment().getTherapy();
        Therapy currentTherapy = mapTherapy(therapyV0);
        patient.setCurrentTherapy(currentTherapy);
    }

    private Therapy mapTherapy(TherapyV0 therapyV0) {
        Therapy therapy = new Therapy();
        mapBasicTherapyInfo(therapyV0, therapy);
        mapTreatments(therapyV0, therapy);
        mapPhases(therapy);
        return therapy;
    }

    private void mapPhases(Therapy therapy) {
        AdherenceList adherenceList = whpAdherenceService.getAdherenceSortedByDate(patient.getPatientId(), therapy.getUid());
        if (adherenceList.size() > 0) {
            Phases phases = therapy.getPhases();
            PhaseRecord ipPhaseRecord = phases.getPhaseRecords().get(Phase.IP);
            Adherence firstLog = adherenceList.get(0);
            ipPhaseRecord.setStartDate(firstLog.getPillDate());
            updateTotalDoseTakenCountTillToday(ipPhaseRecord, adherenceList);
            updateTotalDoseTakenCountTillSunday(ipPhaseRecord, adherenceList);

            phases.getHistory().add(Phase.IP);
        }
    }

    private void updateTotalDoseTakenCountTillToday(PhaseRecord phaseRecord, AdherenceList adherenceList) {
        phaseRecord.setNumberOfDosesTaken(adherenceCount(adherenceList, today()), today());
    }

    private void updateTotalDoseTakenCountTillSunday(PhaseRecord phaseRecord, AdherenceList adherenceList) {
        LocalDate endDate = DateUtil.today();
        LocalDate sundayBeforeEndDate = week(endDate).dateOf(DayOfWeek.Sunday);
        phaseRecord.setNumberOfDosesTaken(adherenceCount(adherenceList, sundayBeforeEndDate), sundayBeforeEndDate);
    }

    private int adherenceCount(AdherenceList adherenceList, LocalDate asOfDate) {
        ArrayList<Adherence> filteredList = new ArrayList<>();
        for (Adherence adherence : adherenceList) {
            if (isOnOrBefore(adherence.getPillDate(), asOfDate))
                filteredList.add(adherence);
        }
        return filteredList.size();
    }

    private void mapTreatments(TherapyV0 therapyV0, Therapy therapy) {
        List<TreatmentV0> treatmentV0List = patientV0.getTreatments(therapyV0.getId());
        if (treatmentV0List.size() > 0) {
            therapy.setCurrentTreatment(map(treatmentV0List.get(0)));
        }

        treatmentV0List.remove(0);
        List<Treatment> treatmentList = new ArrayList<>();
        for (TreatmentV0 treatmentV0 : treatmentV0List) {
            treatmentList.add(map(treatmentV0));
        }

        therapy.setTreatments(treatmentList);
    }

    private Treatment map(TreatmentV0 treatmentV0) {
        Treatment treatment = new Treatment();
        treatment.setProviderId(treatmentV0.getProviderId());
        treatment.setTbId(treatmentV0.getTbId());
        treatment.setStartDate(treatmentV0.getStartDate());
        treatment.setEndDate(treatmentV0.getEndDate());
        treatment.setPatientAddress(map(treatmentV0.getPatientAddress()));
        treatment.setTreatmentOutcome(treatmentV0.getTreatmentOutcome());
        treatment.setPatientType(PatientType.valueOf(treatmentV0.getPatientType().name()));
        treatment.setTbRegistrationNumber(treatmentV0.getTbRegistrationNumber());
        treatment.setSmearTestResults(map(treatmentV0.getSmearTestResults()));
        treatment.setWeightStatistics(map(treatmentV0.getWeightStatistics()));
        treatment.setInterruptions(map(treatmentV0.getInterruptions()));
        return treatment;
    }

    private TreatmentInterruptions map(TreatmentInterruptionsV0 interruptionsV0) {
        TreatmentInterruptions interruptions = new TreatmentInterruptions();
        for (TreatmentInterruptionV0 interruptionV0 : interruptionsV0) {
            TreatmentInterruption interruption = new TreatmentInterruption(interruptionV0.getReasonForPause(), interruptionV0.getPauseDate());
            interruption.setReasonForResumption(interruptionV0.getReasonForResumption());
            interruption.setResumptionDate(interruptionV0.getResumptionDate());
            interruptions.add(interruption);
        }
        return interruptions;
    }

    private WeightStatistics map(WeightStatisticsV0 weightStatisticsV0) {
        WeightStatistics weightStatistics = new WeightStatistics();
        for (WeightStatisticsRecordV0 weightStatisticsRecordV0 : weightStatisticsV0.getAll()) {
            weightStatistics.add(map(weightStatisticsRecordV0));
        }
        return weightStatistics;
    }

    private WeightStatisticsRecord map(WeightStatisticsRecordV0 weightStatisticsRecordV0) {
        return new WeightStatisticsRecord(
                SampleInstance.valueOf(weightStatisticsRecordV0.getWeight_instance().name()),
                weightStatisticsRecordV0.getWeight(),
                weightStatisticsRecordV0.getMeasuringDate());
    }

    private SmearTestResults map(SmearTestResultsV0 smearTestResultsV0) {
        SmearTestResults smearTestResults = new SmearTestResults();
        for (SmearTestRecordV0 smearTestRecordV0 : smearTestResultsV0.getAll()) {
            smearTestResults.add(map(smearTestRecordV0));
        }
        return smearTestResults;
    }

    private SmearTestRecord map(SmearTestRecordV0 smearTestRecordV0) {
        return new SmearTestRecord(SampleInstance.valueOf(smearTestRecordV0.getSmear_sample_instance().name()),
                smearTestRecordV0.getSmear_test_date_1(),
                SmearTestResult.valueOf(smearTestRecordV0.getSmear_test_result_1().name()),
                smearTestRecordV0.getSmear_test_date_2(),
                SmearTestResult.valueOf(smearTestRecordV0.getSmear_test_result_2().name()));
    }

    private Address map(AddressV0 addressV0) {
        Address address = new Address();
        address.setAddress_block(addressV0.getAddress_block());
        address.setAddress_district(addressV0.getAddress_district());
        address.setAddress_landmark(addressV0.getAddress_landmark());
        address.setAddress_location(addressV0.getAddress_location());
        address.setAddress_state(addressV0.getAddress_state());
        address.setAddress_village(addressV0.getAddress_village());
        return address;
    }

    private void mapBasicTherapyInfo(TherapyV0 therapyV0, Therapy therapy) {
        therapy.setUid(therapyV0.getId());
        therapy.setPatientAge(therapyV0.getPatientAge());
        therapy.setCreationDate(therapyV0.getCreationDate());
        therapy.setStartDate(therapyV0.getStartDate());
        therapy.setCloseDate(therapyV0.getCloseDate());
        therapy.setStatus(TherapyStatus.valueOf(therapyV0.getStatus().name()));
        therapy.setTreatmentCategory(treatmentCategory(therapyV0.getTreatmentCategory()));
        therapy.setDiseaseClass(DiseaseClass.valueOf(therapyV0.getDiseaseClass().name()));
    }

    private TreatmentCategory treatmentCategory(TreatmentCategoryV0 treatmentCategoryV0) {
        return new TreatmentCategory(treatmentCategoryV0.getName(), treatmentCategoryV0.getCode(), treatmentCategoryV0.getDosesPerWeek(),
                treatmentCategoryV0.getNumberOfWeeksOfIP(), treatmentCategoryV0.getNumberOfDosesInIP(),
                treatmentCategoryV0.getNumberOfWeeksOfEIP(), treatmentCategoryV0.getNumberOfDosesInEIP(),
                treatmentCategoryV0.getNumberOfWeeksOfCP(), treatmentCategoryV0.getNumberOfDosesInCP(),
                treatmentCategoryV0.getPillDays());
    }

    private void mapBasicInfo() {
        patient.setPatientId(patientV0.getPatientId());
        patient.setFirstName(patientV0.getFirstName());
        patient.setLastName(patientV0.getLastName());
        patient.setGender(Gender.valueOf(patientV0.getGender().name()));
        patient.setPhoneNumber(patientV0.getPhoneNumber());
        patient.setPhi(patientV0.getPhi());
        patient.setStatus(PatientStatus.valueOf(patientV0.getStatus().name()));
        patient.setLastModifiedDate(patientV0.getLastModifiedDate());
        patient.setOnActiveTreatment(patientV0.isOnActiveTreatment());
        patient.setMigrated(patientV0.isMigrated());
    }
}
