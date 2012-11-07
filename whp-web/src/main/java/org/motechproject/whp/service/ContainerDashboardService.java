package org.motechproject.whp.service;

import org.apache.commons.lang.StringUtils;
import org.motechproject.paginator.contract.FilterParams;
import org.motechproject.paginator.response.PageResults;
import org.motechproject.paginator.service.Paging;
import org.motechproject.whp.common.domain.RegistrationInstance;
import org.motechproject.whp.container.domain.Container;
import org.motechproject.whp.container.repository.AllContainerTrackingRecords;
import org.motechproject.whp.mapper.ContainerTrackingDashboardRowMapper;
import org.motechproject.whp.uimodel.ContainerTrackingDashboardRow;

import java.util.ArrayList;
import java.util.List;

public abstract class ContainerDashboardService implements Paging<ContainerTrackingDashboardRow> {
    protected AllContainerTrackingRecords allContainerTrackingRecords;
    protected ContainerTrackingDashboardRowMapper containerTrackingDashboardRowMapper;

    protected abstract RegistrationInstance getSupportedInstance();

    public ContainerDashboardService(AllContainerTrackingRecords allContainerTrackingRecords, ContainerTrackingDashboardRowMapper containerTrackingDashboardRowMapper) {
        this.allContainerTrackingRecords = allContainerTrackingRecords;
        this.containerTrackingDashboardRowMapper = containerTrackingDashboardRowMapper;
    }

    @Override
    public PageResults<ContainerTrackingDashboardRow> page(Integer pageNumber, Integer rowsPerPage, FilterParams searchCriteria, FilterParams sortCriteria) {
        FilterParams nonEmptyParams = filterOutEmptyParams(searchCriteria);

        int startIndex = (pageNumber - 1) * rowsPerPage;
        List<Container> rowsForPage = allContainerTrackingRecords.filter(getSupportedInstance(), nonEmptyParams, sortCriteria, startIndex, rowsPerPage);
        PageResults pageResults = new PageResults();
        pageResults.setPageNo(pageNumber);
        pageResults.setResults(prepareResultsModel(rowsForPage));
        pageResults.setTotalRows(allContainerTrackingRecords.count(getSupportedInstance(), nonEmptyParams));
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

    private List prepareResultsModel(List<Container> rows) {
        ArrayList<ContainerTrackingDashboardRow> containerTrackingDashboardRows = new ArrayList<>();
        for (Container row : rows)
            containerTrackingDashboardRows.add(containerTrackingDashboardRowMapper.mapFrom(row));
        return containerTrackingDashboardRows;
    }
}
