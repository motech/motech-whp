package org.motechproject.whp.controller;

import org.codehaus.jackson.map.ObjectMapper;
import org.motechproject.security.service.MotechUser;
import org.motechproject.whp.refdata.domain.District;
import org.motechproject.whp.refdata.repository.AllDistricts;
import org.motechproject.whp.user.uimodel.ProviderRow;
import org.motechproject.whp.user.domain.Provider;
import org.motechproject.whp.user.service.ProviderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
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
    public String loadProviderSearchPage(Model uiModel, HttpServletRequest request) throws IOException {
        List<District> districtList = allDistrictsCache.getAll();
        String district = districtList.get(0).getName();
        String providerId = "";
        if(request.getParameterMap().isEmpty())
            initQueryModel(uiModel, district, providerId);
        else
            addQueryParametersToUiModel(uiModel, request);

        prepareResultsModel(uiModel, providerService.fetchBy(district, providerId));
        return "provider/list";
    }

    private void addQueryParametersToUiModel(Model uiModel, HttpServletRequest request) throws IOException {
        String providerPaginationId = "provider_pagination";
        uiModel.addAttribute("pageNo", request.getParameter(providerPaginationId+"-pageNo"));
        uiModel.addAttribute("rowsPerPage", request.getParameter(providerPaginationId+"-rowsPerPage"));

        String searchCriteriaJson = request.getParameter(providerPaginationId + "-searchCriteria");
        HashMap<String, String> searchCriteria = new ObjectMapper().readValue(searchCriteriaJson, HashMap.class);
        uiModel.addAttribute(DISTRICT_LIST, allDistrictsCache.getAll());
        uiModel.addAttribute(SELECTED_DISTRICT, searchCriteria.get(SELECTED_DISTRICT));
        uiModel.addAttribute(PROVIDER_ID, searchCriteria.get(PROVIDER_ID));
    }

    @RequestMapping(value = "search", method = RequestMethod.GET)
    public String searchMatchingProviders(@RequestParam("selectedDistrict") String district, @RequestParam("selectedProvider") String providerId, Model uiModel) {
        initQueryModel(uiModel, district, providerId);
        prepareResultsModel(uiModel, providerService.fetchBy(district, providerId));
        return "provider/list";
    }

    @RequestMapping(value = "byDistrict/{districtName}", method = RequestMethod.GET)
    public String allProvidersForDistrict(@PathVariable("districtName") String districtName, Model uiModel) {
        prepareResultsModel(uiModel, providerService.fetchBy(districtName));
        return "provider/listByDistrict";
    }

    private void initQueryModel(Model uiModel, String districtName, String providerId) {
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
