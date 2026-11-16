package com.orangehrm.tests;

import com.orangehrm.base.BaseClass;
import org.testng.Assert;
import org.testng.annotations.Test;

public class BasicTest extends BaseClass {
    @Test
    public void basicTest() {
        String title = driver.getTitle();
        Assert.assertEquals(title,"OrangeHRM");
    }
}
