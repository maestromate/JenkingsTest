package com.acs.vtigercrm.pages;

import com.acs.vtigercrm.or.LoginPageOr;
import com.acs.vtigercrm.utils.WebDriverUtils;
import com.aventstack.extentreports.Status;

public class LoginPage extends LoginPageOr {
//	WebUtil wu=null;
	public LoginPage(WebDriverUtils wuObject) {
		super(wuObject);
		wu = wuObject;
//		System.out.println("LoginPage class constructor exicuted");
		wu.getExtTest().log(Status.INFO, "LoginPage class constructor exicuted");

	}

	private WebDriverUtils wu;

	public void validLogin() {

		wu.SendKeysText(userNameEd, wu.getPropertyValue("username"), "user name box");

		wu.SendKeysText(userPasswordEd,wu.getPropertyValue("password") , "password");

		wu.clickButton(loginBt, "LoginButton");
	}

	public void invalidLogin() {
		wu.SendKeysText(userNameEd, wu.getPropertyValue("username"), "user name box");

		wu.SendKeysText(userPasswordEd, wu.getPropertyValue("password"), "password");

		wu.clickButton(loginBt, "LoginButton");
	}
	

}
