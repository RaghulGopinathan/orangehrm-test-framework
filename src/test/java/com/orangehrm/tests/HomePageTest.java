package com.orangehrm.tests;

import com.orangehrm.base.BaseClass;
import com.orangehrm.pages.HomePage;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class HomePageTest extends BaseClass {
    private HomePage homePage;

    @BeforeMethod
    public void setupPage(){
        homePage = new HomePage();
    }
    @Test (priority = 1)
    public void homePageHeaderTest(){
        Assert.assertTrue(homePage.homePageHeader(),"HomePage Header is not matching");
    }

    @Test (priority = 2)
    public void logoutTest(){
        Assert.assertTrue(homePage.logout(),"Couldn't Logout from the page");
    }
}
