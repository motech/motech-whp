package org.motechproject.whp.wgninbound.response;

import lombok.Data;
import org.motechproject.whp.common.exception.WHPError;

import java.util.ArrayList;
import java.util.List;

import static java.util.Arrays.asList;
import static org.apache.commons.collections.CollectionUtils.isNotEmpty;

@Data
public class VerificationResult {

    private List<WHPError> errors = new ArrayList<>();

    public VerificationResult() {
        errors = new ArrayList<>();
    }

    public VerificationResult(WHPError whpError) {
        this.errors = asList(whpError);
    }

    public void addAllErrors(List<WHPError> errors) {
        if (null != errors) {
            for (WHPError whpError : errors) {
                if (null != whpError) {
                    this.errors.add(whpError);
                }
            }
        }
    }

    public boolean isError() {
        return isNotEmpty(errors);
    }

    public boolean isSuccess() {
        return !isError();
    }
}
