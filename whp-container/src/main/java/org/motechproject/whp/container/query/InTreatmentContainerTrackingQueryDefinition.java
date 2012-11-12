package org.motechproject.whp.container.query;

import org.motechproject.couchdb.lucene.query.field.Field;
import org.motechproject.couchdb.lucene.query.field.QueryField;
import org.motechproject.couchdb.lucene.query.field.RangeField;

import java.util.Arrays;
import java.util.List;

import static org.motechproject.couchdb.lucene.query.field.FieldType.DATE;
import static org.motechproject.couchdb.lucene.query.field.FieldType.STRING;

public class InTreatmentContainerTrackingQueryDefinition extends ContainerTrackingQueryDefinition {

    List<Field> queryFields = Arrays.asList(
            containerId,
            providerId,
            containerIssuedDate,
            cumulativeResult,
            district,
            containerStatus,
            containerInstance,
            reasonForClosure,
            instance,
            mappingInstance
    );

    @Override
    public List<Field> fields() {
        return queryFields;
    }
}
