package org.motechproject.whp.patient.command;

import org.motechproject.whp.patient.contract.PatientRequest;
import org.motechproject.whp.patient.domain.Patient;
import org.motechproject.whp.patient.domain.ProvidedTreatment;
import org.motechproject.whp.patient.domain.Treatment;
import org.motechproject.whp.patient.exception.WHPErrorCode;
import org.motechproject.whp.patient.exception.WHPRuntimeException;
import org.motechproject.whp.patient.mapper.TreatmentMapper;
import org.motechproject.whp.patient.repository.AllPatients;
import org.motechproject.whp.patient.repository.AllTreatments;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

import static org.motechproject.whp.patient.domain.criteria.UpdatePatientCriteria.canOpenNewTreatment;
import static org.motechproject.whp.patient.mapper.PatientMapper.createNewProvidedTreatmentForTreatmentCategoryChange;

@Component
public class OpenNewTreatment extends TreatmentUpdate {

    @Autowired
    public OpenNewTreatment(AllPatients allPatients, AllTreatments allTreatments) {
        super(allPatients, allTreatments);
    }

    @Override
    public void apply(PatientRequest patientRequest) {
        Patient patient = allPatients.findByPatientId(patientRequest.getCase_id());
        List<WHPErrorCode> errorCodes = new ArrayList<WHPErrorCode>();

        if (!canOpenNewTreatment(patient, errorCodes)) {
            throw new WHPRuntimeException(errorCodes);
        }
        addNewTreatmentForCategoryChange(patient, patientRequest, allPatients, allTreatments);
    }

    private void addNewTreatmentForCategoryChange(Patient patient, PatientRequest patientRequest, AllPatients allPatients, AllTreatments allTreatments) {
        Treatment newTreatment = TreatmentMapper.createNewTreatment(patient, patientRequest);
        allTreatments.add(newTreatment);
        ProvidedTreatment newProvidedTreatment = createNewProvidedTreatmentForTreatmentCategoryChange(patient, patientRequest, newTreatment);
        patient.addProvidedTreatment(newProvidedTreatment, patientRequest.getDate_modified());
        allPatients.update(patient);
    }
}
