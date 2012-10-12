package org.motechproject.whp.container.tracking.query;

import org.junit.Test;

import java.util.Properties;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

public class QueryFieldTest {

    @Test
    public void shouldCheckIfFieldExistsInFilterParams(){
        QueryField field = new QueryField("field1", FieldType.STRING);

        Properties filterParamsContainingField = new Properties();
        filterParamsContainingField.put("field1", "value1");
        filterParamsContainingField.put("field2", "value2");

        Properties filterParamsNotContainingField = new Properties();
        filterParamsNotContainingField.put("field2", "value1");
        filterParamsNotContainingField.put("field3", "value2");

        assertThat(field.presentIn(filterParamsContainingField), is(true));
        assertThat(field.presentIn(filterParamsNotContainingField), is(false));
    }

    @Test
    public void shouldCreateQueryCriteria(){
        QueryField field = new QueryField("field1", FieldType.STRING);

        Properties filterParams = new Properties();
        filterParams.put("field1", "value1");
        filterParams.put("field2", "value2");

        assertThat((QueryCriteria) field.createCriteria(filterParams), is(new QueryCriteria(field, "value1")));

    }
}
