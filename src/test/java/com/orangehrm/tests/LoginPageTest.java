package com.orangehrm.tests;

import com.orangehrm.base.BaseClass;
import com.orangehrm.pages.LoginPage;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class LoginPageTest extends BaseClass {

    private LoginPage loginPage;

    @BeforeMethod
    public void setupPage(){
        loginPage = new LoginPage();
    }

    @Test(priority = 1)
    public void verifyLoginTest(){
        loginPage.login(prop.getProperty("username"),prop.getProperty("password"));
    }

    @Test(priority = 2)
    public void verifyInvalidLoginTest(){
        boolean isInvalidLogin = loginPage.invalidLogin(prop.getProperty("username"),prop.getProperty("password")+"1");
        Assert.assertTrue(isInvalidLogin,"Error message validated");
    }

}
