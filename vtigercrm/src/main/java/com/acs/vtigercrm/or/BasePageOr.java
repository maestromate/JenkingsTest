package com.acs.vtigercrm.or;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import com.aventstack.extentreports.Status;
import com.evs.vtiger.utilities.WebDriverUtils;

public class BasePageOr {

    @FindBy(xpath = "//img[@title='Create Lead...']")
    protected WebElement CreateLeadButton;

    @FindBy(xpath = "//a[text()='Leads']")
    protected WebElement LeadsModule;

    @FindBy(xpath="//img[@src='themes/softed/images/user.PNG']")
    protected WebElement Administrator;

    @FindBy(linkText="Sign Out")
    protected WebElement SignOut;
}
