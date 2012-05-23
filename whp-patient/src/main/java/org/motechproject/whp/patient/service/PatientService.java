package org.motechproject.whp.patient.service;

import org.joda.time.LocalDate;
import org.motechproject.whp.patient.command.UpdateCommandFactory;
import org.motechproject.whp.patient.contract.PatientRequest;
import org.motechproject.whp.patient.domain.Patient;
import org.motechproject.whp.patient.domain.ProvidedTreatment;
import org.motechproject.whp.patient.domain.Treatment;
import org.motechproject.whp.patient.mapper.TreatmentMapper;
import org.motechproject.whp.patient.repository.AllPatients;
import org.motechproject.whp.patient.repository.AllTreatments;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static org.motechproject.whp.patient.mapper.PatientMapper.mapBasicInfo;
import static org.motechproject.whp.patient.mapper.PatientMapper.mapProvidedTreatment;

@Service
public class PatientService {

    private AllTreatments allTreatments;
    private AllPatients allPatients;
    private UpdateCommandFactory factory;

    @Autowired
    public PatientService(AllPatients allPatients, AllTreatments allTreatments, UpdateCommandFactory factory) {
        this.allPatients = allPatients;
        this.allTreatments = allTreatments;
        this.factory = factory;
    }

    public void createPatient(PatientRequest patientRequest) {
        Patient patient = mapBasicInfo(patientRequest);

        Treatment treatment = TreatmentMapper.map(patientRequest);
        allTreatments.add(treatment);

        ProvidedTreatment providedTreatment = mapProvidedTreatment(patientRequest, treatment);
        patient.addProvidedTreatment(providedTreatment, patientRequest.getDate_modified());
        allPatients.add(patient);
    }

    public void update(String updateCommand, PatientRequest patientRequest) {
        factory.updateFor(updateCommand).apply(patientRequest);
    }

    public void startTreatment(String patientId, LocalDate firstDoseTakenDate) {
        Patient patient = allPatients.findByPatientId(patientId);
        patient.getCurrentProvidedTreatment().getTreatment().setStartDate(firstDoseTakenDate);
        allPatients.update(patient);
    }

}
