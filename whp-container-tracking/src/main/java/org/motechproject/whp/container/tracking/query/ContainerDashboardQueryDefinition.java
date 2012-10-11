package org.motechproject.whp.container.tracking.query;

import java.util.Arrays;
import java.util.List;

import static org.motechproject.whp.container.tracking.query.FieldType.STRING;

public class ContainerDashboardQueryDefinition extends QueryDefinition {

    private final Field cumulativeSmearResult = new Field("cumulativeSmearResult", STRING);

    List<Field> queryFields = Arrays.asList(
           new Field("providerId", STRING),
           new Field("providerDistrict", STRING),
           new Field("status", STRING),
           new Field("containerIssuedDate", STRING, "containerIssuedDateFrom", "containerIssuedDateTo"),
           cumulativeSmearResult
    );

    @Override
    public List<Field> queryableFields() {
        return queryFields;
    }
}
