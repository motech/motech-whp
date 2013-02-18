package org.motechproject.whp.service;

import org.motechproject.paginator.contract.FilterParams;
import org.motechproject.paginator.contract.SortParams;
import org.motechproject.paginator.response.PageResults;
import org.motechproject.paginator.service.Paging;
import org.motechproject.whp.common.domain.alerts.AlertColorConfiguration;
import org.motechproject.whp.mapper.AlertsFilterTransformer;
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
    private AlertsFilterTransformer alertsFilterTransformer;
    private AlertColorConfiguration alertColorConfiguration;

    @Autowired
    public PatientPagingService(AllPatients allPatients, AlertsFilterTransformer alertsFilterTransformer, AlertColorConfiguration alertColorConfiguration){
        this.allPatients = allPatients;
        this.alertsFilterTransformer = alertsFilterTransformer;
        this.alertColorConfiguration = alertColorConfiguration;
    }

    @Override
    public PageResults<PatientDashboardRow> page(Integer pageNumber, Integer rowsPerPage, FilterParams filterParams, SortParams sortCriteria) {
        filterParams = filterParams.removeEmptyParams();

        int startIndex = (pageNumber - 1) * rowsPerPage;
        FilterParams transformedFilterParams = alertsFilterTransformer.transform(filterParams);
        List<Patient> rowsForPage = allPatients.filter(transformedFilterParams, sortCriteria, startIndex, rowsPerPage);
        PageResults pageResults = new PageResults();
        pageResults.setPageNo(pageNumber);
        pageResults.setResults(prepareResultsModel(rowsForPage));
        pageResults.setTotalRows(allPatients.count(transformedFilterParams));
        return pageResults;
    }

    private List prepareResultsModel(List<Patient> rows) {
        ArrayList<PatientDashboardRow> patientDashboardRows = new ArrayList<>();
        for(Patient patient: rows){
            patientDashboardRows.add(new PatientDashboardRow(patient, alertColorConfiguration));
        }
        return patientDashboardRows;
    }

    @Override
    public String entityName() {
        return "patient_results";
    }
}
