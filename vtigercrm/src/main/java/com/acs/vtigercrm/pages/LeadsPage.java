package com.acs.vtigercrm.pages;

import org.openqa.selenium.support.PageFactory;

import com.acs.vtigercrm.or.LeadsPageOr;
import com.acs.vtigercrm.utils.WebDriverUtils;

public class LeadsPage extends LeadsPageOr {

	
		
		public LeadsPage(WebDriverUtils wuObject) {
			super();
			 wu=wuObject;
				PageFactory.initElements(wu.getDriver(), this);

		}

		WebDriverUtils wu=null;
		
	
	}


