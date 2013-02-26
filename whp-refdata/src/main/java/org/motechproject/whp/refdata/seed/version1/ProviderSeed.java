package org.motechproject.whp.refdata.seed.version1;


import org.motechproject.deliverytools.seed.Seed;
import org.motechproject.util.DateUtil;
import org.motechproject.whp.containermapping.domain.ContainerRange;
import org.motechproject.whp.containermapping.domain.ContainerRanges;
import org.motechproject.whp.containermapping.domain.ProviderContainerMapping;
import org.motechproject.whp.containermapping.service.ProviderContainerMappingService;
import org.motechproject.whp.user.contract.ProviderRequest;
import org.motechproject.whp.user.service.ProviderService;
import org.motechproject.whp.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProviderSeed {

    @Autowired
    private ProviderService providerService;

    @Autowired
    private UserService userService;

    @Autowired
    private ProviderContainerMappingService providerContainerMappingService;

    // Need not have this, till we create logins for acutal providers
    @Seed(priority = 0, version = "1.0")
    public void load() {
        providerService.registerProvider(new ProviderRequest("raj", "Begusarai", "1234567890", DateUtil.now()));
        userService.activateUser("raj");
        createProviderContainerMapping();
    }

    private void createProviderContainerMapping() {
        ProviderContainerMapping mapping = new ProviderContainerMapping();
        ContainerRanges containerRanges = new ContainerRanges();

        mapping.setProviderId("raj");
        containerRanges.add(new ContainerRange(10000000000l, 20000000000l));

        mapping.setContainerRanges(containerRanges);
        providerContainerMappingService.add(mapping);
    }

}
