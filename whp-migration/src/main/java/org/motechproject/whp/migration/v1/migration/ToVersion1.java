package org.motechproject.whp.migration.v1.migration;

import org.motechproject.whp.adherence.service.WHPAdherenceService;
import org.motechproject.whp.migration.logger.MigrationLogger;
import org.motechproject.whp.migration.v0.domain.PatientV0;
import org.motechproject.whp.migration.v0.domain.TherapyV0;
import org.motechproject.whp.migration.v0.repository.AllPatientsV0;
import org.motechproject.whp.migration.v0.repository.AllTherapiesV0;
import org.motechproject.whp.migration.v1.mapper.PatientV1Mapper;
import org.motechproject.whp.migration.v1.repository.AllPatientsV1;
import org.motechproject.whp.patient.domain.Patient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ToVersion1 {

    private static MigrationLogger migrationLogger = new MigrationLogger();

    AllPatientsV0 allPatientsV0;

    AllPatientsV1 allPatientsV1;

    AllTherapiesV0 allTherapiesV0;

    WHPAdherenceService whpAdherenceService;

    @Autowired
    public ToVersion1(AllPatientsV0 allPatientsV0, AllPatientsV1 allPatientsV1,
                      AllTherapiesV0 allTherapiesV0, WHPAdherenceService whpAdherenceService) {
        this.allPatientsV0 = allPatientsV0;
        this.allPatientsV1 = allPatientsV1;
        this.allTherapiesV0 = allTherapiesV0;
        this.whpAdherenceService =  whpAdherenceService;
    }

    public void doo() {
        convert();
        removeStrays();
    }

    private void convert() {
        List<PatientV0> patients = allPatientsV0.getAllVersionedDocs();
        migrationLogger.info("Starting therapy migrations for " + patients.size() + " records.");
        for (PatientV0 patientV0 : patients) {
            migrationLogger.info("for : " + patientV0.getPatientId());
            Patient patientV1 = new PatientV1Mapper(patientV0, whpAdherenceService).map();
            migrationLogger.info("removing version 1: " + patientV0.getPatientId());
            allPatientsV0.remove(patientV0);

            migrationLogger.info("adding version 2: " + patientV0.getPatientId());
            allPatientsV1.add(patientV1);
        }
    }

    private void removeStrays() {
        List<TherapyV0> therapyV0List = allTherapiesV0.getAll();
        for (TherapyV0 therapyV0 : therapyV0List) {
            allTherapiesV0.remove(therapyV0);
        }
    }

}
