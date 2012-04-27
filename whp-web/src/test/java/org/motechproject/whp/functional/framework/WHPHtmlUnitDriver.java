package org.motechproject.whp.functional.framework;

import com.gargoylesoftware.htmlunit.SilentCssErrorHandler;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;

public class WHPHtmlUnitDriver extends HtmlUnitDriver {
    public WHPHtmlUnitDriver(boolean enableJavascript, boolean throwExceptionOnScriptError) {
        super(enableJavascript);
        getWebClient().setThrowExceptionOnScriptError(throwExceptionOnScriptError);
        getWebClient().setCssErrorHandler(new SilentCssErrorHandler());
        getWebClient().setThrowExceptionOnFailingStatusCode(false);
    }
}
