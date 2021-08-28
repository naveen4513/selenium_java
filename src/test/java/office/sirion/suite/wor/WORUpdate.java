package office.sirion.suite.wor;

import office.sirion.util.DatePicker_Enhanced;
import office.sirion.util.TestUtil;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.testng.Assert;
import org.testng.ITestResult;
import org.testng.SkipException;
import org.testng.annotations.*;
import java.io.IOException;

public class WORUpdate extends TestSuiteBase {
    String result = null;
	String runmodes[] = null;
	static int count = -1;
	static boolean fail = false;
	static boolean skip = false;
	static boolean isTestPass = true;

	@BeforeTest
	public void checkTestSkip() {
		if (!TestUtil.isTestCaseRunnable(wor_suite_xls, this.getClass().getSimpleName())) {
			Logger.debug("Skipping Test Case " + this.getClass().getSimpleName() + " as runmode set to NO");
			throw new SkipException("Skipping Test Case " + this.getClass().getSimpleName() + " as runmode set to NO");
			}
		runmodes = TestUtil.getDataSetRunmodes(wor_suite_xls, this.getClass().getSimpleName());
		}

	@Test(dataProvider = "getTestData")
	public void WORUpdateTest(String worName, String worTitle, String worBrief, String worPriority, String worType, String worBillingType,
			String worResponsibility, String worCurrency, String worContractingEntity, String worDeliveryCountry, String worTimezone, String worTier, 
			String worRecipientClientEntity, String worRecipientCompanyCode, String worContractingClientEntity, 
			String worContractingCompanyCode, String worEffectiveDate, String worExpirationDate, String worRequestDate, 
			String worPlannedCompletionDate, String worFunction, String worService, String worServiceCategory, String worManagementRegions,
			String worManagementCountries, String worContractRegions, String worContractCountries, String worAdditionalTCV, String worAdditionalACV,
			String worAdditionalFACV, String worProjectLevel, String worInitiative, String worProjectID,
			String worComments, String worRequestedBy, String worChangeRequest, String worUploadFile, String worParent)
			throws InterruptedException {
		
		count++;
		if (!runmodes[count].equalsIgnoreCase("Y"))
			throw new SkipException("Runmode for Test Data set to NO "+ count);


		openBrowser();
	    endUserLogin(CONFIG.getProperty("endUserURL"), CONFIG.getProperty("endUserUsername"), CONFIG.getProperty("endUserPassword"));
	    
	    Logger.debug("Executing Test Case WOR Update with Name ---- " + worName+ " under parent "+worParent);

        driver.get(CONFIG.getProperty("endUserURL"));
		
		fluentWaitMethod(locateElementBy("wor_quick_link"));

	    
	    locateElementBy("wor_quick_link").click();

		fluentWaitMethod(locateElementBy("entity_listing_page_display_dropdown_link"));
		
		new Select(locateElementBy("entity_listing_page_display_dropdown_link")).selectByVisibleText("100");

		
		if (!worParent.equalsIgnoreCase("")) {
			driver.findElement(By.xpath(".//*[@id='cr']/tbody/tr[@role='row']/td[contains(.,'" + worParent + "')]/preceding-sibling::td[1]/a")).click();
			} else {
				driver.findElement(By.xpath("//html//div[@id='dm']//tr[1]/td[1]/a")).click();
				}


        try{
            driver.findElement(By.xpath("//button[contains(.,'Restore')]")).click();

            fluentWaitMethod(driver.findElement(By.xpath("//button[@type='button'][contains(text(),'Edit')]")));
            Assert.assertNotNull(driver.findElement(By.xpath("//button[@type='button'][contains(text(),'Edit')]")));
            ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", driver.findElement(By.xpath("//button[@type='button'][contains(text(),'Edit')]")));
            driver.findElement(By.xpath("//button[@type='button'][contains(text(),'Edit')]")).click();

            driver.navigate().refresh();
            fluentWaitMethod(driver.findElement(By.xpath("//span[@id='elem_901']")));
        }catch (NoSuchElementException e){
            driver.navigate().refresh();

            fluentWaitMethod(driver.findElement(By.xpath("//button[@type='button'][contains(text(),'Edit')]")));
            Assert.assertNotNull(driver.findElement(By.xpath("//button[@type='button'][contains(text(),'Edit')]")));
            ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", driver.findElement(By.xpath("//button[@type='button'][contains(text(),'Edit')]")));
            driver.findElement(By.xpath("//button[@type='button'][contains(text(),'Edit')]")).click();

            driver.navigate().refresh();
            fluentWaitMethod(driver.findElement(By.xpath("//span[@id='elem_901']")));
        }



    	// WOR - Create Page - BASIC INFORMATION		
		addFieldValue("wor_create_page_name_textbox", worName);
	    
		addFieldValue("wor_create_page_title_textbox", worTitle);
		
		addFieldValue("wor_create_page_brief_textarea", worBrief);
		
		addFieldValue("wor_create_page_priority_dropdown", worPriority);
		
		addFieldValue("wor_create_page_type_multiselect", worType);
		
		addFieldValue("wor_create_page_billing_type_multiselect", worBillingType);
		
		addFieldValue("wor_create_page_responsibility_dropdown", worResponsibility);

		addFieldValue("wor_create_page_currency_dropdown", worCurrency);
		
		addFieldValue("wor_create_page_contracting_entity_dropdown", worContractingEntity);
		
		addFieldValue("wor_create_page_delivery_countries_dropdown", worDeliveryCountry);
		
		addFieldValue("wor_create_page_timezone_dropdown", worTimezone);
		
		addFieldValue("wor_create_page_tier_dropdown", worTier);
		
		// WOR - Create Page - CONTRACTING ENTITY		
		addFieldValue("wor_create_page_recipient_client_entities_multiselect", worRecipientClientEntity);
		
		addFieldValue("wor_create_page_recipient_company_codes_multiselect", worRecipientCompanyCode);
		
		addFieldValue("wor_create_page_contracting_client_entities_multiselect", worContractingClientEntity);
		
		addFieldValue("wor_create_page_contracting_company_codes_multiselect", worContractingCompanyCode);
		
		// WOR - Create Page - IMPORTANT DATES
		DatePicker_Enhanced date = new DatePicker_Enhanced();
		date.selectCalendar(worEffectiveDate,"effectiveDate");
		date.selectCalendar(worExpirationDate,"expirationDate");
		date.selectCalendar(worPlannedCompletionDate,"plannedCompletionDate");
		date.selectCalendar(worRequestDate,"requestDate");
		
		// WOR - Create Page - FUNCTIONS
		if (!worFunction.equalsIgnoreCase("")) {
			for (String entityData : worFunction.split(";")) {
				new Select(locateElementBy("wor_create_page_function_multiselect")).selectByVisibleText(entityData.trim());
			  	}
			addFieldValue("wor_create_page_service_multiselect", worService);
			}
		
		addFieldValue("wor_create_page_service_category_multiselect", worServiceCategory);

		// WOR - Create Page - GEOGRAPHY
		if (locateElementBy("wor_create_page_management_regions_multi_dropdown")!=null) {
			if (!worManagementRegions.equalsIgnoreCase("")) {
				for (String entityData : worManagementRegions.split(";")) {
					new Select(locateElementBy("wor_create_page_management_regions_multi_dropdown")).selectByVisibleText(entityData.trim());
					}
				addFieldValue("wor_create_page_management_countries_multi_dropdown", worManagementCountries);
				}
			}
		
		if (locateElementBy("wor_create_page_contract_regions_multi_dropdown")!=null) {
			if (!worContractRegions.equalsIgnoreCase("")) {
				for (String entityData : worContractRegions.split(";")) {
					new Select(locateElementBy("wor_create_page_contract_regions_multi_dropdown")).selectByVisibleText(entityData.trim());
					}
			    addFieldValue("wor_create_page_contract_countries_multi_dropdown", worContractCountries);
				}
			}
		
		// WOR - Create Page - FINANCIAL INFORMATION
		addFieldValue("wor_create_page_additional_TCV_textbox", worAdditionalTCV);
		
		addFieldValue("wor_create_page_additional_ACV_textbox", worAdditionalACV);
		
		addFieldValue("wor_create_page_additional_FACV_textbox", worAdditionalFACV);
		
		// WOR - Create Page - PROJECT INFORMATION
		addFieldValue("wor_create_page_project_levels_multiselect", worProjectLevel);
		
		addFieldValue("wor_create_page_initiatives_multiselect", worInitiative);
		
		addFieldValue("wor_create_page_project_id_multiselect", worProjectID);

		// WOR - Create Page - Create Page - COMMENTS AND ATTACHMENTS
		addFieldValue("entity_create_page_comments_textarea", worComments);
		    
		addFieldValue("entity_create_page_requested_by_dropdown", worRequestedBy);
		    
		addFieldValue("entity_create_page_change_request_dropdown", worChangeRequest);
		
		if (!worUploadFile.equalsIgnoreCase("")) {
			locateElementBy("entity_create_page_upload_browse_button").sendKeys(System.getProperty("user.dir")+"\\file-upload\\WOR\\" + worUploadFile);

			}
		
		 Assert.assertNotNull(driver.findElement(By.xpath("//button[contains(.,'Update')]")));
		    ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", driver.findElement(By.xpath("//button[contains(.,'Update')]")));
		    driver.findElement(By.xpath("//button[contains(.,'Update')]")).click();

		    
		    if (worTitle.equalsIgnoreCase("")) {
				if(fieldValidationMandatory("wor_create_page_title_textbox")) {
					Logger.debug("WOR Title is Mandatory");
					
					
					return;
		        	}
				}
			
			if (worName.equalsIgnoreCase("")) {
				if(fieldValidationMandatory("wor_create_page_name_textbox")) {
					Logger.debug("WOR Name is Mandatory");
					
					
					return;
		        	}
				}
				
			
			if (worPriority.equalsIgnoreCase("")) {
				if(fieldValidationMandatory("wor_create_page_priority_dropdown")) {
					Logger.debug("WOR Priority is Mandatory");
					
					
					return;
		        	}
				}
			
			if (worExpirationDate.equalsIgnoreCase("")) {
				if(fieldValidationMandatory("wor_create_page_expiration_date")) {
					Logger.debug("WOR Expiration Date is Mandatory");
					
					
					return;
		        	}
				}
			
			if (worEffectiveDate.equalsIgnoreCase("")) {
				if(fieldValidationMandatory("wor_create_page_effective_date")) {
					Logger.debug("WOR Effective Date is Mandatory");
					
					
					return;
		        	}
				}
				if (!worAdditionalACV.equalsIgnoreCase("")) {
					if (worAdditionalACV.split("\\.")[1].length() > 2) {
						String entityFieldError = locateElementBy("wor_create_page_additional_ACV_textbox").findElement(By.tagName("li")).getText();
						Assert.assertEquals(entityFieldError, "Only two decimal values are allowed");

						Logger.debug("WOR Additional ACV has more than 2 Decimals");

						return;
					}
				}
			if (!worAdditionalFACV.equalsIgnoreCase("")) {
				if (worAdditionalFACV.split("\\.")[1].length() > 2) {
					String entityFieldError = locateElementBy("wor_show_page_additional_facv_textbox").findElement(By.tagName("li")).getText();
					Assert.assertEquals(entityFieldError, "Only two decimal values are allowed");

					Logger.debug("WOR Additional FACV has more than 2 Decimals");

					return;
				}
			}
			if (!worAdditionalTCV.equalsIgnoreCase("")) {
				if (worAdditionalTCV.split("\\.")[1].length() > 2) {
					String entityFieldError = locateElementBy("wor_show_page_additional_tcv_textbox").findElement(By.tagName("li")).getText();
					Assert.assertEquals(entityFieldError, "Only two decimal values are allowed");

					Logger.debug("WOR Additional TCV has more than 2 Decimals");

					return;
				}
			}
			String entityShowPageName = locateElementBy("wor_show_page_name").getText();

			Assert.assertEquals(entityShowPageName, worName);
			Logger.debug("WOR name on show page has been verified");
			

			fail = false;
			
	       /* if (!testCaseID.equalsIgnoreCase("")) {
				if (!fail)
			        result= "Pass";
			      else   
			         result= "Fail";
			     TestlinkIntegration.updateTestLinkResult(testCaseID,"",result);
			     }*/

			driver.get(CONFIG.getProperty("endUserURL"));
			 }

