package org.motechproject.whp.functional.page.admin.listprovider;


import org.motechproject.whp.functional.page.HtmlSection;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class ProviderPagination extends HtmlSection {

    protected ProviderPageDriver webDriver;
    private final String providerWithFocus;
    private final WebElement providerRow;

    public ProviderPagination(WebDriver webDriver, String providerId) {
        super(webDriver);
        this.webDriver = new ProviderPageDriver(webDriver);
        this.providerWithFocus = providerId;
        this.providerRow = findProviderRow();
    }

    public WebElement getProviderRow() {
        return providerRow;
    }

    private WebElement findProviderRow() {
        WebElement element = webDriver.findElement(providerWithFocus);
        while (null == element && webDriver.nextLinkEnabled()) {
            webDriver.nextLink().click();
            waitForScript(7000);
            element = webDriver.findElement(providerWithFocus);
        }
        return element;
    }

    private static class ProviderPageDriver {

        private WebDriver driver;

        private ProviderPageDriver(WebDriver driver) {
            this.driver = driver;
        }

        public boolean nextLinkEnabled() {
            return safeFindElement(By.linkText("Next")) != null;
        }

        public WebElement nextLink() {
            return safeFindElement(By.linkText("Next"));
        }

        public WebElement findElement(String id) {
            return safeFindElement(By.xpath(String.format("//tr[@providerid='%s']", id.toLowerCase())));
        }

        private WebElement safeFindElement(By by) {
            try {
                return driver.findElement(by);
            } catch (NoSuchElementException e) {
                return null;
            }
        }
    }
}
