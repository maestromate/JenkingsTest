package com.acs.vtigercrm.testscripts;

import java.lang.reflect.Method;

import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Parameters;

import com.acs.vtigercrm.pages.HomePage;
import com.acs.vtigercrm.pages.LoginPage;
import com.acs.vtigercrm.utils.WebDriverUtils;
import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;


public class CommonAutomationScripts {


	protected WebDriverUtils wu;

	protected ExtentReports ext;

	///////////////////////////////////   before suite  //////////////////////////////////////
	@BeforeSuite
	public void beforeSuite(){

		ext = new ExtentReports();
		ExtentSparkReporter esr =new ExtentSparkReporter("extentReport.html");
		ext.attachReporter(esr);
		System.out.println("before suite method run");
	}



	///////////////////////////////////   before class  //////////////////////////////////////

	/*
	 * @Parameters("browserName")
	 * 
	 * @BeforeClass public void beforeTestClass(String browserName) { wu = new
	 * WebUtil();
	 * 
	 * wu.launchBrowser(browserName); System.out.println(browserName +
	 * "is launched by before class method ");
	 * 
	 * }
	 */

	///////////////////////////////////   before method  //////////////////////////////////////

	
	@BeforeMethod
	public void beforeTestCase(Method methodName) {

		wu = new WebDriverUtils();

		String tcname=methodName.getName();
		wu.createExtentTest(ext, tcname);

		wu.launchBrowser(wu.getPropertyValue("browserC"));

		wu.hitUrl(wu.getPropertyValue("url"));
		LoginPage lp = new LoginPage(wu);
		lp.validLogin();

		//		System.out.println("url hit and valid login successfully by before method ");
		wu.getExtTest().log(Status.INFO, "url hit and valid login successfully by before method ");

	}


	///////////////////////////////////   after method  //////////////////////////////////////

	@AfterMethod
	public void afterTestCase(ITestResult itr, Method methodName) {
		
		if(!itr.isSuccess()) {
			String userdir=System.getProperty("user.dir");
//			String screenshootpath=userdir+"\\Reports\\" + methodName.getName()+ ".png";
			
			String base64=wu.screenshotFullPage();
			
			wu.getExtTest().addScreenCaptureFromBase64String(base64,methodName.getName());
//			wu.getExtTest().addScreenCaptureFromPath(screenshootpath);
		}
		
		
		
		
		HomePage hp=new HomePage(wu);
		hp.logout();
		//		System.out.println("logout successfully by after method ");
		wu.getExtTest().log(Status.INFO,"logout successfully by after method ");

		//		ext.flush();

		wu.quitBrowser();
		//		System.out.println("closed all browser by after class method");
		wu.getExtTest().log(Status.INFO, "closed all browser by after Method");
		ext.flush();

	}


	///////////////////////////////////   before class  //////////////////////////////////////

	/*
	 * @AfterClass public void afterTestClass() {
	 * 
	 * wu.closeAllWindow();
	 * System.out.println("closed all browser by after class method");
	 * wu.getExtTest().log(Status.INFO,
	 * "url hit and valid login successfully by before method ");
	 * 
	 * 
	 * }
	 */

}
