package org.motechproject.whp.importer.csv;

import org.dozer.DozerBeanMapper;
import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.motechproject.whp.importer.csv.request.ImportPatientRequest;
import org.motechproject.whp.patient.contract.PatientRequest;
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
        ImportPatientRequest importPatientRequest = new ImportPatientRequest();
        doThrow(new RuntimeException("Exception to be thrown for test")).when(validator).validate(any(), anyString());

        assertEquals(false, patientRecordImporter.validate(asList((Object) importPatientRequest)));
    }

    @Test
    public void shouldReturnTrueIfValid() {
        ImportPatientRequest importPatientRequest1 = new ImportPatientRequest();
        ImportPatientRequest importPatientRequest2 = new ImportPatientRequest();
        String date_modified = DateTime.now().toString("dd/MM/YYYY HH:mm:ss");
        importPatientRequest1.setDate_modified(date_modified);
        String weight_date = DateTime.now().plusDays(2).toString("dd/MM/YYYY HH:mm:ss");
        importPatientRequest2.setWeight_date(weight_date);

        assertEquals(true, patientRecordImporter.validate(asList((Object) importPatientRequest1, importPatientRequest2)));
        assertEquals(date_modified,importPatientRequest1.getWeight_date());
        assertEquals(weight_date,importPatientRequest2.getWeight_date());

    }
}
