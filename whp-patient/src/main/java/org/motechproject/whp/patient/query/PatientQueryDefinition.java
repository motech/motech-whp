package org.motechproject.whp.patient.query;

import lombok.Getter;
import org.motechproject.couchdb.lucene.query.QueryDefinition;
import org.motechproject.couchdb.lucene.query.field.Field;
import org.motechproject.couchdb.lucene.query.field.QueryField;
import org.motechproject.couchdb.lucene.query.field.RangeField;
import org.motechproject.whp.common.domain.alerts.PatientAlertType;

import java.util.ArrayList;
import java.util.List;

import static org.motechproject.couchdb.lucene.query.field.FieldType.DATE;
import static org.motechproject.couchdb.lucene.query.field.FieldType.STRING;

public class PatientQueryDefinition implements QueryDefinition {

    public static final String ALERT_SEVERITY = "AlertSeverity";
    public static final String ALERT_VALUE = "AlertValue";
    public static final String ALERT_DATE = "AlertDate";

    @Getter
    protected final QueryField isActive = new QueryField("isActive", STRING);
    protected final QueryField patientId = new QueryField("patientId", STRING);
    protected final QueryField providerId = new QueryField("providerId", STRING);
    protected final QueryField providerDistrict = new QueryField("providerDistrict", STRING);
    protected final QueryField treatmentCategory = new QueryField("treatmentCategory", STRING);
    protected final QueryField cumulativeMissedDoses = new QueryField("cumulativeMissedDoses", STRING);
    protected final QueryField adherenceMissingWeeks = new QueryField("adherenceMissingWeeks", STRING);

    @Override
    public List<Field> fields() {
        List<Field> fields = new ArrayList<>();
        fields.add(isActive);
        fields.add(patientId);
        fields.add(providerId);
        fields.add(providerDistrict);
        fields.add(treatmentCategory);
        fields.add(cumulativeMissedDoses);
        fields.add(adherenceMissingWeeks);

        for(PatientAlertType alertType : PatientAlertType.values()){
            fields.add(new QueryField(alertType.name() + ALERT_SEVERITY, STRING));
            fields.add(new QueryField(alertType.name() + ALERT_VALUE, STRING));
            fields.add(new RangeField(alertType.name() + ALERT_DATE, DATE, alertType.name() + ALERT_DATE + "From", alertType.name() + ALERT_DATE + "To"));
        }

        return fields;
    }

    @Override
    public String viewName() {
        return "PatientDashboard";
    }

    @Override
    public String searchFunctionName() {
        return "findPatientsByCriteria";
    }

    @Override
    public String indexFunction() {
        return "function(doc) { " +
                "if(doc.type == 'Patient') { " +
                    "var index=new Document(); " +
                    "index.add(doc.patientId, {field: 'patientId'}); " +
                    "index.add(doc.onActiveTreatment, {field: 'isActive'}); " +
                    "index.add(doc.currentTherapy.currentTreatment.providerId, {field: 'providerId'}); " +
                    "index.add(doc.currentTherapy.currentTreatment.providerDistrict, {field: 'providerDistrict'}); " +
                    "index.add(doc.currentTherapy.treatmentCategory.code, {field: 'treatmentCategory'}); " +

                    "var alertTypes = Object.keys(doc.patientAlerts.alerts); " +
                    " if(doc.patientAlerts.alerts['CumulativeMissedDoses']) { " +
                        "index.add(doc.patientAlerts.alerts['CumulativeMissedDoses'].value, {field: 'cumulativeMissedDoses'}); " +
                    " } "+

                    " if(doc.patientAlerts.alerts['AdherenceMissing']) { " +
                        "index.add(doc.patientAlerts.alerts['AdherenceMissing'].value, {field: 'adherenceMissingWeeks'}); " +
                    " } "+

                    "for (var i=0; i<alertTypes.length ;i++) { " +
                        "var alertType =  alertTypes[i]; " +
                        "index.add(doc.patientAlerts.alerts[alertType].alertSeverity, {field: alertType + '"+ ALERT_SEVERITY + "'}); "+
                        "index.add(doc.patientAlerts.alerts[alertType].value, {field: alertType + '"+ ALERT_VALUE + "'}); "+
                        "index.add(doc.patientAlerts.alerts[alertType].alertDate, {type : 'date', field: alertType + '"+ ALERT_DATE + "'}); "+
                    "} "+

                    "return index;" +
                " }}";
    }

}