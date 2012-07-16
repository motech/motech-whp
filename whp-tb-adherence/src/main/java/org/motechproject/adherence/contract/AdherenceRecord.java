package org.motechproject.adherence.contract;

import org.codehaus.jackson.annotate.JsonProperty;
import org.joda.time.LocalDate;

import java.util.HashMap;
import java.util.Map;


public class AdherenceRecord {

    @JsonProperty
    String externalId;
    @JsonProperty
    String treatmentId;
    @JsonProperty
    LocalDate doseDate;
    @JsonProperty
    int status;
    @JsonProperty
    Map<String, Object> meta;

    public AdherenceRecord() {
    }

    public AdherenceRecord(String externalId, String treatmentId, LocalDate doseDate) {
        this.externalId = externalId;
        this.treatmentId = treatmentId;
        this.doseDate = doseDate;
        meta = new HashMap<>();
    }

    public AdherenceRecord status(int status) {
        this.status = status;
        return this;
    }

    public Map<String, Object> meta() {
        return meta;
    }

    public AdherenceRecord meta(Map<String, Object> meta) {
        this.meta = meta;
        return this;
    }

    public AdherenceRecord addMeta(String key, Object value) {
        meta.put(key, value);
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

}