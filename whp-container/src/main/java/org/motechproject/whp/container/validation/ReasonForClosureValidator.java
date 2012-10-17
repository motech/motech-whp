package org.motechproject.whp.container.validation;

import org.apache.commons.lang.StringUtils;
import org.joda.time.LocalDate;
import org.motechproject.util.DateUtil;
import org.motechproject.whp.common.error.ErrorWithParameters;
import org.motechproject.whp.container.contract.ContainerClosureRequest;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

import static org.motechproject.whp.container.WHPContainerConstants.TB_NEGATIVE_CODE;

@Component
public class ReasonForClosureValidator {

    public static final String SEPARATOR = "/";

    public List<ErrorWithParameters> validate(ContainerClosureRequest containerClosureRequest) {
        List<ErrorWithParameters> errors = new ArrayList<>();

        if (StringUtils.isEmpty(containerClosureRequest.getContainerId()))
            errors.add(new ErrorWithParameters("container.id.invalid.error", containerClosureRequest.getContainerId()));

        if (StringUtils.isEmpty(containerClosureRequest.getReason()))
            errors.add(new ErrorWithParameters("container.reason.for.closure.invalid.error", containerClosureRequest.getReason()));

        if (StringUtils.equals(containerClosureRequest.getReason(), TB_NEGATIVE_CODE)) {
            if (StringUtils.isEmpty(containerClosureRequest.getAlternateDiagnosis()))
                errors.add(new ErrorWithParameters("container.alternate.diagnosis.invalid.error", containerClosureRequest.getAlternateDiagnosis()));
            if (StringUtils.isEmpty(containerClosureRequest.getConsultationDate()) || isInvalidFormat(containerClosureRequest.getConsultationDate()))
                errors.add(new ErrorWithParameters("container.consultation.date.invalid.error", containerClosureRequest.getConsultationDate()));
        } else {
            if (!StringUtils.isEmpty(containerClosureRequest.getAlternateDiagnosis()))
                errors.add(new ErrorWithParameters("container.alternate.diagnosis.invalid.error", containerClosureRequest.getAlternateDiagnosis()));
            if (!StringUtils.isEmpty(containerClosureRequest.getConsultationDate()))
                errors.add(new ErrorWithParameters("container.consultation.date.invalid.error", containerClosureRequest.getConsultationDate()));
        }

        return errors;
    }

    private boolean isInvalidFormat(String consultationDate) {
        String[] dateValues = consultationDate.split(SEPARATOR);
        try {
            LocalDate date = new LocalDate(Integer.parseInt(dateValues[2]), Integer.parseInt(dateValues[1]), Integer.parseInt(dateValues[0]));
            if(date.isAfter(DateUtil.today()))
                return true;
            return false;
        } catch (Exception e) {
            return true;
        }
    }
}
