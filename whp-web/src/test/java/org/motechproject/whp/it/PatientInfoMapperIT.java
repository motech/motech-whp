package org.motechproject.whp.it;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.motechproject.util.DateUtil;
import org.motechproject.whp.common.domain.alerts.AlertColorConfiguration;
import org.motechproject.whp.mapper.PatientInfoMapper;
import org.motechproject.whp.patient.builder.PatientBuilder;
import org.motechproject.whp.patient.domain.Patient;
import org.motechproject.whp.uimodel.PatientInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:/META-INF/spring/applicationContext.xml")
public class PatientInfoMapperIT {

    @Autowired
    AlertColorConfiguration alertColorConfiguration;

    @Test
    public void shouldReadColorConfigurationFromPropertyFile() {

        Patient patient = new PatientBuilder().withDefaults()
                .withAdherenceProvidedForLastWeek()
                .withAdherenceMissedWeeks(6, 2, DateUtil.today())
                .withCumulativeMissedAlertValue(10, 1, DateUtil.today())
                .withTreatmentNotStartedDays(8, 0, DateUtil.today())
                .build();

        PatientInfoMapper patientInfoMapper = new PatientInfoMapper(alertColorConfiguration);
        PatientInfo patientInfo = patientInfoMapper.map(patient);

        assertThat(patientInfo.getAdherenceMissingWeeks(), is(6));
        assertThat(patientInfo.getAdherenceMissingWeeksSeverity(), is(2));
        assertThat(patientInfo.getAdherenceMissingSeverityColor(), is("orange"));
        assertThat(patientInfo.getAdherenceMissingMessageCode(), is("message.alert.filter.adherence.missing.severity.two.alerts"));

        assertThat(patientInfo.getCumulativeMissedDosesSeverityColor(), is("blue"));
        assertThat(patientInfo.getCumulativeMissedDosesMessageCode(), is("message.alert.filter.cumulative.missed.dose.alerts"));

        assertThat(patientInfo.getTreatmentNotStartedSeverityColor(), is(""));
        assertThat(patientInfo.getTreatmentNotStartedMessageCode(), is("message.alert.filter.treatment.not.started.alerts"));
    }
}
