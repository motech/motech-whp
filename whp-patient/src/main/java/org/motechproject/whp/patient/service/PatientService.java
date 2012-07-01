package org.motechproject.whp.patient.service;

import org.joda.time.LocalDate;
import org.motechproject.whp.common.exception.WHPErrorCode;
import org.motechproject.whp.patient.command.UpdateCommandFactory;
import org.motechproject.whp.patient.command.UpdateScope;
import org.motechproject.whp.patient.contract.PatientRequest;
import org.motechproject.whp.patient.domain.Patient;
import org.motechproject.whp.refdata.domain.PhaseName;
import org.motechproject.whp.patient.domain.Therapy;
import org.motechproject.whp.patient.domain.Treatment;
import org.motechproject.whp.patient.mapper.TherapyMapper;
import org.motechproject.whp.patient.repository.AllPatients;
import org.motechproject.whp.patient.repository.AllTherapies;
import org.motechproject.whp.refdata.domain.TreatmentOutcome;
import org.motechproject.whp.validation.RequestValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

import static org.apache.commons.lang.StringUtils.isNotEmpty;
import static org.motechproject.whp.patient.mapper.PatientMapper.mapBasicInfo;
import static org.motechproject.whp.patient.mapper.PatientMapper.mapTreatment;

@Service
public class PatientService {

    private AllTherapies allTherapies;
    private AllPatients allPatients;
    private UpdateCommandFactory updateCommandFactory;
    private RequestValidator validator;

    @Autowired
    public PatientService(AllPatients allPatients,
                          AllTherapies allTherapies,
                          UpdateCommandFactory updateCommandFactory,
                          RequestValidator validator) {
        this.allPatients = allPatients;
        this.allTherapies = allTherapies;
        this.updateCommandFactory = updateCommandFactory;
        this.validator = validator;
    }

    public void createPatient(PatientRequest patientRequest) {
        Patient patient = mapBasicInfo(patientRequest);

        Therapy therapy = TherapyMapper.map(patientRequest);
        allTherapies.add(therapy);

        Treatment treatment = mapTreatment(patientRequest, therapy);
        patient.addTreatment(treatment, patientRequest.getDate_modified());
        patient.setOnActiveTreatment(true);
        allPatients.add(patient);
    }

    public void update(PatientRequest patientRequest) {
        UpdateScope updateScope = patientRequest.updateScope(canBeTransferred(patientRequest.getCase_id()));
        validator.validate(patientRequest, updateScope.name());
        updateCommandFactory.updateFor(updateScope).apply(patientRequest);
    }

    public void startTherapy(String patientId, LocalDate firstDoseTakenDate) {
        Patient patient = allPatients.findByPatientId(patientId);
        patient.startTherapy(firstDoseTakenDate);
        allPatients.update(patient);
    }

    public void updatePillTakenCount(Patient patient, PhaseName name, int dosesTaken) {
        patient.currentTherapy().getPhases().getByPhaseName(name).setNumberOfDosesTaken(dosesTaken);
        allPatients.update(patient);
    }

    public void setNextPhaseName(String patientId, PhaseName nextPhase) {
        Patient patient = allPatients.findByPatientId(patientId);
        patient.nextPhaseName(nextPhase);
        allPatients.update(patient);
    }

    public void endCurrentPhase(String patientId, LocalDate endDate) {
        Patient patient = allPatients.findByPatientId(patientId);
        patient.endCurrentPhase(endDate);
        allPatients.update(patient);
    }

    public void startNextPhase(String patientId) {
        Patient patient = allPatients.findByPatientId(patientId);
        patient.startNextPhase();
        allPatients.update(patient);
    }

    public List<Patient> getAllWithActiveTreatmentForProvider(String providerId) {
        return allPatients.getAllWithActiveTreatmentFor(providerId);
    }

    public Patient findByPatientId(String patientId) {
        return allPatients.findByPatientId(patientId);
    }

    public void update(Patient updatedPatient) {
        allPatients.update(updatedPatient);
    }

    public List<Patient> searchBy(String districtName, String providerId) {
        if (isNotEmpty(providerId))
            return allPatients.getAllWithActiveTreatmentForDistrictAndProvider(districtName, providerId);
        else
            return allPatients.getAllWithActiveTreatmentForDistrict(districtName);
    }

    boolean canBeTransferred(String patientId) {
        Patient patient = allPatients.findByPatientId(patientId);
        List<WHPErrorCode> errors = new ArrayList<>();
        if (patient == null) {
            errors.add(WHPErrorCode.CASE_ID_DOES_NOT_EXIST);
            return false;
        } else if (!patient.hasCurrentTreatment()) {
            errors.add(WHPErrorCode.NO_EXISTING_TREATMENT_FOR_CASE);
            return false;
        } else {
            return TreatmentOutcome.TransferredOut.equals(patient.getCurrentTreatment().getTreatmentOutcome());
        }
    }
}
