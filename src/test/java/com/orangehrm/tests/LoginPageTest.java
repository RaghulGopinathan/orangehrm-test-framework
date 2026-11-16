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

    @Test
    public void verifyLoginTest(){
        loginPage.login(prop.getProperty("username"),prop.getProperty("password"));
    }

    @Test
    public void verifyInvalidLoginTest(){
        boolean isInvalidLogin = loginPage.invalidLogin(prop.getProperty("username"),prop.getProperty("password"));
        Assert.assertTrue(isInvalidLogin,"Error message validated");
    }

}
