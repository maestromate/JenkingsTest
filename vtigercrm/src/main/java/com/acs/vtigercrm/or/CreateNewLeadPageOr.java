package com.acs.vtigercrm.or;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import com.evs.vtiger.utilities.WebDriverUtils;

public class CreateNewLeadPageOr  {

	public CreateNewLeadPageOr(WebDriverUtils wuObject) {
			PageFactory.initElements(wuObject.getDriver(), this);
	}
	
	@FindBy(xpath = "//input[@name='lastname']")
	protected WebElement lastNameEd;

	@FindBy(xpath = "//input[@name='company']")
	protected WebElement companyEd;
	
	@FindBy(xpath ="//input[@class='crmButton small save ']")
	protected WebElement createLeadSaveButton;
	
	
	
	
	
}
