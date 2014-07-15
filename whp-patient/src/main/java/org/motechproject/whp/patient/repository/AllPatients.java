package org.motechproject.whp.patient.repository;

import java.util.ArrayList;
import java.util.List;

import org.codehaus.jackson.type.TypeReference;
import org.ektorp.ComplexKey;
import org.ektorp.ViewQuery;
import org.ektorp.ViewResult;
import org.ektorp.support.GenerateView;
import org.ektorp.support.View;
import org.joda.time.LocalDate;
import org.motechproject.couchdb.lucene.repository.LuceneAwareMotechBaseRepository;
import org.motechproject.couchdb.lucene.util.WhiteSpaceEscape;
import org.motechproject.paginator.contract.FilterParams;
import org.motechproject.paginator.contract.SortParams;
import org.motechproject.whp.common.domain.PhoneNumber;
import org.motechproject.whp.common.ektorp.SearchFunctionUpdater;
import org.motechproject.whp.common.exception.WHPErrorCode;
import org.motechproject.whp.common.exception.WHPRuntimeException;
import org.motechproject.whp.patient.domain.Patient;
import org.motechproject.whp.patient.model.PatientAdherenceStatus;
import org.motechproject.whp.patient.query.PatientQueryDefinition;
import org.motechproject.whp.user.domain.ProviderIds;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import com.github.ldriscoll.ektorplucene.CustomLuceneResult;
import com.github.ldriscoll.ektorplucene.LuceneAwareCouchDbConnector;
import com.github.ldriscoll.ektorplucene.util.IndexUploader;

@Repository
public class AllPatients extends LuceneAwareMotechBaseRepository<Patient> {

    private PatientQueryDefinition patientQueryDefinition;
    private static final String SORT_BY_ASCENDING = "ASC";

    @Autowired
    public AllPatients(@Qualifier("whpLuceneAwareCouchDbConnector") LuceneAwareCouchDbConnector whpLuceneAwareCouchDbConnector,
                       PatientQueryDefinition patientQueryDefinition,
                       @Qualifier("whiteSpaceEscape") WhiteSpaceEscape whiteSpaceEscape) {
        super(Patient.class, whpLuceneAwareCouchDbConnector, whiteSpaceEscape);
        this.patientQueryDefinition = patientQueryDefinition;

        IndexUploader uploader = new IndexUploader();
        uploader.updateSearchFunctionIfNecessary(db, this.patientQueryDefinition.viewName(), this.patientQueryDefinition.searchFunctionName(), this.patientQueryDefinition.indexFunction());
        new SearchFunctionUpdater().updateAnalyzer(db, this.patientQueryDefinition.viewName(), this.patientQueryDefinition.searchFunctionName(), "keyword");
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
        ArrayList<WHPErrorCode> errorCodes = new ArrayList<>();
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
        return singleResult(db.queryView(find_by_patientId, Patient.class));
    }

    @View(name = "find_by_provider_having_active_treatment_sort_by_treatment_start_dt_v1", map = "function(doc) {if (doc.type ==='Patient' && doc.currentTherapy && doc.currentTherapy.currentTreatment && doc.onActiveTreatment === true) {" +
            "emit([doc.currentTherapy.currentTreatment.providerId, doc.currentTherapy.currentTreatment.startDate], doc._id);}}")
    public List<Patient> getAllWithActiveTreatmentFor(String providerId) {
        return getPatientsForProvider("find_by_provider_having_active_treatment_sort_by_treatment_start_dt_v1", providerId, Patient.class, true);
    }


    @View(name = "find_active_patients_by_provider", map = "function(doc) {" +
                    "if (doc.type ==='Patient' && doc.currentTherapy && doc.currentTherapy.currentTreatment && doc.onActiveTreatment === true) {" +
                        "emit([doc.currentTherapy.currentTreatment.providerId, doc.currentTherapy.currentTreatment.startDate], {patientId:doc.patientId, lastAdherenceReportedDate : doc.lastAdherenceWeekStartDate});" +
                    "}}")
    public List<PatientAdherenceStatus> getPatientAdherenceStatusesFor(String providerId) {
        return getPatientsForProvider("find_active_patients_by_provider", providerId, PatientAdherenceStatus.class, false);
    }

