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

    List<Field> queryFields = Arrays.asList(
           new QueryField("providerId", STRING),
           new QueryField("district", STRING),
           new QueryField("containerStatus", STRING),
           new RangeField("containerIssuedDate", DATE, "containerIssuedDateFrom", "containerIssuedDateTo"),
            containerInstance,
           new QueryField("cumulativeResult", STRING),
           new QueryField("diagnosis", STRING),
           new RangeField("consultationDate", DATE, "consultationDateFrom", "consultationDateTo"),
           cumulativeSmearResult
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

                    "if(doc.container.labResults != undefined) { "+
                        "index.add(doc.container.labResults.cumulativeResult, {field: 'cumulativeResult'}); "+
                    "} "+

                    "index.add(doc.container.diagnosis, {field: 'diagnosis'}); "+

                    "if(doc.patient != undefined && doc.patient.currentTherapy != undefined && doc.patient.currentTherapy.currentTreatment != undefined) { "+
                        "index.add(doc.patient.currentTherapy.currentTreatment.startDate, {field: 'consultationDate', type : 'date'}); " +
                    "}"+
                    "return index;" +
                "}";
    }

    public String getContainerInstanceFieldName(){
        return containerInstance.getName();
    }
}