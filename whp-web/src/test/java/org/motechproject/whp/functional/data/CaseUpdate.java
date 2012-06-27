package org.motechproject.whp.functional.data;


import java.io.*;

public class CaseUpdate {

    public static String CloseTreatmentRequest(String caseId, String tbId) {
        String request = String.format(readFile("close_treatment.xml"), caseId, tbId);
        System.out.println(request);
        return request;
    }

    public static String OpenNewTreatmentRequest(String caseId, String providerId) {
        String request = String.format(readFile("open_new_treatment.xml"), caseId, providerId);
        System.out.println(request);
        return request;
    }

    public static String TransferInPatientRequest(String caseId, String tbId, String treatmentCategory, String providerId) {
        String request = String.format(readFile("transfer_in.xml"), caseId, tbId, treatmentCategory, providerId);
        System.out.println(request);
        return request;
    }

    public static String PauseTreatmentRequest(String caseId, String pauseDate, String tbId, String reasonForPause) {
        String request = String.format(readFile("pause_treatment.xml"), caseId, pauseDate, tbId, reasonForPause);
        System.out.println(request);
        return request;
    }

    public static String RestartTreatmentRequest(String caseId, String restartDate, String tbId, String reasonForRestart) {
        String request = String.format(readFile("restart_treatment.xml"), caseId, restartDate, tbId, reasonForRestart);
        System.out.println(request);
        return request;
    }

    private static String readFile(String fileName) {
        try {
            FileReader fr = new FileReader("sample-case-xml/" + fileName);
            BufferedReader br = new BufferedReader(fr);
            String s;
            StringBuilder result = new StringBuilder();
            while ((s = br.readLine()) != null) {
                result.append(s);
            }
            fr.close();
            return result.toString();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}