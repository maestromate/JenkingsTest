package com.acs.vtigercrm.pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.PageFactory;

import com.acs.vtigercrm.or.HomePageOr;
import com.aventstack.extentreports.Status;
import com.evs.vtiger.utilities.WebDriverUtils;

public class HomePage extends HomePageOr {
	WebDriverUtils util ;
	private WebDriver driver;

	public HomePage(WebDriver driver, WebDriverUtils util) {
		super(driver);
		this.util = util;
		this.driver=driver;
		PageFactory.initElements(util.getDriver(), this);
	}

	public String getHomePageTitle() {
		util.getPageTitle("Home Page");
		return null;
	}
	public void logout() {
		util.mouseOver(administrator);
//		System.out.println("successfully mouse over on administrator");
		util.getExtentTest().log(Status.INFO, "successfully mouse over on administrator");
		util.clickByActions(SignOut);
//		System.out.println("successfully click on sign out");
		util.getExtentTest().log(Status.INFO, "successfully click on sign out");
	}

	public void clickOnCreateLeadButton() {

		util.clickButton(CreateLeadButton, "CreateLead Button");

	}


}
