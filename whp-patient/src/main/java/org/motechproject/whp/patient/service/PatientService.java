package org.motechproject.whp.patient.service;

import org.joda.time.LocalDate;
import org.motechproject.whp.patient.command.UpdateCommandFactory;
import org.motechproject.whp.patient.command.UpdateScope;
import org.motechproject.whp.patient.contract.PatientRequest;
import org.motechproject.whp.patient.domain.Patient;
import org.motechproject.whp.patient.domain.ProvidedTreatment;
import org.motechproject.whp.patient.domain.Therapy;
import org.motechproject.whp.patient.exception.WHPErrorCode;
import org.motechproject.whp.patient.mapper.TherapyMapper;
import org.motechproject.whp.patient.repository.AllPatients;
import org.motechproject.whp.patient.repository.AllTherapies;
import org.motechproject.whp.refdata.domain.TreatmentOutcome;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

import static org.motechproject.whp.patient.domain.criteria.UpdatePatientCriteria.sanityCheckFails;
import static org.motechproject.whp.patient.mapper.PatientMapper.mapBasicInfo;
import static org.motechproject.whp.patient.mapper.PatientMapper.mapProvidedTreatment;

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

        ProvidedTreatment providedTreatment = mapProvidedTreatment(patientRequest, therapy);
        patient.addProvidedTreatment(providedTreatment, patientRequest.getDate_modified());
        allPatients.add(patient);
    }

    public void update(UpdateScope updateScope, PatientRequest patientRequest) {
        updateCommandFactory.updateFor(updateScope).apply(patientRequest);
    }

    public void startTreatment(String patientId, LocalDate firstDoseTakenDate) {
        Patient patient = allPatients.findByPatientId(patientId);
        patient.latestTreatment().setStartDate(firstDoseTakenDate);
        allPatients.update(patient);
    }

    public boolean canBeTransferred(String patientId) {
        Patient patient = allPatients.findByPatientId(patientId);
        if (!sanityCheckFails(patient, new ArrayList<WHPErrorCode>())) {
            return TreatmentOutcome.TransferredOut.equals(patient.getCurrentProvidedTreatment().getTreatmentOutcome());
        }
        return false;
    }

}
