package org.motechproject.whp.functional.page;

import org.apache.commons.lang.StringUtils;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.openqa.selenium.By.id;

public abstract class HtmlSection {

    private Logger logger = LoggerFactory.getLogger(Page.class);

    protected WebDriver webDriver;

    protected WebDriverWait wait;

    private static final long RetryTimes = 5;

    private static final long MaxPageLoadTime = 30;

    private static final long RetryInterval = 5;

    protected WebDriverWait waitWithRetry;

    public HtmlSection(WebDriver webDriver) {
        this.webDriver = webDriver;
        this.wait = new WebDriverWait(webDriver, MaxPageLoadTime);
        this.waitWithRetry = new WebDriverWait(webDriver, RetryInterval);
    }

    public void postInitialize() {
    }

    protected void waitForElementWithIdToLoad(final String id) {
        waitForElementToLoad(By.id(id));
    }

    protected void waitForElementWithXPATHToLoad(final String path) {
        waitForElementToLoad(By.xpath(path));
    }

    protected void waitForElementWithCSSToLoad(final String className) {
        waitForElementToLoad(By.className(className));
    }

    protected void waitForElementToLoadWithRetry(final By by) {
        for (int i = 1; i <= RetryTimes; i++) {
            try {
                Boolean foundElement = waitWithRetry.until(new ExpectedCondition<Boolean>() {
                    @Override
                    public Boolean apply(WebDriver webDriver) {
                        return webDriver.findElement(by) != null;
                    }
                });
                if (foundElement) break;
            } catch (WebDriverException e) {
                logger.info(String.format("Retried %s time(s) ...", i));
                if (i == RetryTimes)
                    throw e;
            }
            webDriver.get(webDriver.getCurrentUrl());
        }
    }

    protected WebElement safeFindElement(By by) {
        try {
            return webDriver.findElement(by);
        } catch (NoSuchElementException e) {
            return null;
        }
    }

    protected WebElement safeFindElementIn(WebElement webElement, By by) {
        try {
            return webElement.findElement(by);
        } catch (NoSuchElementException e) {
            return null;
        }
    }

    protected void waitUntilElementEditable(final By by) {
        wait.until(new ExpectedCondition<Boolean>() {
            @Override
            public Boolean apply(WebDriver webDriver) {
                return webDriver.findElement(by).isEnabled() && webDriver.findElement(by).isDisplayed();
            }
        });
    }

    protected void waitUntilElementIsNotPresent(final By by) {
        wait.until(new ExpectedCondition<Boolean>() {
            @Override
            public Boolean apply(WebDriver webDriver) {
                return safeFindElement(by) == null;
            }
        });
    }

    protected void waitForScript(int timeOut) {
        try {
            Thread.sleep(timeOut);
        } catch (InterruptedException e) {

        }
    }

    private void waitForElementToLoad(final By by) {
        wait.until(new ExpectedCondition<Boolean>() {
            @Override
            public Boolean apply(WebDriver webDriver) {
                return webDriver.findElement(by) != null;
            }
        });
    }

    protected void waitForElementToBeReloadedByAjax() {
        wait.until(new ExpectedCondition<Boolean>() {
            @Override
            public Boolean apply(WebDriver webDriver) {
                return (Boolean) ((JavascriptExecutor) webDriver).executeScript("return jQuery.active == 0");
            }
        });
    }

    protected void waitForSuccess(String operation) {
        System.out.println("START: " + operation + " - Wait for success ...................................");
        wait.until(new ExpectedCondition<Boolean>() {
            @Override
            public Boolean apply(WebDriver webDriver) {
                WebElement statusMessage = webDriver.findElement(id("statusMessage"));
                System.out.println(statusMessage.getText());
                return StringUtils.containsIgnoreCase(statusMessage.getText(), "success");
            }
        });
        System.out.println("END: " + operation + " - Wait for success ...................................");
        System.out.println("***************************************************************************");
    }

}
