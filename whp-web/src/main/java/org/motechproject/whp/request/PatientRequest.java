package org.motechproject.whp.request;

import org.apache.commons.lang.StringUtils;
import org.motechproject.whp.patient.domain.*;
import org.motechproject.whp.validation.ProviderIdValidator;
import org.motechproject.whp.validation.ValidationScope;
import org.motechproject.whp.validation.constraints.Enumeration;
import org.motechproject.whp.validation.constraints.NamedConstraint;
import org.motechproject.whp.validation.constraints.Scope;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.Digits;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

public class PatientRequest {

    @NotNull
    private String case_id;

    @DateTimeFormat(pattern = "dd/MM/YYYY HH:mm:ss")
    private String date_modified;

    private String case_type;

    private String phi;

    @NotNull
    @Scope(scope = {ValidationScope.create})
    private String first_name;

    @NotNull
    @Scope(scope = {ValidationScope.create})
    private String last_name;

    @NotNull
    @Enumeration(type = Gender.class)
    @Scope(scope = {ValidationScope.create})
    private String gender;

    @Enumeration(type = PatientType.class)
    @Scope(scope = {ValidationScope.create})
    private String patient_type;

    @Pattern(regexp = "^$|[0-9]{10}", message = "Mobile number should be empty or should have 10 digits")
    private String mobile_number;

    @Enumeration(type = DiseaseClass.class)
    @Scope(scope = {ValidationScope.create})
    private String disease_class;

    @NotNull
    @Scope(scope = {ValidationScope.create})
    private String address_location;

    @NotNull
    @Scope(scope = {ValidationScope.create})
    private String address_landmark;

    @NotNull
    @Scope(scope = {ValidationScope.create})
    private String address_village;

    @NotNull
    @Scope(scope = {ValidationScope.create})
    private String address_block;

    @NotNull
    @Scope(scope = {ValidationScope.create})
    private String address_district;

    @NotNull
    @Scope(scope = {ValidationScope.create})
    private String address_state;

    @Enumeration(type = SmearTestSampleInstance.class)
    @Scope(scope = {ValidationScope.create})
    private String smear_sample_instance;

    @Scope(scope = {ValidationScope.create})
    @DateTimeFormat(pattern = "dd/MM/YYYY")
    private String smear_test_date_1;

    @Enumeration(type = SmearTestResult.class)
    @Scope(scope = {ValidationScope.create})
    private String smear_test_result_1;

    @DateTimeFormat(pattern = "dd/MM/YYYY")
    @Scope(scope = {ValidationScope.create})
    private String smear_test_date_2;

    @Enumeration(type = SmearTestResult.class)
    @Scope(scope = {ValidationScope.create})
    private String smear_test_result_2;

    @Enumeration(type = WeightInstance.class)
    @Scope(scope = {ValidationScope.create})
    private String weight_instance;

    @NotNull
    @Digits(integer = Integer.MAX_VALUE, fraction = Integer.MAX_VALUE, message = "Weight must be a real number")
    @Scope(scope = {ValidationScope.create})
    private String weight;

    @NotNull
    @Size(min = 11, max = 11)
    @Scope(scope = {ValidationScope.create})
    private String tb_id;

    @NotNull
    @NamedConstraint(name = ProviderIdValidator.PROVIDER_ID_CONSTRAINT)
    @Scope(scope = {ValidationScope.create})
    private String provider_id;

    @Pattern(regexp = "[0|1][1|2]")
    private String treatment_category;

    private String tb_registration_number;

    @NotNull
    @Digits(integer = 3, fraction = 0, message = "Age must be numeric")
    @Scope(scope = {ValidationScope.create})
    private String age;

    public PatientRequest() {
    }

    public PatientRequest setPatientInfo(String caseId, String firstName, String lastName, String gender, String patientType, String patientMobileNumber, String phi) {
        this.case_id = caseId;
        this.first_name = firstName;
        this.last_name = lastName;
        this.gender = gender;
        this.patient_type = patientType;
        this.mobile_number = patientMobileNumber;
        this.phi = phi;
        return this;
    }

    public PatientRequest setPatientAddress(String houseNumber, String landmark, String block, String village, String district, String state) {
        this.address_location = houseNumber;
        this.address_landmark = landmark;
        this.address_block = block;
        this.address_village = village;
        this.address_district = district;
        this.address_state = state;
        return this;
    }

    public PatientRequest setTreatmentData(String category, String tbId, String providerId, String diseaseClass, String patientAge, String registrationNumber) {
        this.treatment_category = category;
        this.tb_id = tbId;
        this.provider_id = providerId;
        this.disease_class = diseaseClass;
        this.age = patientAge;
        this.tb_registration_number = registrationNumber;
        return this;
    }

    public PatientRequest setSmearTestResults(String smear_sample_instance_1, String smear_test_date_1, String smear_result_1, String smear_test_date_2, String smear_result_2) {
        this.smear_sample_instance = smear_sample_instance_1;
        this.smear_test_date_1 = smear_test_date_1;
        this.smear_test_result_1 = smear_result_1;
        this.smear_test_date_2 = smear_test_date_2;
        this.smear_test_result_2 = smear_result_2;
        return this;
    }

    public PatientRequest setWeightStatistics(String weightInstance, String weight) {
        this.weight_instance = weightInstance;
        this.weight = weight;
        return this;
    }

    public String getCase_id() {
        return StringUtils.isNotEmpty(case_id) ? case_id : null;
    }

