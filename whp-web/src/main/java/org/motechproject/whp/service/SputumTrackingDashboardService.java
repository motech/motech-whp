package org.motechproject.whp.service;

import org.motechproject.paginator.response.PageResults;
import org.motechproject.paginator.service.Paging;
import org.motechproject.whp.container.dashboard.model.ContainerDashboardRow;
import org.motechproject.whp.container.dashboard.repository.AllContainerDashboardRows;
import org.motechproject.whp.uimodel.SputumTrackingDashboardRow;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

@Service
public class SputumTrackingDashboardService implements Paging<SputumTrackingDashboardRow> {
    private AllContainerDashboardRows allContainerDashboardRows;

    @Autowired
    public SputumTrackingDashboardService(AllContainerDashboardRows allContainerDashboardRows) {
        this.allContainerDashboardRows = allContainerDashboardRows;
    }

    @Override
    public PageResults<SputumTrackingDashboardRow> page(Integer pageNumber, Integer rowsPerPage, Properties searchCriteria) {
        int startIndex = (pageNumber - 1) * rowsPerPage;
        List<ContainerDashboardRow> rowsForPage = allContainerDashboardRows.getAllPretreatmentContainerDashboardRows(startIndex, rowsPerPage);
        int totalRows = allContainerDashboardRows.numberOfPreTreatmentRows();
        PageResults pageResults = new PageResults();
        pageResults.setTotalRows(totalRows);
        pageResults.setPageNo(pageNumber);
        pageResults.setResults(prepareResultsModel(rowsForPage));
        return pageResults;

    }

    private List prepareResultsModel(List<ContainerDashboardRow> rows) {
        ArrayList<SputumTrackingDashboardRow> sputumTrackingDashboardRows = new ArrayList<>();
        for (ContainerDashboardRow row : rows)
            sputumTrackingDashboardRows.add(new SputumTrackingDashboardRow(row));
        return sputumTrackingDashboardRows;
    }

    @Override
    public String entityName() {
        return "sputum_tracking_dashboard_row";
    }
}
