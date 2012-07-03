package org.motechproject.whp.patient.repository;

import org.ektorp.ComplexKey;
import org.ektorp.CouchDbConnector;
import org.ektorp.ViewQuery;
import org.ektorp.support.GenerateView;
import org.ektorp.support.View;
import org.motechproject.whp.common.exception.WHPErrorCode;
import org.motechproject.whp.common.exception.WHPRuntimeException;
import org.motechproject.dao.MotechBaseRepository;
import org.motechproject.whp.patient.domain.Patient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

import static org.ektorp.ComplexKey.emptyObject;
import static org.ektorp.ComplexKey.of;

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

    @View(name = "find_by_providerId", map = "function(doc) {if (doc.type ==='Patient' && doc.currentTreatment) {emit(doc.currentTreatment.providerId, doc._id);}}")
    public List<Patient> findByCurrentProviderId(String providerId) {
        if (providerId == null)
            return new ArrayList<Patient>();
        String keyword = providerId.toLowerCase();
        ViewQuery q = createQuery("find_by_providerId").key(keyword).includeDocs(true).inclusiveEnd(true);
        List<Patient> patients = db.queryView(q, Patient.class);
        loadDependencies(patients);
        return patients;
    }

    @View(name = "find_by_provider_having_active_treatment", map = "function(doc) {if (doc.type ==='Patient' && doc.currentTreatment && doc.onActiveTreatment === true) {emit([doc.currentTreatment.providerId, doc.firstName], doc._id);}}")
    public List<Patient> getAllWithActiveTreatmentFor(String providerId) {
        if (providerId == null)
            return new ArrayList<Patient>();
        String keyword = providerId.toLowerCase();
        ComplexKey startKey = ComplexKey.of(keyword, null);
        ComplexKey endKey = ComplexKey.of(keyword, ComplexKey.emptyObject());
        ViewQuery q = createQuery("find_by_provider_having_active_treatment").startKey(startKey).endKey(endKey).includeDocs(true).inclusiveEnd(true);
        List<Patient> patients = db.queryView(q, Patient.class);
        loadDependencies(patients);
        return patients;
    }

    @View(name = "find_by_district_and_provider_on_active_treatment", map = "function(doc) {if (doc.type ==='Patient' && doc.currentTreatment && doc.onActiveTreatment === true) {emit([doc.currentTreatment.patientAddress.address_district, doc.currentTreatment.providerId, doc.firstName], doc._id);}}")
    public List<Patient> getAllWithActiveTreatmentForDistrictAndProvider(String districtName, String providerId) {
        if (districtName == null)
            return new ArrayList();
        ViewQuery q = createQuery("find_by_district_and_provider_on_active_treatment").startKey(of(districtName, providerId, null)).endKey(of(districtName, providerId, emptyObject())).includeDocs(true).inclusiveEnd(true);
        List<Patient> patients = db.queryView(q, Patient.class);
        loadDependencies(patients);
        return patients;
    }

    @View(name = "find_by_district_on_active_treatment", map = "function(doc) {if (doc.type ==='Patient' && doc.currentTreatment && doc.onActiveTreatment === true) {emit([doc.currentTreatment.patientAddress.address_district, doc.firstName], doc._id);}}")
    public List<Patient> getAllWithActiveTreatmentForDistrict(String districtName) {
        if (districtName == null)
            return new ArrayList();
        ViewQuery q = createQuery("find_by_district_on_active_treatment").startKey(of(districtName, null)).endKey(of(districtName, emptyObject())).includeDocs(true).inclusiveEnd(true);
        List<Patient> patients = db.queryView(q, Patient.class);
        loadDependencies(patients);
        return patients;
    }

    private void loadDependencies(List<Patient> patients) {
        for (Patient patient : patients) {
            patient.loadTherapyIntoTreatments();
        }
    }
}
