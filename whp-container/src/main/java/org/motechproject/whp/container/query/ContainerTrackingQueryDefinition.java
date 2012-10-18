package org.motechproject.whp.container.query;

import org.motechproject.couchdb.lucene.query.QueryDefinition;
import org.motechproject.couchdb.lucene.query.field.QueryField;

import static org.motechproject.couchdb.lucene.query.field.FieldType.STRING;

public abstract class ContainerTrackingQueryDefinition implements QueryDefinition {

    protected final QueryField containerInstance = new QueryField("containerInstance", STRING);

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
                "index.add(doc.providerId, {field: 'providerId'}); " +
                "index.add(doc.district, {field: 'district'});" +
                "index.add(doc.status, {field: 'containerStatus'});" +
                "index.add(doc.containerIssuedDate, {field: 'containerIssuedDate', type : 'date'});" +
                "index.add(doc.instance, {field: 'containerInstance'}); " +
                "index.add(doc.diagnosis, {field: 'diagnosis'}); " +
                "index.add(doc.reasonForClosure, {field: 'reasonForClosure'}); " +
                "index.add(doc.consultationDate, {field: 'consultationDate', type : 'date'}); " +

                "if(doc.mappingInstance != undefined) {" +
                "index.add(doc.mappingInstance, {field: 'mappingInstance'}); " +
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
}