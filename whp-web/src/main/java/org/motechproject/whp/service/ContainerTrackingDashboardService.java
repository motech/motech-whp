package org.motechproject.whp.service;

import org.motechproject.paginator.response.PageResults;
import org.motechproject.paginator.service.Paging;
import org.motechproject.whp.container.tracking.model.ContainerTrackingRecord;
import org.motechproject.whp.container.tracking.repository.AllContainerTrackingRecords;
import org.motechproject.whp.uimodel.ContainerTrackingDashboardRow;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

@Service
public class ContainerTrackingDashboardService implements Paging<ContainerTrackingDashboardRow> {
    private AllContainerTrackingRecords allContainerTrackingRecords;

    @Autowired
    public ContainerTrackingDashboardService(AllContainerTrackingRecords allContainerTrackingRecords) {
        this.allContainerTrackingRecords = allContainerTrackingRecords;
    }

    @Override
    public PageResults<ContainerTrackingDashboardRow> page(Integer pageNumber, Integer rowsPerPage, Properties searchCriteria) {
        int startIndex = (pageNumber - 1) * rowsPerPage;
        List<ContainerTrackingRecord> rowsForPage = allContainerTrackingRecords.getAllPretreatmentContainerDashboardRows(startIndex, rowsPerPage);
        int totalRows = allContainerTrackingRecords.numberOfPreTreatmentRows();
        PageResults pageResults = new PageResults();
        pageResults.setTotalRows(totalRows);
        pageResults.setPageNo(pageNumber);
        pageResults.setResults(prepareResultsModel(rowsForPage));
        return pageResults;

    }

    private List prepareResultsModel(List<ContainerTrackingRecord> rows) {
        ArrayList<ContainerTrackingDashboardRow> containerTrackingDashboardRows = new ArrayList<>();
        for (ContainerTrackingRecord row : rows)
            containerTrackingDashboardRows.add(new ContainerTrackingDashboardRow(row));
        return containerTrackingDashboardRows;
    }

    @Override
    public String entityName() {
        return "container_tracking_dashboard_row";
    }
}
