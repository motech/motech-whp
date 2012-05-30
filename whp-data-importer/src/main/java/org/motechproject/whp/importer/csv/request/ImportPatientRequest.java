package org.motechproject.whp.importer.csv.request;

import lombok.Data;
import org.motechproject.importer.annotation.ColumnName;
import org.motechproject.validation.constraints.DateTimeFormat;
import org.motechproject.validation.constraints.Enumeration;
import org.motechproject.validation.constraints.NamedConstraint;
import org.motechproject.validation.constraints.NotNullOrEmpty;
import org.motechproject.whp.refdata.domain.*;
import org.motechproject.whp.validation.ProviderIdValidator;

import javax.validation.Valid;
import javax.validation.constraints.Digits;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.List;

@Data
public class ImportPatientRequest {

    @NotNullOrEmpty
    @ColumnName(name = "Patient ID *")
    private String case_id;

    @NotNullOrEmpty
    @DateTimeFormat(pattern = WHPConstants.DATE_TIME_FORMAT, validateEmptyString = false)
    @ColumnName(name = "Registration date")
    private String date_modified;

    @ColumnName(name = "Patient TB Registration Number*")
    private String tb_registration_number;

    @ColumnName(name = "PHI: World Health Partners (default for all cases)")
    private String phi;

    @ColumnName(name = "Patient Address: Landmark")
    private String address_landmark;

    @NotNullOrEmpty
    @ColumnName(name = "Patient First Name*")
    private String first_name;

    @NotNullOrEmpty
    @ColumnName(name = "Patient Last Name*")
    private String last_name;

    @NotNullOrEmpty
    @Enumeration(type = Gender.class)
    @ColumnName(name = "Patient Gender:(F/M/O)*")
    private String gender;

    @Enumeration(type = PatientType.class, validateEmptyString = false)
    @ColumnName(name = "Type of Patient:(New, PHSTransfer)*")
    private String patient_type;

    @Pattern(regexp = "^$|[0-9]{10}", message = "Mobile number should be empty or should have 10 digits")
    @ColumnName(name = "Patient Mobile Number")
    private String mobile_number;

    @NotNullOrEmpty
    @Enumeration(type = DiseaseClass.class)
    @ColumnName(name = "Disease Classification: (P,E) *")
    private String disease_class;

    // Address
    @NotNullOrEmpty
    @ColumnName(name = "Patient Address: Location*")
    private String address_location;

    @NotNullOrEmpty
    @ColumnName(name = "Patient Address: Village*")
    private String address_village;

    @NotNullOrEmpty
    @ColumnName(name = "Patient Address: Block*")
    private String address_block;

    @NotNullOrEmpty
    @ColumnName(name = "Patient Address: District*")
    private String address_district;

    @NotNullOrEmpty
    @ColumnName(name = "Patient Address: State*")
    private String address_state;
    // End of address

    @NotNullOrEmpty
    @Size(min = 11, max = 11)
    @ColumnName(name = "Patient TB ID*")
    private String tb_id;

    @NotNullOrEmpty
    @NamedConstraint(name = ProviderIdValidator.PROVIDER_ID_CONSTRAINT)
    @ColumnName(name = "Provider ID*")
    private String provider_id;

    @NotNullOrEmpty
    @Pattern(regexp = "[0|1][1|2]")
    @ColumnName(name = "Current Treatment Category: 01/ 02 /11/12*")
    private String treatment_category;

    @NotNullOrEmpty
    @Digits(integer = 3, fraction = 0, message = "Age must be numeric and not fractional")
    @ColumnName(name = "Patient Age*")
    private String age;

    @Valid
    private SmearTestResultRequests smearTestResultRequest = new SmearTestResultRequests();
    @Valid
    private WeightStatisticsRequests weightStatisticsRequest = new WeightStatisticsRequests();

    private boolean migrated;

    @ColumnName(name = "PreTreatment Sputum Date of Test 1:*")
    public void setPreTreatmentSmearTestDate1(String date) {
        smearTestResultRequest.setDate1(SmearTestSampleInstance.PreTreatment, date);
    }

