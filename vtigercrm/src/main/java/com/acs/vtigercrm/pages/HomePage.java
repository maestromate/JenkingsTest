package com.acs.vtigercrm.pages;

import org.openqa.selenium.support.PageFactory;

import com.acs.vtigercrm.or.HomePageOr;
import com.acs.vtigercrm.utils.WebDriverUtils;
import com.aventstack.extentreports.Status;

public class HomePage extends HomePageOr {
	public HomePage(WebDriverUtils wuObject) {

		super(wuObject);
		wu = wuObject;
		PageFactory.initElements(wu.getDriver(), this);
//		System.out.println("home page constructor 2");
	    // new commit to check jenkins update 2
		wu.getExtTest().log(Status.INFO, "HomePage class constructor exicuted");
wu.getExtTest().log(Status.INFO, "HomePage class constructor exicuted");

	}

	WebDriverUtils wu ;

}



