package org.motechproject.whp.controller;

import org.joda.time.DateTime;
import org.motechproject.whp.common.util.WHPDateTime;
import org.motechproject.whp.providerreminder.configuration.ProviderReminderConfiguration;
import org.motechproject.whp.providerreminder.domain.ProviderReminderType;
import org.motechproject.whp.providerreminder.service.ProviderReminderScheduler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@Controller
@RequestMapping(value = "/providerreminder")
public class ProviderReminderController extends BaseWebController {


    private ProviderReminderScheduler providerReminderScheduler;

    @Autowired
    public ProviderReminderController(ProviderReminderScheduler providerReminderScheduler) {
        this.providerReminderScheduler = providerReminderScheduler;
    }

    @RequestMapping(value = "schedule/{reminderType}", method = RequestMethod.GET)
    public String scheduleFor(@PathVariable("reminderType") ProviderReminderType reminderType, Model model) throws IOException {
        model.addAttribute(providerReminderScheduler.getReminder(reminderType));
        return "reminders/providerReminder";
    }
}
