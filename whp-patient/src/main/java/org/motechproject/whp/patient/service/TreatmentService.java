package org.motechproject.whp.patient.service;

import org.motechproject.whp.patient.contract.PatientRequest;
import org.motechproject.whp.patient.domain.Patient;
import org.motechproject.whp.patient.domain.Therapy;
import org.motechproject.whp.patient.domain.Treatment;
import org.motechproject.whp.patient.repository.AllPatients;
import org.motechproject.whp.patient.repository.AllTherapies;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static org.motechproject.whp.patient.mapper.PatientMapper.createNewTreatmentForTreatmentCategoryChange;
import static org.motechproject.whp.patient.mapper.TherapyMapper.createNewTreatment;

@Service
public class TreatmentService {

    private AllPatients allPatients;
    private AllTherapies allTherapies;
    private ProviderService providerService;

    @Autowired
    public TreatmentService(AllPatients allPatients, AllTherapies allTherapies, ProviderService providerService) {
        this.allPatients = allPatients;
        this.allTherapies = allTherapies;
        this.providerService = providerService;
    }

    public void openTreatment(PatientRequest patientRequest) {
        Patient patient = allPatients.findByPatientId(patientRequest.getCase_id());

        Therapy newTherapy = createNewTreatment(patient, patientRequest);
        allTherapies.add(newTherapy);

        Treatment newTreatment = createNewTreatmentForTreatmentCategoryChange(patient, patientRequest, newTherapy);
        patient.addTreatment(newTreatment, patientRequest.getDate_modified());

        allPatients.update(patient);
    }

    public void closeTreatment(PatientRequest patientRequest) {
        Patient patient = allPatients.findByPatientId(patientRequest.getCase_id());
        patient.closeCurrentTreatment(patientRequest.getTreatment_outcome(), patientRequest.getDate_modified());
        allPatients.update(patient);
    }

    public void pauseTreatment(PatientRequest patientRequest) {
        Patient patient = allPatients.findByPatientId(patientRequest.getCase_id());
        patient.pauseCurrentTreatment(patientRequest.getReason(), patientRequest.getDate_modified());
        allPatients.update(patient);
    }

    public void restartTreatment(PatientRequest patientRequest) {
        Patient patient = allPatients.findByPatientId(patientRequest.getCase_id());
        patient.restartCurrentTreatment(patientRequest.getReason(), patientRequest.getDate_modified());
        allPatients.update(patient);
    }

    public void transferInPatient(PatientRequest patientRequest) {
        Patient patient = allPatients.findByPatientId(patientRequest.getCase_id());
        providerService.transferIn(patientRequest.getProvider_id(),
                patient, patientRequest.getTb_id(),
                patientRequest.getTb_registration_number(),
                patientRequest.getDate_modified(),
                patientRequest.getSmearTestResults(),
                patientRequest.getWeightStatistics());
        patient.reviveLastClosedTreatment();
        allTherapies.update(patient.latestTherapy());
    }
}
