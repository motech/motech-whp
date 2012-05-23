package org.motechproject.whp.patient.command;

public enum UpdateScope {

    create, simpleUpdate, openTreatment, closeTreatment, transferIn, pauseTreatment, restartTreatment;

    public static final String createScope = "create";
    public static final String simpleUpdateScope = "simpleUpdate";
    public static final String openTreatmentScope = "openTreatment";
    public static final String closeTreatmentScope = "closeTreatment";
    public static final String transferInScope = "transferIn";
    public static final String pauseTreatmentScope = "pauseTreatment";
    public static final String restartTreatmentScope = "restartTreatment";

}
