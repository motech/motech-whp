package org.motechproject.whp.importer.csv;

import org.motechproject.importer.CSVDataImporter;
import org.motechproject.whp.importer.csv.exceptions.ExceptionMessages;
import org.motechproject.whp.importer.csv.exceptions.WHPImportException;
import org.motechproject.whp.importer.csv.logger.ImporterLogger;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.io.File;

public class CsvImporter {
    private static final String APPLICATION_CONTEXT_XML = "applicationDataImporterContext.xml";

    private static final String PROVIDER_MODE = "provider";
    private static final String PATIENT_MODE = "patient";

    public static void main(String argvs[]) throws Exception {
        try {
            validateAndSetUpLogger(argvs);

            ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext(APPLICATION_CONTEXT_XML);
            CSVDataImporter csvDataImporter = (CSVDataImporter) context.getBean("csvDataImporter");

            if (argvs[0].toLowerCase().contains(PATIENT_MODE)) {
                ImporterLogger.info("Importing patient records from file :" + argvs[1]);
                csvDataImporter.importData("patientRecordImporter", argvs[1]);
            } else {
                    ImporterLogger.info("Importing provider records from file : " + argvs[1]);
                    csvDataImporter.importData("providerRecordImporter", argvs[1]);
            }
        } catch (Exception exception) {
            ImporterLogger.error(exception);
            throw exception;
        }
    }

    private static void validateAndSetUpLogger(String[] argvs) throws Exception {
        validateArgCount(argvs);
        setLogger(argvs[2]);
        validateImporterMode(argvs[0]);
        validateImportFile(argvs[1]);
    }

    private static void validateImporterMode(String mode) throws WHPImportException {
        String importerMode = mode.toLowerCase();
        if (!(importerMode.contains(PATIENT_MODE) || importerMode.contains(PROVIDER_MODE))) {
            throw new WHPImportException(ExceptionMessages.ILLEGAL_ARGUMENTS);
        }
    }

    private static void validateImportFile(String importFile) throws WHPImportException {

        try {
            if (!new File(importFile).canRead()) {
                throw new WHPImportException("invalid file");
            }
        } catch (Exception exception) {
            throw new WHPImportException("Unable to read file - " + importFile + " Either file does not exist or the file does not have read permission");
        }
    }

    private static void setLogger(String logFile) throws WHPImportException {
        try {
            new File(logFile).createNewFile();
            ImporterLogger.loadAppender(logFile);
        } catch (Exception exception) {
            throw new WHPImportException("Unable to create/access the log file -" + logFile);
        }
    }

    public static void validateArgCount(String args[]) throws Exception {
        if (args.length < 3) {
            throw new WHPImportException(ExceptionMessages.ILLEGAL_ARGUMENTS);
        }

    }
}

