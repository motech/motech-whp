package org.motechproject.whp.importer.csv;

import org.motechproject.importer.CSVDataImporter;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class CsvImporter {
    public static final String APPLICATION_CONTEXT_XML = "META-INF/spring/applicationContext.xml";

    public static void main(String argvs[]) {
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext(APPLICATION_CONTEXT_XML);
        CSVDataImporter csvDataImporter = (CSVDataImporter) context.getBean("csvDataImporter");
        if (argvs[0].toLowerCase().contains("patient")) {
            System.out.println("Importing patient records from file :" + argvs[1]);
            csvDataImporter.importData("patientWebRequest", argvs[1]);
        }
    }
}
