package com.orangehrm.tests;

import com.orangehrm.base.BasePage;
import org.testng.Assert;
import org.testng.annotations.Test;

public class BasicTest extends BasePage{
    @Test
    public void basicTest() {
        String title = driver.getTitle();
        Assert.assertEquals(title,"OrangeHRM");
    }
}
