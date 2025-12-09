package com.acs.vtigercrm.or;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class LoginPageOr {

	protected LoginPageOr(WebDriver driver) {
	PageFactory.initElements(driver, this); // initializes all @FindBy elements
	}
	// Define your web elements here

	@FindBy(xpath = "//input[@name='user_name']")
	protected WebElement userNameEd;

	@FindBy(xpath = "//input[@name='user_password']")
	protected WebElement userPasswordEd;

	@FindBy(xpath = "//input[@id='submitButton']")
	protected WebElement loginBt;
	
	@FindBy(xpath = "//div[@class='errorMessage']")
	protected WebElement errorMsg;
}
