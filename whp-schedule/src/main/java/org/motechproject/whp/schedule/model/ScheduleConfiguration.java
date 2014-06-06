package org.motechproject.whp.schedule.model;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import javax.validation.constraints.NotNull;

import lombok.Data;

import org.apache.commons.lang.StringUtils;
import org.ektorp.support.TypeDiscriminator;
import org.joda.time.LocalDateTime;
import org.motechproject.model.DayOfWeek;
import org.motechproject.model.MotechBaseDataObject;
import org.motechproject.whp.schedule.domain.ScheduleType;

@Data
@TypeDiscriminator("doc.type === 'ScheduleConfiguration'")
public class ScheduleConfiguration extends MotechBaseDataObject {

    @NotNull
    private List<DayOfWeek> dayOfWeek;
    @NotNull
    private int hour;
    @NotNull
    private int minute;
    @NotNull
    private ScheduleType scheduleType;

    private String messageId;

    private boolean scheduled = false;

    public ScheduleConfiguration() {
    }

    public ScheduleConfiguration(ScheduleType reminderType, Date date) {
        setScheduleType(reminderType);
        updateDateTimeValues(date);
    }

    public void updateDateTimeValues(Date date) {
        LocalDateTime localDateTime = new LocalDateTime(date);
        setDayOfWeek(Arrays.asList(DayOfWeek.getDayOfWeek(localDateTime.getDayOfWeek())));
        setHour(localDateTime.getHourOfDay());
        setMinute(localDateTime.getMinuteOfHour());
    }

    public void setScheduleType(ScheduleType scheduleType) {
        if (StringUtils.isBlank(getId())) {
            setId(scheduleType.name());
        }
        this.scheduleType = scheduleType;
    }

    public String generateCronExpression() {
    	String weekDay = "" ;
    	String delimiter = "";
    	for(DayOfWeek day : dayOfWeek){
    		weekDay += delimiter;
    		weekDay += day.getShortName();
    		delimiter = ",";
    	}
    	
        return String.format("0 %s %s ? * %s", minute, hour, weekDay);    
     }
}