    @ColumnName(name = "PreTreatment Sputum Result 1:*")
    public void setPreTreatmentSmearTestResult1(String result) {
        smearTestResultRequest.setResult1(SmearTestSampleInstance.PreTreatment, result);
    }

    @ColumnName(name = "PreTreatment Sputum Date of Test 2:*")
    public void setPreTreatmentSmearTestDate2(String date) {
        smearTestResultRequest.setDate2(SmearTestSampleInstance.PreTreatment, date);
    }

    @ColumnName(name = "PreTreatment Sputum Result 2:*")
    public void setPreTreatmentSmearTestResult2(String result) {
        smearTestResultRequest.setResult2(SmearTestSampleInstance.PreTreatment, result);
    }

    @ColumnName(name = "EndIP Date of Test 1:")
    public void setEndIpSmearTestDate1(String date) {
        smearTestResultRequest.setDate1(SmearTestSampleInstance.EndIP, date);
    }

    @ColumnName(name = "EndIP Result 1:")
    public void setEndIpSmearTestResult1(String result) {
        smearTestResultRequest.setResult1(SmearTestSampleInstance.EndIP, result);
    }

    @ColumnName(name = "EndIP Date of Test 2:")
    public void setEndIpSmearTestDate2(String date) {
        smearTestResultRequest.setDate2(SmearTestSampleInstance.EndIP, date);
    }

    @ColumnName(name = "EndIP Result 2:")
    public void setEndIpSmearTestResult2(String result) {
        smearTestResultRequest.setResult2(SmearTestSampleInstance.EndIP, result);
    }

    @ColumnName(name = "ExtendedIP Date of Test 1:")
    public void setExtendedIpSmearTestDate1(String date) {
        smearTestResultRequest.setDate1(SmearTestSampleInstance.ExtendedIP, date);
    }

    @ColumnName(name = "ExtendedIP Result 1:")
    public void setExtendedIpSmearTestResult1(String result) {
        smearTestResultRequest.setResult1(SmearTestSampleInstance.ExtendedIP, result);
    }

    @ColumnName(name = "ExtendedIP Date of Test 2:")
    public void setExtendedIpSmearTestDate2(String date) {
        smearTestResultRequest.setDate2(SmearTestSampleInstance.ExtendedIP, date);
    }

    @ColumnName(name = "ExtendedIP Result 2:")
    public void setExtendedIpSmearTestResult2(String result) {
        smearTestResultRequest.setResult2(SmearTestSampleInstance.ExtendedIP, result);
    }

    @ColumnName(name = "TwoMonthsIntoCP Date of Test 1:")
    public void setTwoMonthsIntoCpSmearTestDate1(String date) {
        smearTestResultRequest.setDate1(SmearTestSampleInstance.TwoMonthsIntoCP, date);
    }

    @ColumnName(name = "TwoMonthsIntoCP Result 1:")
    public void setTwoMonthsIntoCpSmearTestResult1(String result) {
        smearTestResultRequest.setResult1(SmearTestSampleInstance.TwoMonthsIntoCP, result);
    }

    @ColumnName(name = "TwoMonthsIntoCP Date of Test 2:")
    public void setTwoMonthsIntoCpSmearTestDate2(String date) {
        smearTestResultRequest.setDate2(SmearTestSampleInstance.TwoMonthsIntoCP, date);
    }

    @ColumnName(name = "TwoMonthsIntoCP Result 2:")
    public void setTwoMonthsIntoCpSmearTestResult2(String result) {
        smearTestResultRequest.setResult2(SmearTestSampleInstance.TwoMonthsIntoCP, result);
    }

    @ColumnName(name = "EndTreatment Date of Test 1:")
    public void setEndTreatmentSmearTestDate1(String date) {
        smearTestResultRequest.setDate1(SmearTestSampleInstance.EndTreatment, date);
    }

    @ColumnName(name = "EndTreatment Result 1:")
    public void setEndTreatmentSmearTestResult1(String result) {
        smearTestResultRequest.setResult1(SmearTestSampleInstance.EndTreatment, result);
    }

