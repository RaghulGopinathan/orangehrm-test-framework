package com.orangehrm.actiondriver;

import com.orangehrm.base.BasePage;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class ActionDriver extends BasePage {

    private static WebDriver driver;
    private WebDriverWait wait;

    public ActionDriver() {
        driver = getDriver();
        int explicitWait = Integer.parseInt(prop.getProperty("explicit.wait"));
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(explicitWait)); //configuring wait object, The timer will start when I use until method, Here
        //the max wait time is 10s. This is of no use without the until method
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

    public String compareText(By by){
        waitForElementToBeVisible(by);
        return driver.findElement(by).getText();
    }

    public void waitForElementToBeClickable(By by) {
        try {
            wait.until(ExpectedConditions.elementToBeClickable(by)); //It waits maximum of 30 sec until the element becomes clickable else it throws timeout exception
        } catch (Exception e) {
            System.out.println("Unable to click the element: " + e.getMessage());
        }
    }

    public void waitForElementToBeVisible(By by) {
        try {
            wait.until(ExpectedConditions.visibilityOfElementLocated(by));
        } catch (Exception e) {
            System.out.println("Element is not visible: " + e.getMessage());
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
            System.out.println("Unable to scroll to element: "+e.getMessage());
        }
    }

    public void waitForPageLoad(int timeOut){
        try {
            //wait.withTimeout() - Setting the maximum waiting time on your stopwatch. Earlier it will be the value of dynamicwait
            wait.withTimeout(Duration.ofSeconds(timeOut)).until(WebDriver -> ((JavascriptExecutor) WebDriver)
                    .executeScript("return document.readyState").equals("complete"));

            System.out.println("Page loaded successfully!");
        }
        catch (Exception e)
        {
            System.out.println("Page didn't load within "+timeOut+" seconds: "+e.getMessage());
        }
    }
}
