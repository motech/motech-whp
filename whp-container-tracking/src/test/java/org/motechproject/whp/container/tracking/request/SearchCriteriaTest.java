package org.motechproject.whp.container.tracking.request;

import org.junit.Test;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

public class SearchCriteriaTest {

    @Test
    public void shouldCreateSearchCriteriaFromKeyValueProperties(){
        Properties filterParams = new Properties();
        filterParams.put("providerId", "provider1");
        filterParams.put("providerDistrict", "East Champaran");
        filterParams.put("containerStatus", "Open");
        filterParams.put("containerIssuedDateFrom", "17/09/2012");
        filterParams.put("containerIssuedDateTo", "24/09/2012");
        filterParams.put("consultationDateFrom", "17/09/2012");
        filterParams.put("consultationDateTo", "24/09/2012");
        filterParams.put("cumulativeLabResult","Positive");
        filterParams.put("diagnosis","Positive");
        filterParams.put("reasonForClosure","Container Lost");


        Map<String, String> expectedSearchParams = new HashMap<>();
        expectedSearchParams.put("providerId", "provider1");
        expectedSearchParams.put("providerDistrict", "East Champaran");
        expectedSearchParams.put("containerStatus", "Open");
        expectedSearchParams.put("containerIssuedDate<date>", "[17/09/2012 TO 24/09/2012]");
        expectedSearchParams.put("consultationDate<date>", "[17/09/2012 TO 24/09/2012]");
        expectedSearchParams.put("cumulativeLabResult","Positive");
        expectedSearchParams.put("diagnosis","Positive");
        expectedSearchParams.put("reasonForClosure","Container Lost");
        expectedSearchParams.put("containerInstance","PreTreatment");

        SearchCriteria searchCriteria = new SearchCriteria(filterParams);
        Map<String, String> searchParams = searchCriteria.preTreatmentCriteria();
        assertThat(searchParams, is(expectedSearchParams));
    }
}
