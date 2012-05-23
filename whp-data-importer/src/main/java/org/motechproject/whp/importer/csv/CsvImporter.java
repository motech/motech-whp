package org.motechproject.whp.importer.csv;

import org.joda.time.DateTime;
import org.motechproject.importer.CSVDataImporter;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class CsvImporter {
    public static final String APPLICATION_CONTEXT_XML = "applicationDataImporterContext.xml";

    public static void main(String argvs[]) {
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext(APPLICATION_CONTEXT_XML);
        CSVDataImporter csvDataImporter = (CSVDataImporter) context.getBean("csvDataImporter");
        System.out.println("\n\nInvoked importer at "+DateTime.now().toString("dd-MM-YYYY HH:MM:SS"));
        if (argvs[0].toLowerCase().contains("patient")) {
            System.out.println("Importing patient records from file :" + argvs[1]);
            csvDataImporter.importData("patientRecordImporter", argvs[1]);
        }
        else if(argvs[0].toLowerCase().contains("provider")){
            System.out.println("Importing provider records from file : "+ argvs[1]);
            csvDataImporter.importData("providerRecordImporter",argvs[1]);
        }
        else {
            System.out.println("Invalid request! Arguments to be passed - \"{provider/patient},filename\"");
        }
    }
}
