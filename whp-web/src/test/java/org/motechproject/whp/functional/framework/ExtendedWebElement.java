package org.motechproject.whp.functional.framework;

import org.openqa.selenium.WebElement;

public interface ExtendedWebElement extends WebElement {
    void select(String value);

    void sendKey(CharSequence charSequence);
}
