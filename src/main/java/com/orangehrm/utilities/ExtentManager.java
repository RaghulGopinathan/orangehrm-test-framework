package com.orangehrm.utilities;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class ExtentManager {
    private static ExtentReports extentReports;
    private static ThreadLocal<ExtentTest> extentTest = new ThreadLocal<>();

    private static ExtentSparkReporter extentSparkReporter;
    private static Map<Long, WebDriver> driverSet = new HashMap<>();

    //Intialize extent report
    public static ExtentReports getExtentReports(){
        if (extentReports==null) {
            extentReports = new ExtentReports();
            extentReports.setSystemInfo("Operating System",System.getProperty("os.name"));
            extentReports.setSystemInfo("Java version",System.getProperty("java.version"));
            extentReports.setSystemInfo("User",System.getProperty("user.name"));

            String recordPath = System.getProperty("user.dir") + "/src/test/resources/reports/ExtentReport.html";
            extentSparkReporter = new ExtentSparkReporter(recordPath);
            extentSparkReporter.config().setReportName("OrangeHRMReport");
            extentSparkReporter.config().setDocumentTitle("Test Report");
            extentSparkReporter.config().setTheme(Theme.STANDARD);
            extentReports.attachReporter(extentSparkReporter);
        }
        return extentReports;
    }

    public static ThreadLocal<ExtentTest> startTest(String testName){
        ExtentTest extentTestTemp = getExtentReports().createTest(testName);
        extentTest.set(extentTestTemp);
        return extentTest;
    }

    public static ExtentTest getTest(){
        return extentTest.get();
    }

    public static String getTestName(){
        if(getTest()!=null)
        {
            return getTest().getModel().getName();
        }
        else {
            return "No test is currently active";
        }

    }

    public static void logStep(String logMessage){
        getTest().info(logMessage);
    }

    public static void logPass(WebDriver driver,String logMessage, String screenShotMessage)
    {
        getTest().pass(logMessage);
        attachScreenshot(driver,screenShotMessage);
    }

    public static void logFail(WebDriver driver,String logMessage, String screenShotMessage){
        getTest().fail(logMessage);
        attachScreenshot(driver,screenShotMessage);
    }

    public static void logSkip(String logMessage){
        getTest().skip(logMessage);
    }

    public static String takeScreenshot(WebDriver driver,String screenshotName) throws IOException {
        TakesScreenshot ts = (TakesScreenshot) driver;
        File source = ts.getScreenshotAs(OutputType.FILE);

        //Format date and time
        String timestamp = new SimpleDateFormat("dd-mm-yyyy_hh-mm-ss").format(new Date());

        //Saving the screenshot to file
        String path = System.getProperty("user.dir")+"/src/test/resources/screenshots/"+screenshotName+"_"+timestamp+".png";

        File destinationPath = new File(path);

        try {
            FileUtils.copyFile(source,destinationPath);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        //convert ss to base64 for adding in report
        String base64Format = convertToBase64(source);
        return base64Format;


    }

    //Covert screenshot to base64
    public static String convertToBase64(File screenshotFile) throws IOException {
        String base64Format = "";

        byte[] fileContent = FileUtils.readFileToByteArray(screenshotFile);
        base64Format = Base64.getEncoder().encodeToString(fileContent);
        return base64Format;

    }
    public static void endTest(){
        getExtentReports().flush();
    }

    //Attach screenshot to report
    public static void attachScreenshot(WebDriver driver,String message) {
        try {
            String screenShotBase64 = takeScreenshot(driver,getTestName());
            getTest().info(message,com.aventstack.extentreports.MediaEntityBuilder.createScreenCaptureFromBase64String(screenShotBase64).build());
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

    }



    public static void registerDriver(WebDriver driver){
        driverSet.put(Thread.currentThread().threadId(),driver);
    }
}
