package org.motechproject.whp.importer.csv.request;

import lombok.Data;
import org.motechproject.importer.annotation.ColumnName;
import org.motechproject.validation.constraints.DateTimeFormat;
import org.motechproject.validation.constraints.Enumeration;
import org.motechproject.validation.constraints.NamedConstraint;
import org.motechproject.validation.constraints.NotNullOrEmpty;
import org.motechproject.whp.refdata.domain.DiseaseClass;
import org.motechproject.whp.refdata.domain.Gender;
import org.motechproject.whp.refdata.domain.PatientType;
import org.motechproject.whp.refdata.domain.SampleInstance;
import org.motechproject.whp.validation.ProviderIdValidator;

import javax.validation.Valid;
import javax.validation.constraints.Digits;
import javax.validation.constraints.Pattern;

import static org.motechproject.whp.common.util.WHPDate.DATE_FORMAT;

@Data
public class ImportPatientRequest {

    @NotNullOrEmpty
    @ColumnName(name = "Patient ID *")
    private String case_id;

    @NotNullOrEmpty
    @DateTimeFormat(pattern = DATE_FORMAT, validateEmptyString = false)
    @ColumnName(name = "Registration date")
    private String date_modified;

    @ColumnName(name = "Patient TB Registration Number*")
    private String tb_registration_number;

    @ColumnName(name = "PHI: World Health Partners (default for all cases)")
    private String phi;

    @NotNullOrEmpty
    @ColumnName(name = "Patient First Name*")
    private String first_name;

    @ColumnName(name = "Patient Last Name")
    private String last_name;

    @NotNullOrEmpty
    @Enumeration(type = Gender.class)
    @ColumnName(name = "Patient Gender:(F/M/O)*")
    private String gender;

    @Enumeration(type = PatientType.class, validateEmptyString = false)
    @ColumnName(name = "Type of Patient:(New/Relapse/TransferredIn/TreatmentAfterDefault/TreatmentFailure/Chronic/Others)*")
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

