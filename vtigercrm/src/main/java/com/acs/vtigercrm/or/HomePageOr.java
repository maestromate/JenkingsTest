package com.acs.vtigercrm.or;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;


public class HomePageOr{
	
public HomePageOr(WebDriver driver) {
	PageFactory.initElements(driver, this);
}
	
	@FindBy(id="")
	protected WebElement administrator;
	
	@FindBy()
	protected WebElement SignOut;
	
	@FindBy()
	protected WebElement CreateLeadButton;
}
