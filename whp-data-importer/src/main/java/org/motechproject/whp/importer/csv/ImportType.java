package org.motechproject.whp.importer.csv;

import org.motechproject.importer.CSVDataImporter;
import org.motechproject.whp.importer.csv.logger.ImporterLogger;

public enum ImportType {
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
    }, ContainerMapping {
        @Override
        void importData(String importFile, CSVDataImporter csvDataImporter) {
            ImporterLogger logger = new ImporterLogger();
            logger.info("Importing patient records from file : " + importFile);
            csvDataImporter.importData("containerMappingImporter", importFile);
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
}
