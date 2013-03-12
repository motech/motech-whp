package org.motechproject.whp.importer.csv.builder;

import org.joda.time.DateTime;
import org.motechproject.whp.common.domain.SmearTestResult;
import org.motechproject.whp.common.domain.SputumTrackingInstance;
import org.motechproject.whp.importer.csv.request.ImportPatientRequest;
import org.motechproject.whp.common.domain.Gender;
import org.motechproject.whp.patient.domain.PatientType;

import static org.motechproject.whp.common.util.WHPDate.DATE_TIME_FORMAT;

public class ImportPatientRequestBuilder {

    private ImportPatientRequest importPatientRequest = new ImportPatientRequest();

    public ImportPatientRequestBuilder withDefaults() {
        return withPatientInfo("1234567890", "Foo", "Bar", Gender.M.name(), PatientType.Chronic.name(), "1234567890", "phi")
                .withPatientAddress("house number", "landmark", "block", "village", "district", "state")
                .withSmearTestResults("19/07/2000", SmearTestResult.Positive.name(), "21/09/2000", SmearTestResult.Positive.name())
                .withWeightStatistics("22/09/2000", "99.7")
                .withTreatmentData("01", "12345678901", "123456", "P", "40", "registrationNumber")
                .withLastModifiedDate("10/10/2010");
    }

    public ImportPatientRequestBuilder withSimpleUpdateFields() {
        return withPatientInfo("1234567890", null, null, null, null, "9087654321", null)
                .withPatientAddress("new_house number", "new_landmark", "new_block", "new_village", "new_district", "new_state")
                .withSmearTestResults("19/07/2010", SmearTestResult.Negative.name(), "21/09/2010", SmearTestResult.Negative.name())
                .withWeightStatistics("20/07/2010", "99.7")
                .withTreatmentData(null, "elevenDigit", null, null, "50", null)
                .withLastModifiedDate("15/10/2010 10:10:10");
    }

    public ImportPatientRequestBuilder withOnlyRequiredTreatmentUpdateFields() {
        return withPatientInfo("1234567890", null, null, null, null, null, null)
                .withTreatmentData("01", "tbtbtbtbtbt", "providerId", null, null, null)
                .withLastModifiedDate("15/10/2010 10:10:10");
    }

    public ImportPatientRequestBuilder withDefaultsForTransferIn() {
        return withTreatmentData(null, "elevenDigit", "newProviderId", null, null, null)
                .withCaseId("12345")
                .withLastModifiedDate("15/10/2010 10:10:10");
    }

    public ImportPatientRequestBuilder withDefaultsForPauseTreatment() {
        return withTreatmentData(null, "elevenDigit", "newProviderId", null, null, null)
                .withCaseId("12345")
                .withLastModifiedDate("15/10/2010 10:10:10");
    }

    public ImportPatientRequestBuilder withDefaultsForRestartTreatment() {
        return withTreatmentData(null, "elevenDigit", "newProviderId", null, null, null)
                .withCaseId("12345")
                .withLastModifiedDate("15/10/2010 10:10:10");
    }

    public ImportPatientRequestBuilder withSmearTestResults(SputumTrackingInstance type,
                                                            String testResultDate1,
                                                            String testResult1,
                                                            String testResultDate2,
                                                            String testResult2) {
        switch (type) {
            case PreTreatment: {
                importPatientRequest.setPreTreatmentSmearTestDate1(testResultDate1);
                importPatientRequest.setPreTreatmentSmearTestResult1(testResult1);
                importPatientRequest.setPreTreatmentSmearTestDate2(testResultDate2);
                importPatientRequest.setPreTreatmentSmearTestResult2(testResult2);
                break;
            }
            case EndIP: {
                importPatientRequest.setEndIpSmearTestDate1(testResultDate1);
                importPatientRequest.setEndIpSmearTestResult1(testResult1);
                importPatientRequest.setEndIpSmearTestDate2(testResultDate2);
                importPatientRequest.setEndIpSmearTestResult2(testResult2);
                break;
            }
            case ExtendedIP: {
                importPatientRequest.setExtendedIpSmearTestDate1(testResultDate1);
                importPatientRequest.setExtendedIpSmearTestResult1(testResult1);
                importPatientRequest.setExtendedIpSmearTestDate2(testResultDate2);
                importPatientRequest.setExtendedIpSmearTestResult2(testResult2);
                break;
            }
            case TwoMonthsIntoCP: {
                importPatientRequest.setTwoMonthsIntoCpSmearTestDate1(testResultDate1);
                importPatientRequest.setTwoMonthsIntoCpSmearTestResult1(testResult1);
                importPatientRequest.setTwoMonthsIntoCpSmearTestDate2(testResultDate2);
                importPatientRequest.setTwoMonthsIntoCpSmearTestResult2(testResult2);
                break;
            }
            case EndTreatment: {
                importPatientRequest.setEndTreatmentSmearTestDate1(testResultDate1);
                importPatientRequest.setEndTreatmentSmearTestResult1(testResult1);
                importPatientRequest.setEndTreatmentSmearTestDate2(testResultDate2);
                importPatientRequest.setEndTreatmentSmearTestResult2(testResult2);
                break;
            }
        }
        return this;
    }

