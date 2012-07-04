package org.motechproject.whp.functional.framework;

import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;

public class WHPHtmlUnitWebElement extends WHPWebElement {
    public WHPHtmlUnitWebElement(WebElement webElement) {
        super(webElement);
    }

    @Override
    public void sendKeys(CharSequence... keysToSend) {
        click();
        clear();
        click();
        webElement.sendKeys(keysToSend);
    }
}
