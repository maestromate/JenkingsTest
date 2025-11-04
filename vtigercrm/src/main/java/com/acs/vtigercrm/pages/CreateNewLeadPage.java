package com.acs.vtigercrm.pages;

import com.acs.vtigercrm.or.CreateNewLeadPageOr;
import com.acs.vtigercrm.utils.WebDriverUtils;
import com.aventstack.extentreports.Status;

public class CreateNewLeadPage extends CreateNewLeadPageOr {
	public CreateNewLeadPage(WebDriverUtils wuObject) {
		super(wuObject);
		wu = wuObject;
		wu.getExtTest().log(Status.INFO, "CreateNewLeadPage class constructor exicuted");

	}

 private	WebDriverUtils wu = null;

	public void createLeadByRequiredFieldOnly() {

		wu.SendKeysText(lastNameEd, "atul", "lastname");

		wu.SendKeysText(companyEd, "coding", "company name");

	}

	public void createLeadByRequiredFieldOnly2() {

		wu.SendKeysText(lastNameEd, "atu", "lastname");

		wu.SendKeysText(companyEd, "coding", "company name");

	}

	public void createLeadByRequiredFieldOnly3() {

		wu.SendKeysText(lastNameEd, "at", "lastname");

		wu.SendKeysText(companyEd, "coding", "company name");

	}
	
	
	public void createLeadByRequiredFieldOnly4() {

		wu.SendKeysText(lastNameEd, "a", "lastname");

		wu.SendKeysText(companyEd, "coding", "company name");

	}
	
	public void createLeadByAllField() {

	}

	public void creatLeadByFilledOnlyOneRequredField() {

	}

	public void clickOnSaveButton() {

		wu.clickButton(createLeadSaveButton, "createLeadSaveButton");

	}

}
