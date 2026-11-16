package com.orangehrm.pages;

import com.orangehrm.actiondriver.ActionDriver;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class LoginPage {

    //Drivers
    private WebDriver driver;
    private ActionDriver actionDriver;

    //Locators
    private By loginHeaderField = By.xpath("//h5[text()='Login']");
    private By usernameField = By.name("username");
    private By passwordField = By.name("password");
    private By loginBtnField = By.xpath("//button[@class='oxd-button oxd-button--medium oxd-button--main orangehrm-login-button']");
    private By errorMessageField = By.xpath("p[text()='Invalid credentials']");

    public LoginPage(){
        this.actionDriver=new ActionDriver(driver);
    }

    //Actions involved in this page
    public boolean header(String expectedText){
        System.out.println("Verifying the header field..");
        return actionDriver.compareText(loginHeaderField,expectedText);
    }

    public void login(String username, String password){
        actionDriver.enterText(usernameField,username);
        actionDriver.enterText(passwordField,password);
        actionDriver.click(loginBtnField);
    }

    public boolean invalidLogin(String username, String password){
        login(username,password);
        return actionDriver.compareText(errorMessageField,"Invalid credentials");
    }
}
