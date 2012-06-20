package org.motechproject.whp.patient.service;

import org.joda.time.LocalDate;
import org.motechproject.common.exception.WHPErrorCode;
import org.motechproject.whp.patient.command.UpdateCommandFactory;
import org.motechproject.whp.patient.command.UpdateScope;
import org.motechproject.whp.patient.contract.PatientRequest;
import org.motechproject.whp.patient.domain.Patient;
import org.motechproject.whp.patient.domain.Therapy;
import org.motechproject.whp.patient.domain.Treatment;
import org.motechproject.whp.patient.mapper.TherapyMapper;
import org.motechproject.whp.patient.repository.AllPatients;
import org.motechproject.whp.patient.repository.AllTherapies;
import org.motechproject.whp.refdata.domain.TreatmentOutcome;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

import static org.motechproject.whp.patient.mapper.PatientMapper.mapBasicInfo;
import static org.motechproject.whp.patient.mapper.PatientMapper.mapTreatment;

@Service
public class PatientService {

    private AllTherapies allTherapies;
    private AllPatients allPatients;
    private UpdateCommandFactory updateCommandFactory;

    @Autowired
    public PatientService(AllPatients allPatients, AllTherapies allTherapies, UpdateCommandFactory updateCommandFactory) {
        this.allPatients = allPatients;
        this.allTherapies = allTherapies;
        this.updateCommandFactory = updateCommandFactory;
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

    public void update(UpdateScope updateScope, PatientRequest patientRequest) {
        updateCommandFactory.updateFor(updateScope).apply(patientRequest);
    }

    public void startTherapy(String patientId, LocalDate firstDoseTakenDate) {
        Patient patient = allPatients.findByPatientId(patientId);
        patient.startTherapy(firstDoseTakenDate);
        allPatients.update(patient);
    }

    public boolean canBeTransferred(String patientId) {
        Patient patient = allPatients.findByPatientId(patientId);
        List<WHPErrorCode> errors = new ArrayList<WHPErrorCode>();
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
