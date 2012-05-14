package org.motechproject.whp.adherence.report;

import org.motechproject.reports.builder.PagedReportBuilder;
import org.motechproject.whp.adherence.domain.Adherence;
import org.motechproject.whp.adherence.service.WHPAdherenceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

import static java.util.Arrays.asList;

@Component
public class AdherenceReportBuilder extends PagedReportBuilder<Adherence> {

    WHPAdherenceService adherenceService;
    public static final int PAGE_SIZE = 10000;

    @Autowired
    public AdherenceReportBuilder(WHPAdherenceService adherenceService) {
        super("Adherence Report", asList("Patient ID", "TB Id", "Date of Adherence", "Adherence Value"));
        this.adherenceService = adherenceService;
    }

    @Override
    protected List<String> createRowData(Adherence adherence) {
        List<String> row = new ArrayList<String>();
        row.add(adherence.getPatientId());
        row.add(adherence.getTbId());
        row.add(adherence.getPillDate().toString("dd/MM/yyyy"));
        row.add(adherence.getPillStatus().name());
        return row;
    }

    @Override
    protected List<Adherence> data(int pageNumber) {
        return adherenceService.allAdherenceData(pageNumber - 1, PAGE_SIZE);
    }

}
