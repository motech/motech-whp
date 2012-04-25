package org.motechproject.whp.repository;

import org.ektorp.CouchDbConnector;
import org.ektorp.ViewQuery;
import org.ektorp.support.GenerateView;
import org.motechproject.dao.MotechBaseRepository;
import org.motechproject.whp.domain.Patient;
import org.motechproject.whp.domain.ProvidedTreatment;
import org.motechproject.whp.domain.Treatment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

@Repository
public class AllPatients extends MotechBaseRepository<Patient> {

    AllTreatments allTreatments;

    @Autowired
    public AllPatients(@Qualifier("whpDbConnector") CouchDbConnector dbCouchDbConnector, AllTreatments allTreatments) {
        super(Patient.class, dbCouchDbConnector);
        this.allTreatments = allTreatments;
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
        ProvidedTreatment latestProvidedTreatment = patient.getLatestProvidedTreatment();
        Treatment latestTreatment = allTreatments.get(latestProvidedTreatment.getTreatmentDocId());
        latestProvidedTreatment.setTreatment(latestTreatment);

        for (ProvidedTreatment providedTreatment : patient.getProvidedTreatments()) {
            Treatment treatment = allTreatments.get(providedTreatment.getTreatmentDocId());
            providedTreatment.setTreatment(treatment);
        }
    }

    public void update(Patient patientReturned, Patient patient) {


    }
}
