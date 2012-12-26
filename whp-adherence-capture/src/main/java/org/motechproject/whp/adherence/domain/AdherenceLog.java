package org.motechproject.whp.adherence.domain;

import org.codehaus.jackson.annotate.JsonProperty;
import org.ektorp.support.TypeDiscriminator;
import org.joda.time.LocalDate;
import org.motechproject.model.MotechBaseDataObject;

import java.util.HashMap;
import java.util.Map;

@TypeDiscriminator("doc.type === 'AdherenceLog'")
public class AdherenceLog extends MotechBaseDataObject {
    public static final String PROVIDER_ID = "PROVIDER_ID";
    public static final String TB_ID = "TB_ID";

    @JsonProperty
    private String externalId;
    @JsonProperty
    private String treatmentId;
    @JsonProperty
    private LocalDate doseDate;
    @JsonProperty
    private int status;
    @JsonProperty
    Map<String, Object> meta = new HashMap<>();

    public AdherenceLog() {
    }

    public AdherenceLog(String externalId, String treatmentId, LocalDate doseDate) {
        super();
        this.externalId = externalId;
        this.treatmentId = treatmentId;
        this.doseDate = doseDate;
    }

    public AdherenceLog status(int status) {
        this.status = status;
        return this;
    }

    public AdherenceLog treatmentId(String treatmentId) {
        this.treatmentId = treatmentId;
        return this;
    }

    public String externalId() {
        return externalId;
    }

    public String treatmentId() {
        return treatmentId;
    }

    public LocalDate doseDate() {
        return doseDate;
    }

    public int status() {
        return status;
    }

    public void tbId(String tbId) {
        meta.put(TB_ID, tbId);
    }

    public String tbId() {
        return (String) meta.get(TB_ID);
    }

    public AdherenceLog providerId(String providerId) {
        meta.put(PROVIDER_ID, providerId);
        return this;
    }

    public String providerId() {
        return (String) meta.get(PROVIDER_ID);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        AdherenceLog that = (AdherenceLog) o;

        if (status != that.status) return false;
        if (doseDate != null ? !doseDate.equals(that.doseDate) : that.doseDate != null) return false;
        if (externalId != null ? !externalId.equals(that.externalId) : that.externalId != null) return false;
        if (providerId() != null ? !providerId().equals(that.providerId()) : that.providerId() != null) return false;
        if (tbId() != null ? !tbId().equals(that.tbId()) : that.tbId() != null) return false;
        if (treatmentId != null ? !treatmentId.equals(that.treatmentId) : that.treatmentId != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = externalId != null ? externalId.hashCode() : 0;
        result = 31 * result + (treatmentId != null ? treatmentId.hashCode() : 0);
        result = 31 * result + (doseDate != null ? doseDate.hashCode() : 0);
        result = 31 * result + status;
        result = 31 * result + (providerId() != null ? providerId().hashCode() : 0);
        result = 31 * result + (tbId() != null ? tbId().hashCode() : 0);
        return result;
    }
}
