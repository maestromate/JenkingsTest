package com.acs.vtigercrm.testscripts;

import java.lang.reflect.Method;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import com.acs.vtigercrm.pages.LoginPage;
import com.acs.vtigercrm.utils.WebDriverUtils;
import com.aventstack.extentreports.Status;

public class LoginPageAutomationScripts extends CommonAutomationScripts {

	
	//////////////////////////////////   TestCase = 001   ///////////////////////////////////////

	
	  @Parameters()
	  
	  @Test public void varifyValidlogin() {
	  
	  LoginPage lp = new LoginPage(wu); lp.validLogin();}
	 






	



	//////////////////////////////////TestCase = 002   ///////////////////////////////////////


	public void varifyInvalidlogin() {






	}





}
