package org.motechproject.whp.refdata.seed;


import org.motechproject.deliverytools.seed.Seed;
import org.motechproject.whp.refdata.domain.CmfLocation;
import org.motechproject.whp.refdata.repository.AllCmfLocations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CmfLocationSeed {

    @Autowired
    private AllCmfLocations allCmfLocations;

    @Seed(priority = 0, version = "2.0")
    public void load() {
        allCmfLocations.add(new CmfLocation("Delhi"));
        allCmfLocations.add(new CmfLocation("Patna"));
    }

}
