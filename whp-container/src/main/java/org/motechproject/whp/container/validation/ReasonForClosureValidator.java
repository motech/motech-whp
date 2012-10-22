package org.motechproject.whp.container.validation;

import org.apache.commons.lang.StringUtils;
import org.joda.time.LocalDate;
import org.motechproject.util.DateUtil;
import org.motechproject.whp.container.contract.ContainerClosureRequest;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

import static org.motechproject.whp.container.WHPContainerConstants.TB_NEGATIVE_CODE;

@Component
public class ReasonForClosureValidator {

    public static final String SEPARATOR = "/";

    public List<String> validate(ContainerClosureRequest containerClosureRequest) {
        List<String> errors = new ArrayList<>();

        if (StringUtils.isEmpty(containerClosureRequest.getContainerId()))
            errors.add("Container Id must be of" + containerClosureRequest.getContainerId() + " digits in length");

        if (StringUtils.isEmpty(containerClosureRequest.getReason()))
            errors.add("Enter reason for closure");

        if (StringUtils.equals(containerClosureRequest.getReason(), TB_NEGATIVE_CODE)) {
            if (StringUtils.isEmpty(containerClosureRequest.getAlternateDiagnosis()))
                errors.add("Enter alternate diagnosis");
            if (StringUtils.isEmpty(containerClosureRequest.getConsultationDate()) || isInvalidFormat(containerClosureRequest.getConsultationDate()))
                errors.add("Enter consultation date");
        } else {
            if (!StringUtils.isEmpty(containerClosureRequest.getAlternateDiagnosis()))
                errors.add("Enter alternate diagnosis");
            if (!StringUtils.isEmpty(containerClosureRequest.getConsultationDate()))
                errors.add("Enter consultation date");
        }

        return errors;
    }

    private boolean isInvalidFormat(String consultationDate) {
        String[] dateValues = consultationDate.split(SEPARATOR);
        try {
            LocalDate date = new LocalDate(Integer.parseInt(dateValues[2]), Integer.parseInt(dateValues[1]), Integer.parseInt(dateValues[0]));
            if (date.isAfter(DateUtil.today()))
                return true;
            return false;
        } catch (Exception e) {
            return true;
        }
    }
}
