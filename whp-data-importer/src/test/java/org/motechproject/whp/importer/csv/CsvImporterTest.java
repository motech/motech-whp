package org.motechproject.whp.importer.csv;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath*:/applicationDataImporterContext.xml")
public class CsvImporterTest {
    @Test
    public void testMain() throws Exception {
        String[] arguments = new String[2];
        arguments[0] = "patient";
        arguments[1] = "patientRecords.csv";
        CsvImporter.main(arguments);
    }
}
