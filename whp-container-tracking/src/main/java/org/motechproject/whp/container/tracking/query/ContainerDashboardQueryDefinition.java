package org.motechproject.whp.container.tracking.query;

import org.motechproject.couchdb.lucene.query.QueryDefinition;
import org.motechproject.couchdb.lucene.query.field.Field;
import org.motechproject.couchdb.lucene.query.field.QueryField;
import org.motechproject.couchdb.lucene.query.field.RangeField;

import java.util.Arrays;
import java.util.List;

import static org.motechproject.couchdb.lucene.query.field.FieldType.DATE;
import static org.motechproject.couchdb.lucene.query.field.FieldType.STRING;

public class ContainerDashboardQueryDefinition implements QueryDefinition {

    private final QueryField cumulativeSmearResult = new QueryField("cumulativeSmearResult", STRING);
    private final QueryField containerInstance = new QueryField("containerInstance", STRING);
    private final QueryField providerId = new QueryField("providerId", STRING);
    private final QueryField district = new QueryField("district", STRING);
    private final QueryField containerStatus = new QueryField("containerStatus", STRING);
    private final RangeField containerIssuedDate = new RangeField("containerIssuedDate", DATE, "containerIssuedDateFrom", "containerIssuedDateTo");
    private final QueryField cumulativeResult = new QueryField("cumulativeResult", STRING);
    private final QueryField diagnosis = new QueryField("diagnosis", STRING);
    private final QueryField reasonForClosure = new QueryField("reasonForClosure", STRING);
    private final RangeField consultationDate = new RangeField("consultationDate", DATE, "consultationDateFrom", "consultationDateTo");

    List<Field> queryFields = Arrays.asList(
            providerId,
            district,
            containerStatus,
            containerIssuedDate,
            containerInstance,
            cumulativeResult,
            diagnosis,
            consultationDate,
            cumulativeSmearResult,
            reasonForClosure
    );

    @Override
    public List<Field> fields() {
        return queryFields;
    }

    @Override
    public String viewName() {
        return "ContainerTracking";
    }

    @Override
    public String searchFunctionName() {
        return "findByCriteria";
    }

    @Override
    public String indexFunction() {
        return "function(doc) { " +
                "var index=new Document(); " +
                "index.add(doc.provider.providerId, {field: 'providerId'}); " +
                "index.add(doc.provider.district, {field: 'district'});" +
                "index.add(doc.container.status, {field: 'containerStatus'});" +
                "index.add(doc.container.creationTime, {field: 'containerIssuedDate', type : 'date'});" +
                "index.add(doc.container.instance, {field: 'containerInstance'}); " +

                "if(doc.container.labResults != undefined) { " +
                    "index.add(doc.container.labResults.cumulativeResult, {field: 'cumulativeResult'}); " +
                "} " +

                "index.add(doc.container.diagnosis, {field: 'diagnosis'}); " +
                "index.add(doc.container.reasonForClosure, {field: 'reasonForClosure'}); " +

                "if(doc.patient != undefined && doc.patient.currentTherapy != undefined && doc.patient.currentTherapy.currentTreatment != undefined) { " +
                    "index.add(doc.patient.currentTherapy.currentTreatment.startDate, {field: 'consultationDate', type : 'date'}); " +
                "}" +
                "return index;" +
                "}";
    }

    public String getContainerInstanceFieldName() {
        return containerInstance.getName();
    }
}