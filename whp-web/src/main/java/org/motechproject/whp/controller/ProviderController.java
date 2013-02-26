package org.motechproject.whp.controller;

import org.motechproject.security.service.MotechUser;
import org.motechproject.whp.common.repository.AllDistricts;
import org.motechproject.whp.user.domain.Provider;
import org.motechproject.whp.user.service.ProviderService;
import org.motechproject.whp.user.uimodel.ProviderRow;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping(value = "/providers")
public class ProviderController extends BaseWebController {

    private ProviderService providerService;
    private AllDistricts allDistrictsCache;
    private String reportsUrl;

    public static final String PROVIDER_LIST = "providerList";
    public static final String DISTRICT_LIST = "districts";
    private static final String REPORTS_URL = "reportsURL";

    @Autowired
    public ProviderController(ProviderService providerService, AllDistricts allDistrictsCache, @Value("#{whpProperties['whp.reports.url']}") String reportsUrl) {
        this.providerService = providerService;
        this.allDistrictsCache = allDistrictsCache;
        this.reportsUrl = reportsUrl;
    }

    @RequestMapping(value = "list", method = RequestMethod.GET)
    public String loadProviderSearchPage(Model uiModel) throws IOException {
        uiModel.addAttribute(DISTRICT_LIST, allDistrictsCache.getAll());
        uiModel.addAttribute(REPORTS_URL, reportsUrl);
        return "provider/list";
    }

    @RequestMapping(value = "byDistrict/{districtName}", method = RequestMethod.GET)
    public String allProvidersForDistrict(@PathVariable("districtName") String districtName, Model uiModel) {
        prepareResultsModel(uiModel, providerService.fetchBy(districtName));
        return "provider/listByDistrict";
    }


    private void prepareResultsModel(Model uiModel, List<Provider> matchingProviders) {
        Map<String, MotechUser> users = providerService.fetchAllWebUsers();

        List<ProviderRow> providerRows = new ArrayList<>();
        for (Provider provider : matchingProviders) {
            MotechUser motechUser = users.get(provider.getProviderId());
            if (motechUser != null) {
                providerRows.add(new ProviderRow(provider, motechUser.isActive()));
            }
        }
        uiModel.addAttribute(PROVIDER_LIST, providerRows);
        uiModel.addAttribute(REPORTS_URL, reportsUrl);
    }
}
