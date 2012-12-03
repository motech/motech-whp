package org.motechproject.whp.it.patient.repository.allTherapyRemarks;

import org.joda.time.DateTime;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.motechproject.whp.common.util.SpringIntegrationTest;
import org.motechproject.whp.patient.domain.TherapyRemark;
import org.motechproject.whp.patient.repository.AllTherapyRemarks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;

import java.util.List;

import static java.util.Arrays.asList;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

@ContextConfiguration(locations = "classpath*:/applicationPatientContext.xml")
public class AllTherapyRemarksIT extends SpringIntegrationTest {

    @Autowired
    AllTherapyRemarks allTherapyRemarks;
    private final String THERAPY_ID = "therapy1";
    private final String PATIENT_ID = "patient1";
    private TherapyRemark remark1;
    private TherapyRemark remark2;
    private TherapyRemark remark3;

    @Before
    public void setUp() {
        DateTime now = DateTime.now();

        remark1 = new TherapyRemark(PATIENT_ID, THERAPY_ID,"test remark","cmfAdmin");
        remark1.setCreationTime(now.minusMonths(2));
        allTherapyRemarks.add(remark1);

        remark2 = new TherapyRemark(PATIENT_ID, THERAPY_ID,"test remark","cmfAdmin");
        remark2.setCreationTime(now.minusMonths(1));
        allTherapyRemarks.add(remark2);

        remark3 = new TherapyRemark(PATIENT_ID, THERAPY_ID,"test remark","cmfAdmin");
        remark3.setCreationTime(now.minusMonths(1).plusDays(1));
        allTherapyRemarks.add(remark3);

        TherapyRemark therapyRemarkForAnotherTherapy = new TherapyRemark(PATIENT_ID, "therapy2","test remark","cmfAdmin");
        therapyRemarkForAnotherTherapy.setCreationTime(now.minusHours(1));
        allTherapyRemarks.add(therapyRemarkForAnotherTherapy);


    }
    @Test
    public void shouldFetchRemarksChronologicallyByTherapyId() {
        List<TherapyRemark> therapyRemarks = allTherapyRemarks.findByTherapyId(THERAPY_ID);

        assertThat(therapyRemarks, is(asList(remark3,remark2,remark1)));
    }

    @After
    public void tearDown() {
        allTherapyRemarks.removeAll();;
    }
}