    @ColumnName(name = "EndTreatment Date of Test 2:")
    public void setEndTreatmentSmearTestDate2(String date) {
        smearTestResultRequest.setDate2(SmearTestSampleInstance.EndTreatment, date);
    }

    @ColumnName(name = "EndTreatment Result 2:")
    public void setEndTreatmentSmearTestResult2(String result) {
        smearTestResultRequest.setResult2(SmearTestSampleInstance.EndTreatment, result);
    }

    @ColumnName(name = "PreTreatmentDate on which Weight was measured:")
    public void setPreTreatmentWeightDate(String date) {
        weightStatisticsRequest.setWeightDate(WeightInstance.PreTreatment, date);
    }

    @ColumnName(name = "PreTreatment Weight Result:")
    public void setPreTreatmentWeight(String weight) {
        weightStatisticsRequest.setWeight(WeightInstance.PreTreatment, weight);
    }

    @ColumnName(name = "EndIP Date on which Weight was measured:")
    public void setEndIpWeightDate(String date) {
        weightStatisticsRequest.setWeightDate(WeightInstance.EndIP, date);
    }

    @ColumnName(name = "EndIP Weight Result:")
    public void setEndIpWeight(String weight) {
        weightStatisticsRequest.setWeight(WeightInstance.EndIP, weight);
    }

    @ColumnName(name = "ExtendedIP Date on which Weight was measured:")
    public void setExtendedIpWeightDate(String date) {
        weightStatisticsRequest.setWeightDate(WeightInstance.ExtendedIP, date);
    }

    @ColumnName(name = "ExtendedIP Weight Result:")
    public void setExtendedIpWeight(String weight) {
        weightStatisticsRequest.setWeight(WeightInstance.ExtendedIP, weight);
    }

    @ColumnName(name = "TwoMonthsIntoCPDate on which Weight was measured:")
    public void setTwoMonthsIntoCpWeightDate(String date) {
        weightStatisticsRequest.setWeightDate(WeightInstance.TwoMonthsIntoCP, date);
    }

    @ColumnName(name = "TwoMonthsIntoCP Result:")
    public void setTwoMonthsIntoCpWeight(String weight) {
        weightStatisticsRequest.setWeight(WeightInstance.TwoMonthsIntoCP, weight);
    }

    @ColumnName(name = "EndTreatment Date on which Weight was measured:")
    public void setEndTreatmentWeightDate(String date) {
        weightStatisticsRequest.setWeightDate(WeightInstance.EndTreatment, date);
    }

    @ColumnName(name = "EndTreatment Result:")
    public void setEndTreatmentWeight(String weight) {
        weightStatisticsRequest.setWeight(WeightInstance.EndTreatment, weight);
    }

    public String getTestDate1(SmearTestSampleInstance type) {
        return smearTestResultRequest.getTestDate1(type);
    }

    public String getTestResult1(SmearTestSampleInstance type) {
        return smearTestResultRequest.getTestResult1(type);
    }

    public String getTestDate2(SmearTestSampleInstance type) {
        return smearTestResultRequest.getTestDate2(type);
    }

    public String getTestResult2(SmearTestSampleInstance type) {
        return smearTestResultRequest.getTestResult2(type);
    }

    public String getWeightDate(WeightInstance type) {
        return weightStatisticsRequest.getWeightDate(type);
    }

    public String getWeight(WeightInstance type) {
        return weightStatisticsRequest.getWeight(type);
    }


    public boolean hasSmearTestInstanceRecord(SmearTestSampleInstance type) {
        return smearTestResultRequest.hasSmearTestInstanceRecord(type);
    }

    public boolean hasWeightInstanceRecord(WeightInstance type) {
        return weightStatisticsRequest.hasWeightInstanceRecord(type);
    }

    public SmearTestResultRequests.SmearTestResultRequest getSmearTestResultRequestByType(SmearTestSampleInstance instance) {
        return smearTestResultRequest.getSmearTestInstanceRecord(instance);
    }

    public WeightStatisticsRequests.WeightStatisticsRequest getWeightStatisticsRequestByType(WeightInstance instance) {
        return weightStatisticsRequest.getWeightStatisticsRecord(instance);
    }
}