    private List getPatientsForProvider(String view, String selectedProvider, Class clazz, boolean includeDocs) {
        if (selectedProvider == null)
            return new ArrayList<>();
        String keyword = selectedProvider.toLowerCase();
        ComplexKey startKey = ComplexKey.of(keyword, null);
        ComplexKey endKey = ComplexKey.of(keyword, ComplexKey.emptyObject());
        ViewQuery q = createQuery(view)
                    .startKey(startKey).endKey(endKey).includeDocs(includeDocs).inclusiveEnd(true);

        return db.queryView(q, clazz);
    }


    public ProviderIds providersWithActivePatients(ProviderIds providersToSearchFor) {
        ViewQuery query = createQuery("with_active_patients").keys(providersToSearchFor.asList());
        return filterProviderIds(db.queryView(query));
    }

    public ProviderIds providersWithActivePatients() {
        ViewQuery query = createQuery("with_active_patients");
        return filterProviderIds(db.queryView(query));
    }

    @View(name = "with_active_patients", map = "classpath:filterProvidersWithActivePatients.js")
    public void createViewForProvidersWithActivePatients() {
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

    public List<Patient> filter(FilterParams queryParams, SortParams sortParams, int skip, int limit) {
        sortParams.put(PatientQueryDefinition.patientIdSortParam(), SORT_BY_ASCENDING);
        return filter(patientQueryDefinition, activePatientFilter(queryParams), sortParams, skip, limit);
    }

    private FilterParams activePatientFilter(FilterParams queryParams) {
        queryParams.put(patientQueryDefinition.getIsActive().getName(), "true");
        return queryParams;
    }

    public int count(FilterParams queryParams) {
        return super.count(patientQueryDefinition, activePatientFilter(queryParams));
    }

    private ProviderIds filterProviderIds(ViewResult rows) {
        ProviderIds providerIds = new ProviderIds();
        for (ViewResult.Row row : rows) {
            providerIds.add(row.getValue());
        }
        return providerIds;
    }

    @Override
    protected TypeReference<CustomLuceneResult<Patient>> getTypeReference() {
        return new TypeReference<CustomLuceneResult<Patient>>() {
        };
    }

    public List<Patient> getAll(int pageNumber, int pageSize) {
        ViewQuery q = createQuery("all").skip(pageNumber * pageSize).limit(pageSize).inclusiveEnd(true).includeDocs(true);
        return db.queryView(q, Patient.class);
    }

    public void remove(Patient patient){
        ArrayList<WHPErrorCode> errorCodes = new ArrayList<>();
        if (!patient.isValid(errorCodes)) {
            throw new WHPRuntimeException(errorCodes);
        }
        super.remove(patient);
    }
    
    @View(name = "find_treatment_type", map = "function(doc) {" +
            "if (doc.type ==='Patient' && doc.currentTherapy && doc.currentTherapy.currentTreatment && doc.onActiveTreatment === true) {" +
                "emit(doc.currentTherapy.treatmentCategory.name,doc.patientId);" +
            "}}")
    public List<Patient> findByTreatmentType(String treatmentType){
    	ViewQuery query = createQuery("find_treatment_type").key(treatmentType).includeDocs(true);
    	return db.queryView(query , Patient.class);
    }
    
    @View(name = "find_by_phone_number", map = "function(doc) {" +
            "if (doc.type ==='Patient' && doc.phoneNumber) {" +
            "emit(doc.phoneNumber, doc.patientId);" +
        "}}")
    public Patient findByPhoneNumber(String phoneNumber){
    	phoneNumber = new PhoneNumber(phoneNumber, true, true).value();
    	ViewQuery query = createQuery("find_by_phone_number").key(phoneNumber).includeDocs(true);
    	List<Patient> list =  db.queryView(query , Patient.class);
    	if(list.size() < 1)
    		return null;
    	return list.get(0);
    }
}