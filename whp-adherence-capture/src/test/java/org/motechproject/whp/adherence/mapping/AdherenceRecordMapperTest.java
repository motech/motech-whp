package org.motechproject.whp.adherence.mapping;

import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Test;
import org.motechproject.util.DateUtil;
import org.motechproject.whp.adherence.domain.Adherence;
import org.motechproject.whp.adherence.domain.PillStatus;
import org.motechproject.whp.patient.builder.PatientBuilder;
import org.motechproject.whp.patient.domain.*;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.motechproject.model.DayOfWeek.Monday;

public class AdherenceRecordMapperTest {

    public static final String PROVIDER_ID = "providerid";
    public static final String TB_ID = "tb_id";
    public static final String PROVIDER_DISTRICT = "PROVIDER_DISTRICT";

    private Patient patient;

    @Before
    public void setup() {
        Therapy therapy = new Therapy(new TreatmentCategory(), DiseaseClass.E, 10);
        Treatment currentTreatment = new Treatment(PROVIDER_ID, PROVIDER_DISTRICT, TB_ID, PatientType.New);
        therapy.addTreatment(currentTreatment, DateTime.now());

        patient = new PatientBuilder().withDefaults().withCurrentTreatment(currentTreatment).build();
    }

    @Test
    public void shouldSetExternalIdOnRequest() {
        Adherence day = new Adherence(patient.getPatientId(), patient.currentTherapyId(), Monday, DateUtil.today(), PillStatus.NotTaken, TB_ID, PROVIDER_ID);

        assertEquals(patient.getPatientId(), AdherenceRecordMapper.map(day).externalId());
    }

    @Test
    public void shouldSetProviderIdOnRequest() {
        Adherence day = new Adherence(patient.getPatientId(), patient.currentTherapyId(), Monday, DateUtil.today(), PillStatus.NotTaken, TB_ID, PROVIDER_ID);

        assertEquals(PROVIDER_ID, AdherenceRecordMapper.map(day).providerId());
    }

    @Test
    public void shouldSetTbIdAsMetaDataOnRequest() {
        Adherence day = new Adherence(patient.getPatientId(), patient.currentTherapyId(), Monday, DateUtil.today(), PillStatus.Unknown, TB_ID, PROVIDER_ID);

        Treatment currentTreatment = patient.getCurrentTherapy().getCurrentTreatment();
        assertNotNull(currentTreatment  .getTbId());
        assertEquals(currentTreatment.getTbId(), AdherenceRecordMapper.map(day).tbId());
    }

    @Test
    public void shouldMarkDosesTaken() {
        Adherence day = new Adherence(patient.getPatientId(), patient.currentTherapyId(), Monday, DateUtil.today(), PillStatus.Taken, TB_ID, PROVIDER_ID);

        assertEquals(1, AdherenceRecordMapper.map(day).status());
    }

    @Test
    public void shouldMarkDosesMissed() {
        Adherence day = new Adherence(patient.getPatientId(), patient.currentTherapyId(), Monday, DateUtil.today(), PillStatus.NotTaken, TB_ID, PROVIDER_ID);

        assertEquals(2, AdherenceRecordMapper.map(day).status());
    }
}
