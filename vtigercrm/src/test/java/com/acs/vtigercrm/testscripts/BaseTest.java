package com.acs.vtigercrm.testscripts;

import java.lang.reflect.Method;

import org.openqa.selenium.WebDriver;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;
import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.evs.vtiger.utilities.ConfigReader;
import com.evs.vtiger.utilities.ExcelUtils;
import com.evs.vtiger.utilities.WebDriverUtils;
import org.testng.ITestResult;

public class BaseTest {


	protected ExtentReports extent;
	protected ExtentTest extentTest;
    protected WebDriver driver;
    protected WebDriverUtils util;
    protected ConfigReader configReader;


    // ================================
    // 1. Initialize Report Once per Suite
    // ================================
    @BeforeSuite
    public void setupReport() {
    	 configReader= new ConfigReader();
    	 String reportPath =  configReader.getPropertyValue("reportPath");

         ExtentSparkReporter spark = new ExtentSparkReporter(reportPath);
         extent = new ExtentReports();
         extent.attachReporter(spark);
    }
  
    // ================================
    // 2. Launch Browser + Load Excel
    // ================================
    @BeforeClass
    public void launchBrowser() {
    	  // Launch driver using static method that doesn't depend on extentTest
       

        // Load test data
       // ExcelUtils.loadExcel(ConfigReader.getPropertyValue("testdataPath"), "Sheet1");

        // Open the application URL
        
    }

    // ================================
    // 3. Create a Test Node
    // ================================
    @BeforeMethod
    public void startTest(Method method) {
    	  extentTest = extent.createTest(method.getName());
    	  util = new WebDriverUtils(driver, extentTest); // this is the real util used by tests
          driver = util.launchBrowser(configReader.getPropertyValue("browser"));
          util.setDriver(driver);
          util.hitUrl(configReader.getPropertyValue("url"));
    	}

    // ================================
    // 4. Test Cleanup + Screenshot on Fail
    // ================================
    @AfterMethod
    public void afterEachTest(ITestResult result) {
    	 if (result.getStatus() == ITestResult.FAILURE) {
             util.takeSnapshot(result.getMethod().getMethodName());
             extentTest.fail(result.getThrowable());
         }

         driver.manage().deleteAllCookies();
     }
    // ================================
    // 5. Close Browser + Excel
    // ================================
    @AfterClass
    public void tearDownBrowser() {
        ExcelUtils.closeExcel();
        if (driver != null) {
            driver.quit();
        }
    }

    // ================================
    // 6. Flush Report
    // ================================
    @AfterSuite
    public void tearDownReport() {
        if (extent != null) {
            extent.flush();
        }
    }
}