	 @AfterMethod
		public void reportDataSetResult(ITestResult testResult) throws IOException {
			takeScreenShotOnFailure(testResult.getStatus(), this.getClass().getSimpleName());

			if(testResult.getStatus()==ITestResult.SKIP)
				TestUtil.reportDataSetResult(wor_suite_xls, this.getClass().getSimpleName(), count+2, "SKIP");
			else if(testResult.getStatus()==ITestResult.FAILURE) {
				isTestPass=false;
				TestUtil.reportDataSetResult(wor_suite_xls, this.getClass().getSimpleName(), count+2, "FAIL");
				result= "Fail";
			}
			else if (testResult.getStatus()==ITestResult.SUCCESS) {
				TestUtil.reportDataSetResult(wor_suite_xls, this.getClass().getSimpleName(), count+2, "PASS");
				result= "Pass";
			}
			try {
				for (int i = 2; i <= wor_suite_xls.getRowCount(this.getClass().getSimpleName()); i++) {
					String testCaseID = wor_suite_xls.getCellData(this.getClass().getSimpleName(), "testCaseID", i);
					if (!testCaseID.equalsIgnoreCase("")) {
						updateRun(convertStringToInteger(testCaseID), new java.lang.Exception().toString(), result);
					}
				}
			}catch(Exception e){
				Logger.debug(e);
			}
		}
	

	@AfterTest
	public void reportTestResult() {
		if (isTestPass)
			TestUtil.reportDataSetResult(wor_suite_xls, "Test Cases", TestUtil
					.getRowNum(wor_suite_xls, this.getClass().getSimpleName()),
					"PASS");
		else
			TestUtil.reportDataSetResult(wor_suite_xls, "Test Cases", TestUtil
					.getRowNum(wor_suite_xls, this.getClass().getSimpleName()),
					"FAIL");

	}

	@DataProvider
	public Object[][] getTestData() {
		return TestUtil.getData(wor_suite_xls, this.getClass().getSimpleName());
	}
}
