package org.motechproject.whp.adherence.request;

import lombok.EqualsAndHashCode;
import org.joda.time.LocalDate;
import org.motechproject.whp.common.domain.TreatmentWeek;

import java.util.*;

@EqualsAndHashCode
public class DailyAdherenceRequests implements Iterable<DailyAdherenceRequest>{

    private List<DailyAdherenceRequest> requestList;

    public DailyAdherenceRequests(List<DailyAdherenceRequest> requestList) {
        this.requestList = new ArrayList<>(requestList);
    }

    public LocalDate maxDoseDate() {
        ArrayList<DailyAdherenceRequest> requestsByDoseDate = new ArrayList<>(requestList);
        Collections.sort(requestsByDoseDate, new DoseDateComparator());
        return requestsByDoseDate.get(requestsByDoseDate.size() - 1).getDoseDate();
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


class DoseDateComparator implements Comparator<DailyAdherenceRequest> {
    @Override
    public int compare(DailyAdherenceRequest request1, DailyAdherenceRequest request2) {
        return request1.getDoseDate().compareTo(request2.getDoseDate());
    }
}