  package com.acs.vtigercrm.testscripts;

import org.testng.Assert;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import com.acs.vtigercrm.pages.HomePage;
import com.acs.vtigercrm.pages.LoginPage;

public class LoginTests extends BaseTest {

	@Test(description = "Valid Login Test")
	public void verifyValidLogin() {
		LoginPage loginObj = new LoginPage(driver, util);
		HomePage hpObj =loginObj.login(configReader.getPropertyValue("userName"), configReader.getPropertyValue("userPass"));
	    String actualTitle = hpObj.getHomePageTitle();
	    Assert.assertEquals(actualTitle, "Administrator - Home - vtiger CRM 5 - Commercial Open Source CRM", "Title mismatch!");

	}

	@Test(description = "Invalid Login Test - Wrong Password")
	public void verifyInvalidLogin_WrongPassword() {
		LoginPage loginPage = new LoginPage(driver, util);
		loginPage.login(configReader.getPropertyValue("userName"), configReader.getPropertyValue("wrongPass"));
		String actualError = loginPage.getErrorMessage();
	    Assert.assertEquals(actualError, "You must specify a valid username and password.", "Error message mismatch!");

	}
	
	 @Test(description = "Invalid Login Test - Blank Username")
	    public void verifyInvalidLogin_BlankUsername() {
	        LoginPage login = new LoginPage(driver, util);
	        login.login("", configReader.getPropertyValue("validPassword"));

	        String error = login.getErrorMessage();
	        Assert.assertEquals(error, "Username is required", "Error message mismatch");
	    }
}
