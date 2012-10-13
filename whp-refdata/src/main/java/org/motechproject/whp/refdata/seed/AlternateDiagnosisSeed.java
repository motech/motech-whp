package org.motechproject.whp.refdata.seed;

import org.motechproject.deliverytools.seed.Seed;
import org.motechproject.whp.container.domain.AlternateDiagnosis;
import org.motechproject.whp.container.repository.AllAlternateDiagnosis;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AlternateDiagnosisSeed {

    @Autowired
    private AllAlternateDiagnosis allAlternateDiagnosis;

    @Seed(priority = 0, version = "4.0")
    public void load() {
        allAlternateDiagnosis.addOrReplace(new AlternateDiagnosis("Other bacterial diseases,not elsewhere classified", "1027"));
        allAlternateDiagnosis.addOrReplace(new AlternateDiagnosis("Malignant neoplasm of bronchus and lung", "1110"));
        allAlternateDiagnosis.addOrReplace(new AlternateDiagnosis("Angina pectoris", "1330"));
        allAlternateDiagnosis.addOrReplace(new AlternateDiagnosis("Chronic ischaemic heart disease", "1334"));
        allAlternateDiagnosis.addOrReplace(new AlternateDiagnosis("Heart failure", "1354"));
        allAlternateDiagnosis.addOrReplace(new AlternateDiagnosis("Heart disease, unspecified", "1356"));
        allAlternateDiagnosis.addOrReplace(new AlternateDiagnosis("Pneumonia, organism unspecified", "1389"));
        allAlternateDiagnosis.addOrReplace(new AlternateDiagnosis("Vasomotor and allergic rhinitis", "1392"));
        allAlternateDiagnosis.addOrReplace(new AlternateDiagnosis("Chronic sinusitis", "1394"));
        allAlternateDiagnosis.addOrReplace(new AlternateDiagnosis("Other disorders of nose and nasal sinuses", "1396"));
        allAlternateDiagnosis.addOrReplace(new AlternateDiagnosis("Bronchitis, not specified as acute or chronic", "1400"));
        allAlternateDiagnosis.addOrReplace(new AlternateDiagnosis("Chronic obstructive pulmonary disease, unspecified", "1403"));
        allAlternateDiagnosis.addOrReplace(new AlternateDiagnosis("Asthma", "1404"));
        allAlternateDiagnosis.addOrReplace(new AlternateDiagnosis("Bronchiectasis", "1406"));
        allAlternateDiagnosis.addOrReplace(new AlternateDiagnosis("Pulmonary oedema", "1410"));
        allAlternateDiagnosis.addOrReplace(new AlternateDiagnosis("Pulmonary eosinophilia, not elsewhere classified", "1411"));
        allAlternateDiagnosis.addOrReplace(new AlternateDiagnosis("Abscess of lung and mediastinum", "1412"));
        allAlternateDiagnosis.addOrReplace(new AlternateDiagnosis("Pleural effusion, not elsewhere classified", "1414"));
        allAlternateDiagnosis.addOrReplace(new AlternateDiagnosis("Respiratory disorder, unspecified", "1417"));
        allAlternateDiagnosis.addOrReplace(new AlternateDiagnosis("Haemoptysis", "1740"));
        allAlternateDiagnosis.addOrReplace(new AlternateDiagnosis("Cough", "1741"));
        allAlternateDiagnosis.addOrReplace(new AlternateDiagnosis("Dyspnoea", "1742"));
        allAlternateDiagnosis.addOrReplace(new AlternateDiagnosis("Pleurisy", "1754"));
        allAlternateDiagnosis.addOrReplace(new AlternateDiagnosis("Abnormal sputum", "1756"));
        allAlternateDiagnosis.addOrReplace(new AlternateDiagnosis("Fever of unknown origin", "1843"));
        allAlternateDiagnosis.addOrReplace(new AlternateDiagnosis("Allergy, unspecified", "1948"));
    }
}