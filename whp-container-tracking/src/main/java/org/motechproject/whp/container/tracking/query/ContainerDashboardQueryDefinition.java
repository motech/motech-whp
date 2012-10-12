package org.motechproject.whp.container.tracking.query;

import java.util.Arrays;
import java.util.List;

import static org.motechproject.whp.container.tracking.query.FieldType.DATE;
import static org.motechproject.whp.container.tracking.query.FieldType.STRING;

public class ContainerDashboardQueryDefinition implements QueryDefinition {

    private final QueryField cumulativeSmearResult = new QueryField("cumulativeSmearResult", STRING);

    List<Field> queryFields = Arrays.asList(
           new QueryField("providerId", STRING),
           new QueryField("providerDistrict", STRING),
           new QueryField("containerStatus", STRING),
           new RangeField("containerIssuedDate", DATE, "containerIssuedDateFrom", "containerIssuedDateTo"),
           new QueryField("containerInstance", STRING),
           new QueryField("smearTestResult1", STRING),
           new QueryField("smearTestResult2", STRING),
           new QueryField("diagnosis", STRING),
           new RangeField("consultationDate", DATE, "consultationDateFrom", "consultationDateTo"),
           cumulativeSmearResult
    );

    @Override
    public List<Field> getFields() {
        return queryFields;
    }

}