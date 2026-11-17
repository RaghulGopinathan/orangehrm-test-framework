package com.orangehrm.base;

import com.orangehrm.actiondriver.ActionDriver;
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
    private static ThreadLocal<WebDriver> tDriver = new ThreadLocal<>();
    private static ThreadLocal<ActionDriver> tActionDriver = new ThreadLocal<>();


    @BeforeSuite
    public void loadConfig() throws IOException {
        //Initialize properties
        prop = new Properties();
        FileInputStream fileInputStream = new FileInputStream("src/main/resources/config.properties");
        prop.load(fileInputStream);
    }

    @BeforeMethod
    public void setup() throws IOException {
        launchBrowser();
        configBrowser();
        staticWait(5);
    }

    private void launchBrowser(){
        //Launch browser
        String browser = prop.getProperty("browser");

        if (browser.equalsIgnoreCase("chrome"))
        {
            tDriver.set(new ChromeDriver());
        }
        else
        {
            tDriver.set(new EdgeDriver());
        }

        setActionDriver(new ActionDriver(getDriver()));
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
        return tActionDriver.get();
    }

    public static void setActionDriver(ActionDriver actionDriver){
        tActionDriver.set(actionDriver);
    }

    public static void setDriver(WebDriver driver){
        tDriver.set(driver);
    }

    public static WebDriver getDriver(){
        return tDriver.get();
    }
    public static Properties getProp(){
        return prop;
    }


}
