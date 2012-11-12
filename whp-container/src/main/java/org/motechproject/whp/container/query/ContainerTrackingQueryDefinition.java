package org.motechproject.whp.container.query;

import org.motechproject.couchdb.lucene.query.QueryDefinition;
import org.motechproject.couchdb.lucene.query.field.QueryField;
import org.motechproject.couchdb.lucene.query.field.RangeField;

import static org.motechproject.couchdb.lucene.query.field.FieldType.DATE;
import static org.motechproject.couchdb.lucene.query.field.FieldType.STRING;

public abstract class ContainerTrackingQueryDefinition implements QueryDefinition {

    protected final QueryField containerInstance = new QueryField("containerInstance", STRING);
    protected final QueryField containerId = new QueryField("containerId", STRING);
    protected final QueryField providerId = new QueryField("providerId", STRING);
    protected final QueryField district = new QueryField("district", STRING);
    protected final QueryField containerStatus = new QueryField("containerStatus", STRING);
    protected final RangeField containerIssuedDate = new RangeField("containerIssuedDate", DATE, "containerIssuedDateFrom", "containerIssuedDateTo");
    protected final QueryField cumulativeResult = new QueryField("cumulativeResult", STRING);
    protected final QueryField reasonForClosure = new QueryField("reasonForClosure", STRING);
    protected final QueryField instance = new QueryField("instance", STRING);
    protected final QueryField mappingInstance = new QueryField("mappingInstance", STRING);
    protected final RangeField consultationDate = new RangeField("consultationDate", DATE, "consultationDateFrom", "consultationDateTo");
    protected final QueryField diagnosis = new QueryField("diagnosis", STRING);

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
                "index.add(doc.containerId, {field: 'containerId'}); " +
                "index.add(doc.providerId, {field: 'providerId'}); " +
                "index.add(doc.district, {field: 'district'});" +
                "index.add(doc.status, {field: 'containerStatus'});" +
                "index.add(doc.containerIssuedDate, {field: 'containerIssuedDate', type : 'date'});" +
                "index.add(doc.currentTrackingInstance, {field: 'containerInstance'}); " +
                "index.add(doc.diagnosis, {field: 'diagnosis'}); " +
                "index.add(doc.reasonForClosure, {field: 'reasonForClosure'}); " +
                "index.add(doc.consultationDate, {field: 'consultationDate', type : 'date'}); " +

                "if(doc.mappingInstance != undefined) {" +
                "   if(doc.mappingInstance.indexOf('TreatmentInterruption')!== -1) { " +
                "     index.add('TreatmentInterruption', {field: 'mappingInstance'}); " +
                "   }else {" +
                "     index.add(doc.mappingInstance, {field: 'mappingInstance'}); " +
                "   }" +
                "} " +

                "if(doc.labResults != undefined) { " +
                "index.add(doc.labResults.cumulativeResult, {field: 'cumulativeResult'}); " +
                "} " +

                "return index;" +
                "}";
    }

    public String getContainerInstanceFieldName() {
        return containerInstance.getName();
    }

    public String getContainerIdFieldName() {
        return containerId.getName();
    }
}