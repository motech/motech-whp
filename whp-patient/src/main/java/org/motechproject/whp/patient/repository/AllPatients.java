package org.motechproject.whp.patient.repository;

import org.ektorp.ComplexKey;
import org.ektorp.CouchDbConnector;
import org.ektorp.ViewQuery;
import org.ektorp.support.GenerateView;
import org.ektorp.support.View;
import org.motechproject.dao.MotechBaseRepository;
import org.motechproject.whp.common.exception.WHPErrorCode;
import org.motechproject.whp.common.exception.WHPRuntimeException;
import org.motechproject.whp.patient.domain.Patient;
import org.motechproject.whp.patient.domain.PatientComparatorByFirstName;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Repository
public class AllPatients extends MotechBaseRepository<Patient> {


    @Autowired
    public AllPatients(@Qualifier("whpDbConnector") CouchDbConnector dbCouchDbConnector) {
        super(Patient.class, dbCouchDbConnector);
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
        ArrayList<WHPErrorCode> errorCodes = new ArrayList<WHPErrorCode>();
        if (!patient.isValid(errorCodes)) {
            throw new WHPRuntimeException(errorCodes);
        }
        super.update(patient);
    }

    @GenerateView
    public Patient findByPatientId(String patientId) {
        if (patientId == null)
            return null;
        ViewQuery find_by_patientId = createQuery("by_patientId").key(patientId.toLowerCase()).includeDocs(true);
        Patient patient = singleResult(db.queryView(find_by_patientId, Patient.class));
        if (patient != null)
            patient.loadTherapyIntoTreatments();
        return patient;
    }

    @View(name = "find_by_provider_having_active_treatment_sort_by_firstname", map = "function(doc) {if (doc.type ==='Patient' && doc.currentTreatment && doc.onActiveTreatment === true) {emit([doc.currentTreatment.providerId, doc.firstName], doc._id);}}")
    public List<Patient> getAllWithActiveTreatmentFor(String providerId) {
        if (providerId == null)
            return new ArrayList<Patient>();
        String keyword = providerId.toLowerCase();
        ComplexKey startKey = ComplexKey.of(keyword, null);
        ComplexKey endKey = ComplexKey.of(keyword, ComplexKey.emptyObject());
        ViewQuery q = createQuery("find_by_provider_having_active_treatment_sort_by_firstname").startKey(startKey).endKey(endKey).includeDocs(true).inclusiveEnd(true);
        List<Patient> patients = db.queryView(q, Patient.class);
        loadDependencies(patients);
        return patients;
    }

    @View(name = "find_by_provider_having_active_treatment", map = "function(doc) {if (doc.type ==='Patient' && doc.currentTreatment && doc.onActiveTreatment === true) {emit(doc.currentTreatment.providerId, doc._id);}}")
    public List<Patient> getAllUnderActiveTreatmentWithCurrentProviders(List<String> providerIds) {
        ViewQuery q = createQuery("find_by_provider_having_active_treatment").keys(providerIds).includeDocs(true);
        List<Patient> patients = db.queryView(q, Patient.class);
        Collections.sort(patients,new PatientComparatorByFirstName());
        loadDependencies(patients);
        return patients;
    }


    private void loadDependencies(List<Patient> patients) {
        for (Patient patient : patients) {
            patient.loadTherapyIntoTreatments();
        }
    }
}
