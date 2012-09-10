package org.motechproject.whp.user.service;

import org.motechproject.paginator.response.PageResults;
import org.motechproject.paginator.service.Paging;
import org.motechproject.security.service.MotechUser;
import org.motechproject.whp.user.domain.Provider;
import org.motechproject.whp.user.repository.AllProviders;
import org.motechproject.whp.user.uimodel.ProviderRow;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import static java.util.Arrays.asList;

@Service
public class ProviderPaginationService implements Paging {

    private ProviderService providerService;
    private AllProviders allProviders;

    @Autowired
    public ProviderPaginationService(AllProviders allProviders, ProviderService providerService) {
        this.allProviders = allProviders;
        this.providerService = providerService;
    }

    @Override
    public PageResults page(Integer pageNo, Integer rowsPerPage, Properties searchCriteria) {
        List<Provider> allProviders = providerService.fetchBy((String) searchCriteria.get("selectedDistrict"), (String) searchCriteria.get("providerId"));

        int startIndex = (pageNo - 1) * rowsPerPage;

        int endIndex = (pageNo * rowsPerPage);
        endIndex = allProviders.size() < endIndex ? allProviders.size() : endIndex;

        List<Provider> providers = (startIndex == endIndex) ? new ArrayList<Provider>() : allProviders.subList(startIndex, endIndex);
        PageResults pageResults = new PageResults();
        pageResults.setTotalRows(allProviders.size());
        pageResults.setPageNo(pageNo);
        pageResults.setResults(prepareResultsModel(providers));
        return pageResults;
    }

    private List<ProviderRow> prepareResultsModel(List<Provider> matchingProviders) {
        Map<String, MotechUser> users = providerService.fetchAllWebUsers();

        List<ProviderRow> providerRows = new ArrayList<ProviderRow>();
        for (Provider provider : matchingProviders) {
            providerRows.add(new ProviderRow(provider, users.get(provider.getProviderId()).isActive()));
        }
        return providerRows;
    }

    @Override
    public String entityName() {
        return "provider";
    }
}
