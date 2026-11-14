package com.orangehrm.base;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.testng.annotations.BeforeMethod;

import java.io.FileInputStream;
import java.io.IOException;
import java.time.Duration;
import java.util.Properties;

public class BasePage {

    protected Properties prop;
    protected WebDriver driver;

    @BeforeMethod
    public void setup() throws IOException {
        //Initialize properties
        prop = new Properties();
        FileInputStream fileInputStream = new FileInputStream("src/main/resources/config.properties");
        prop.load(fileInputStream);

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
        driver.get(prop.getProperty("url"));

        //Set timeout and size
        int seconds = Integer.parseInt(prop.getProperty("implicitwait"));
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(seconds));
        driver.manage().window().maximize();

    }
}
