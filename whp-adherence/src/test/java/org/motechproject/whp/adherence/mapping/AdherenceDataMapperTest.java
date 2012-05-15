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
        Adherence day = new Adherence(patient.getPatientId(), patient.currentTreatmentId(), Monday, DateUtil.today(), PillStatus.NotTaken, patient.tbId(), null);

        AdherenceDataMapper mapper = new AdherenceDataMapper(day);
        assertEquals(patient.getPatientId(), mapper.request().externalId());
    }

    @Test
    public void shouldSetProviderIdOnRequest(){
        Adherence day = new Adherence(patient.getPatientId(), patient.currentTreatmentId(), Monday, DateUtil.today(), PillStatus.NotTaken, null, patient.getCurrentProvidedTreatment().getProviderId());

        AdherenceDataMapper mapper = new AdherenceDataMapper(day);
        assertEquals(PROVIDER_ID, mapper.request().meta().get(AdherenceConstants.PROVIDER_ID));
    }

    @Test
    public void shouldSetTbIdAsMetaDataOnRequest() {
        Adherence day = new Adherence(patient.getPatientId(), patient.currentTreatmentId(), Monday, DateUtil.today(), PillStatus.Unknown, patient.tbId(), null);

        AdherenceDataMapper mapper = new AdherenceDataMapper(day);
        assertNotNull(patient.tbId());
        assertEquals(patient.tbId(), mapper.request().meta().get(AdherenceConstants.TB_ID));
    }

    @Test
    public void shouldMarkDosesTaken() {
        Adherence day = new Adherence(patient.getPatientId(), patient.currentTreatmentId(), Monday, DateUtil.today(), PillStatus.Taken, patient.tbId(), null);

        AdherenceDataMapper mapper = new AdherenceDataMapper(day);
        assertEquals(1, mapper.request().status());
    }

    @Test
    public void shouldMarkDosesMissed() {
        Adherence day = new Adherence(patient.getPatientId(), patient.currentTreatmentId(), Monday, DateUtil.today(), PillStatus.NotTaken, patient.tbId(), null);

        AdherenceDataMapper mapper = new AdherenceDataMapper(day);
        assertEquals(2, mapper.request().status());
    }
}
