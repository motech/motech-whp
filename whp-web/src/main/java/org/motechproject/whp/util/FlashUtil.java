package org.motechproject.whp.util;

import org.apache.commons.lang.StringUtils;
import org.motechproject.flash.Flash;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

public class FlashUtil {

    public static void flashAllOut(String baseMessageName, List<String> messages, HttpServletRequest httpServletRequest) {
        for (int i = 0; i < messages.size(); i++) {
            Flash.out(baseMessageName + i, messages.get(i), httpServletRequest);
        }
    }

    public static List<String> flashAllIn(String baseMessageName, HttpServletRequest httpServletRequest) {
        List<String> messages = new ArrayList<String>();
        for (int i = 0; ; i++) {
            String message = Flash.in(baseMessageName + i, httpServletRequest);
            if (StringUtils.isNotEmpty(message)) {
                messages.add(message);
            } else {
                break;
            }
        }
        return messages;
    }

}
