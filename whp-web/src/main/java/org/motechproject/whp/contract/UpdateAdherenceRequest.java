package org.motechproject.whp.contract;

import lombok.Data;
import org.joda.time.LocalDate;

@Data
public class UpdateAdherenceRequest {
    private int day;
    private int month;
    private int year;
    private String patientId;
    private int pillStatus;
    private LocalDate date;

    public LocalDate getDate(){
        return  new LocalDate(year,month,day);
    }

}