    @ColumnName(name = "Patient Address: Landmark")
    private String address_landmark;

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
        smearTestResultRequest.setDate1(SampleInstance.PreTreatment, date);
    }

    @ColumnName(name = "PreTreatment Sputum Result 1:*")
    public void setPreTreatmentSmearTestResult1(String result) {
        smearTestResultRequest.setResult1(SampleInstance.PreTreatment, result);
    }

    @ColumnName(name = "PreTreatment Sputum Date of Test 2:*")
    public void setPreTreatmentSmearTestDate2(String date) {
        smearTestResultRequest.setDate2(SampleInstance.PreTreatment, date);
    }

    @ColumnName(name = "PreTreatment Sputum Result 2:*")
    public void setPreTreatmentSmearTestResult2(String result) {
        smearTestResultRequest.setResult2(SampleInstance.PreTreatment, result);
    }

    @ColumnName(name = "EndIP Date of Test 1:")
    public void setEndIpSmearTestDate1(String date) {
        smearTestResultRequest.setDate1(SampleInstance.EndIP, date);
    }

    @ColumnName(name = "EndIP Result 1:")
    public void setEndIpSmearTestResult1(String result) {
        smearTestResultRequest.setResult1(SampleInstance.EndIP, result);
    }

    @ColumnName(name = "EndIP Date of Test 2:")
    public void setEndIpSmearTestDate2(String date) {
        smearTestResultRequest.setDate2(SampleInstance.EndIP, date);
    }

    @ColumnName(name = "EndIP Result 2:")
    public void setEndIpSmearTestResult2(String result) {
        smearTestResultRequest.setResult2(SampleInstance.EndIP, result);
    }

    @ColumnName(name = "ExtendedIP Date of Test 1:")
    public void setExtendedIpSmearTestDate1(String date) {
        smearTestResultRequest.setDate1(SampleInstance.ExtendedIP, date);
    }

    @ColumnName(name = "ExtendedIP Result 1:")
    public void setExtendedIpSmearTestResult1(String result) {
        smearTestResultRequest.setResult1(SampleInstance.ExtendedIP, result);
    }

    @ColumnName(name = "ExtendedIP Date of Test 2:")
    public void setExtendedIpSmearTestDate2(String date) {
        smearTestResultRequest.setDate2(SampleInstance.ExtendedIP, date);
    }

    @ColumnName(name = "ExtendedIP Result 2:")
    public void setExtendedIpSmearTestResult2(String result) {
        smearTestResultRequest.setResult2(SampleInstance.ExtendedIP, result);
    }

    @ColumnName(name = "TwoMonthsIntoCP Date of Test 1:")
    public void setTwoMonthsIntoCpSmearTestDate1(String date) {
        smearTestResultRequest.setDate1(SampleInstance.TwoMonthsIntoCP, date);
    }

    @ColumnName(name = "TwoMonthsIntoCP Result 1:")
    public void setTwoMonthsIntoCpSmearTestResult1(String result) {
        smearTestResultRequest.setResult1(SampleInstance.TwoMonthsIntoCP, result);
    }

    @ColumnName(name = "TwoMonthsIntoCP Date of Test 2:")
    public void setTwoMonthsIntoCpSmearTestDate2(String date) {
        smearTestResultRequest.setDate2(SampleInstance.TwoMonthsIntoCP, date);
    }

    @ColumnName(name = "TwoMonthsIntoCP Result 2:")
    public void setTwoMonthsIntoCpSmearTestResult2(String result) {
        smearTestResultRequest.setResult2(SampleInstance.TwoMonthsIntoCP, result);
    }

    @ColumnName(name = "EndTreatment Date of Test 1:")
    public void setEndTreatmentSmearTestDate1(String date) {
        smearTestResultRequest.setDate1(SampleInstance.EndTreatment, date);
    }

    @ColumnName(name = "EndTreatment Result 1:")
    public void setEndTreatmentSmearTestResult1(String result) {
        smearTestResultRequest.setResult1(SampleInstance.EndTreatment, result);
    }

    @ColumnName(name = "EndTreatment Date of Test 2:")
    public void setEndTreatmentSmearTestDate2(String date) {
        smearTestResultRequest.setDate2(SampleInstance.EndTreatment, date);
    }

    @ColumnName(name = "EndTreatment Result 2:")
    public void setEndTreatmentSmearTestResult2(String result) {
        smearTestResultRequest.setResult2(SampleInstance.EndTreatment, result);
    }

    @ColumnName(name = "PreTreatmentDate on which Weight was measured:")
    public void setPreTreatmentWeightDate(String date) {
        weightStatisticsRequest.setWeightDate(SampleInstance.PreTreatment, date);
    }

    @ColumnName(name = "PreTreatment Weight Result:")
    public void setPreTreatmentWeight(String weight) {
        weightStatisticsRequest.setWeight(SampleInstance.PreTreatment, weight);
    }

    @ColumnName(name = "EndIP Date on which Weight was measured:")
    public void setEndIpWeightDate(String date) {
        weightStatisticsRequest.setWeightDate(SampleInstance.EndIP, date);
    }

    @ColumnName(name = "EndIP Weight Result:")
    public void setEndIpWeight(String weight) {
        weightStatisticsRequest.setWeight(SampleInstance.EndIP, weight);
    }

    @ColumnName(name = "ExtendedIP Date on which Weight was measured:")
    public void setExtendedIpWeightDate(String date) {
        weightStatisticsRequest.setWeightDate(SampleInstance.ExtendedIP, date);
    }

    @ColumnName(name = "ExtendedIP Weight Result:")
    public void setExtendedIpWeight(String weight) {
        weightStatisticsRequest.setWeight(SampleInstance.ExtendedIP, weight);
    }

    @ColumnName(name = "TwoMonthsIntoCPDate on which Weight was measured:")
    public void setTwoMonthsIntoCpWeightDate(String date) {
        weightStatisticsRequest.setWeightDate(SampleInstance.TwoMonthsIntoCP, date);
    }

    @ColumnName(name = "TwoMonthsIntoCP Result:")
    public void setTwoMonthsIntoCpWeight(String weight) {
        weightStatisticsRequest.setWeight(SampleInstance.TwoMonthsIntoCP, weight);
    }

    @ColumnName(name = "EndTreatment Date on which Weight was measured:")
    public void setEndTreatmentWeightDate(String date) {
        weightStatisticsRequest.setWeightDate(SampleInstance.EndTreatment, date);
    }

    @ColumnName(name = "EndTreatment Result:")
    public void setEndTreatmentWeight(String weight) {
        weightStatisticsRequest.setWeight(SampleInstance.EndTreatment, weight);
    }

    public String getTestDate1(SampleInstance type) {
        return smearTestResultRequest.getTestDate1(type);
    }

    public String getTestResult1(SampleInstance type) {
        return smearTestResultRequest.getTestResult1(type);
    }

    public String getTestDate2(SampleInstance type) {
        return smearTestResultRequest.getTestDate2(type);
    }

    public String getTestResult2(SampleInstance type) {
        return smearTestResultRequest.getTestResult2(type);
    }

    public String getWeightDate(SampleInstance type) {
        return weightStatisticsRequest.getWeightDate(type);
    }

    public String getWeight(SampleInstance type) {
        return weightStatisticsRequest.getWeight(type);
    }


    public boolean hasSmearTestInstanceRecord(SampleInstance type) {
        return smearTestResultRequest.hasSmearTestInstanceRecord(type);
    }

    public boolean hasWeightInstanceRecord(SampleInstance type) {
        return weightStatisticsRequest.hasWeightInstanceRecord(type);
    }

    public SmearTestResultRequests.SmearTestResultRequest getSmearTestResultRequestByType(SampleInstance instance) {
        return smearTestResultRequest.getSmearTestInstanceRecord(instance);
    }

    public WeightStatisticsRequests.WeightStatisticsRequest getWeightStatisticsRequestByType(SampleInstance instance) {
        return weightStatisticsRequest.getWeightStatisticsRecord(instance);
    }
}
