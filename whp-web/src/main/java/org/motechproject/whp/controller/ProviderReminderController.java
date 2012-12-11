package org.motechproject.whp.controller;

import org.joda.time.DateTime;
import org.motechproject.whp.common.util.WHPDateTime;
import org.motechproject.whp.providerreminder.configuration.ProviderReminderConfiguration;
import org.motechproject.whp.providerreminder.domain.ProviderReminderType;
import org.motechproject.whp.providerreminder.service.ProviderReminderScheduler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Date;

@Controller
@RequestMapping(value = "/providerreminder")
public class ProviderReminderController extends BaseWebController {


    private ProviderReminderScheduler providerReminderScheduler;

    @Autowired
    public ProviderReminderController(ProviderReminderScheduler providerReminderScheduler) {
        this.providerReminderScheduler = providerReminderScheduler;
    }

    @RequestMapping(value = "schedule/{reminderType}", method = RequestMethod.GET)
    @ResponseBody
    public String scheduleFor(@PathVariable("reminderType") ProviderReminderType reminderType) throws IOException {
        return WHPDateTime.date(new DateTime(providerReminderScheduler.getNextFireTime(reminderType))).value();
    }
}
