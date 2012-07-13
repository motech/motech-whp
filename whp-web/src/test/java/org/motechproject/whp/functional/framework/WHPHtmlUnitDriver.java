package org.motechproject.whp.functional.framework;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.SilentCssErrorHandler;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;

public class WHPHtmlUnitDriver extends HtmlUnitDriver {
    public WHPHtmlUnitDriver(boolean enableJavascript, boolean throwExceptionOnScriptError) {
        super(BrowserVersion.FIREFOX_3_6);
        getWebClient().setJavaScriptEnabled(enableJavascript);
        getWebClient().setThrowExceptionOnScriptError(throwExceptionOnScriptError);
        getWebClient().setThrowExceptionOnFailingStatusCode(false);
    }
}
