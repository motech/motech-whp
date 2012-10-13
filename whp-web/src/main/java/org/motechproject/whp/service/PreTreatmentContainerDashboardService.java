package org.motechproject.whp.service;

import org.apache.commons.lang.StringUtils;
import org.motechproject.paginator.response.PageResults;
import org.motechproject.paginator.service.Paging;
import org.motechproject.whp.container.tracking.model.ContainerTrackingRecord;
import org.motechproject.whp.container.tracking.repository.AllContainerTrackingRecords;
import org.motechproject.whp.mapper.ContainerTrackingDashboardRowMapper;
import org.motechproject.whp.uimodel.ContainerTrackingDashboardRow;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

@Service
public class PreTreatmentContainerDashboardService implements Paging<ContainerTrackingDashboardRow> {
    private AllContainerTrackingRecords allContainerTrackingRecords;
    private ContainerTrackingDashboardRowMapper containerTrackingDashboardRowMapper;

    @Autowired
    public PreTreatmentContainerDashboardService(AllContainerTrackingRecords allContainerTrackingRecords, ContainerTrackingDashboardRowMapper containerTrackingDashboardRowMapper) {
        this.allContainerTrackingRecords = allContainerTrackingRecords;
        this.containerTrackingDashboardRowMapper = containerTrackingDashboardRowMapper;
    }

    @Override
    public PageResults<ContainerTrackingDashboardRow> page(Integer pageNumber, Integer rowsPerPage, Properties searchCriteria) {

        Properties properties = new Properties();
        for(Object key : searchCriteria.keySet()){
            Object value = searchCriteria.get(key);
            if(!StringUtils.isBlank((String) value)){
                properties.put(key, value);
            }
        }
        searchCriteria = properties;

        int startIndex = (pageNumber - 1) * rowsPerPage;
        List<ContainerTrackingRecord> rowsForPage = allContainerTrackingRecords.filterPreTreatmentRecords(searchCriteria, startIndex, rowsPerPage);
        PageResults pageResults = new PageResults();
        pageResults.setPageNo(pageNumber);
        pageResults.setResults(prepareResultsModel(rowsForPage));
        pageResults.setTotalRows(allContainerTrackingRecords.countPreTreatmentRecords(searchCriteria));
        return pageResults;
    }

    private List prepareResultsModel(List<ContainerTrackingRecord> rows) {
        ArrayList<ContainerTrackingDashboardRow> containerTrackingDashboardRows = new ArrayList<>();
        for (ContainerTrackingRecord row : rows)
            containerTrackingDashboardRows.add(containerTrackingDashboardRowMapper.mapFrom(row));
        return containerTrackingDashboardRows;
    }

    @Override
    public String entityName() {
        return "container_tracking_dashboard_row";
    }
}
