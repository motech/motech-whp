package org.motechproject.whp.importer.csv;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.motechproject.whp.request.PatientWebRequest;
import org.motechproject.whp.validation.RequestValidator;
import org.motechproject.whp.webservice.PatientWebService;

import java.util.Properties;

import static java.util.Arrays.asList;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.initMocks;

public class PatientRecordImporterUnitTest {

    @Mock
    Properties properties;
    @Mock
    PatientWebService patientWebService;
    @Mock
    RequestValidator validator;
    PatientRecordImporter patientRecordImporter;

    @Before
    public void setup() {
        initMocks(this);
        patientRecordImporter = new PatientRecordImporter(patientWebService, properties, validator);
    }

    @Test
    public void shouldSavePatientData() {
        PatientWebRequest patientWebRequest1 = new PatientWebRequest();
        patientWebRequest1.setCase_id("1");
        PatientWebRequest patientWebRequest2 = new PatientWebRequest();
        patientWebRequest2.setCase_id("2");

        patientRecordImporter.post(asList((Object) patientWebRequest1, patientWebRequest2));

        verify(patientWebService, times(1)).createCase(patientWebRequest1);
        verify(patientWebService, times(1)).createCase(patientWebRequest2);
    }

    @Test
    public void shouldReturnFalseIfInvalid() {
        PatientWebRequest patientWebRequest = new PatientWebRequest();

        when(properties.getProperty("remedi.api.key")).thenReturn("app_key");
        doThrow(new RuntimeException()).when(validator).validate(any(), anyString());

        assertEquals(false, patientRecordImporter.validate(asList((Object) patientWebRequest)));
        assertEquals("app_key", patientWebRequest.getApi_key());
        verify(properties, times(1)).getProperty("remedi.api.key");
    }

    @Test
    public void shouldReturnTrueIfValid() {
        PatientWebRequest patientWebRequest = new PatientWebRequest();

        when(properties.getProperty("remedi.api.key")).thenReturn("app_key");

        assertEquals(true, patientRecordImporter.validate(asList((Object) patientWebRequest)));
        assertEquals("app_key", patientWebRequest.getApi_key());
        verify(properties, times(1)).getProperty("remedi.api.key");
    }
}
