package org.motechproject.whp.patient.command;

import org.motechproject.whp.patient.contract.PatientRequest;
import org.motechproject.whp.patient.domain.Patient;
import org.motechproject.whp.patient.domain.ProvidedTreatment;
import org.motechproject.whp.patient.domain.Therapy;
import org.motechproject.whp.patient.exception.WHPErrorCode;
import org.motechproject.whp.patient.exception.WHPRuntimeException;
import org.motechproject.whp.patient.mapper.TherapyMapper;
import org.motechproject.whp.patient.repository.AllPatients;
import org.motechproject.whp.patient.repository.AllTherapies;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

import static org.motechproject.whp.patient.domain.criteria.UpdatePatientCriteria.canOpenNewTreatment;
import static org.motechproject.whp.patient.mapper.PatientMapper.createNewProvidedTreatmentForTreatmentCategoryChange;

@Component
public class OpenNewTreatment extends TreatmentUpdate {

    @Autowired
    public OpenNewTreatment(AllPatients allPatients, AllTherapies allTreatments) {
        super(allPatients, allTreatments, UpdateScope.openTreatment);
    }

    @Override
    public void apply(PatientRequest patientRequest) {
        Patient patient = allPatients.findByPatientId(patientRequest.getCase_id());
        List<WHPErrorCode> errorCodes = new ArrayList<WHPErrorCode>();

        if (!canOpenNewTreatment(patient, errorCodes)) {
            throw new WHPRuntimeException(errorCodes);
        }
        addNewTreatmentForCategoryChange(patient, patientRequest, allPatients, allTherapies);
    }

    private void addNewTreatmentForCategoryChange(Patient patient, PatientRequest patientRequest, AllPatients allPatients, AllTherapies allTreatments) {
        Therapy newTherapy = TherapyMapper.createNewTreatment(patient, patientRequest);
        allTreatments.add(newTherapy);
        ProvidedTreatment newProvidedTreatment = createNewProvidedTreatmentForTreatmentCategoryChange(patient, patientRequest, newTherapy);
        patient.addProvidedTreatment(newProvidedTreatment, patientRequest.getDate_modified());
        allPatients.update(patient);
    }
}
