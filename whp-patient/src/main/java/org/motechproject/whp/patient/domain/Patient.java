package org.motechproject.whp.patient.domain;

import lombok.Data;
import org.codehaus.jackson.annotate.JsonIgnore;
import org.ektorp.support.TypeDiscriminator;
import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.motechproject.common.exception.WHPErrorCode;
import org.motechproject.model.MotechBaseDataObject;
import org.motechproject.util.DateUtil;
import org.motechproject.whp.refdata.domain.Gender;
import org.motechproject.whp.refdata.domain.PatientStatus;
import org.motechproject.whp.refdata.domain.PatientType;
import org.motechproject.whp.refdata.domain.TreatmentOutcome;

import java.util.ArrayList;
import java.util.List;

import static org.motechproject.whp.patient.util.WHPDateUtil.isOnOrAfter;

@TypeDiscriminator("doc.type == 'Patient'")
@Data
public class Patient extends MotechBaseDataObject {

    private String patientId;
    private String firstName;
    private String lastName;
    private Gender gender;
    private String phoneNumber;
    private String phi;
    private PatientStatus status = PatientStatus.Open;
    private List<Treatment> treatments = new ArrayList<Treatment>();
    private DateTime lastModifiedDate;
    private Treatment currentTreatment;
    private boolean onActiveTreatment = true;

    private boolean migrated;

    public Patient() {
    }

    public Patient(String patientId, String firstName, String lastName, Gender gender, String phoneNumber) {
        setPatientId(patientId);
        this.firstName = firstName;
        this.lastName = lastName;
        this.gender = gender;
        this.phoneNumber = phoneNumber;
    }

    public void addTreatment(Treatment treatment, DateTime dateModified) {
        if (currentTreatment != null) {
            treatments.add(currentTreatment);
        }
        currentTreatment = treatment;
        lastModifiedDate = dateModified;
    }

    public Therapy latestTherapy() {
        return currentTreatment.getTherapy();
    }

    public PatientType currentTreatmentType() {
        return getCurrentTreatment().getPatientType();
    }

    public DateTime getLastModifiedDate() {
        return DateUtil.setTimeZone(lastModifiedDate);
    }

    public void closeCurrentTreatment(TreatmentOutcome treatmentOutcome, DateTime dateModified) {
        lastModifiedDate = dateModified;
        currentTreatment.close(treatmentOutcome, dateModified);
    }

    public void pauseCurrentTreatment(String reasonForPause, DateTime dateModified) {
        lastModifiedDate = dateModified;
        currentTreatment.pause(reasonForPause, dateModified);
    }

    public void restartCurrentTreatment(String reasonForResumption, DateTime dateModified) {
        lastModifiedDate = dateModified;
        currentTreatment.resume(reasonForResumption, dateModified);
    }

    @JsonIgnore
    public String tbId() {
        return currentTreatment.getTbId();
    }

    @JsonIgnore
    public String providerId() {
        return currentTreatment.getProviderId();
    }

    @JsonIgnore
    public String currentTreatmentId() {
        if (getCurrentTreatment() == null) return null;
        return this.getCurrentTreatment().getTherapy().getId();
    }

    @JsonIgnore
    public boolean isValid(List<WHPErrorCode> errorCodes) {
        return currentTreatment.isValid(errorCodes);
    }

    @JsonIgnore
    public boolean hasCurrentTreatment() {
        return currentTreatment != null;
    }

    @JsonIgnore
    public boolean isCurrentTreatmentClosed() {
        return currentTreatment.isClosed();
    }

    @JsonIgnore
    public boolean isCurrentTreatmentPaused() {
        return currentTreatment.isPaused();
    }

    @JsonIgnore
    public TreatmentOutcome getTreatmentOutcome() {
        return getCurrentTreatment().getTreatmentOutcome();
    }

    @JsonIgnore
    public TreatmentInterruptions getCurrentTreatmentInterruptions() {
        return currentTreatment.getInterruptions();
    }

    @JsonIgnore
    public SmearTestResults getSmearTestResults() {
        return currentTreatment.getSmearTestResults();
    }

    @JsonIgnore
    public WeightStatistics getWeightStatistics() {
        return currentTreatment.getWeightStatistics();
    }

    @JsonIgnore
    public Integer getAge() {
        return latestTherapy().getPatientAge();
    }

    @JsonIgnore
    public TreatmentInterruptions getAllTreatmentInterruptions() {
        TreatmentInterruptions interruptions = new TreatmentInterruptions();
        interruptions.addAll(getCurrentTreatmentInterruptions());
        for (Treatment treatment : getTreatments()) {
            interruptions.addAll(treatment.getInterruptions());
        }
        return interruptions;
    }

    @JsonIgnore
    public Treatment getTreatmentForDateInTherapy(LocalDate date, String therapyDocId) {
        /* A treatment not closed is treated as the last treatment extending to the end of IP
             - this can only be the last treatment (there cannot be 2 open treatments) */

        if (currentTreatment.getTherapyDocId().equals(therapyDocId)
                && isOnOrAfter(date, currentTreatment.getStartDate())) {
            return currentTreatment;
        }

        List<Treatment> treatments = allTreatmentsChronologically();

        /* Not including last treatment as that check has already been made at the start */
        for (int i = 0; i < treatments.size() - 1; i++) {
            Treatment treatment = treatments.get(i);
            if (treatment.getTherapyDocId().equals(therapyDocId)
                    && isOnOrAfter(date, treatment.getStartDate())
                    && date.isBefore(treatments.get(i + 1).getStartDate())) {
                return treatment;
            }
        }
        return null;
    }

    @JsonIgnore
    public List<Treatment> allTreatmentsChronologically() {
        List<Treatment> treatments = new ArrayList<Treatment>();
        treatments.addAll(this.treatments);
        treatments.add(currentTreatment);
        return treatments;
    }

    public void reviveLastClosedTreatment() {
        latestTherapy().revive();
    }

    public void startTherapy(LocalDate firstDoseTakenDate) {
        latestTherapy().start(firstDoseTakenDate);
    }

    public void setPatientId(String patientId) {
        if (patientId == null)
            this.patientId = null;
        else
            this.patientId = patientId.toLowerCase();
    }
}
