package com.orangehrm.tests;

import com.orangehrm.actiondriver.ActionDriver;
import com.orangehrm.base.BasePage;
import org.openqa.selenium.By;
import org.testng.annotations.Test;

public class LoginTest extends BasePage {

    By userName = By.xpath("//input[@placeholder=\"Username\"]");
    By password = By.xpath("//input[@placeholder=\"Password\"]");
    By loginBtn = By.xpath("//button[@class=\"oxd-button oxd-button--medium oxd-button--main orangehrm-login-button\"]");

    @Test
    public void loginTest(){
        ActionDriver actionDriver = new ActionDriver();
        actionDriver.enterText(userName,prop.getProperty("username"));
        actionDriver.enterText(password,prop.getProperty("password"));
        actionDriver.click(loginBtn);
    }
}
