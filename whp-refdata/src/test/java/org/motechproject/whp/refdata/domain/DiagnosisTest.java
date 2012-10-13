package org.motechproject.whp.refdata.domain;

import org.junit.Test;
import org.motechproject.whp.common.domain.Diagnosis;

import java.util.List;

import static org.hamcrest.CoreMatchers.hasItems;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.motechproject.whp.common.domain.Diagnosis.Negative;
import static org.motechproject.whp.common.domain.Diagnosis.Pending;
import static org.motechproject.whp.common.domain.Diagnosis.Positive;


public class DiagnosisTest {
    @Test
    public void shouldReturnAllDiagnosisNames(){
        List<String> diagnosisNames = Diagnosis.allNames();
        assertThat(diagnosisNames, hasItems(Negative.name(), Positive.name(), Pending.name()));
        assertThat(diagnosisNames.size(), is(Diagnosis.values().length));
    }
}
