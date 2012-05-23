package org.motechproject.whp.importer.csv.request;

import lombok.Data;
import org.motechproject.importer.annotation.ColumnName;
import org.motechproject.validation.constraints.Enumeration;
import org.motechproject.validation.constraints.NamedConstraint;
import org.motechproject.validation.constraints.NotNull;
import org.motechproject.whp.refdata.domain.*;
import org.motechproject.whp.validation.ProviderIdValidator;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.Digits;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Data
public class ImportPatientRequest {
    //EndIP Date of Test 1:,EndIP Result 1:,EndIP Date of Test 2:,EndIP Result 2:,ExtendedIP Date of Test 1:,ExtendedIP Result 1:,ExtendedIP Date of Test 2:,ExtendedIP Result 2:,TwoMonthsIntoCP Date of Test 1:,TwoMonthsIntoCP Result 1:,TwoMonthsIntoCP Date of Test 2:,TwoMonthsIntoCP Result 2:,EndTreatment Date of Test 1:,EndTreatment Result 1:,EndTreatment Date of Test 2:,EndTreatment Result 2:
    //EndIP Date on which Weight was measured:,EndIP Weight Result:,ExtendedIP Date on which Weight was measured:,ExtendedIP Weight Result:,TwoMonthsIntoCPDate on which Weight was measured:,TwoMonthsIntoCP Result:,EndTreatment Date on which Weight was measured:,EndTreatment Result:,
    //Container Ids

    @NotNull
    @ColumnName(name = "Patient ID *")
    private String case_id;

    @DateTimeFormat(pattern = "dd/MM/YYYY HH:mm:ss")
    @ColumnName(name = "Registration date")
    private String date_modified;

    @ColumnName(name = "Patient TB Registration Number*")
    private String tb_registration_number;

    @ColumnName(name = "PHI: World Health Partners (default for all cases)")
    private String phi;

    @ColumnName(name = "Patient Address: Landmark")
    private String address_landmark;

    @NotNull
    @ColumnName(name = "Patient First Name*")
    private String first_name;

    @NotNull
    @ColumnName(name = "Patient Last Name*")
    private String last_name;

    @NotNull
    @Enumeration(type = Gender.class)
    @ColumnName(name = "Patient Gender:(F/M/O)*")
    private String gender;

    @NotNull
    @Enumeration(type = PatientType.class)
    @ColumnName(name = "Type of Patient:(New, PHSTransfer)*")
    private String patient_type;

    @Pattern(regexp = "^$|[0-9]{10}", message = "Mobile number should be empty or should have 10 digits")
    @ColumnName(name = "Patient Mobile Number")
    private String mobile_number;

    @NotNull
    @Enumeration(type = DiseaseClass.class)
    @ColumnName(name = "Disease Classification: (P,E) *")
    private String disease_class;

    @NotNull
    @ColumnName(name = "Patient Address: Location*")
    private String address_location;

    @NotNull
    @ColumnName(name = "Patient Address: Village*")
    private String address_village;

    @NotNull
    @ColumnName(name = "Patient Address: Block*")
    private String address_block;

    @NotNull
    @ColumnName(name = "Patient Address: District*")
    private String address_district;

    @NotNull
    @ColumnName(name  = "Patient Address: State*")
    private String address_state;

    private String smear_sample_instance = SmearTestSampleInstance.PreTreatment.name();

    @NotNull
    @DateTimeFormat(pattern = "dd/MM/YYYY")
    @ColumnName(name = "PreTreatment Sputum Date of Test 1:*")
    private String smear_test_date_1;

    @NotNull
    @Enumeration(type = SmearTestResult.class)
    @ColumnName(name = "PreTreatment Sputum Result 1:*")
    private String smear_test_result_1;

    @NotNull
    @DateTimeFormat(pattern = "dd/MM/YYYY")
    @ColumnName(name = "PreTreatment Sputum Date of Test 2:*")
    private String smear_test_date_2;

    @NotNull
    @Enumeration(type = SmearTestResult.class)
    @ColumnName(name = "PreTreatment Sputum Result 2:*")
    private String smear_test_result_2;

    private String weight_instance = WeightInstance.PreTreatment.name();

    @DateTimeFormat(pattern = "dd/MM/YYYY")
    @ColumnName(name = "PreTreatmentDate on which Weight was measured:")
    private String weight_date;

    @Digits(integer = Integer.MAX_VALUE, fraction = Integer.MAX_VALUE, message = "Weight must be a real number")
    @ColumnName(name = "PreTreatment Weight Result:")
    private String weight;

    @NotNull
    @Size(min = 11, max = 11)
    @ColumnName(name = "Patient TB ID*")
    private String tb_id;

    @NamedConstraint(name = ProviderIdValidator.PROVIDER_ID_CONSTRAINT)
    @ColumnName(name = "Provider ID*")
    private String provider_id;

    @NotNull
    @Pattern(regexp = "[0|1][1|2]")
    @ColumnName(name = "Current Treatment Category: 01/ 02 /11/12*")
    private String treatment_category;

    @NotNull
    @Digits(integer = 3, fraction = 0, message = "Age must be numeric and not fractional")
    @ColumnName(name = "Patient Age*")
    private String age;

    public ImportPatientRequest() {
    }

    public ImportPatientRequest setPatientInfo(String caseId, String firstName, String lastName, String gender, String patientType, String patientMobileNumber, String phi) {
        this.case_id = caseId;
        this.first_name = firstName;
        this.last_name = lastName;
        this.gender = gender;
        this.patient_type = patientType;
        this.mobile_number = patientMobileNumber;
        this.phi = phi;
        return this;
    }

    public ImportPatientRequest setPatientAddress(String houseNumber, String landmark, String block, String village, String district, String state) {
        this.address_location = houseNumber;
        this.address_landmark = landmark;
        this.address_block = block;
        this.address_village = village;
        this.address_district = district;
        this.address_state = state;
        return this;
    }

    public ImportPatientRequest setTreatmentData(String category, String tbId, String providerId, String diseaseClass, String patientAge, String registrationNumber) {
        this.treatment_category = category;
        this.tb_id = tbId;
        this.provider_id = providerId;
        this.disease_class = diseaseClass;
        this.age = patientAge;
        this.tb_registration_number = registrationNumber;
        return this;
    }

    public ImportPatientRequest setSmearTestResults(String smear_sample_instance_1, String smear_test_date_1, String smear_result_1, String smear_test_date_2, String smear_result_2) {
        this.smear_sample_instance = smear_sample_instance_1;
        this.smear_test_date_1 = smear_test_date_1;
        this.smear_test_result_1 = smear_result_1;
        this.smear_test_date_2 = smear_test_date_2;
        this.smear_test_result_2 = smear_result_2;
        return this;
    }

    public ImportPatientRequest setWeightStatistics(String weightInstance, String weight) {
        this.weight_instance = weightInstance;
        this.weight = weight;
        return this;
    }

}
