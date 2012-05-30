package org.motechproject.whp.importer.csv.request.importPatientRequestTests;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

import static junit.framework.Assert.assertTrue;

@RunWith(Suite.class)
@Suite.SuiteClasses({
        SmearTestResultsMappingTestPart.class,
        WeightStatisticsRecordsMappingTestPart.class,
        ValidationsTestPart.class
})
public class ImportPatientRequestTest {

}
