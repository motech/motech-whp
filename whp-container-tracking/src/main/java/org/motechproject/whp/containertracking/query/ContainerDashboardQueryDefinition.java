package org.motechproject.whp.containertracking.query;

import org.motechproject.couchdb.lucene.query.QueryDefinition;
import org.motechproject.couchdb.lucene.query.field.QueryField;

import static org.motechproject.couchdb.lucene.query.field.FieldType.STRING;

public abstract class ContainerDashboardQueryDefinition implements QueryDefinition {

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
                "index.add(doc.provider.providerId, {field: 'providerId'}); " +
                "index.add(doc.provider.district, {field: 'district'});" +
                "index.add(doc.container.status, {field: 'containerStatus'});" +
                "index.add(doc.container.containerIssuedDate, {field: 'containerIssuedDate', type : 'date'});" +
                "index.add(doc.container.instance, {field: 'containerInstance'}); " +
                "index.add(doc.container.diagnosis, {field: 'diagnosis'}); " +
                "index.add(doc.container.reasonForClosure, {field: 'reasonForClosure'}); " +
                "index.add(doc.container.consultationDate, {field: 'consultationDate', type : 'date'}); " +

                "if(doc.container.labResults != undefined) { " +
                "index.add(doc.container.labResults.cumulativeResult, {field: 'cumulativeResult'}); " +
                "} " +

                "return index;" +
                "}";
    }

    public String getContainerInstanceFieldName() {
        return containerInstance.getName();
    }
}