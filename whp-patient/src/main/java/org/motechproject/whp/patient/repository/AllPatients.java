package org.motechproject.whp.patient.repository;

import org.ektorp.ComplexKey;
import org.ektorp.CouchDbConnector;
import org.ektorp.ViewQuery;
import org.ektorp.support.GenerateView;
import org.ektorp.support.View;
import org.motechproject.dao.MotechBaseRepository;
import org.motechproject.whp.patient.domain.Patient;
import org.motechproject.whp.patient.domain.ProvidedTreatment;
import org.motechproject.whp.patient.domain.Treatment;
import org.motechproject.whp.patient.exception.WHPErrorCode;
import org.motechproject.whp.patient.exception.WHPRuntimeException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class AllPatients extends MotechBaseRepository<Patient> {

    AllTreatments allTreatments;

    @Autowired
    public AllPatients(@Qualifier("whpDbConnector") CouchDbConnector dbCouchDbConnector, AllTreatments allTreatments) {
        super(Patient.class, dbCouchDbConnector);
        this.allTreatments = allTreatments;
    }

    @Override
    public void add(Patient patient) {
        Patient savedPatient = findByPatientId(patient.getPatientId());
        if (savedPatient != null) {
            throw new WHPRuntimeException(WHPErrorCode.DUPLICATE_CASE_ID);
        }

        ArrayList<WHPErrorCode> errorCodes = new ArrayList<WHPErrorCode>();
        if (!patient.isValid(errorCodes)) {
            throw new WHPRuntimeException(errorCodes);
        }
        super.add(patient);
    }

    @Override
    public void update(Patient patient) {
        allTreatments.update(patient.getCurrentProvidedTreatment().getTreatment());
         ArrayList<WHPErrorCode> errorCodes = new ArrayList<WHPErrorCode>();
        if (!patient.isValid(errorCodes)) {
            throw new WHPRuntimeException(errorCodes);
        }
        super.update(patient);
    }

    @GenerateView
    public Patient findByPatientId(String patientId) {
        ViewQuery find_by_patientId = createQuery("by_patientId").key(patientId).includeDocs(true);
        Patient patient = singleResult(db.queryView(find_by_patientId, Patient.class));
        if (patient != null)
            loadPatientDependencies(patient);
        return patient;
    }

    private void loadPatientDependencies(Patient patient) {
        ProvidedTreatment latestProvidedTreatment = patient.latestProvidedTreatment();
        Treatment latestTreatment = allTreatments.get(latestProvidedTreatment.getTreatmentDocId());
        latestProvidedTreatment.setTreatment(latestTreatment);

        for (ProvidedTreatment providedTreatment : patient.getProvidedTreatments()) {
            Treatment treatment = allTreatments.get(providedTreatment.getTreatmentDocId());
            providedTreatment.setTreatment(treatment);
        }
    }

    @View(name = "find_by_providerId", map = "function(doc) {if (doc.type ==='Patient' && doc.currentProvidedTreatment) {emit([doc.currentProvidedTreatment.providerId, doc.firstName], doc._id);}}")
    public List<Patient> findByCurrentProviderId(String providerId) {
        ComplexKey startKey = ComplexKey.of(providerId, null);
        ComplexKey endKey = ComplexKey.of(providerId, ComplexKey.emptyObject());
        ViewQuery q = createQuery("find_by_providerId").startKey(startKey).endKey(endKey).includeDocs(true).inclusiveEnd(true);
        List<Patient> patients = db.queryView(q, Patient.class);
        for (Patient patient : patients) {
            loadPatientDependencies(patient);
        }
        return patients;
    }

    public List<Patient> getAllWithActiveTreatment(String providerId) {
        ArrayList<Patient> patientsWithActiveTreatment = new ArrayList<Patient>();
        for (Patient patient : findByCurrentProviderId(providerId)) {
            if(patient.isCurrentTreatmentClosed()) continue;
            patientsWithActiveTreatment.add(patient);
        }
        return patientsWithActiveTreatment;
    }
}
