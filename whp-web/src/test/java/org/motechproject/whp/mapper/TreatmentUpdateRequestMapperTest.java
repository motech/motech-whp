package org.motechproject.whp.mapper;

import org.dozer.DozerBeanMapper;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.motechproject.model.DayOfWeek;
import org.motechproject.whp.builder.PatientWebRequestBuilder;
import org.motechproject.whp.patient.contract.PatientRequest;
import org.motechproject.whp.patient.contract.TreatmentUpdateRequest;
import org.motechproject.whp.patient.domain.*;
import org.motechproject.whp.patient.repository.AllTreatmentCategories;
import org.motechproject.whp.patient.repository.SpringIntegrationTest;
import org.motechproject.whp.request.PatientWebRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;

import java.util.Arrays;

import static org.junit.Assert.assertEquals;
import static org.mockito.MockitoAnnotations.initMocks;


@ContextConfiguration(locations = "classpath*:META-INF/spring/applicationContext.xml")
public class TreatmentUpdateRequestMapperTest extends SpringIntegrationTest {

    @Autowired
    AllTreatmentCategories allTreatmentCategories;

    @Autowired
    DozerBeanMapper treatmentUpdateRequestMapper;

    @Before
    public void setUp() {
        initMocks(this);
        TreatmentCategory treatmentCategory = new TreatmentCategory("cat1", "01", 3, 12, 22, Arrays.asList(DayOfWeek.Monday));
        allTreatmentCategories.add(treatmentCategory);
    }

    @Test
    public void shouldCreateTreatmentUpdateRequest() {
        PatientWebRequest patientWebRequest = new PatientWebRequestBuilder().withOnlyRequiredTreatmentUpdateFields().build();
        TreatmentUpdateRequest treatmentUpdateRequest = treatmentUpdateRequestMapper.map(patientWebRequest, TreatmentUpdateRequest.class);

        assertEquals(patientWebRequest.getCase_id(), treatmentUpdateRequest.getCase_id());
        assertEquals(patientWebRequest.getDate_modified(), treatmentUpdateRequest.getDate_modified().toString("dd/MM/YYYY HH:mm:ss"));
        assertEquals(patientWebRequest.getOld_tb_id(), treatmentUpdateRequest.getOld_tb_id());
        assertEquals(patientWebRequest.getProvider_id(), treatmentUpdateRequest.getProvider_id());
        assertEquals(patientWebRequest.getReason_for_closure(), treatmentUpdateRequest.getReason_for_closure());
        assertEquals(patientWebRequest.getTb_id(), treatmentUpdateRequest.getTb_id());
        assertEquals(patientWebRequest.getTreatment_category(), treatmentUpdateRequest.getTreatment_category().getCode());
        assertEquals(patientWebRequest.getTreatment_complete(), treatmentUpdateRequest.getTreatment_complete());
        assertEquals(patientWebRequest.getTreatment_update(), treatmentUpdateRequest.getTreatment_update().name());
    }
}