    public ImportPatientRequestBuilder withSmearTestResults(String testResultDate1,
                                                            String testResult1,
                                                            String testResultDate2,
                                                            String testResult2) {
        return withSmearTestDate1(testResultDate1)
                .withSmearTestDate2(testResultDate2)
                .withSmearTestResult1(testResult1)
                .withSmearTestResult2(testResult2);
    }

    public ImportPatientRequestBuilder withTreatmentData(String category,
                                                         String tbId,
                                                         String providerId,
                                                         String diseaseClass,
                                                         String patientAge,
                                                         String registrationNumber) {
        return withTreatmentCategory(category)
                .withTBId(tbId)
                .withProviderId(providerId)
                .withDiseaseClass(diseaseClass)
                .withAge(patientAge)
                .withTbRegistrationNumber(registrationNumber);
    }

    public ImportPatientRequestBuilder withPatientAddress(String houseNumber,
                                                          String landmark,
                                                          String block,
                                                          String village,
                                                          String district,
                                                          String state) {
        return withAddressLocation(houseNumber)
                .withLandmark(landmark)
                .withBlock(block)
                .withVillage(village)
                .withDistrict(district)
                .withState(state);
    }

    public ImportPatientRequestBuilder withPatientInfo(String caseId,
                                                       String firstName,
                                                       String lastName,
                                                       String gender,
                                                       String patientType,
                                                       String patientMobileNumber,
                                                       String phi) {
        return withCaseId(caseId)
                .withFirstName(firstName)
                .withLastName(lastName)
                .withGender(gender)
                .withPatientType(patientType)
                .withMobileNumber(patientMobileNumber)
                .withPhi(phi);
    }

    public ImportPatientRequestBuilder withWeightStatistics(SputumTrackingInstance type,
                                                            String measuringDate,
                                                            String weight) {
        switch (type) {
            case PreTreatment: {
                importPatientRequest.setPreTreatmentWeightDate(measuringDate);
                importPatientRequest.setPreTreatmentWeight(weight);
                break;
            }
            case EndIP: {
                importPatientRequest.setEndIpWeightDate(measuringDate);
                importPatientRequest.setEndIpWeight(weight);
                break;
            }
            case ExtendedIP: {
                importPatientRequest.setExtendedIpWeightDate(measuringDate);
                importPatientRequest.setExtendedIpWeight(weight);
                break;
            }
            case TwoMonthsIntoCP: {
                importPatientRequest.setTwoMonthsIntoCpWeightDate(measuringDate);
                importPatientRequest.setTwoMonthsIntoCpWeight(weight);
                break;
            }
            case EndTreatment: {
                importPatientRequest.setEndTreatmentWeightDate(measuringDate);
                importPatientRequest.setEndTreatmentWeight(weight);
                break;
            }
        }
        return this;
    }

    public ImportPatientRequestBuilder withWeightStatistics(String weightDate, String weight) {
        return withWeight(weight).withWeightDate(weightDate);
    }

    public ImportPatientRequestBuilder withCaseId(String caseId) {
        importPatientRequest.setCase_id(caseId);
        return this;
    }

    public ImportPatientRequestBuilder withProviderId(String providerId) {
        importPatientRequest.setProvider_id(providerId);
        return this;
    }

