package org.motechproject.whp.adherence.mapping;

import org.junit.Before;
import org.junit.Test;
import org.motechproject.util.DateUtil;
import org.motechproject.whp.adherence.domain.Adherence;
import org.motechproject.whp.adherence.domain.AdherenceConstants;
import org.motechproject.whp.adherence.domain.PillStatus;
import org.motechproject.whp.patient.builder.PatientBuilder;
import org.motechproject.whp.patient.domain.Patient;
import org.motechproject.whp.patient.domain.ProvidedTreatment;
import org.motechproject.whp.patient.domain.Treatment;
import org.motechproject.whp.patient.domain.TreatmentCategory;
import org.motechproject.whp.refdata.domain.DiseaseClass;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.motechproject.model.DayOfWeek.Monday;

public class AdherenceDataMapperTest {

    public static final String PROVIDER_ID = "ProviderId";
    private Patient patient;

    @Before
    public void setup() {
        Treatment treatment = new Treatment(new TreatmentCategory(), DiseaseClass.E, 10);

        ProvidedTreatment currentProvidedTreatment = new ProvidedTreatment(PROVIDER_ID, "TB_ID");
        currentProvidedTreatment.setTreatment(treatment);

        patient = new PatientBuilder().withDefaults().withCurrentProvidedTreatment(currentProvidedTreatment).build();
    }

    @Test
    public void shouldSetExternalIdOnRequest() {
        Adherence day = new Adherence(patient.getPatientId(), patient.currentTreatmentId(), Monday, DateUtil.today(), PillStatus.NotTaken, getMetaDataInfo(patient));

        assertEquals(patient.getPatientId(), AdherenceDataMapper.request(day).externalId());
    }

    private Map<String, Object> getMetaDataInfo(Patient patient) {
        HashMap<String, Object> map = new HashMap<String, Object>();
        map.put(AdherenceConstants.PROVIDER_ID, patient.providerId());
        map.put(AdherenceConstants.TB_ID, patient.tbId());
        return map;
    }

    @Test
    public void shouldSetProviderIdOnRequest(){
        Adherence day = new Adherence(patient.getPatientId(), patient.currentTreatmentId(), Monday, DateUtil.today(), PillStatus.NotTaken, getMetaDataInfo(patient));

        assertEquals(PROVIDER_ID, AdherenceDataMapper.request(day).meta().get(AdherenceConstants.PROVIDER_ID));
    }

    @Test
    public void shouldSetTbIdAsMetaDataOnRequest() {
        Adherence day = new Adherence(patient.getPatientId(), patient.currentTreatmentId(), Monday, DateUtil.today(), PillStatus.Unknown, getMetaDataInfo(patient));

        assertNotNull(patient.tbId());
        assertEquals(patient.tbId(), AdherenceDataMapper.request(day).meta().get(AdherenceConstants.TB_ID));
    }

    @Test
    public void shouldMarkDosesTaken() {
        Adherence day = new Adherence(patient.getPatientId(), patient.currentTreatmentId(), Monday, DateUtil.today(), PillStatus.Taken, getMetaDataInfo(patient));

        assertEquals(1, AdherenceDataMapper.request(day).status());
    }

    @Test
    public void shouldMarkDosesMissed() {
        Adherence day = new Adherence(patient.getPatientId(), patient.currentTreatmentId(), Monday, DateUtil.today(), PillStatus.NotTaken, getMetaDataInfo(patient));

        assertEquals(2, AdherenceDataMapper.request(day).status());
    }
}
