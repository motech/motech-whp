package org.motechproject.whp.webservice.builder;

import org.motechproject.whp.webservice.request.SputumLabResultsWebRequest;

public class SputumLabResultsWebRequestBuilder {

    private SputumLabResultsWebRequest request = new SputumLabResultsWebRequest();

    public SputumLabResultsWebRequestBuilder withCase_id(String case_id) {
        request.setCase_id(case_id);
        return this;
    }

    public SputumLabResultsWebRequestBuilder withApi_key(String api_key) {
        request.setApi_key(api_key);
        return this;
    }

    public SputumLabResultsWebRequestBuilder withDate_modified(String date_modified) {
        request.setDate_modified(date_modified);
        return this;
    }

    public SputumLabResultsWebRequestBuilder withUpdate_type(String update_type) {
        request.setUpdate_type(update_type);
        return this;
    }

    public SputumLabResultsWebRequestBuilder withSmear_test_date_1(String smear_test_date_1) {
        request.setSmear_test_date_1(smear_test_date_1);
        return this;
    }

    public SputumLabResultsWebRequestBuilder withSmear_test_result_1(String smear_test_result_1) {
        request.setSmear_test_result_1(smear_test_result_1);
        return this;
    }

    public SputumLabResultsWebRequestBuilder withSmear_test_date_2(String smear_test_date_2) {
        request.setSmear_test_date_2(smear_test_date_2);
        return this;
    }

    public SputumLabResultsWebRequestBuilder withSmear_test_result_2(String smear_test_result_2) {
        request.setSmear_test_result_2(smear_test_result_2);
        return this;
    }

    public SputumLabResultsWebRequestBuilder withLab_name(String lab_name) {
        request.setLab_name(lab_name);
        return this;
    }

    public SputumLabResultsWebRequestBuilder withLab_number(String lab_number) {
        request.setLab_number(lab_number);
        return this;
    }

    public SputumLabResultsWebRequest build(){
        return request;
    }

}
