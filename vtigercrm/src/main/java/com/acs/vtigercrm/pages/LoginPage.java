package com.acs.vtigercrm.pages;

import org.openqa.selenium.WebDriver;

import com.acs.vtigercrm.or.LoginPageOr;
import com.evs.vtiger.utilities.ConfigReader;
import com.evs.vtiger.utilities.WebDriverUtils;

public class LoginPage extends LoginPageOr {

	private WebDriverUtils util;
	private WebDriver driver;

	public LoginPage(WebDriver driver, WebDriverUtils util) {
		super(driver);
		this.util = util;
		this.driver = driver;
	}

	public void enterUserName(String userName) {
		util.sendKeysText(userNameEd, userName, "User Name");
	}

	public void enterPassword(String password) {
		util.sendKeysText(userPasswordEd, password, "Password");
	}

	public void clickLogin() {
		util.clickButton(loginBt, "Login Button");
	}

	public HomePage login(String userName, String userPass) {
		enterUserName(userName);
		enterPassword(userPass);
		clickLogin();
		return new HomePage(driver,util);
	}

	public String getErrorMessage() {
		return util.getInnerText(errorMsg, "error message");
	}
}
