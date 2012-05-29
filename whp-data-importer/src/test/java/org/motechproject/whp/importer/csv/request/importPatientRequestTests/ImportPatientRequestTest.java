package org.motechproject.whp.importer.csv.request.importPatientRequestTests;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.motechproject.validation.constraints.NotNullOrEmpty;
import org.motechproject.whp.importer.csv.request.ImportPatientRequest;

import java.lang.reflect.Field;

import static junit.framework.Assert.assertTrue;

@RunWith(Suite.class)
@Suite.SuiteClasses({
        SmearTestResultsMappingTestPart.class,
        WeightStatisticsRecordsMappingTestPart.class,
        ValidationsTest.class
})
public class ImportPatientRequestTest {

}
