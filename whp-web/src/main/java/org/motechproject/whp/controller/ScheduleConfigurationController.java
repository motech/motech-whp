package org.motechproject.whp.controller;

import org.motechproject.flash.Flash;
import org.motechproject.scheduler.context.EventContext;
import org.motechproject.whp.providerreminder.domain.ScheduleType;
import org.motechproject.whp.providerreminder.model.ScheduleConfiguration;
import org.motechproject.whp.providerreminder.service.WHPSchedulerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.io.IOException;

import static org.motechproject.util.DateUtil.now;
import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

@Controller
@RequestMapping(value = "/schedule")
public class ScheduleConfigurationController extends BaseWebController {

    private EventContext eventContext;
    private WHPSchedulerService WHPSchedulerService;

    @Autowired
    public ScheduleConfigurationController(EventContext eventContext, WHPSchedulerService WHPSchedulerService) {
        this.eventContext = eventContext;
        this.WHPSchedulerService = WHPSchedulerService;
    }

    @RequestMapping(value = "/{scheduleType}", method = GET)
    public String scheduleFor(@PathVariable("scheduleType") ScheduleType reminderType, Model model, HttpServletRequest request) throws IOException {
        ScheduleConfiguration reminderConfiguration = WHPSchedulerService.configuration(reminderType);
        if (reminderConfiguration == null) {
            reminderConfiguration = new ScheduleConfiguration(reminderType, now().toDate());
        }
        model.addAttribute(reminderConfiguration);
        if (Flash.has("reminder.updated", request)) {
            model.addAttribute("message", Flash.in("reminder.updated", request));
        }
        return "schedule/scheduleConfiguration";
    }

    @RequestMapping(value = "/execute", method = GET)
    @ResponseBody
    public String remind(@RequestParam("type") ScheduleType scheduleType) throws IOException {
        eventContext.send(scheduleType.getEventSubject());
        return "Triggered reminder";
    }

    @RequestMapping(value = "/update", method = POST)
    public String updateSchedule(@Valid ScheduleConfiguration scheduleConfiguration, HttpServletRequest request) throws IOException {
        WHPSchedulerService.scheduleReminder(scheduleConfiguration);
        Flash.out("reminder.updated", "true", request);
        return "redirect:/schedule/" + scheduleConfiguration.getScheduleType().name();
    }

    @RequestMapping(value = "/update/unschedule", method = POST)
    public String unSchedule(@Valid ScheduleConfiguration scheduleConfiguration, HttpServletRequest request) throws IOException {
        WHPSchedulerService.unScheduleReminder(scheduleConfiguration);
        return "redirect:/schedule/" + scheduleConfiguration.getScheduleType().name();
    }
}
