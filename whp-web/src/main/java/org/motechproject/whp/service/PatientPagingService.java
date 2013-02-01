package org.motechproject.whp.service;

import org.apache.commons.lang.StringUtils;
import org.motechproject.paginator.contract.FilterParams;
import org.motechproject.paginator.contract.SortParams;
import org.motechproject.paginator.response.PageResults;
import org.motechproject.paginator.service.Paging;
import org.motechproject.whp.patient.domain.Patient;
import org.motechproject.whp.patient.repository.AllPatients;
import org.motechproject.whp.uimodel.PatientDashboardRow;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class PatientPagingService implements Paging<PatientDashboardRow>{
    private AllPatients allPatients;

    @Autowired
    public PatientPagingService(AllPatients allPatients){
        this.allPatients = allPatients;
    }

    @Override
    public PageResults<PatientDashboardRow> page(Integer pageNumber, Integer rowsPerPage, FilterParams filterParams, SortParams sortCriteria) {
        int startIndex = (pageNumber - 1) * rowsPerPage;
        filterParams = filterOutEmptyParams(filterParams);
        List<Patient> rowsForPage = allPatients.filter(filterParams, sortCriteria, startIndex, rowsPerPage);
        PageResults pageResults = new PageResults();
        pageResults.setPageNo(pageNumber);
        pageResults.setResults(prepareResultsModel(rowsForPage));
        pageResults.setTotalRows(allPatients.count(filterParams));
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
        ArrayList<PatientDashboardRow> patientDashboardRows = new ArrayList<>();
        for(Patient patient: rows){
            patientDashboardRows.add(new PatientDashboardRow(patient));
        }
        return patientDashboardRows;
    }

    @Override
    public String entityName() {
        return "patient_results";
    }
}
