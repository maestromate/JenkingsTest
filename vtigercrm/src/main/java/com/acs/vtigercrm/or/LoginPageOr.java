package com.acs.vtigercrm.or;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import com.acs.vtigercrm.basepage.BasePage;
import com.acs.vtigercrm.utils.WebDriverUtils;

public class LoginPageOr extends BasePage {
	 protected LoginPageOr(WebDriverUtils wuObject) {
		 super(wuObject);
			PageFactory.initElements(wuObject.getDriver(), this);

	}

	@FindBy(xpath = "//input[@name='user_name']")
	protected WebElement userNameEd;

	@FindBy(xpath = "//input[@name='user_password']")
	protected WebElement userPasswordEd;

	@FindBy(xpath = "//input[@id='submitButton']")
	protected WebElement loginBt;

}
