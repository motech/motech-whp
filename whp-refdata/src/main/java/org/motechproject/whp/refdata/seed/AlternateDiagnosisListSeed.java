package org.motechproject.whp.refdata.seed;

import org.motechproject.deliverytools.seed.Seed;
import org.motechproject.whp.refdata.domain.AlternateDiagnosisList;
import org.motechproject.whp.refdata.repository.AllAlternateDiagnosisList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AlternateDiagnosisListSeed {

    @Autowired
    private AllAlternateDiagnosisList allAlternateDiagnosisList;

    @Seed(priority = 0, version = "4.0")
    public void load() {
        allAlternateDiagnosisList.addOrReplace(new AlternateDiagnosisList("Other bacterial diseases,not elsewhere classified", "1027"));
        allAlternateDiagnosisList.addOrReplace(new AlternateDiagnosisList("Malignant neoplasm of bronchus and lung", "1110"));
        allAlternateDiagnosisList.addOrReplace(new AlternateDiagnosisList("Angina pectoris", "1330"));
        allAlternateDiagnosisList.addOrReplace(new AlternateDiagnosisList("Chronic ischaemic heart disease", "1334"));
        allAlternateDiagnosisList.addOrReplace(new AlternateDiagnosisList("Heart failure", "1354"));
        allAlternateDiagnosisList.addOrReplace(new AlternateDiagnosisList("Heart disease, unspecified", "1356"));
        allAlternateDiagnosisList.addOrReplace(new AlternateDiagnosisList("Pneumonia, organism unspecified", "1389"));
        allAlternateDiagnosisList.addOrReplace(new AlternateDiagnosisList("Vasomotor and allergic rhinitis", "1392"));
        allAlternateDiagnosisList.addOrReplace(new AlternateDiagnosisList("Chronic sinusitis", "1394"));
        allAlternateDiagnosisList.addOrReplace(new AlternateDiagnosisList("Other disorders of nose and nasal sinuses", "1396"));
        allAlternateDiagnosisList.addOrReplace(new AlternateDiagnosisList("Bronchitis, not specified as acute or chronic", "1400"));
        allAlternateDiagnosisList.addOrReplace(new AlternateDiagnosisList("Chronic obstructive pulmonary disease, unspecified", "1403"));
        allAlternateDiagnosisList.addOrReplace(new AlternateDiagnosisList("Asthma", "1404"));
        allAlternateDiagnosisList.addOrReplace(new AlternateDiagnosisList("Bronchiectasis", "1406"));
        allAlternateDiagnosisList.addOrReplace(new AlternateDiagnosisList("Pulmonary oedema", "1410"));
        allAlternateDiagnosisList.addOrReplace(new AlternateDiagnosisList("Pulmonary eosinophilia, not elsewhere classified", "1411"));
        allAlternateDiagnosisList.addOrReplace(new AlternateDiagnosisList("Abscess of lung and mediastinum", "1412"));
        allAlternateDiagnosisList.addOrReplace(new AlternateDiagnosisList("Pleural effusion, not elsewhere classified", "1414"));
        allAlternateDiagnosisList.addOrReplace(new AlternateDiagnosisList("Respiratory disorder, unspecified", "1417"));
        allAlternateDiagnosisList.addOrReplace(new AlternateDiagnosisList("Haemoptysis", "1740"));
        allAlternateDiagnosisList.addOrReplace(new AlternateDiagnosisList("Cough", "1741"));
        allAlternateDiagnosisList.addOrReplace(new AlternateDiagnosisList("Dyspnoea", "1742"));
        allAlternateDiagnosisList.addOrReplace(new AlternateDiagnosisList("Pleurisy", "1754"));
        allAlternateDiagnosisList.addOrReplace(new AlternateDiagnosisList("Abnormal sputum", "1756"));
        allAlternateDiagnosisList.addOrReplace(new AlternateDiagnosisList("Fever of unknown origin", "1843"));
        allAlternateDiagnosisList.addOrReplace(new AlternateDiagnosisList("Allergy, unspecified", "1948"));
    }
}