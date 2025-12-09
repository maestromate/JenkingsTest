package com.acs.vtigercrm.pages;

import com.acs.vtigercrm.or.CreateNewLeadPageOr;
import com.aventstack.extentreports.Status;
import com.evs.vtiger.utilities.WebDriverUtils;

public class CreateNewLeadPage extends CreateNewLeadPageOr {
	WebDriverUtils wu;
	
	
	public CreateNewLeadPage(WebDriverUtils wuObject) {
		super(wuObject);
		this.wu = wuObject;
		wu.getExtentTest().log(Status.INFO, "CreateNewLeadPage class constructor exicuted");

	}


	public void createLeadByRequiredFieldOnly() {

		wu.sendKeysText(lastNameEd, "atul", "lastname");

		wu.sendKeysText(companyEd, "coding", "company name");

	}

	public void createLeadByRequiredFieldOnly2() {

		wu.sendKeysText(lastNameEd, "atu", "lastname");

		wu.sendKeysText(companyEd, "coding", "company name");

	}

	public void createLeadByRequiredFieldOnly3() {

		wu.sendKeysText(lastNameEd, "at", "lastname");

		wu.sendKeysText(companyEd, "coding", "company name");

	}
	
	
	public void createLeadByRequiredFieldOnly4() {

		wu.sendKeysText(lastNameEd, "a", "lastname");

		wu.sendKeysText(companyEd, "coding", "company name");

	}
	
	public void createLeadByAllField() {

	}

	public void creatLeadByFilledOnlyOneRequredField() {

	}

	public void clickOnSaveButton() {

		wu.clickButton(createLeadSaveButton, "createLeadSaveButton");

	}

}
