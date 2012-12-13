package org.motechproject.whp.refdata.seed;

import org.motechproject.deliverytools.seed.Seed;
import org.motechproject.whp.containermapping.domain.AdminContainerMapping;
import org.motechproject.whp.containermapping.domain.ContainerRange;
import org.motechproject.whp.containermapping.repository.AllAdminContainerMappings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AdminContainerMappingSeed {

    @Autowired
    private AllAdminContainerMappings allAdminContainerMappings;

    @Seed(priority = 0, version = "4.0")
    public void load() {
        ContainerRange range = new ContainerRange(9999900001L, 9999902999L);
        AdminContainerMapping mapping = new AdminContainerMapping();
        mapping.add(range);
        allAdminContainerMappings.add(mapping);
    }
}
