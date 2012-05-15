package org.motechproject.whp.functional.framework;

import org.openqa.selenium.Dimension;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;

public class WebDriverFactory {
    public static final String TEST_DRIVER = "test.driver";

    public static WebElement createWebElement(WebElement webElement) {
        if (Driver.HTML_UNIT.is(driverName()))
            return new WHPHtmlUnitWebElement(webElement);
        else
            return new WHPWebElement(webElement);
    }

    private enum Driver {
        FIREFOX("firefox") {
            @Override
            WebDriver give() {
                FirefoxDriver firefoxDriver = new FirefoxDriver();
                firefoxDriver.manage().window().maximize();
                return firefoxDriver;
            }
        },

        CHROME("chrome") {
            @Override
            WebDriver give() {
                return new ChromeDriver();
            }
        },

        IE("ie") {
            @Override
            WebDriver give() {
                return new InternetExplorerDriver();
            }
        },

        HTML_UNIT("htmlunit") {
            @Override
            WebDriver give() {
                return new WHPHtmlUnitDriver(true, false);
            }
        };

        private String name;

        Driver(String name) {
            this.name = name;
        }

        abstract WebDriver give();

        public static Driver enumFor(String name) {
            for (Driver driver : values()) {
                if (driver.is(name)) return driver;
            }
            return null;
        }

        public boolean is(String name) {
            return this.name.equalsIgnoreCase(name);
        }

        @Override
        public String toString() {
            return name;
        }
    }

    public static WebDriver getInstance() {
        String name = driverName();
        return Driver.enumFor(name).give();
    }

    private static String driverName() {
        return System.getProperty(TEST_DRIVER, Driver.FIREFOX.toString());
    }
}