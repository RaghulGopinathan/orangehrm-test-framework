package com.orangehrm.actiondriver;

import com.orangehrm.base.BaseClass;
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
    private Logger logger;

    public ActionDriver(WebDriver driver) {
        this.driver = driver;

        int explicitWait = Integer.parseInt(BaseClass.getProp().getProperty("explicit.wait"));
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(explicitWait)); //configuring wait object, The timer will start when I use until method, Here
        //the max wait time is 10s. This is of no use without the until method
        logger = LogManager.getLogger(ActionDriver.class);
    }

    public void click(By by) {
        waitForElementToBeClickable(by);
        driver.findElement(by).click();
    }

    public void enterText(By by, String value) {
        waitForElementToBeVisible(by);
        driver.findElement(by).clear();
        driver.findElement(by).sendKeys(value);
    }

    public String getText(By by){
        waitForElementToBeVisible(by);
        return driver.findElement(by).getText();
    }

    public boolean compareText(By by, String expectedText){
        try {
            waitForElementToBeVisible(by);
            String actualText=driver.findElement(by).getText();
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

            logger.info("Page loaded successfully!");
        }
        catch (Exception e)
        {
            logger.info("Page didn't load within "+timeOut+" seconds: "+e.getMessage());
        }
    }
}
