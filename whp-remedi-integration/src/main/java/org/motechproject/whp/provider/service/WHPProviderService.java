package org.motechproject.whp.provider.service;

import org.motechproject.provider.registration.service.ProviderRegistrationService;
import org.motechproject.whp.provider.domain.WHPProvider;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Created by IntelliJ IDEA.
 * User: pchandra
 * Date: 4/16/12
 * Time: 4:57 AM
 * To change this template use File | Settings | File Templates.
 */
@Controller
@RequestMapping("/whp/provider/**")
public class WHPProviderService extends ProviderRegistrationService<WHPProvider> {
    public WHPProviderService() {
        super(WHPProvider.class);
    }

    @Override
    public void createOrUpdate(WHPProvider whpProvider) {
        System.out.println(whpProvider.getPrimary_mobile());
    }
}
