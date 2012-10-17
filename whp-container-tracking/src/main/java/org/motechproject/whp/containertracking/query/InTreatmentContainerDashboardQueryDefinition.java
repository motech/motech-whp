package org.motechproject.whp.containertracking.query;

import org.motechproject.couchdb.lucene.query.field.Field;
import org.motechproject.couchdb.lucene.query.field.QueryField;
import org.motechproject.couchdb.lucene.query.field.RangeField;

import java.util.Arrays;
import java.util.List;

import static org.motechproject.couchdb.lucene.query.field.FieldType.DATE;
import static org.motechproject.couchdb.lucene.query.field.FieldType.STRING;

public class InTreatmentContainerDashboardQueryDefinition extends ContainerDashboardQueryDefinition {
    private final QueryField providerId = new QueryField("providerId", STRING);
    private final QueryField district = new QueryField("district", STRING);
    private final QueryField containerStatus = new QueryField("containerStatus", STRING);
    private final RangeField containerIssuedDate = new RangeField("containerIssuedDate", DATE, "containerIssuedDateFrom", "containerIssuedDateTo");
    private final QueryField cumulativeResult = new QueryField("cumulativeResult", STRING);
    private final QueryField reasonForClosure = new QueryField("reasonForClosure", STRING);

    List<Field> queryFields = Arrays.asList(
            providerId,
            containerIssuedDate,
            cumulativeResult,
            district,
            containerStatus,
            containerInstance,
            reasonForClosure
    );

    @Override
    public List<Field> fields() {
        return queryFields;
    }
}
