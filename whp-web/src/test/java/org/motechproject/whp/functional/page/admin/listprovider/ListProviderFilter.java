package org.motechproject.whp.functional.page.admin.listprovider;

import org.motechproject.whp.functional.framework.WebDriverFactory;
import org.motechproject.whp.functional.page.HtmlSection;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;

public class ListProviderFilter extends HtmlSection {

    @FindBy(how = How.ID, using = "providerId")
    protected WebElement providerId;

    @FindBy(how = How.ID, using = "search")
    protected WebElement submit;

    public ListProviderFilter(WebDriver webDriver) {
        super(webDriver);
    }

    @Override
    public void postInitialize() {
        providerId = WebDriverFactory.createWebElement(providerId);
        submit = WebDriverFactory.createWebElement(submit);
    }

    public void searchBy(String district, String providerId, boolean expectingResult) {
        this.providerId.clear();
        this.providerId.sendKeys(providerId.toLowerCase());
        JavascriptExecutor javascriptExecutor = (JavascriptExecutor) webDriver;
        javascriptExecutor.executeScript("$('#district').val('" + district + "');"); // can't set select box directly as it is hidden
        this.submit.click();
        if (expectingResult) {
            waitForElementWithCSSToLoad("provider-row");
        } else {
            waitForElementWithCSSToLoad("warning");
        }
        waitForScript(1000);
    }
}
