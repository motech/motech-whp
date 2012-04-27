package org.motechproject.whp.functional.framework;

import org.openqa.selenium.*;

import java.util.List;

//This class should be agnostic of any specific driver
public class WHPWebElement implements ExtendedWebElement {
    protected WebElement webElement;

    public WHPWebElement(WebElement webElement) {
        this.webElement = webElement;
    }

    @Override
    public void click() {
        try {
            webElement.click();
        } catch (Exception e) {
            webElement.click();
        }
    }

    @Override
    public void submit() {
        webElement.sendKeys(Keys.ENTER);
    }

    @Override
    public String getValue() {
        return webElement.getValue();
    }

    @Override
    public void sendKeys(CharSequence... charSequences) {
        click();
        clear();
        webElement.sendKeys(charSequences);
    }

    @Override
    public void clear() {
        webElement.clear();
    }

    @Override
    public String getTagName() {
        return webElement.getTagName();
    }

    @Override
    public String getAttribute(String s) {
        return webElement.getAttribute(s);
    }

    @Override
    public boolean toggle() {
        return webElement.toggle();
    }

    @Override
    public boolean isSelected() {
        return webElement.isSelected();
    }

    @Override
    public void setSelected() {
        webElement.setSelected();
    }

    @Override
    public boolean isEnabled() {
        return webElement.isEnabled();
    }

    @Override
    public String getText() {
        return webElement.getText();
    }

    @Override
    public List<WebElement> findElements(By by) {
        return webElement.findElements(by);
    }

    @Override
    public WebElement findElement(By by) {
        try {
            return webElement.findElement(by);
        } catch (NoSuchElementException e) {
            return null;
        }
    }

    @Override
    public void select(String value) {
        sendKeys(value);
        click(); // required to simulate onKeyPress that sets the dojo backing element
    }

    @Override
    public void sendKey(CharSequence charSequence) {
        webElement.sendKeys(charSequence);
    }
}
