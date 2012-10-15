package org.motechproject.whp.wgninbound.response;

import lombok.EqualsAndHashCode;
import org.motechproject.whp.common.exception.WHPError;

import java.util.ArrayList;
import java.util.List;

import static java.util.Arrays.asList;
import static org.apache.commons.collections.CollectionUtils.isNotEmpty;

@EqualsAndHashCode
public class VerificationResult {

    private String verifiedValue;
    private List<WHPError> error = new ArrayList<>();

    public VerificationResult(String verifiedValue) {
        this.verifiedValue = verifiedValue;
        error = new ArrayList<>();
    }

    public VerificationResult(WHPError whpError, String verifiedValue) {
        this.error = asList(whpError);
        this.verifiedValue = verifiedValue;
    }

    public void addAllErrors(List<WHPError> errors) {
        if (null != errors) {
            for (WHPError whpError : errors) {
                if (null != whpError) {
                    this.error.add(whpError);
                }
            }
        }
    }

    public boolean isError() {
        return isNotEmpty(error);
    }

    public boolean isSuccess() {
        return !isError();
    }

    public List<WHPError> getErrors() {
        return error;
    }
}
