package org.motechproject.whp.functional.page;

import org.apache.commons.lang.StringUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;

import java.util.List;

public class ListProvidersPage extends LoggedInUserPage {

    @FindBy(how = How.CLASS_NAME, using = "providerId")
    protected List<WebElement> providerIds;

    public ListProvidersPage(WebDriver webDriver) {
        super(webDriver);
    }

    public boolean hasProvider(String providerId) {
        for (WebElement providerIdElement : providerIds) {
            if (StringUtils.containsIgnoreCase(providerIdElement.getText(), providerId)) {
                return true;
            }
        }
        return false;
    }

    public String getProviderIdText(String providerId) {
        return webDriver.findElement(By.id(String.format("provider_%s_ProviderId", providerId))).getText();
    }

    public String getDistrictText(String providerId) {
        return webDriver.findElement(By.id(String.format("provider_%s_District", providerId))).getText();
    }

    public String getPrimaryMobileText(String providerId) {
        return webDriver.findElement(By.id(String.format("provider_%s_PrimaryMobile", providerId))).getText();
    }

    public String getSecondaryMobileText(String providerId) {
        return webDriver.findElement(By.id(String.format("provider_%s_SecondaryMobile", providerId))).getText();
    }

    public String getTertiaryMobileText(String providerId) {
        return webDriver.findElement(By.id(String.format("provider_%s_TertiaryMobile", providerId))).getText();
    }
}
