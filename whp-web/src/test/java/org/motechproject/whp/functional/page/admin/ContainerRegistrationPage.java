package org.motechproject.whp.functional.page.admin;

import org.motechproject.whp.functional.framework.WHPWebElement;
import org.motechproject.whp.functional.framework.WebDriverFactory;
import org.motechproject.whp.functional.page.Page;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;

public class ContainerRegistrationPage extends Page {

    @FindBy(how = How.ID, using = "newContainer")
    WebElement newContainerOption;

    @FindBy(how = How.ID, using = "containerId")
    WebElement container;

    @FindBy(how = How.ID, using = "providerId")
    WebElement provider;

    @FindBy(how = How.ID, using = "instance")
    WebElement instance;


    @FindBy(how = How.ID, using = "registerButton")
    WebElement registerButton;

    public ContainerRegistrationPage(WebDriver webDriver) {
        super(webDriver);
    }

    @Override
    public void postInitialize() {
        newContainerOption = WebDriverFactory.createWebElement(newContainerOption);
        container = WebDriverFactory.createWebElement(container);
        provider = WebDriverFactory.createWebElement(provider);
        instance = WebDriverFactory.createWebElement(instance);
        registerButton = WebDriverFactory.createWebElement(registerButton);
    }

    @Override
    protected void waitForPageToLoad() {
        waitForElementWithIdToLoad("registerButton");
    }

    public void registerContainer(String containerId, String providerId, String instance) {
        newContainerOption.click();
        this.container.sendKeys(containerId);
        this.provider.sendKeys(providerId);
        ((WHPWebElement) this.instance).select(instance);
        registerButton.click();
    }
}
