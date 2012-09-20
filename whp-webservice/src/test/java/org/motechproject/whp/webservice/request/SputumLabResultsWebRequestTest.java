package org.motechproject.whp.webservice.request;

import org.junit.Test;
import org.motechproject.whp.webservice.builder.SputumLabResultsWebRequestBuilder;

import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertTrue;

public class SputumLabResultsWebRequestTest {

    @Test
    public void shouldCheckLabResultsAreComplete(){

        SputumLabResultsWebRequest requestWithCompleteLabResults = new SputumLabResultsWebRequestBuilder().withCase_id("case-id")
                .withDate_modified("03/04/2012 11:23:40")
                .withSmear_test_date_1("01/03/2012")
                .withSmear_test_result_1("Positive")
                .withSmear_test_date_2("01/03/2012")
                .withSmear_test_result_2("Positive")
                .withLab_name("XYZ")
                .withLab_number("1234")
                .build();

        SputumLabResultsWebRequest requestWithIncompleteLabResult1 = new SputumLabResultsWebRequestBuilder().withCase_id("case-id")
                .withDate_modified("03/04/2012 11:23:40")
                .withSmear_test_date_1(null)
                .withSmear_test_result_1(null)
                .withSmear_test_date_2("01/03/2012")
                .withSmear_test_result_2("Positive")
                .withLab_name("XYZ")
                .withLab_number("1234")
                .build();

        SputumLabResultsWebRequest requestWithIncompleteLabResult2 = new SputumLabResultsWebRequestBuilder().withCase_id("case-id")
                .withDate_modified("03/04/2012 11:23:40")
                .withSmear_test_date_1("01/03/2012")
                .withSmear_test_result_1("Positive")
                .withSmear_test_date_2(null)
                .withSmear_test_result_2(null)
                .withLab_name("XYZ")
                .withLab_number("1234")
                .build();

        SputumLabResultsWebRequest requestWithNoLabResults = new SputumLabResultsWebRequestBuilder().withCase_id("case-id")
                .withDate_modified("03/04/2012 11:23:40")
                .withSmear_test_date_1("")
                .withSmear_test_result_1("")
                .withSmear_test_date_1("")
                .withSmear_test_result_1("")
                .withLab_name("XYZ")
                .withLab_number("1235")
                .build();

        assertTrue(requestWithCompleteLabResults.hasCompleteLabResults());
        assertFalse(requestWithIncompleteLabResult1.hasCompleteLabResults());
        assertFalse(requestWithIncompleteLabResult2.hasCompleteLabResults());
        assertFalse(requestWithNoLabResults.hasCompleteLabResults());
    }

}
