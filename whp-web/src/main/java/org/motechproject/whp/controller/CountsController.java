package org.motechproject.whp.controller;

import org.motechproject.whp.common.repository.Countable;
import org.motechproject.whp.uimodel.Count;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/count")
public class CountsController {

    public static final String COUNTS = "counts";
    public static final String REPORTS_URL = "reportsURL";

    private List<Countable> countableEntities;
    private String reportsUrl;

    @Autowired
    public CountsController(List<Countable> countableEntities, @Value("#{whpProperties['whp.reports.url']}") String reportsUrl) {
        this.countableEntities = countableEntities;
        this.reportsUrl = reportsUrl;
    }

    @RequestMapping(value = "all", method = RequestMethod.GET)
    public String all(Model uiModel) {
        List<Count> counts = new ArrayList<>();
        for (Countable countableEntity : countableEntities) {
            counts.add(new Count(countableEntity.getClass().getSimpleName(), countableEntity.count()));
        }
        uiModel.addAttribute(COUNTS, counts);
        uiModel.addAttribute(REPORTS_URL, reportsUrl);
        return "counts/counts";
    }
}
