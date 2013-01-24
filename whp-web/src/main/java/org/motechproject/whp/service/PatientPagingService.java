package org.motechproject.whp.service;

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

        Integer pageNumberStartingWithZero = pageNumber - 1;
        List<Patient> rowsForPage = allPatients.getAll(pageNumberStartingWithZero, rowsPerPage);
        PageResults pageResults = new PageResults();
        pageResults.setPageNo(pageNumber);
        pageResults.setResults(prepareResultsModel(rowsForPage));
        pageResults.setTotalRows(Integer.parseInt(allPatients.count()));
        return pageResults;
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
}
