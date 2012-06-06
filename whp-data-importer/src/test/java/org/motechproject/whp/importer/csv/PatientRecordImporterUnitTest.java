package org.motechproject.whp.importer.csv;

import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.motechproject.whp.importer.csv.builder.ImportPatientRequestBuilder;
import org.motechproject.whp.importer.csv.mapper.ImportPatientRequestMapper;
import org.motechproject.whp.importer.csv.request.ImportPatientRequest;
import org.motechproject.whp.patient.command.UpdateScope;
import org.motechproject.whp.patient.contract.PatientRequest;
import org.motechproject.whp.patient.domain.Patient;
import org.motechproject.whp.patient.repository.AllPatients;
import org.motechproject.whp.refdata.domain.PatientType;
import org.motechproject.whp.refdata.domain.WeightInstance;
import org.motechproject.whp.registration.service.RegistrationService;
import org.motechproject.whp.validation.RequestValidator;

import static java.util.Arrays.asList;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.initMocks;

public class PatientRecordImporterUnitTest {

    @Mock
    RequestValidator validator;

    PatientRecordImporter patientRecordImporter;

    @Mock
    private RegistrationService registrationService;

    @Mock
    private ImportPatientRequestMapper importPatientRequestMapper;

    @Mock
    AllPatients allPatients;

    @Before
    public void setup() {
        initMocks(this);
        patientRecordImporter = new PatientRecordImporter(registrationService, validator, importPatientRequestMapper, allPatients);
    }

    @Test
    public void shouldSaveAllPatientDataEvenIfErrorOccurs() {
        ImportPatientRequest importPatientRequest1 = new ImportPatientRequest();
        importPatientRequest1.setCase_id("1");
        ImportPatientRequest importPatientRequest2 = new ImportPatientRequest();
        importPatientRequest2.setCase_id("2");
        PatientRequest patientRequest1 = new PatientRequest();
        patientRequest1.setCase_id("1");
        PatientRequest patientRequest2 = new PatientRequest();
        patientRequest2.setCase_id("2");

        when(importPatientRequestMapper.map(importPatientRequest1)).thenReturn(patientRequest1);
        when(importPatientRequestMapper.map(importPatientRequest2)).thenReturn(patientRequest2);
        doThrow(new RuntimeException("Exception to be thrown for test")).when(registrationService).registerPatient(patientRequest1);

        patientRecordImporter.post(asList((Object) importPatientRequest1, importPatientRequest2));

        verify(registrationService, times(1)).registerPatient(patientRequest1);
        verify(registrationService, times(1)).registerPatient(patientRequest2);
    }

    @Test
    public void shouldReturnFalseIfInvalid() {
        ImportPatientRequest importPatientRequest1 = new ImportPatientRequestBuilder().withCaseId("1").withLastModifiedDate(DateTime.now()).build();
        ImportPatientRequest importPatientRequest2 = new ImportPatientRequestBuilder().withCaseId("2").withLastModifiedDate(DateTime.now()).build();
        doThrow(new RuntimeException("Exception to be thrown for test")).when(validator).validate(importPatientRequest2, UpdateScope.createScope);

        assertEquals(false, patientRecordImporter.validate(asList((Object) importPatientRequest1, importPatientRequest2)).isValid());
    }

    @Test
    public void shouldReturnTrueIfValid() {
        ImportPatientRequest importPatientRequest1 = new ImportPatientRequestBuilder().withDefaults().withCaseId("1").build();
        ImportPatientRequest importPatientRequest2 = new ImportPatientRequestBuilder().withDefaults().withCaseId("2").build();

        assertEquals(true, patientRecordImporter.validate(asList((Object) importPatientRequest1, importPatientRequest2)).isValid());
    }

    @Test
    public void shouldFailValidationIfDuplicateCaseIdPresentInGivenData() {
        ImportPatientRequest importPatientRequest1 = new ImportPatientRequestBuilder().withDefaults().withCaseId("1").build();
        ImportPatientRequest importPatientRequest2 = new ImportPatientRequestBuilder().withDefaults().withCaseId("1").build();

        assertEquals(false, patientRecordImporter.validate(asList((Object) importPatientRequest1, importPatientRequest2)).isValid());
    }

    @Test
    public void shouldFailValidationIfDuplicateCaseIdPresentInDb() {
        ImportPatientRequest importPatientRequest1 = new ImportPatientRequestBuilder().withDefaults().withCaseId("1").build();
        when(allPatients.findByPatientId("1")).thenReturn(new Patient());
        assertEquals(false, patientRecordImporter.validate(asList((Object) importPatientRequest1)).isValid());
    }


    @Test
    public void shouldSetDefaultValuesOnValidation() {
        DateTime patientRegDate = DateTime.now().plusDays(-2);
        ImportPatientRequest importPatientRequest1 = new ImportPatientRequestBuilder().withPhi(null).withDate_Modified(patientRegDate).withWeightDate("10/12/2010").withWeight("30").withPatientType("").build();
        DateTime patient2WtMeasuredDate = DateTime.now().plusDays(-5);
        String dateFormat = "dd/MM/YYYY";
        ImportPatientRequest importPatientRequest2 = new ImportPatientRequestBuilder().withDefaults().withDate_Modified(patientRegDate).withPhi("").withCaseId("1").withWeightDate(patient2WtMeasuredDate.toString(dateFormat)).build();
        ImportPatientRequest importPatientRequest3 = new ImportPatientRequestBuilder().withDate_Modified(patientRegDate).withCaseId("2").withPhi("yy").withDate_Modified(patientRegDate).withWeightDate(null).withPatientType(null).build();

        assertEquals(true, patientRecordImporter.validate(asList((Object) importPatientRequest1, importPatientRequest2, importPatientRequest3)).isValid());

        assertEquals(patientRegDate.toString(dateFormat), importPatientRequest1.getWeightDate(WeightInstance.PreTreatment));
        assertEquals(patientRegDate.toString(dateFormat), importPatientRequest2.getWeightDate(WeightInstance.PreTreatment));
        assertEquals(patientRegDate.toString(dateFormat), importPatientRequest3.getWeightDate(WeightInstance.PreTreatment));

        assertEquals(PatientType.New.name(), importPatientRequest1.getPatient_type());
        assertEquals(PatientType.Chronic.name(), importPatientRequest2.getPatient_type());
        assertEquals(PatientType.New.name(), importPatientRequest3.getPatient_type());

        assertEquals("WHP", importPatientRequest1.getPhi());
        assertEquals("WHP", importPatientRequest2.getPhi());
        assertEquals("yy", importPatientRequest3.getPhi());
        assertEquals(patientRegDate.toString(dateFormat),importPatientRequest1.getWeightDate(WeightInstance.PreTreatment));
    }
}
