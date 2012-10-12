package org.motechproject.whp.container.tracking.query;

import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

public class RangeCriteriaTest {

    @Test
    public void shouldBuildCriteriaString(){
        RangeField field = new RangeField("field1", FieldType.STRING, "field1From", "field1To");
        RangeCriteria criteria = new RangeCriteria(field, "value1", "value2");

        assertThat(criteria.buildCriteriaString(), is("field1<string>:[value1 TO value2]"));
    }
}
