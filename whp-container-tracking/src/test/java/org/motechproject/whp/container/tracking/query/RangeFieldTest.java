package org.motechproject.whp.container.tracking.query;

import org.junit.Test;

import java.util.Properties;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

public class RangeFieldTest {

    @Test
    public void shouldCheckIfFieldExistsInFilterParams(){
        RangeField field = new RangeField("field1", FieldType.STRING, "field1From", "field1To");

        Properties filterParamsContainingField = new Properties();
        filterParamsContainingField.put("field1From", "value1");
        filterParamsContainingField.put("field1To", "value2");
        filterParamsContainingField.put("field2", "value3");

        Properties filterParamsNotContainingField = new Properties();
        filterParamsNotContainingField.put("field2", "value1");
        filterParamsNotContainingField.put("field3", "value2");

        assertThat(field.presentIn(filterParamsContainingField), is(true));
        assertThat(field.presentIn(filterParamsNotContainingField), is(false));
    }

    @Test
    public void shouldCreateQueryCriteria(){
        RangeField field = new RangeField("field1", FieldType.STRING, "field1From", "field1To");

        Properties filterParams = new Properties();
        filterParams.put("field1From", "value1");
        filterParams.put("field1To", "value2");
        filterParams.put("field2", "value3");

        assertThat((RangeCriteria) field.createCriteria(filterParams), is(new RangeCriteria(field, "value1", "value2")));
    }

}
