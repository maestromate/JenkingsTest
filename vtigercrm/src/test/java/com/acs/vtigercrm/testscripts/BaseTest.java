package com.acs.vtigercrm.testscripts;

import java.lang.reflect.Method;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.BeforeTest;

import com.acs.vtigercrm.pages.LoginPage;
import com.acs.vtigercrm.utils.WebDriverUtils;
import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;

public class BaseTest {

	protected WebDriverUtils util;
	protected WebDriver driver;
	protected ExtentTest ext;
	protected ExtentReports extents;
	protected LoginPage loginPage;

    @BeforeSuite
    public void setupReport() {
        // Initialize Extent Reports or Allure
    	extents =new ExtentReports();
    	ExtentSparkReporter spark =new ExtentSparkReporter("reports/index.html");
    	extents.attachReporter(spark);
    }

    @BeforeClass
    public void launchBrowser() {
       driver =new ChromeDriver();
       driver.manage().window().maximize();
       new WebDriverUtils(driver, null);// extTest not yet created
       driver.get("https://your-app-url.com");
    }
    

    @BeforeMethod
    public void createExtentTest(Method method) {
    	  // Dynamically create test entry in ExtentReport
        util.createExtentTest(ext,method.getName(),
            "Executing test case: " + method.getName(),
            "Regression" // you can add more categories here
        );
    }

    @AfterMethod
    public void logout() {
        // logout if needed or clean cookies
    }

    @AfterClass
    public void closeBrowser() {
        util.quitBrowser();
    }

    @AfterSuite
    public void tearDownReport() {
        // close report, flush logs
    	util.
    }
}
