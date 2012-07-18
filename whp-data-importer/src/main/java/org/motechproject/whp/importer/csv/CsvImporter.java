package org.motechproject.whp.importer.csv;

import org.motechproject.importer.CSVDataImporter;
import org.motechproject.whp.importer.csv.exceptions.ExceptionMessages;
import org.motechproject.whp.importer.csv.exceptions.WHPImportException;
import org.motechproject.whp.importer.csv.logger.ImporterLogger;
import org.motechproject.whp.common.mapping.StringToEnumeration;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.io.File;

public class CsvImporter {

    private static final String APPLICATION_CONTEXT_XML = "applicationDataImporterContext.xml";
    private static ImporterLogger importerLogger = new ImporterLogger();

    public static void main(String[] args) throws Exception {
        try {
            validateArgCount(args);
            ImportType importType = verifyImportType(args[0]);
            String file = verifyImportFile(args[1]);
            importData(importType, file);
        } catch (Exception exception) {
            importerLogger.error(exception);
            throw exception;
        }
    }

    private static ImportType verifyImportType(String mode) throws WHPImportException {
        ImportType importType = (ImportType) new StringToEnumeration().convert(mode, ImportType.class);
        if (importType == null) {
            throw new WHPImportException(ExceptionMessages.ILLEGAL_ARGUMENTS);
        }
        return importType;
    }

    private static String verifyImportFile(String importFile) throws WHPImportException {
        try {
            if (!new File(importFile).canRead()) {
                throw new WHPImportException("invalid file");
            }
        } catch (Exception exception) {
            throw new WHPImportException("Unable to read file - " + importFile + " Either file does not exist or the file does not have read permission");
        }
        return importFile;
    }

    private static void validateArgCount(String args[]) throws Exception {
        if (args.length < 2) {
            throw new WHPImportException(ExceptionMessages.ILLEGAL_ARGUMENTS);
        }
    }

    private static void importData(ImportType importType, String file) {
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext(APPLICATION_CONTEXT_XML);
        CSVDataImporter csvDataImporter = (CSVDataImporter) context.getBean("csvDataImporter");
        importType.importData(file, csvDataImporter);
    }
}

enum ImportType {
    Provider() {
        @Override
        void importData(String importFile, CSVDataImporter csvDataImporter) {
            ImporterLogger logger = new ImporterLogger();
            logger.info("Importing provider records from file : " + importFile);
            csvDataImporter.importData("providerRecordImporter", importFile);
        }
    }, Patient {
        @Override
        void importData(String importFile, CSVDataImporter csvDataImporter) {
            ImporterLogger logger = new ImporterLogger();
            logger.info("Importing patient records from file : " + importFile);
            csvDataImporter.importData("patientRecordImporter", importFile);
        }
    }, ProviderTest {
        @Override
        void importData(String importFile, CSVDataImporter csvDataImporter) {
            ImporterLogger logger = new ImporterLogger();
            logger.info("Testing import of provider records from file : " + importFile);
            csvDataImporter.importData("providerRecordValidator", importFile);
        }
    }, PatientTest {
        @Override
        void importData(String importFile, CSVDataImporter csvDataImporter) {
            ImporterLogger logger = new ImporterLogger();
            logger.info("Testing import of patient records from file : " + importFile);
            csvDataImporter.importData("patientRecordValidator", importFile);
        }
    };

    abstract void importData(String importFile, CSVDataImporter csvDataImporter);
};

