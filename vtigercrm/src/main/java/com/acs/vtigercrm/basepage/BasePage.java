package com.acs.vtigercrm.basepage;

import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.InvalidSelectorException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import com.acs.vtigercrm.or.BasePageOr;
import com.acs.vtigercrm.utils.WebDriverUtils;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;

public class BasePage extends BasePageOr{
	  protected WebDriverUtils util;
	    protected WebDriver driver;
	    protected ExtentTest extTest;

	    public BasePage(WebDriverUtils util) {
	        this.util = util;
	        this.driver = util.getDriver();
	        this.extTest = util.getExtTest();
	        extTest.log(Status.INFO, "BasePage initialized successfully");

	    }

	public List<String> getAllElementsText(String xpath) {
	    List<String> elementsTextList = new ArrayList<>();

	    try {
	        if (xpath == null || xpath.trim().isEmpty()) {
	            throw new IllegalArgumentException("XPath cannot be null or empty");
	        }

	        List<WebElement> elements = driver.findElements(By.xpath(xpath));

	        if (elements == null || elements.isEmpty()) {
	            extTest.log(Status.WARNING, "No elements found for XPath: " + xpath);
	            return elementsTextList;
	        } else {
	            for (WebElement we : elements) {
	                elementsTextList.add(we.getText().trim());
	            }
	            extTest.log(Status.INFO, "Fetched " + elements.size() + 
	                    " elements' text for XPath: " + xpath + " → " + elementsTextList);
	        }

	    } catch (InvalidSelectorException e) {
	        extTest.log(Status.FAIL, "Invalid XPath syntax: " + xpath);
	        throw e;

	    } catch (Exception e) {
	        util.takeSnapshot("GetAllElementsTextError");
	        extTest.log(Status.FAIL, "Failed to fetch elements text for XPath: " + xpath + 
	                " | Exception: " + e.getMessage());
	        e.printStackTrace();
	        throw new RuntimeException("Error fetching text for: " + xpath, e);
	    }

	    return elementsTextList;
	}
	
	public void logout() {
		util.mouseOver(Administrator);
//		System.out.println("successfully mouse over on administrator");
		util.getExtTest().log(Status.INFO, "successfully mouse over on administrator");
		util.clickByActions(SignOut);
//		System.out.println("successfully click on sign out");
		util.getExtTest().log(Status.INFO, "successfully click on sign out");
	}

	public void clickOnCreateLeadButton() {

		util.clickButton(CreateLeadButton, "CreateLead Button");

	}

}
