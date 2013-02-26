package org.motechproject.whp.refdata.seed.version1;


import org.motechproject.deliverytools.seed.Seed;
import org.motechproject.whp.common.domain.District;
import org.motechproject.whp.common.repository.AllDistricts;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service
public class DistrictSeed {
    @Autowired
    @Qualifier(value = "allDistricts")
    AllDistricts allDistricts;

    @Seed(priority = 1, version = "1.0")
    public void load() {
        allDistricts.add(new District("Begusarai"));
        allDistricts.add(new District("Bhagalpur"));
        allDistricts.add(new District("East Champaran"));
        allDistricts.add(new District("Gopalganj"));
        allDistricts.add(new District("Jehanabad"));
        allDistricts.add(new District("Khagaria"));
        allDistricts.add(new District("Muzaffarpur"));
        allDistricts.add(new District("Nalanda"));
        allDistricts.add(new District("Patna"));
        allDistricts.add(new District("Saharsa"));
        allDistricts.add(new District("Samastipur"));
        allDistricts.add(new District("Vaishali"));
        allDistricts.add(new District("West Champaran"));
    }

}
