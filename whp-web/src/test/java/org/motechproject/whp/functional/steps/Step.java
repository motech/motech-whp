package org.motechproject.whp.functional.steps;

import org.motechproject.whp.functional.page.Page;
import org.openqa.selenium.WebDriver;

public abstract class Step {

    protected WebDriver webDriver;

    public Step(WebDriver webDriver) {
        this.webDriver = webDriver;
    }

    public Page execute() {
        return null;
    }

    public <T extends Page> Page execute(T startingPage) {
        return null;
    }
}
