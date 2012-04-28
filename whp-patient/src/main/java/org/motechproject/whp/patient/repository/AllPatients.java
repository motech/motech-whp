package org.motechproject.whp.patient.repository;

import org.ektorp.CouchDbConnector;
import org.ektorp.ViewQuery;
import org.ektorp.support.GenerateView;
import org.ektorp.support.View;
import org.motechproject.dao.MotechBaseRepository;
import org.motechproject.whp.patient.domain.Patient;
import org.motechproject.whp.patient.domain.ProvidedTreatment;
import org.motechproject.whp.patient.domain.Treatment;
import org.motechproject.whp.patient.exception.WHPDomainException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

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
            throw new WHPDomainException("patient with the same case-id is already registered.");
        }
        ValidationErrors validationErrors = new ValidationErrors();
        if (!patient.isValid(validationErrors)) {
            throw new WHPDomainException("invalid patient data:" + validationErrors);
        }
        super.add(patient);
    }

    @Override
    public void update(Patient patient) {
        allTreatments.update(patient.getCurrentProvidedTreatment().getTreatment());
        ValidationErrors validationErrors = new ValidationErrors();
        if (!patient.isValid(validationErrors)) {
            throw new WHPDomainException("invalid patient data." + validationErrors);
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

    @View(name = "find_by_providerId", map = "function(doc) {if (doc.type ==='Patient' && doc.currentProvidedTreatment) {emit(doc.currentProvidedTreatment.providerId, doc._id);}}")
    public List<Patient> findByCurrentProviderId(String providerId) {
        ViewQuery q = createQuery("find_by_providerId").key(providerId).includeDocs(true);
        List<Patient> patients = db.queryView(q, Patient.class);
        for (Patient patient : patients) {
            loadPatientDependencies(patient);
        }
        return patients;
    }

}
