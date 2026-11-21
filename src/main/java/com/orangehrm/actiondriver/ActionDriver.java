package com.orangehrm.actiondriver;

import com.orangehrm.base.BaseClass;
import com.orangehrm.utilities.ExtentManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class ActionDriver{

    private WebDriver driver;
    private WebDriverWait wait;
    private static Logger logger;

    public ActionDriver(WebDriver driver) {
        logger = BaseClass.logger;
        this.driver = driver;
        int explicitWait = Integer.parseInt(BaseClass.getProp().getProperty("explicit.wait"));
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(explicitWait)); //configuring wait object, The timer will start when I use until method, Here
        //the max wait time is 10s. This is of no use without the until method
    }

    public void click(By by) {
        waitForElementToBeClickable(by);
        logger.info("The element "+getElementDescription(by)+" is clicked.");
        ExtentManager.logStep("The element "+getElementDescription(by)+" is clicked.");
        driver.findElement(by).click();
    }

    public void enterText(By by, String value) {
        waitForElementToBeVisible(by);
        driver.findElement(by).clear();
        driver.findElement(by).sendKeys(value);
        logger.info("Entered Text for the element "+getElementDescription(by));
        ExtentManager.logStep("Entered Text for the element "+getElementDescription(by));
    }

    public String getText(By by){
        waitForElementToBeVisible(by);
        return driver.findElement(by).getText();
    }

    public boolean compareText(By by, String expectedText){
        try {
            waitForElementToBeVisible(by);
            String actualText=driver.findElement(by).getText();
            logger.info("Text compared "+getElementDescription(by));
            ExtentManager.logPass(driver,"CompareText","Test verified");
            return actualText.equals(expectedText);
        }
        catch (Exception e){
            logger.error("Unable to compare texts: "+e.getMessage());
            return false;
        }

    }

    public boolean isDisplayed(By by){
       try {
           waitForElementToBeVisible(by);
           logger.info("The element "+getElementDescription(by)+" is displayed");
           ExtentManager.logStep("The element "+getElementDescription(by)+" is displayed");

           return driver.findElement(by).isDisplayed();
       }
       catch (Exception e)
       {
           logger.error("Unable to check whether the element is displayed: "+e.getMessage());
           return false;
       }
    }

    public void waitForElementToBeClickable(By by) {
        try {
            wait.until(ExpectedConditions.elementToBeClickable(by)); //It waits maximum of 10 sec until the element becomes clickable else it throws timeout exception
        } catch (Exception e) {
            logger.error("Unable to click the element: " + e.getMessage());
        }
    }

    public void waitForElementToBeVisible(By by) {
        try {
            wait.until(ExpectedConditions.visibilityOfElementLocated(by));
        } catch (Exception e) {
            logger.error("Element is not visible: " + e.getMessage());
        }
    }

// Methods that involves javascript
    public void scrollToElement(By by){
        try {
            JavascriptExecutor js = (JavascriptExecutor) driver;
            WebElement element = driver.findElement(by);
            js.executeScript("arguments[0].scrollIntoView(true)",element);
        }
        catch (Exception e){
            logger.error("Unable to scroll to element: "+e.getMessage());
        }
    }

    public void waitForPageLoad(int timeOut){
        try {
            //wait.withTimeout() - Setting the maximum waiting time on your stopwatch. Earlier it will be the value of dynamicwait
            wait.withTimeout(Duration.ofSeconds(timeOut)).until(WebDriver -> ((JavascriptExecutor) WebDriver)
                    .executeScript("return document.readyState").equals("complete"));
        }
        catch (Exception e)
        {
            logger.info("Page didn't load within "+timeOut+" seconds: "+e.getMessage());
        }
    }

    public String getElementDescription(By by)
    {
        WebElement webElement = driver.findElement(by);
        String name = webElement.getDomAttribute("name");
        String className = webElement.getDomAttribute("class");
        String type = webElement.getDomAttribute("type");
        String placeholder = webElement.getDomAttribute("placeholder");

        if (isElementNotNull(name))
            return name;
        else if(isElementNotNull(type))
            return className;
        else if(isElementNotNull(placeholder))
            return type;
        else if (isElementNotNull(className))
            return placeholder;
        else
            return "";

    }

    public boolean isElementNotNull(String element){
        if(element != null && !element.isEmpty())
            return true;
        else
            return false;
    }
}
