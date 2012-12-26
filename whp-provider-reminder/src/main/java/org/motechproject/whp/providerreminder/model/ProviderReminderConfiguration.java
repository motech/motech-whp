package org.motechproject.whp.providerreminder.model;

import lombok.Data;
import org.apache.commons.lang.StringUtils;
import org.ektorp.support.TypeDiscriminator;
import org.joda.time.LocalDateTime;
import org.motechproject.model.DayOfWeek;
import org.motechproject.model.MotechBaseDataObject;
import org.motechproject.whp.providerreminder.domain.ProviderReminderType;

import javax.validation.constraints.NotNull;
import java.util.Date;

@Data
@TypeDiscriminator("doc.type === 'ProviderReminderConfiguration'")
public class ProviderReminderConfiguration extends MotechBaseDataObject {

    @NotNull
    private DayOfWeek dayOfWeek;
    @NotNull
    private int hour;
    @NotNull
    private int minute;
    @NotNull
    private ProviderReminderType reminderType;

    public ProviderReminderConfiguration() {
    }

    public ProviderReminderConfiguration(ProviderReminderType reminderType, Date date) {
        LocalDateTime localDateTime = new LocalDateTime(date);
        setReminderType(reminderType);
        setDayOfWeek(DayOfWeek.getDayOfWeek(localDateTime.getDayOfWeek()));
        setHour(localDateTime.getHourOfDay());
        setMinute(localDateTime.getMinuteOfHour());
    }

    public void setReminderType(ProviderReminderType reminderType) {
        if (StringUtils.isBlank(getId())) {
            setId(reminderType.name());
        }
        this.reminderType = reminderType;
    }

    public String generateCronExpression() {
        String weekDay = getDayOfWeek().getShortName();
        return String.format("0 %s %s ? * %s", minute, hour, weekDay);
    }
}
