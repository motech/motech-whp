package org.motechproject.whp.container.tracking.query;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.motechproject.whp.container.tracking.query.FieldType.STRING;

public class QueryBuilderTest {

    @Test
    public void shouldCreateQueryForContainerTracking() {
        Properties filterParams = new Properties();
        filterParams.put("field1", "value1");
        filterParams.put("field2", "value2");

        QueryDefinition queryDefinition = new QueryDefinition() {
            @Override
            public List<Field> getFields() {
                List<Field> fields = new ArrayList<>();
                fields.add(new QueryField("field1", STRING));
                fields.add(new QueryField("field2", STRING));
                return fields;
            }
        };

        QueryBuilder queryBuilder = new QueryBuilder(filterParams, queryDefinition);
        String expectedQuery = "field1:value1 AND field2:value2";
        assertThat(queryBuilder.build(), is(expectedQuery));
    }

    @Test
    public void shouldCreateRangedQueryForContainerTracking() {
        Properties filterParam = new Properties();
        filterParam.put("fieldFrom", "val1");
        filterParam.put("anotherField", "val2");
        filterParam.put("fieldTo", "val3");

        QueryDefinition queryDefinition = new QueryDefinition() {
            @Override
            public List<Field> getFields() {
                List<Field> fields = new ArrayList<Field>();
                fields.add(new RangeField("field", STRING, "fieldFrom", "fieldTo"));
                fields.add(new QueryField("anotherField", STRING));
                return fields;
            }
        };
        QueryBuilder queryBuilder = new QueryBuilder(filterParam, queryDefinition);
        String expectedQuery = "field<string>:[val1 TO val3] AND anotherField:val2";
        assertThat(queryBuilder.build(), is(expectedQuery));
    }
}
