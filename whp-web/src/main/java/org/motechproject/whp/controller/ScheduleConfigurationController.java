package org.motechproject.whp.controller;

import org.motechproject.flash.Flash;
import org.motechproject.scheduler.context.EventContext;
import org.motechproject.whp.schedule.domain.ScheduleType;
import org.motechproject.whp.schedule.model.ScheduleConfiguration;
import org.motechproject.whp.schedule.service.WHPSchedulerService;
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

    private static final String SCHEDULE_UPDATED_FLAG = "schedule.updated";

    private EventContext eventContext;
    private WHPSchedulerService whpSchedulerService;

    @Autowired
    public ScheduleConfigurationController(EventContext eventContext, WHPSchedulerService whpSchedulerService) {
        this.eventContext = eventContext;
        this.whpSchedulerService = whpSchedulerService;
    }

    @RequestMapping(value = "/{scheduleType}", method = GET)
    public String getSchedule(@PathVariable("scheduleType") ScheduleType reminderType, Model model, HttpServletRequest request) throws IOException {
        ScheduleConfiguration configuration = whpSchedulerService.configuration(reminderType);
        if (configuration == null) {
            configuration = new ScheduleConfiguration(reminderType, now().toDate());
        }
        model.addAttribute(configuration);
        if (Flash.has(SCHEDULE_UPDATED_FLAG, request)) {
            model.addAttribute("message", Flash.in(SCHEDULE_UPDATED_FLAG, request));
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
        whpSchedulerService.scheduleEvent(scheduleConfiguration);
        Flash.out(SCHEDULE_UPDATED_FLAG, "true", request);
        return "redirect:/schedule/" + scheduleConfiguration.getScheduleType().name();
    }

    @RequestMapping(value = "/update/unschedule", method = POST)
    public String unSchedule(@Valid ScheduleConfiguration scheduleConfiguration) throws IOException {
        whpSchedulerService.unScheduleReminder(scheduleConfiguration);
        return "redirect:/schedule/" + scheduleConfiguration.getScheduleType().name();
    }
}