    public void setCase_id(String case_id) {
        this.case_id = case_id;
    }

    public String getDate_modified() {
        return StringUtils.isNotEmpty(date_modified) ? date_modified : null;
    }

    public void setDate_modified(String date_modified) {
        this.date_modified = date_modified;
    }

    public String getCase_type() {
        return StringUtils.isNotEmpty(case_type) ? case_type : null;
    }

    public void setCase_type(String case_type) {
        this.case_type = case_type;
    }

    public String getPhi() {
        return StringUtils.isNotEmpty(phi) ? phi : null;
    }

    public void setPhi(String phi) {
        this.phi = phi;
    }

    public String getFirst_name() {
        return StringUtils.isNotEmpty(first_name) ? first_name : null;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public String getLast_name() {
        return StringUtils.isNotEmpty(last_name) ? last_name : null;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    public String getGender() {
        return StringUtils.isNotEmpty(gender) ? gender : null;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getPatient_type() {
        return StringUtils.isNotEmpty(patient_type) ? patient_type : null;
    }

    public void setPatient_type(String patient_type) {
        this.patient_type = patient_type;
    }

    public String getMobile_number() {
        return StringUtils.isNotEmpty(mobile_number) ? mobile_number : null;
    }

    public void setMobile_number(String mobile_number) {
        this.mobile_number = mobile_number;
    }

    public String getDisease_class() {
        return StringUtils.isNotEmpty(disease_class) ? disease_class : null;
    }

    public void setDisease_class(String disease_class) {
        this.disease_class = disease_class;
    }

    public String getAddress_location() {
        return StringUtils.isNotEmpty(address_location) ? address_location : null;
    }

    public void setAddress_location(String address_location) {
        this.address_location = address_location;
    }

    public String getAddress_landmark() {
        return StringUtils.isNotEmpty(address_landmark) ? address_landmark : null;
    }

    public void setAddress_landmark(String address_landmark) {
        this.address_landmark = address_landmark;
    }

    public String getAddress_village() {
        return StringUtils.isNotEmpty(address_village) ? address_village : null;
    }

    public void setAddress_village(String address_village) {
        this.address_village = address_village;
    }

    public String getAddress_block() {
        return StringUtils.isNotEmpty(address_block) ? address_block : null;
    }

    public void setAddress_block(String address_block) {
        this.address_block = address_block;
    }

    public String getAddress_district() {
        return StringUtils.isNotEmpty(address_district) ? address_district : null;
    }

    public void setAddress_district(String address_district) {
        this.address_district = address_district;
    }

    public String getAddress_state() {
        return StringUtils.isNotEmpty(address_state) ? address_state : null;
    }

    public void setAddress_state(String address_state) {
        this.address_state = address_state;
    }

    public String getSmear_sample_instance() {
        return StringUtils.isNotEmpty(smear_sample_instance) ? smear_sample_instance : null;
    }

    public void setSmear_sample_instance(String smear_sample_instance) {
        this.smear_sample_instance = smear_sample_instance;
    }

    public String getSmear_test_date_1() {
        return StringUtils.isNotEmpty(smear_test_date_1) ? smear_test_date_1 : null;
    }

    public void setSmear_test_date_1(String smear_test_date_1) {
        this.smear_test_date_1 = smear_test_date_1;
    }

    public String getSmear_test_result_1() {
        return StringUtils.isNotEmpty(smear_test_result_1) ? smear_test_result_1 : null;
    }

    public void setSmear_test_result_1(String smear_test_result_1) {
        this.smear_test_result_1 = smear_test_result_1;
    }

    public String getSmear_test_date_2() {
        return StringUtils.isNotEmpty(smear_test_date_2) ? smear_test_date_2 : null;
    }

    public void setSmear_test_date_2(String smear_test_date_2) {
        this.smear_test_date_2 = smear_test_date_2;
    }

    public String getSmear_test_result_2() {
        return StringUtils.isNotEmpty(smear_test_result_2) ? smear_test_result_2 : null;
    }

    public void setSmear_test_result_2(String smear_test_result_2) {
        this.smear_test_result_2 = smear_test_result_2;
    }

    public String getWeight_instance() {
        return StringUtils.isNotEmpty(weight_instance) ? weight_instance : null;
    }

    public void setWeight_instance(String weight_instance) {
        this.weight_instance = weight_instance;
    }

    public String getWeight() {
        return StringUtils.isNotEmpty(weight) ? weight : null;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    public String getTb_id() {
        return StringUtils.isNotEmpty(tb_id) ? tb_id : null;
    }

    public void setTb_id(String tb_id) {
        this.tb_id = tb_id;
    }

    public String getProvider_id() {
        return StringUtils.isNotEmpty(provider_id) ? provider_id : null;
    }

    public void setProvider_id(String provider_id) {
        this.provider_id = provider_id;
    }

    public String getTreatment_category() {
        return StringUtils.isNotEmpty(treatment_category) ? treatment_category : null;
    }

    public void setTreatment_category(String treatment_category) {
        this.treatment_category = treatment_category;
    }

    public String getTb_registration_number() {
        return StringUtils.isNotEmpty(tb_registration_number) ? tb_registration_number : null;
    }

    public void setTb_registration_number(String tb_registration_number) {
        this.tb_registration_number = tb_registration_number;
    }

    public String getAge() {
        return StringUtils.isNotEmpty(age) ? age : null;
    }

    public void setAge(String age) {
        this.age = age;
    }

}
