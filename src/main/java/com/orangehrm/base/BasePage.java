package com.orangehrm.base;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Time;
import java.time.Duration;
import java.util.Properties;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.LockSupport;

public class BasePage {

    protected static Properties prop; //define this variable as static or else BasicTest2 will fail
    protected WebDriver driver;


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
            driver = new ChromeDriver();
        }
        else
        {
            driver = new EdgeDriver();
        }
    }

    private void configBrowser(){
        //Set timeout and size
        int seconds = Integer.parseInt(prop.getProperty("implicitwait"));
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(seconds));
        driver.manage().window().maximize();
        try {
            driver.get(prop.getProperty("url"));
        }
        catch (Exception e)
        {
            System.out.println(e.getMessage());
        }

    }

    @AfterMethod
    public void teardown(){
        if (driver!=null)
        {
            try {
                driver.quit();
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


}
