package com.acs.vtigercrm.testscripts;

import org.testng.annotations.Test;

import com.acs.vtigercrm.pages.CreateNewLeadPage;
import com.acs.vtigercrm.pages.HomePage;
import com.aventstack.extentreports.Status;


public class LeadPageAutomationScripts extends CommonAutomationScripts {

	
//	================================== TestCase-1 ========================================
	
	@Test()
	public void testLeadCreationWithRequiredFieldsOnly() {

		HomePage hp = new HomePage(wu);
		hp.clickLeadsModule();
		hp.clickOnCreateLeadButton();

		CreateNewLeadPage createNewLeadButton = new CreateNewLeadPage(wu);
		createNewLeadButton.createLeadByRequiredFieldOnly();
		createNewLeadButton.createLeadByRequiredFieldOnly();
		createNewLeadButton.clickOnSaveButton();
//		System.out.println("testLeadCreationWithRequiredFieldsOnly TestCase exicute successfully");
		wu.getExtTest().log(Status.INFO,"testLeadCreationWithRequiredFieldsOnly TestCase exicute successfully");

	}

//	================================== TestCase-2 ========================================
	
	  @Test(priority=1) 
	  public void testLeadCreationWithRequiredFieldsOnlyTwo() {
	  
	  HomePage hp = new HomePage(wu); hp.clickLeadsModule();
	  hp.clickOnCreateLeadButton();
	  
	  CreateNewLeadPage createNewLeadButton = new CreateNewLeadPage(wu);
	  createNewLeadButton.createLeadByRequiredFieldOnly();
	  createNewLeadButton.clickOnSaveButton(); System.out.
	  println("testLeadCreationWithRequiredFieldsOnly2 TestCase exicute successfully"); 
	
	  } 
	  
	  
	  
	  // ================================== TestCase-3 ========================================
	  
	  @Test(priority=2) 
	  public void testLeadCreationWithRequiredFieldsOnlyThree() {
	  
	  HomePage hp = new HomePage(wu); hp.clickLeadsModule();
	  hp.clickOnCreateLeadButton();
	  
	  CreateNewLeadPage createNewLeadButton = new CreateNewLeadPage(wu);
	  createNewLeadButton.createLeadByRequiredFieldOnly();
	  createNewLeadButton.clickOnSaveButton();
	  System.out.println("testLeadCreationWithRequiredFieldsOnly3 = regression");
	  
	  } 
	 
	  
	  // ================================== TestCase-4 ========================================
	  
	  @Test(priority=3) 
	  public void testLeadCreationWithRequiredFieldsOnlyFour() {
	  
	  HomePage hp = new HomePage(wu); hp.clickLeadsModule();
	  hp.clickOnCreateLeadButton();
	  
	  CreateNewLeadPage createNewLeadButton = new CreateNewLeadPage(wu);
	  createNewLeadButton.createLeadByRequiredFieldOnly();
	  createNewLeadButton.clickOnSaveButton();
	  System.out.println("testLeadCreationWithRequiredFieldsOnly4 = smoke4"); 
	  
	  }
	
	
}

