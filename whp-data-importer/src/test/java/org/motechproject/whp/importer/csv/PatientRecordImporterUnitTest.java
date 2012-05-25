package org.motechproject.whp.importer.csv;

import org.dozer.DozerBeanMapper;
import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.motechproject.whp.importer.csv.builder.ImportPatientRequestBuilder;
import org.motechproject.whp.importer.csv.request.ImportPatientRequest;
import org.motechproject.whp.patient.command.UpdateScope;
import org.motechproject.whp.patient.contract.PatientRequest;
import org.motechproject.whp.refdata.domain.PatientType;
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
    private DozerBeanMapper patientImportRequestMapper;

    @Before
    public void setup() {
        initMocks(this);
        patientRecordImporter = new PatientRecordImporter(registrationService, validator, patientImportRequestMapper);
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

        when(patientImportRequestMapper.map(importPatientRequest1, PatientRequest.class)).thenReturn(patientRequest1);
        when(patientImportRequestMapper.map(importPatientRequest2, PatientRequest.class)).thenReturn(patientRequest2);
        doThrow(new RuntimeException("Exception to be thrown for test")).when(registrationService).registerPatient(patientRequest1);

        patientRecordImporter.post(asList((Object) importPatientRequest1, importPatientRequest2));

        verify(registrationService, times(1)).registerPatient(patientRequest1);
        verify(registrationService, times(1)).registerPatient(patientRequest2);
    }

    @Test
    public void shouldReturnFalseIfInvalid() {
        ImportPatientRequest importPatientRequest1 = new ImportPatientRequestBuilder().withCaseId("1").withDate_Modified(DateTime.now()).build();
        ImportPatientRequest importPatientRequest2 = new ImportPatientRequestBuilder().withCaseId("2").withDate_Modified(DateTime.now()).build();
        doThrow(new RuntimeException("Exception to be thrown for test")).when(validator).validate(importPatientRequest2, UpdateScope.createScope);

        assertEquals(false, patientRecordImporter.validate(asList((Object) importPatientRequest1,importPatientRequest2)));
    }

    @Test
    public void shouldReturnTrueIfValid() {
        ImportPatientRequest importPatientRequest1 = new ImportPatientRequestBuilder().withDefaults().build();
        ImportPatientRequest importPatientRequest2 = new ImportPatientRequestBuilder().withDefaults().build();

        assertEquals(true, patientRecordImporter.validate(asList((Object) importPatientRequest1, importPatientRequest2)));
    }

    @Test
    public void shouldSetDefaultValuesOnValidation() {
        DateTime patient1RegDate = DateTime.now().plusDays(-2);
        ImportPatientRequest importPatientRequest1 = new ImportPatientRequestBuilder().withDate_Modified(patient1RegDate).withWeightMeasuredDate("").withPatientType("").build();
        DateTime patient2WtMeasuredDate = DateTime.now().plusDays(-5);
        String dateFormat = "dd/MM/YYYY";
        ImportPatientRequest importPatientRequest2 = new ImportPatientRequestBuilder().withDefaults().withWeightMeasuredDate(patient2WtMeasuredDate.toString(dateFormat)).build();
        ImportPatientRequest importPatientRequest3 = new ImportPatientRequestBuilder().withDate_Modified(patient1RegDate).withWeightMeasuredDate(null).withPatientType(null).build();

        assertEquals(true, patientRecordImporter.validate(asList((Object) importPatientRequest1, importPatientRequest2,importPatientRequest3)));
        assertEquals(patient1RegDate.toString(dateFormat),importPatientRequest1.getWeight_date());
        assertEquals(patient2WtMeasuredDate.toString(dateFormat),importPatientRequest2.getWeight_date());
        assertEquals(PatientType.New.name(),importPatientRequest1.getPatient_type());
        assertEquals(PatientType.PHCTransfer.name(),importPatientRequest2.getPatient_type());
        assertEquals(PatientType.New.name(),importPatientRequest3.getPatient_type());
        assertEquals(patient1RegDate.toString(dateFormat),importPatientRequest3.getWeight_date());


    }
}
