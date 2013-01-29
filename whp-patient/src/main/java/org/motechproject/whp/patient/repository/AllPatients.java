package org.motechproject.whp.patient.repository;

import org.ektorp.ComplexKey;
import org.ektorp.CouchDbConnector;
import org.ektorp.ViewQuery;
import org.ektorp.ViewResult;
import org.ektorp.support.GenerateView;
import org.ektorp.support.View;
import org.joda.time.LocalDate;
import org.motechproject.dao.MotechBaseRepository;
import org.motechproject.scheduler.context.EventContext;
import org.motechproject.whp.common.exception.WHPErrorCode;
import org.motechproject.whp.common.exception.WHPRuntimeException;
import org.motechproject.whp.common.repository.Countable;
import org.motechproject.whp.patient.domain.Patient;
import org.motechproject.whp.user.domain.ProviderIds;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import static org.motechproject.whp.patient.WHPPatientConstants.PATIENT_UPDATED_SUBJECT;

@Repository
public class AllPatients extends MotechBaseRepository<Patient> implements Countable {


    private EventContext eventContext;

    @Autowired
    public AllPatients(@Qualifier("whpDbConnector") CouchDbConnector dbCouchDbConnector, EventContext eventContext) {
        super(Patient.class, dbCouchDbConnector);
        this.eventContext = eventContext;
    }

    @Override
    public void add(Patient patient) {
        Patient savedPatient = findByPatientId(patient.getPatientId());
        if (savedPatient != null) {
            throw new WHPRuntimeException(WHPErrorCode.DUPLICATE_CASE_ID);
        }
        ArrayList<WHPErrorCode> errorCodes = new ArrayList<>();
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
        eventContext.send(PATIENT_UPDATED_SUBJECT, patient);
    }

    @GenerateView
    public Patient findByPatientId(String patientId) {
        if (patientId == null)
            return null;
        ViewQuery find_by_patientId = createQuery("by_patientId").key(patientId.toLowerCase()).includeDocs(true);
        return singleResult(db.queryView(find_by_patientId, Patient.class));
    }

    @View(name = "count_patients", map = "function(doc){ if(doc.type === 'Patient') { emit(null,doc._id); } }", reduce = "_count")
    public String count() {
        ViewQuery query = createQuery("count_patients").reduce(true);
        ViewResult rows = db.queryView(query);
        String firstValue = firstValue(rows);
        return (null == firstValue) ? "0" : firstValue;
    }

    @View(name = "find_by_provider_having_active_treatment_sort_by_treatment_start_dt_v1", map = "function(doc) {if (doc.type ==='Patient' && doc.currentTherapy && doc.currentTherapy.currentTreatment && doc.onActiveTreatment === true) {" +
            "emit([doc.currentTherapy.currentTreatment.providerId, doc.currentTherapy.currentTreatment.startDate], doc._id);}}")
    public List<Patient> getAllWithActiveTreatmentFor(String providerId) {
        if (providerId == null)
            return new ArrayList<>();
        String keyword = providerId.toLowerCase();

        ComplexKey startKey = ComplexKey.of(keyword, null);
        ComplexKey endKey = ComplexKey.of(keyword, ComplexKey.emptyObject());
        ViewQuery q = createQuery("find_by_provider_having_active_treatment_sort_by_treatment_start_dt_v1").startKey(startKey).endKey(endKey).includeDocs(true).inclusiveEnd(true);
        return db.queryView(q, Patient.class);
    }

    @View(name = "find_by_district_having_active_treatment_v1", map = "function(doc) {if (doc.type ==='Patient' && doc.currentTherapy.currentTreatment && doc.onActiveTreatment === true) {emit(doc.currentTherapy.currentTreatment.providerDistrict, doc._id);}}")
    public List<Patient> getAllUnderActiveTreatmentInDistrict(String district) {
        ViewQuery q = createQuery("find_by_district_having_active_treatment_v1").key(district).includeDocs(true);
        List<Patient> patients = db.queryView(q, Patient.class);
        Collections.sort(patients, new PatientComparatorByFirstName());
        return patients;
    }

    public ProviderIds providersWithActivePatients(ProviderIds providersToSearchFor) {
        ViewQuery query = createQuery("with_active_patients").keys(providersToSearchFor.asList());
        return filterProviderIds(db.queryView(query));
    }

    public ProviderIds providersWithActivePatients() {
        ViewQuery query = createQuery("with_active_patients");
        return filterProviderIds(db.queryView(query));
    }

    @GenerateView
    public List<Patient> getAll(int pageNumber, int pageSize) {
        ViewQuery query = createQuery("by_patientId").skip(pageNumber * pageSize).limit(pageSize).includeDocs(true);
        return db.queryView(query, Patient.class);
    }

    @View(name = "with_active_patients", map = "classpath:filterProvidersWithActivePatients.js")
    public void createViewForProvidersWithActivePatients() {
    }


    @View(name = "providers_with_adherence_last_week", map = "classpath:filterProvidersWithAdherence.js")
    public ProviderIds findAllProvidersWithAdherenceAsOf(LocalDate asOf) {
        ViewQuery query = createQuery("providers_with_adherence_last_week").startKey(asOf);
        return filterProviderIds(db.queryView(query));

    }


    @View(name = "providers_with_adherence_last_week", map = "classpath:filterProvidersWithAdherence.js")
    public ProviderIds findAllProvidersWithoutAdherenceAsOf(LocalDate asOf) {
        ViewQuery query = createQuery("providers_with_adherence_last_week").startKey(asOf.minusDays(1)).descending(true);
        return filterProviderIds(db.queryView(query));
    }

    @View(name = "active_patient_ids", map = "function(doc) {\n" +
            " if(doc.type ==='Patient' && doc.currentTherapy.currentTreatment && doc.onActiveTreatment === true) {" +
            "        emit(doc.patientId, null);\n" +
            "    }\n" +
            "}")
    public List<String> findAllActivePatientIds() {
        ViewQuery query = createQuery("active_patient_ids");
        ViewResult rows = db.queryView(query);

        List<String> patientIds = new ArrayList<>();
        for (ViewResult.Row row : rows) {
            patientIds.add(row.getKey());
        }
        return patientIds;
    }

    public static class PatientComparatorByFirstName implements Comparator<Patient> {

        @Override
        public int compare(Patient patient1, Patient patient2) {
            return patient1.getFirstName().compareTo(patient2.getFirstName());
        }
    }

    private ProviderIds filterProviderIds(ViewResult rows) {
        ProviderIds providerIds = new ProviderIds();
        for (ViewResult.Row row : rows) {
            providerIds.add(row.getValue());
        }
        return providerIds;
    }

    private String firstValue(ViewResult rows) {
        return (rows.getSize() > 0) ? rows.getRows().get(0).getValue() : null;
    }
}