    public ImportPatientRequestBuilder withTreatmentCategory(String category) {
        importPatientRequest.setTreatment_category(category);
        return this;
    }

    public ImportPatientRequestBuilder withSmearTestDate1(String smearTestDate1) {
        importPatientRequest.setPreTreatmentSmearTestDate1(smearTestDate1);
        return this;
    }

    public ImportPatientRequestBuilder withSmearTestDate2(String smearTestDate2) {
        importPatientRequest.setPreTreatmentSmearTestDate2(smearTestDate2);
        return this;
    }

    public ImportPatientRequestBuilder withMobileNumber(String mobileNumber) {
        importPatientRequest.setMobile_number(mobileNumber);
        return this;
    }

    public ImportPatientRequestBuilder withSmearTestResult1(String smearTestResult) {
        importPatientRequest.setPreTreatmentSmearTestResult1(smearTestResult);
        return this;
    }

    public ImportPatientRequestBuilder withTBId(String tbId) {
        importPatientRequest.setTb_id(tbId);
        return this;
    }

    public ImportPatientRequestBuilder withGender(String gender) {
        importPatientRequest.setGender(gender);
        return this;
    }

    public ImportPatientRequestBuilder withAge(String age) {
        importPatientRequest.setAge(age);
        return this;
    }

    public ImportPatientRequestBuilder withWeight(String weight) {
        importPatientRequest.setPreTreatmentWeight(weight);
        return this;
    }

    public ImportPatientRequestBuilder withWeightDate(String date) {
        importPatientRequest.setPreTreatmentWeightDate(date);
        return this;
    }

    private ImportPatientRequestBuilder withSmearTestResult2(String testResult2) {
        importPatientRequest.setPreTreatmentSmearTestResult2(testResult2);
        return this;
    }

    private ImportPatientRequestBuilder withTbRegistrationNumber(String registrationNumber) {
        importPatientRequest.setTb_registration_number(registrationNumber);
        return this;
    }

    private ImportPatientRequestBuilder withDiseaseClass(String diseaseClass) {
        importPatientRequest.setDisease_class(diseaseClass);
        return this;
    }

    private ImportPatientRequestBuilder withDistrict(String district) {
        importPatientRequest.setAddress_district(district);
        return this;
    }

    private ImportPatientRequestBuilder withLandmark(String landmark) {
        importPatientRequest.setAddress_landmark(landmark);
        return this;
    }

    private ImportPatientRequestBuilder withState(String state) {
        importPatientRequest.setAddress_state(state);
        return this;
    }

    private ImportPatientRequestBuilder withBlock(String block) {
        importPatientRequest.setAddress_block(block);
        return this;
    }

    private ImportPatientRequestBuilder withVillage(String village) {
        importPatientRequest.setAddress_village(village);
        return this;
    }

    private ImportPatientRequestBuilder withAddressLocation(String addressLocation) {
        importPatientRequest.setAddress_location(addressLocation);
        return this;
    }

    public ImportPatientRequestBuilder withPhi(String phi) {
        importPatientRequest.setPhi(phi);
        return this;
    }

    private ImportPatientRequestBuilder withLastName(String lastName) {
        importPatientRequest.setLast_name(lastName);
        return this;
    }

    private ImportPatientRequestBuilder withFirstName(String firstName) {
        importPatientRequest.setFirst_name(firstName);
        return this;
    }

    public ImportPatientRequestBuilder withLastModifiedDate(DateTime date_modified) {
        importPatientRequest.setDate_modified(date_modified.toString(DATE_TIME_FORMAT));
        return this;
    }

    private ImportPatientRequestBuilder withLastModifiedDate(String modifiedDate) {
        importPatientRequest.setDate_modified(modifiedDate);
        return this;
    }

    public ImportPatientRequestBuilder withPatientType(String patientType) {
        importPatientRequest.setPatient_type(patientType);
        return this;
    }

    public ImportPatientRequestBuilder withDate_Modified(DateTime date) {
        importPatientRequest.setDate_modified(date.toString("dd/MM/YYYY"));
        return this;
    }

    public ImportPatientRequest build() {
        return importPatientRequest;
    }
}
