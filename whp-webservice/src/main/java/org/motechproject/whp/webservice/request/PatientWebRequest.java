package org.motechproject.whp.webservice.request;

import lombok.Data;
import org.motechproject.validation.constraints.*;
import org.motechproject.whp.mapping.StringToEnumeration;
import org.motechproject.whp.patient.command.UpdateScope;
import org.motechproject.whp.refdata.domain.*;
import org.motechproject.whp.validation.APIKeyValidator;
import org.motechproject.whp.validation.ProviderIdValidator;
import org.motechproject.whp.webservice.contract.TreatmentUpdateScenario;

import javax.validation.constraints.Digits;
import javax.validation.constraints.Pattern;

import static org.apache.commons.lang.StringUtils.isNotBlank;
import static org.motechproject.whp.common.util.WHPDate.DATE_FORMAT;
import static org.motechproject.whp.common.util.WHPDate.DATE_TIME_FORMAT;

@Data
public class PatientWebRequest {

    @NotNullOrEmpty
    private String case_id;

    @NotNullOrEmpty
    @NamedConstraint(name = APIKeyValidator.API_KEY_VALIDATION)
    private String api_key;

    @NotNullOrEmpty
    @DateTimeFormat(pattern = DATE_TIME_FORMAT)
    private String date_modified;

    private String case_type;

    private String tb_registration_number;

    private String phi;

    @NotNullOrEmpty
    @Scope(scope = {UpdateScope.createScope})
    private String first_name;

    @Scope(scope = {UpdateScope.createScope})
    private String last_name;

    @NotNullOrEmpty
    @Enumeration(type = Gender.class)
    @Scope(scope = {UpdateScope.createScope})
    private String gender;

    @NotNullOrEmpty
    @Enumeration(type = PatientType.class)
    @Scope(scope = {UpdateScope.createScope, UpdateScope.openTreatmentScope})
    private String patient_type;

    @Enumeration(type = TreatmentUpdateScenario.class)
    private String treatment_update;

    @NotNullOrEmpty
    @Enumeration(type = TreatmentOutcome.class)
    @Scope(scope = {UpdateScope.closeTreatmentScope})
    private String treatment_outcome;

    @NotNullOrEmpty
    @Scope(scope = {UpdateScope.pauseTreatmentScope, UpdateScope.restartTreatmentScope})
    private String reason;

    @Pattern(regexp = "^$|[0-9]{10}", message = "Mobile number should be empty or should have 10 digits")
    private String mobile_number;

    @NotNullOrEmpty
    @Enumeration(type = DiseaseClass.class)
    @Scope(scope = {UpdateScope.createScope, UpdateScope.openTreatmentScope})
    private String disease_class;

    private String address_location;

    private String address_landmark;

    @NotNullOrEmpty
    @Scope(scope = {UpdateScope.createScope})
    private String address_village;

    @NotNullOrEmpty
    @Scope(scope = {UpdateScope.createScope})
    private String address_block;

    @NotNullOrEmpty
    @Scope(scope = {UpdateScope.createScope})
    private String address_district;

    @NotNullOrEmpty
    @Scope(scope = {UpdateScope.createScope})
    private String address_state;

    @Enumeration(type = SampleInstance.class)
    private String smear_sample_instance;

    @DateTimeFormat(pattern = DATE_FORMAT)
    private String smear_test_date_1;

    @Enumeration(type = SmearTestResult.class)
    private String smear_test_result_1;

    @DateTimeFormat(pattern = DATE_FORMAT)
    private String smear_test_date_2;

    @Enumeration(type = SmearTestResult.class)
    private String smear_test_result_2;

    @Enumeration(type = SampleInstance.class)
    private String weight_instance;

    @Digits(integer = Integer.MAX_VALUE, fraction = Integer.MAX_VALUE, message = "Weight must be a real number")
    private String weight;

    @NotNullOrEmpty
    @Scope(scope = {UpdateScope.createScope,
            UpdateScope.simpleUpdateScope,
            UpdateScope.openTreatmentScope,
            UpdateScope.closeTreatmentScope,
            UpdateScope.pauseTreatmentScope,
            UpdateScope.restartTreatmentScope})
    private String tb_id;

    @NamedConstraint(name = ProviderIdValidator.PROVIDER_ID_CONSTRAINT)
    @Scope(scope = {UpdateScope.createScope, UpdateScope.openTreatmentScope, UpdateScope.transferInScope})
    private String provider_id;

    @NotNullOrEmpty
    @Scope(scope = {UpdateScope.createScope, UpdateScope.openTreatmentScope})
    @Pattern(regexp = "[0|1][1|2]")
    private String treatment_category;

    @NotNullOrEmpty(scope = UpdateScope.createScope)
    @Digits(integer = 3, fraction = 0, message = "Age must be numeric and not fractional")
    @Scope(scope = {UpdateScope.createScope, UpdateScope.simpleUpdateScope})
    private String age;

    public PatientWebRequest() {
    }

    public PatientWebRequest setPatientInfo(String caseId, String firstName, String lastName, String gender, String patientType, String patientMobileNumber, String phi) {
        this.case_id = caseId;
        this.first_name = firstName;
        this.last_name = lastName;
        this.gender = gender;
        this.patient_type = patientType;
        this.mobile_number = patientMobileNumber;
        this.phi = phi;
        return this;
    }

    public PatientWebRequest setPatientAddress(String houseNumber, String landmark, String block, String village, String district, String state) {
        this.address_location = houseNumber;
        this.address_landmark = landmark;
        this.address_block = block;
        this.address_village = village;
        this.address_district = district;
        this.address_state = state;
        return this;
    }

    public PatientWebRequest setTreatmentData(String category, String tbId, String providerId, String diseaseClass, String patientAge, String registrationNumber) {
        this.treatment_category = category;
        this.tb_id = tbId;
        this.provider_id = providerId;
        this.disease_class = diseaseClass;
        this.age = patientAge;
        this.tb_registration_number = registrationNumber;
        return this;
    }

    public PatientWebRequest setTreatmentUpdateData(String treatment_update, String treatment_outcome) {
        this.treatment_update = treatment_update;
        this.treatment_outcome = treatment_outcome;
        return this;
    }

    public PatientWebRequest setSmearTestResults(String smear_sample_instance_1, String smear_test_date_1, String smear_result_1, String smear_test_date_2, String smear_result_2) {
        this.smear_sample_instance = smear_sample_instance_1;
        this.smear_test_date_1 = smear_test_date_1;
        this.smear_test_result_1 = smear_result_1;
        this.smear_test_date_2 = smear_test_date_2;
        this.smear_test_result_2 = smear_result_2;
        return this;
    }

    public PatientWebRequest setWeightStatistics(String SampleInstance, String weight) {
        this.weight_instance = SampleInstance;
        this.weight = weight;
        return this;
    }

    public void setApi_key(String apiKey) {
        this.api_key = apiKey;
    }

    public UpdateScope updateScope() {
        TreatmentUpdateScenario updateScenario = updateScenario();
        if (updateScenario == null) {
            return UpdateScope.simpleUpdate;
        }
        return updateScenario.getScope();
    }

    public TreatmentUpdateScenario updateScenario() {
        if (isNotBlank(treatment_update)) {
            return (TreatmentUpdateScenario) StringToEnumeration.get(treatment_update, TreatmentUpdateScenario.class);
        }
        return null;
    }
}
