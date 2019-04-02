package com.ubiwhere.Ignite;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.testng.Assert;
import org.testng.annotations.Test;

public class ApplicationTest extends AbstractTestClass {


    @Autowired
    private ApplicationContext applicationContext;

    @Test
    public void isCustomSSLServerRunning() throws InterruptedException {
        Assert.assertNotNull(applicationContext, "App Failed to Start");
    }


}