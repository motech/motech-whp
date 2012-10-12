package org.motechproject.whp.container.tracking.validation;

import org.apache.commons.lang.StringUtils;
import org.joda.time.LocalDate;
import org.motechproject.util.DateUtil;
import org.motechproject.whp.common.error.ErrorWithParameters;
import org.motechproject.whp.container.contract.UpdateReasonForClosureRequest;
import org.springframework.stereotype.Component;
import java.util.ArrayList;
import java.util.List;

@Component
public class ReasonForClosureValidator {

    public static final String TB_NEGATIVE_CODE = "1";
    public static final String SEPARATOR = "/";

    public List<ErrorWithParameters> validate(UpdateReasonForClosureRequest updateReasonForClosureRequest) {
        List<ErrorWithParameters> errors = new ArrayList<>();

        if (StringUtils.isEmpty(updateReasonForClosureRequest.getContainerId()))
            errors.add(new ErrorWithParameters("container.id.invalid.error", updateReasonForClosureRequest.getContainerId()));

        if (StringUtils.isEmpty(updateReasonForClosureRequest.getReason()))
            errors.add(new ErrorWithParameters("container.reason.for.closure.invalid.error", updateReasonForClosureRequest.getReason()));

        if (StringUtils.equals(updateReasonForClosureRequest.getReason(), TB_NEGATIVE_CODE)) {
            if (StringUtils.isEmpty(updateReasonForClosureRequest.getAlternateDiagnosis()))
                errors.add(new ErrorWithParameters("container.alternate.diagnosis.invalid.error", updateReasonForClosureRequest.getAlternateDiagnosis()));
            if (StringUtils.isEmpty(updateReasonForClosureRequest.getConsultationDate()) || isInvalidFormat(updateReasonForClosureRequest.getConsultationDate()))
                errors.add(new ErrorWithParameters("container.consultation.date.invalid.error", updateReasonForClosureRequest.getConsultationDate()));
        } else {
            if (!StringUtils.isEmpty(updateReasonForClosureRequest.getAlternateDiagnosis()))
                errors.add(new ErrorWithParameters("container.alternate.diagnosis.invalid.error", updateReasonForClosureRequest.getAlternateDiagnosis()));
            if (!StringUtils.isEmpty(updateReasonForClosureRequest.getConsultationDate()))
                errors.add(new ErrorWithParameters("container.consultation.date.invalid.error", updateReasonForClosureRequest.getConsultationDate()));
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
