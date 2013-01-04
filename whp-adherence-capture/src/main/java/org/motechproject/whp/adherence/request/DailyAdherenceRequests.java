package org.motechproject.whp.adherence.request;

import lombok.EqualsAndHashCode;
import org.joda.time.LocalDate;
import org.motechproject.whp.common.domain.TreatmentWeek;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import static ch.lambdaj.Lambda.on;
import static ch.lambdaj.collection.LambdaCollections.with;

@EqualsAndHashCode
public class DailyAdherenceRequests implements Iterable<DailyAdherenceRequest>{

    private List<DailyAdherenceRequest> requestList;

    public DailyAdherenceRequests(List<DailyAdherenceRequest> requestList) {
        this.requestList = new ArrayList<>(requestList);
    }

    public LocalDate maxDoseDate() {
        return with(requestList).sort(on(DailyAdherenceRequest.class).getDoseDate()).get(requestList.size() - 1).getDoseDate();
    }

    public LocalDate getLastAdherenceProvidedWeekStartDate() {
        LocalDate lastAdherenceProvidedDate = maxDoseDate();
        return new TreatmentWeek(lastAdherenceProvidedDate).startDate();
    }


    public boolean isEmpty() {
        return requestList.isEmpty();
    }

    @Override
    public Iterator<DailyAdherenceRequest> iterator() {
        return requestList.iterator();
    }
}
