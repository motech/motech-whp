package org.motechproject.whp.controller;

import org.motechproject.security.service.MotechUser;
import org.motechproject.whp.refdata.domain.District;
import org.motechproject.whp.refdata.repository.AllDistricts;
import org.motechproject.whp.uimodel.ProviderRow;
import org.motechproject.whp.user.domain.Provider;
import org.motechproject.whp.user.service.ProviderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping(value = "/providers")
public class ProviderController extends BaseWebController {

    private ProviderService providerService;
    private AllDistricts allDistrictsCache;

    public static final String PROVIDER_LIST = "providerList";
    private static final String DISTRICT_LIST = "districts";
    private static final String PROVIDER_ID = "selectedProvider";
    private static final String SELECTED_DISTRICT = "selectedDistrict";

    @Autowired
    public ProviderController(ProviderService providerService, AllDistricts allDistrictsCache) {
        this.providerService = providerService;
        this.allDistrictsCache = allDistrictsCache;
    }

    @RequestMapping(value = "list", method = RequestMethod.GET)
    public String loadProviderSearchPage(Model uiModel) {
        List<District> districtList = allDistrictsCache.getAll();
        String district = districtList.get(0).getName();
        String providerId = "";

        prepareQueryModel(uiModel, district, providerId);

        prepareResultsModel(uiModel, providerService.fetchBy(district, providerId));
        return "provider/list";
    }

    @RequestMapping(value = "search", method = RequestMethod.GET)
    public String searchMatchingProviders(@RequestParam("selectedDistrict") String district, String providerId, Model uiModel) {
        prepareQueryModel(uiModel, district, providerId);
        prepareResultsModel(uiModel, providerService.fetchBy(district, providerId));
        return "provider/list";
    }

    @RequestMapping(value = "byDistrict/{districtName}", method = RequestMethod.GET)
    public String allProvidersForDistrict(@PathVariable("districtName") String districtName, Model uiModel) {
        prepareResultsModel(uiModel, providerService.fetchBy(districtName));
        return "provider/listByDistrict";
    }

    private void prepareQueryModel(Model uiModel, String districtName, String providerId) {
        uiModel.addAttribute(DISTRICT_LIST, allDistrictsCache.getAll());
        uiModel.addAttribute(SELECTED_DISTRICT, districtName);
        uiModel.addAttribute(PROVIDER_ID, providerId);
    }

    private void prepareResultsModel(Model uiModel, List<Provider> matchingProviders) {
        Map<String, MotechUser> users = providerService.fetchAllWebUsers();

        List<ProviderRow> providerRows = new ArrayList<ProviderRow>();
        for (Provider provider : matchingProviders) {
            providerRows.add(new ProviderRow(provider, users.get(provider.getProviderId()).isActive()));
        }
        uiModel.addAttribute(PROVIDER_LIST, providerRows);
    }
}
