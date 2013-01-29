package org.motechproject.whp.service;

import org.apache.commons.lang.StringUtils;
import org.motechproject.paginator.contract.FilterParams;
import org.motechproject.paginator.contract.SortParams;
import org.motechproject.paginator.response.PageResults;
import org.motechproject.paginator.service.Paging;
import org.motechproject.whp.patient.domain.Patient;
import org.motechproject.whp.patient.repository.AllPatients;
import org.motechproject.whp.uimodel.PatientInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class PatientPagingService implements Paging<PatientInfo>{
    private AllPatients allPatients;

    @Autowired
    public PatientPagingService(AllPatients allPatients){
        this.allPatients = allPatients;
    }

    @Override
    public PageResults<PatientInfo> page(Integer pageNumber, Integer rowsPerPage, FilterParams searchCriteria, SortParams sortCriteria) {

        int startIndex = (pageNumber - 1);
        FilterParams nonEmptyParams = filterOutEmptyParams(searchCriteria);
        List<Patient> rowsForPage = allPatients.filter(nonEmptyParams, sortCriteria, startIndex, rowsPerPage);
        PageResults pageResults = new PageResults();
        pageResults.setPageNo(pageNumber);
        pageResults.setResults(prepareResultsModel(rowsForPage));
        pageResults.setTotalRows(Integer.parseInt(allPatients.count()));
        return pageResults;
    }

    private FilterParams filterOutEmptyParams(FilterParams searchCriteria) {
        FilterParams properties = new FilterParams();
        for (Object key : searchCriteria.keySet()) {
            Object value = searchCriteria.get(key);
            if (!StringUtils.isBlank((String) value)) {
                properties.put(key.toString(), value.toString());
            }
        }
        return properties;
    }

    private List prepareResultsModel(List<Patient> rows) {

        ArrayList<PatientInfo> patientInfos = new ArrayList<>();
        for(Patient patient: rows){
            patientInfos.add(new PatientInfo(patient));
        }
        return patientInfos;
    }

    public List<Patient> getAll() {
        return allPatients.getAll();
    }

    @Override
    public String entityName() {
        return "patient_results";
    }

    public List<Patient> getAllWithActiveTreatmentForProvider(String providerId) {
        return allPatients.getAllWithActiveTreatmentFor(providerId);
    }

    public List<Patient> searchBy(String districtName) {
        return allPatients.getAllUnderActiveTreatmentInDistrict(districtName);
    }

/*
    public List<Patient> getAllWithActiveTreatmentForProvider(String providerId, distrit ) {
        return  allPatients.getAllWithActiveTreatmentFor(providerId);
    }

    private List<Patient> getPagedPatientsFor(String district, String providerId) {
        List<Patient> patients;
        if (isNotEmpty(providerId))
            patients = patientPagingService.getAllWithActiveTreatmentForProvider(providerId);
        else if (isNotEmpty(district))
            patients = patientPagingService.searchBy(districtName);
        else
            patients = new ArrayList<>();

        return patients;
    }
*/
}
