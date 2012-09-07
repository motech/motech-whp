package org.motechproject.whp.user.service;

import org.apache.commons.lang.StringUtils;
import org.motechproject.paginator.response.PageResults;
import org.motechproject.paginator.service.Paging;
import org.motechproject.scheduler.context.EventContext;
import org.motechproject.security.service.MotechAuthenticationService;
import org.motechproject.security.service.MotechUser;
import org.motechproject.whp.common.exception.WHPErrorCode;
import org.motechproject.whp.common.exception.WHPRuntimeException;
import org.motechproject.whp.user.contract.ProviderRequest;
import org.motechproject.whp.user.domain.Provider;
import org.motechproject.whp.user.domain.WHPRole;
import org.motechproject.whp.user.repository.AllProviders;
import org.motechproject.whp.user.uimodel.ProviderRow;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.*;

import static java.util.Arrays.asList;
import static org.motechproject.whp.common.event.EventKeys.PROVIDER_DISTRICT_CHANGE;

@Service
public class ProviderService implements Paging {

    MotechAuthenticationService motechAuthenticationService;

    private AllProviders allProviders;
    private EventContext eventContext;

    @Autowired
    public ProviderService(MotechAuthenticationService motechAuthenticationService, AllProviders allProviders, @Qualifier("eventContext") EventContext eventContext) {
        this.motechAuthenticationService = motechAuthenticationService;
        this.allProviders = allProviders;
        this.eventContext = eventContext;
    }

    public void registerProvider(ProviderRequest providerRequest) {
        String providerDocId = createOrUpdateProvider(providerRequest);
        try {
            // TODO : make this idempotent
            motechAuthenticationService.register(providerRequest.getProviderId(), "password", providerDocId, Arrays.asList(WHPRole.PROVIDER.name()), false);
        } catch (Exception e) {
            throw new WHPRuntimeException(WHPErrorCode.WEB_ACCOUNT_REGISTRATION_ERROR, e.getMessage());
        }
    }

    String createOrUpdateProvider(ProviderRequest providerRequest) {
        Provider provider = providerRequest.makeProvider();
        Provider existingProvider = allProviders.findByProviderId(providerRequest.getProviderId());

        String docId = addOrReplace(provider);

        if (existingProvider != null) {
            if (existingProvider.hasDifferentDistrict(providerRequest.getDistrict())) {
                eventContext.send(PROVIDER_DISTRICT_CHANGE, provider.getProviderId());
            }
        }

        return docId;
    }

    private String addOrReplace(Provider provider) {
        allProviders.addOrReplace(provider);
        return provider.getId();
    }

    public List<Provider> fetchBy(String district, String providerId) {
        if (StringUtils.isEmpty(providerId)) {
            return fetchBy(district);
        } else {
            return allProviders.findByDistrictAndProviderId(district, providerId);
        }
    }

    public List<Provider> fetchBy(String district) {
        return allProviders.findByDistrict(district);
    }

    public Map<String, MotechUser> fetchAllWebUsers() {
        Map<String, MotechUser> allWebUsers = new HashMap();
        for (MotechUser motechUser : motechAuthenticationService.findByRole(WHPRole.PROVIDER.name())) {
            allWebUsers.put(motechUser.getUserName(), motechUser);
        }
        return allWebUsers;
    }

    public Provider findByProviderId(String providerId) {
        return allProviders.findByProviderId(providerId);
    }

    public Provider findByMobileNumber(String mobileNumber) {
        return allProviders.findByMobileNumber(mobileNumber);
    }

    @Override
    public PageResults page(Integer pageNo, Integer rowsPerPage, Properties searchCriteria) {
        List<Provider> allProviders = this.allProviders.getAll();

        int startIndex = (pageNo - 1) * rowsPerPage;
        int endIndex = (pageNo * rowsPerPage) - 1;
        List<Provider> providers = (startIndex == endIndex) ? asList(allProviders.get(startIndex)) : allProviders.subList(startIndex, endIndex);
        PageResults pageResults = new PageResults();
        pageResults.setTotalRows(allProviders.size());
        pageResults.setPageNo(pageNo);
        pageResults.setResults(prepareResultsModel(providers));
        return pageResults;
    }

    private List<ProviderRow> prepareResultsModel(List<Provider> matchingProviders) {
        Map<String, MotechUser> users = fetchAllWebUsers();

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
