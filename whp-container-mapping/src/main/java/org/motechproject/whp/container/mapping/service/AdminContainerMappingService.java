package org.motechproject.whp.container.mapping.service;

import org.motechproject.whp.container.mapping.domain.AdminContainerMapping;
import org.motechproject.whp.container.mapping.repository.AllAdminContainerMappings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AdminContainerMappingService {

    private AllAdminContainerMappings allAdminContainerMappings;

    @Autowired
    public AdminContainerMappingService(AllAdminContainerMappings allAdminContainerMappings) {
        this.allAdminContainerMappings = allAdminContainerMappings;
    }

    public boolean isValidContainer(long containerId) {
        AdminContainerMapping adminContainerMapping = allAdminContainerMappings.get();
        if(adminContainerMapping == null) {
            return false;
        }

        return adminContainerMapping.hasContainerId(containerId);
    }
}
