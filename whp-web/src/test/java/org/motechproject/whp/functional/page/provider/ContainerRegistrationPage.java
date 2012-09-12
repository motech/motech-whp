package org.motechproject.whp.functional.page.provider;

import org.motechproject.whp.functional.framework.MyPageFactory;
import org.motechproject.whp.functional.page.LoggedInUserPage;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;

public class ContainerRegistrationPage extends LoggedInUserPage {

    @FindBy(how = How.ID, using = "instance")
    private WebElement instanceDropDown;

    @FindBy(how = How.ID, using = "containerId")
    private WebElement containerId;

    @FindBy(how = How.ID, using = "registerButton")
    private WebElement registerButton;

    public ContainerRegistrationPage(WebDriver webDriver) {
        super(webDriver);
    }

    public ContainerRegistrationPage registerContainer(String containerId, String instance) {
        this.containerId.sendKeys(containerId);
        this.instanceDropDown.sendKeys(instance);

        registerButton.click();

        waitForElementWithIdToLoad("container-registration-confirmation");
        return MyPageFactory.initElements(webDriver, ContainerRegistrationPage.class);
    }
}
