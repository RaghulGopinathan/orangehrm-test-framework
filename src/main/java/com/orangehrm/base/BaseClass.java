package com.orangehrm.base;

import com.orangehrm.actiondriver.ActionDriver;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;

import java.io.FileInputStream;
import java.io.IOException;
import java.time.Duration;
import java.util.Properties;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.LockSupport;

public class BaseClass {

    protected static Properties prop; //define this variable as static or else BasicTest2 will fail
    private static ThreadLocal<WebDriver> driver = new ThreadLocal<>();
    private static ThreadLocal<ActionDriver> actionDriver = new ThreadLocal<>();
    public static Logger logger;

    @BeforeSuite
    public void loadConfig() throws IOException {
        //Initialize properties
        prop = new Properties();
        FileInputStream fileInputStream = new FileInputStream("src/main/resources/config.properties");
        prop.load(fileInputStream);
        logger= LogManager.getLogger(BaseClass.class);
        logger.info("Properties file Loaded");
    }

    @BeforeMethod
    public void setup() throws IOException {
        logger.info("Webdriver setup for "+this.getClass().getSimpleName());
        launchBrowser();
        configBrowser();
        staticWait(5);
        logger.info("Setup completed..");
    }


    private void launchBrowser(){
        //Launch browser
        String browser = prop.getProperty("browser");

        if (browser.equalsIgnoreCase("chrome"))
        {
            driver.set(new ChromeDriver());
        }
        else
        {
            driver.set(new EdgeDriver());
        }

        setActionDriver(new ActionDriver(getDriver()));
        logger.info("Browser Launched..");
    }

    private void configBrowser(){
        //Set timeout and size
        int seconds = Integer.parseInt(prop.getProperty("implicit.wait"));
        getDriver().manage().timeouts().implicitlyWait(Duration.ofSeconds(seconds));
        getDriver().manage().window().maximize();
        try {
            getDriver().get(prop.getProperty("url"));
        }
        catch (Exception e)
        {
            System.out.println(e.getMessage());
        }
        getActionDriver().waitForPageLoad(5);
        logger.info("Browser configuration completed..");
    }

    @AfterMethod
    public void teardown(){
        if (getDriver()!=null)
        {
            try {
                getDriver().quit();
            }
            catch (Exception e)
            {
                System.out.println("Unable to quit driver: "+e.getMessage());
            }
        }
        logger.info("Teardown started");
        driver.remove();
        actionDriver.remove();
    }

    public void staticWait(int seconds){
        LockSupport.parkNanos(TimeUnit.SECONDS.toNanos(seconds));
    }

//    public static WebDriver getDriver(){
//        return driver;
//    }
//
//    public void setDriver(WebDriver webDriver){
//        this.driver = driver;
//    }

    public static ActionDriver getActionDriver(){
        return actionDriver.get();
    }

    public static void setActionDriver(ActionDriver actionDriver){
        BaseClass.actionDriver.set(actionDriver);
    }

    public static WebDriver getDriver(){
        return driver.get();
    }
    public static Properties getProp(){
        return prop;
    }


}
