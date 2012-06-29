package org.motechproject.whp.functional.steps;

import org.openqa.selenium.WebDriver;

public abstract class Step {

    protected WebDriver webDriver;

    public Step(WebDriver webDriver) {
        this.webDriver = webDriver;
    }

}
