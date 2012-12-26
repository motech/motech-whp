package org.motechproject.whp.controller;

import org.motechproject.whp.providerreminder.domain.ProviderReminderType;
import org.motechproject.whp.providerreminder.model.ProviderReminderConfiguration;
import org.motechproject.whp.providerreminder.service.ProviderReminderScheduler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;
import java.io.IOException;

import static org.motechproject.util.DateUtil.now;
import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

@Controller
@RequestMapping(value = "/providerreminder")
public class ProviderReminderController extends BaseWebController {

    private ProviderReminderScheduler providerReminderScheduler;

    @Autowired
    public ProviderReminderController(ProviderReminderScheduler providerReminderScheduler) {
        this.providerReminderScheduler = providerReminderScheduler;
    }

    @RequestMapping(value = "/{reminderType}", method = GET)
    public String scheduleFor(@PathVariable("reminderType") ProviderReminderType reminderType, Model model) throws IOException {
        ProviderReminderConfiguration reminderConfiguration = providerReminderScheduler.configuration(reminderType);
        if (reminderConfiguration == null) {
            reminderConfiguration = new ProviderReminderConfiguration(reminderType, now().toDate());
        }
        model.addAttribute(reminderConfiguration);
        return "reminders/providerReminder";
    }

    @RequestMapping(value = "/update", method = POST)
    public String updateSchedule(@Valid ProviderReminderConfiguration providerReminderConfiguration) throws IOException {
        providerReminderScheduler.scheduleReminder(providerReminderConfiguration);
        return "redirect:/providerreminder/" + providerReminderConfiguration.getReminderType().name();
    }
}
