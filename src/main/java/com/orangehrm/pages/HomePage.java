package com.orangehrm.pages;

import com.orangehrm.actiondriver.ActionDriver;
import com.orangehrm.base.BaseClass;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class HomePage {

    //Drivers
    private WebDriver driver;
    private ActionDriver actionDriver;
    private LoginPage loginPage;

    //Locators
    private By dashboardHeader = By.xpath("//h6[text()='Dashboard']");
    private By profile = By.className("oxd-userdropdown-tab");
    private By logoutBtn = By.xpath("//a[text()='Logout']");

    //Constructor
    public HomePage(){
        driver = BaseClass.getDriver();
        actionDriver=BaseClass.getActionDriver();
        loginPage = new LoginPage();
    }

    //Action methods
    public boolean homePageHeader(){
        loginPage.login("admin","admin123");
        return actionDriver.isDisplayed(dashboardHeader);
    }

    public boolean logout(){
        try {
            loginPage.login("admin","admin123");
            actionDriver.click(profile);
            actionDriver.click(logoutBtn);
            return true;
        }
        catch (Exception e)
        {
            System.out.println("Unable to logout: "+e.getMessage());
            return false;
        }
    }
}
