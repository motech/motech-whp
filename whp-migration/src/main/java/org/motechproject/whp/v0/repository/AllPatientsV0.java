package org.motechproject.whp.v0.repository;

import org.ektorp.ComplexKey;
import org.ektorp.CouchDbConnector;
import org.ektorp.ViewQuery;
import org.ektorp.support.CouchDbRepositorySupport;
import org.ektorp.support.GenerateView;
import org.ektorp.support.View;
import org.motechproject.whp.patient.domain.Patient;
import org.motechproject.whp.v0.domain.PatientV0;
import org.motechproject.whp.v0.domain.TherapyV0;
import org.motechproject.whp.v0.domain.TreatmentV0;
import org.motechproject.whp.v0.exception.WHPErrorCodeV0;
import org.motechproject.whp.v0.exception.WHPRuntimeExceptionV0;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

import static org.motechproject.whp.constants.MigrationConstants.OLD_VERSION;
import static org.springframework.dao.support.DataAccessUtils.singleResult;

@Repository
public class AllPatientsV0 extends CouchDbRepositorySupport<PatientV0> {

    AllTherapiesV0 allTherapies;

    @Autowired
    public AllPatientsV0(@Qualifier("whpDbConnector") CouchDbConnector dbCouchDbConnector, AllTherapiesV0 allTherapies) {
        super(PatientV0.class, dbCouchDbConnector, Patient.class.getSimpleName());
        initStandardDesignDocument();
        this.allTherapies = allTherapies;
    }

    @Override
    @GenerateView
    public List<PatientV0> getAll() {
        return super.getAll();
    }

    @View(name = "all_with_version" + OLD_VERSION, map = "function(doc) {" +
            "if (doc.type ==='Patient' " +
            "&& ((doc.version && doc.version ==" + OLD_VERSION + ") " +
            "|| doc.version == null)" +
            ") {emit(null, doc._id)} }")
    public List<PatientV0> getAllVersionedDocs() {
        List<PatientV0> patientV0List = queryView("all_with_version" + OLD_VERSION);
        for (PatientV0 patientV0 : patientV0List) {
            loadPatientDependencies(patientV0);
        }
        return patientV0List;
    }

    @Override
    public void add(PatientV0 patient) {
        PatientV0 savedPatient = findByPatientId(patient.getPatientId());
        if (savedPatient != null) {
            throw new WHPRuntimeExceptionV0(WHPErrorCodeV0.DUPLICATE_CASE_ID);
        }
        ArrayList<WHPErrorCodeV0> errorCodes = new ArrayList<>();
        if (!patient.isValid(errorCodes)) {
            throw new WHPRuntimeExceptionV0(errorCodes);
        }
        super.add(patient);
    }

    @Override
    public void update(PatientV0 patient) {
        allTherapies.update(patient.latestTherapy());
        ArrayList<WHPErrorCodeV0> errorCodes = new ArrayList<WHPErrorCodeV0>();
        if (!patient.isValid(errorCodes)) {
            throw new WHPRuntimeExceptionV0(errorCodes);
        }
        super.update(patient);
    }

    @GenerateView
    public PatientV0 findByPatientId(String patientId) {
        if (patientId == null)
            return null;
        ViewQuery find_by_patientId = createQuery("by_patientId").key(patientId.toLowerCase()).includeDocs(true);
        PatientV0 patient = singleResult(db.queryView(find_by_patientId, PatientV0.class));
        if (patient != null)
            loadPatientDependencies(patient);
        return patient;
    }

    @View(name = "find_by_providerId", map = "function(doc) {if (doc.type ==='Patient' && doc.currentTreatment) {emit([doc.currentTreatment.providerId, doc.firstName], doc._id);}}")
    public List<PatientV0> findByCurrentProviderId(String providerId) {
        if (providerId == null)
            return new ArrayList<PatientV0>();
        String keyword = providerId.toLowerCase();
        ComplexKey startKey = ComplexKey.of(keyword, null);
        ComplexKey endKey = ComplexKey.of(keyword, ComplexKey.emptyObject());
        ViewQuery q = createQuery("find_by_providerId").startKey(startKey).endKey(endKey).includeDocs(true).inclusiveEnd(true);
        List<PatientV0> patients = db.queryView(q, PatientV0.class);
        for (PatientV0 patient : patients) {
            loadPatientDependencies(patient);
        }
        return patients;
    }

    @View(name = "find_by_provider_having_active_treatment", map = "function(doc) {if (doc.type ==='Patient' && doc.currentTreatment && doc.onActiveTreatment === true) {emit([doc.currentTreatment.providerId, doc.firstName], doc._id);}}")
    public List<PatientV0> getAllWithActiveTreatmentFor(String providerId) {
        if (providerId == null)
            return new ArrayList<PatientV0>();
        String keyword = providerId.toLowerCase();
        ComplexKey startKey = ComplexKey.of(keyword, null);
        ComplexKey endKey = ComplexKey.of(keyword, ComplexKey.emptyObject());
        ViewQuery q = createQuery("find_by_provider_having_active_treatment").startKey(startKey).endKey(endKey).includeDocs(true).inclusiveEnd(true);
        List<PatientV0> patients = db.queryView(q, PatientV0.class);
        for (PatientV0 patient : patients) {
            loadPatientDependencies(patient);
        }
        return patients;
    }

    @View(name = "having_active_treatment", map = "function(doc) {if (doc.type ==='Patient' && doc.currentTreatment && doc.onActiveTreatment === true) {emit(doc.firstName, doc._id);}}")
    public List<PatientV0> getAllWithActiveTreatment() {
        ViewQuery q = createQuery("having_active_treatment").includeDocs(true).inclusiveEnd(true);
        List<PatientV0> patients = db.queryView(q, PatientV0.class);
        for (PatientV0 patient : patients) {
            loadPatientDependencies(patient);
        }
        return patients;
    }

    private void loadPatientDependencies(PatientV0 patientV0) {
        TreatmentV0 latestTreatment = patientV0.getCurrentTreatment();
        TherapyV0 latestTherapy = allTherapies.get(latestTreatment.getTherapyDocId());
        latestTreatment.setTherapy(latestTherapy);

        for (TreatmentV0 treatment : patientV0.getTreatments()) {
            TherapyV0 therapy = allTherapies.get(treatment.getTherapyDocId());
            treatment.setTherapy(therapy);
        }
    }

}