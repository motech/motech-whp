package org.motechproject.whp.service;

import org.apache.commons.lang.StringUtils;
import org.motechproject.paginator.response.PageResults;
import org.motechproject.paginator.service.Paging;
import org.motechproject.whp.container.tracking.model.ContainerTrackingRecord;
import org.motechproject.whp.container.tracking.repository.AllInTreatmentContainerTrackingRecordsImpl;
import org.motechproject.whp.mapper.ContainerTrackingDashboardRowMapper;
import org.motechproject.whp.uimodel.ContainerTrackingDashboardRow;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public abstract class ContainerDashboardService  implements Paging<ContainerTrackingDashboardRow> {
    protected ContainerTrackingDashboardRowMapper containerTrackingDashboardRowMapper;

    protected abstract Integer count(Properties searchCriteria);

    protected abstract List<ContainerTrackingRecord> filter(Integer rowsPerPage, Properties searchCriteria, int startIndex);

    public ContainerDashboardService(ContainerTrackingDashboardRowMapper containerTrackingDashboardRowMapper) {
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
        List<ContainerTrackingRecord> rowsForPage = filter(rowsPerPage, searchCriteria, startIndex);
        PageResults pageResults = new PageResults();
        pageResults.setPageNo(pageNumber);
        pageResults.setResults(prepareResultsModel(rowsForPage));
        pageResults.setTotalRows(count(searchCriteria));
        return pageResults;
    }

    private List prepareResultsModel(List<ContainerTrackingRecord> rows) {
        ArrayList<ContainerTrackingDashboardRow> containerTrackingDashboardRows = new ArrayList<>();
        for (ContainerTrackingRecord row : rows)
            containerTrackingDashboardRows.add(containerTrackingDashboardRowMapper.mapFrom(row));
        return containerTrackingDashboardRows;
    }
}
