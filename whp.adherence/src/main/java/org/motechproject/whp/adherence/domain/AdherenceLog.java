package org.motechproject.whp.adherence.domain;

import org.ektorp.support.TypeDiscriminator;
import org.joda.time.LocalDate;
import org.motechproject.model.MotechBaseDataObject;
import org.motechproject.util.DateUtil;

import java.util.HashMap;
import java.util.Map;

@TypeDiscriminator("doc.type === 'AdherenceLog'")
public class AdherenceLog extends MotechBaseDataObject {

    private String patientId;
    private LocalDate fromDate = DateUtil.today();
    private LocalDate toDate = DateUtil.today();
    private int doseTaken;
    private int doseToBeTaken;
    private Map<String, Object> meta = new HashMap<String, Object>();

    public AdherenceLog() {
        super();
    }

    public AdherenceLog(String patientId, LocalDate fromDate, LocalDate toDate, int doseTaken, int doseToBeTaken, Map<String, Object> meta){
        super();
        this.patientId = patientId;
        this.fromDate = fromDate;
        this.toDate = toDate;
        this.doseTaken = doseTaken;
        this.doseToBeTaken = doseToBeTaken;
        this.meta = meta;
    }

    public LocalDate getFromDate() {
        return fromDate;
    }

    public AdherenceLog setFromDate(LocalDate fromDate) {
        this.fromDate = fromDate;
        return this;
    }

    public LocalDate getToDate() {
        return toDate;
    }

    public AdherenceLog setToDate(LocalDate toDate) {
        this.toDate = toDate;
        return this;
    }

    public int getDoseTaken() {
        return doseTaken;
    }

    public AdherenceLog setDoseTaken(int doseTaken) {
        this.doseTaken = doseTaken;
        return this;
    }

    public int getDoseToBeTaken() {
        return doseToBeTaken;
    }

    public AdherenceLog setDoseToBeTaken(int doseToBeTaken) {
        this.doseToBeTaken = doseToBeTaken;
        return this;
    }

    public Map<String, Object> getMeta() {
        return meta;
    }

    public AdherenceLog setMeta(Map<String, Object> meta) {
        this.meta = meta;
        return this;
    }

    public String getPatientId() {
        return patientId;
    }

    public void setPatientId(String patientId) {
        this.patientId = patientId;
    }
}
