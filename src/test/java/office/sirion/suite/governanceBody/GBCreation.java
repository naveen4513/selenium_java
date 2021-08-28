package office.sirion.suite.governanceBody;

import java.io.IOException;
import office.sirion.util.*;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.testng.Assert;
import org.testng.ITestResult;
import org.testng.SkipException;
import org.testng.annotations.*;

public class GBCreation extends TestSuiteBase {
	String result = null;
	String runmodes[] = null;
	static int count = -1;
	static boolean isTestPass = true;
	
	@BeforeTest
	public void checkTestSkip() {
		if (!TestUtil.isTestCaseRunnable(governance_body_suite_xls, this.getClass().getSimpleName())) {
			Logger.debug("Skipping Test Case" + this.getClass().getSimpleName() + " as runmode set to NO");
			throw new SkipException("Skipping Test Case" + this.getClass().getSimpleName() + " as runmode set to NO");
			}
		runmodes = TestUtil.getDataSetRunmodes(governance_body_suite_xls, this.getClass().getSimpleName());
		}
	
	@Test(dataProvider = "getTestData")
	public void GBCreationTest (String gbTitle, String gbDescription, String gbGoal, String gbGovernanceBodyType, String gbTimezone, String gbStartTime,
			String gbDuration, String gbSupplierAccess, String gbLocation, String gbFrequencyType, String gbFrequency, String gbWeekType, String gbStartDate,
			String gbEndDate, String gbIncludeStartDate, String gbIncludeEndDate, String gbPatternDate, String gbEffectiveDate, String gbRecipientClientEntity,
			String gbRecipientCompanyCode, String gbContractingClientEntity, String gbContractingCompanycode, String gbFunctions, String gbServices,
			String gbServiceCategory, String gbRegions, String gbCountries, String gbProjectID, String gbInitiatives, String gbProjectLevels,
			String gbComments, String gbActualDate, String gbRequestedBy, String gbChangeRequest, String gbUploadFile, String gbSupplier, String gbContracts
			) throws InterruptedException {
		
		count++;
		if (!runmodes[count].equalsIgnoreCase("Y"))
			throw new SkipException("Runmode for Test Data Set to NO -- " + count);
	
		

		openBrowser();
	    endUserLogin(CONFIG.getProperty("endUserURL"), CONFIG.getProperty("endUserUsername"), CONFIG.getProperty("endUserPassword"));
	    DatePicker_Enhanced date = new DatePicker_Enhanced();
		Logger.debug("Executing Test Case Governance Body Creation with Title -- " + gbTitle + " under Supplier -- " + gbSupplier);
		
	    driver.get(CONFIG.getProperty("endUserURL"));

		fluentWaitMethod(locateElementBy("gb_quick_link"));
	    
	    locateElementBy("gb_quick_link").click();

		fluentWaitMethod(driver.findElement(By.linkText("VIEW GOVERNANCE BODY")));

		driver.findElement(By.linkText("VIEW GOVERNANCE BODY")).click();

		fluentWaitMethod(locateElementBy("gb_listing_plus_icon_lonk"));

	    locateElementBy("gb_listing_plus_icon_lonk").click();

	    fluentWaitMethod(locateElementBy("gb_global_create_page_supplier_dropdown"));
	    
	    addFieldValue("gb_global_create_page_supplier_dropdown", gbSupplier);

	    addFieldValue("gb_global_create_page_contracts_multi_dropdown", gbContracts);

	    Assert.assertNotNull(driver.findElement(By.xpath("//button[contains(.,'Submit')]")));
	    ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", driver.findElement(By.xpath("//button[contains(.,'Submit')]")));
	    driver.findElement(By.xpath("//button[contains(.,'Submit')]")).click();

	    fluentWaitMethod(locateElementBy("gb_create_page_title_textbox"));

	    // Governance Body - Create Page - BASIC INFORMATION	    
	    addFieldValue("gb_create_page_title_textbox",gbTitle);

	    addFieldValue("gb_create_page_description_textarea",gbDescription);

	    addFieldValue("gb_create_page_goal_textarea",gbGoal);

	    addFieldValue("gb_create_page_gb_type_dropdown",gbGovernanceBodyType);
	    
	    addFieldValue("gb_create_page_timezone_dropdown",gbTimezone);
	    
	    if(gbTimezone.equalsIgnoreCase("")){
	    	new Select (driver.findElement(By.xpath("//*[@id='elem_4740']/select"))).selectByIndex(2);	
	    }
	    
	    try {
	    	if (driver.findElement(By.className("success-icon")).getText().contains("Current Date is different for the selected Time Zone"))
	    		driver.findElement(By.xpath(".//button[contains(.,'OK')]")).click();
	    	} catch (Exception e) {
	    		
	    	}    
	    addFieldValue("gb_create_page_start_time_dropdown",gbStartTime.split(" ")[0]);
    
	    addFieldValue("gb_create_page_duration_dropdown",gbDuration);

	    addFieldValue("gb_create_page_supplier_access_checkbox",gbSupplierAccess);
	    
	    addFieldValue("gb_create_page_location_textbox",gbLocation);
	    
	    // Governance Body - Create Page  - IMPORTANT DATES
	    addFieldValue("gb_create_page_frequency_type_dropdown",gbFrequencyType);

	    addFieldValue("gb_create_page_frequency_dropdown",gbFrequency);
	    
	    addFieldValue("gb_create_page_week_type_dropdown",gbWeekType);

	    addFieldValue("gb_create_page_start_date",gbStartDate);
	    
	    addFieldValue("gb_create_page_exp_date",gbEndDate);
	    
	    addFieldValue("gb_create_page_include_start_date_checkbox",gbIncludeStartDate);
	    
	    addFieldValue("gb_create_page_include_exp_date_checkbox",gbIncludeEndDate);

	    addFieldValue("gb_create_page_pattern_date",gbPatternDate);

	    addFieldValue("gb_create_page_effective_date",gbEffectiveDate);

	    
	    // Governance Body - Create Page  - CONTRACTING ENTITY
	    addFieldValue("gb_create_page_recipient_client_entities_multiselect",gbRecipientClientEntity);
	    
	    addFieldValue("gb_create_page_recipient_company_codes_multiselect",gbRecipientCompanyCode);
	    
	    addFieldValue("gb_create_page_contracting_client_entities_multiselect",gbContractingClientEntity);
	    
	    addFieldValue("gb_create_page_contracting_company_codes_multiselect",gbContractingCompanycode);
	    
		// Governance Body - Create Page - FUNCTIONS
	    if (!gbFunctions.equalsIgnoreCase("")) {
	    	if (locateElementBy("gb_create_page_functions_multi_dropdown").findElements(By.tagName("select")).size()!=0) {
	    		for (String entityData : gbFunctions.split(";")) {
	    			new Select(locateElementBy("gb_create_page_functions_multi_dropdown").findElement(By.tagName("select"))).selectByVisibleText(entityData.trim());
	    			}
	    	    if (!gbServices.equalsIgnoreCase("")) {
	    	    	if (locateElementBy("gb_create_page_services_multi_dropdown").findElements(By.tagName("select")).size()!=0) {
	    	    		for (String entityData : gbServices.split(";")) {
	    	    			new Select(locateElementBy("gb_create_page_services_multi_dropdown").findElement(By.tagName("select"))).selectByVisibleText(entityData.trim());
	    	    			}
	    	    		}
	    	    	}
	    		}
	    	}
		
	    if (!gbServiceCategory.equalsIgnoreCase("")) {
	    	if (locateElementBy("gb_create_page_service_category_multi_dropdown").findElements(By.tagName("select")).size()!=0) {
	    		for (String entityData : gbServiceCategory.split(";")) {
	    			new Select(locateElementBy("gb_create_page_service_category_multi_dropdown").findElement(By.tagName("select"))).selectByVisibleText(entityData.trim());
	    			}
	    		}
	    	}

	    // Governance Body - Create Page - GEOGRAPHY
	    if (!gbRegions.equalsIgnoreCase("")) {
	    	if (locateElementBy("gb_create_page_management_regions_multi_dropdown").findElements(By.tagName("select")).size()!=0) {
	    		for (String entityData : gbRegions.split(";")) {
	    			new Select(locateElementBy("gb_create_page_management_regions_multi_dropdown").findElement(By.tagName("select"))).selectByVisibleText(entityData.trim());
	    			}
	    	    if (!gbCountries.equalsIgnoreCase("")) {
	    	    	if (locateElementBy("gb_create_page_management_countries_multi_dropdown").findElements(By.tagName("select")).size()!=0) {
	    	    		for (String entityData : gbCountries.split(";")) {
	    	    			new Select(locateElementBy("gb_create_page_management_countries_multi_dropdown").findElement(By.tagName("select"))).selectByVisibleText(entityData.trim());
	    	    			}
	    	    		}
	    	    	}
	    		}
	    	}
 
		
		// Governance Body - Create Page - PROJECT INFORMATION
		addFieldValue("gb_create_page_project_id_multiselect", gbProjectID);

		addFieldValue("gb_create_page_initiatives_multiselect", gbInitiatives);

		addFieldValue("gb_create_page_project_levels_multiselect", gbProjectLevels);
		
		// Governance Body - Create Page - COMMENTS AND ATTACHMENTS
		addFieldValue("entity_create_page_comments_textarea", gbComments);
		    
		addFieldValue("entity_create_page_requested_by_dropdown", gbRequestedBy);

		date.selectCalendar(gbActualDate,"actualDate");
		    
		addFieldValue("entity_create_page_change_request_dropdown", gbChangeRequest);
		
		if (!gbUploadFile.equalsIgnoreCase("")) {
			locateElementBy("entity_create_page_upload_browse_button").sendKeys(System.getProperty("user.dir")+"\\file-upload\\GB\\" + gbUploadFile);

			}

		Assert.assertNotNull(driver.findElement(By.xpath("//button[contains(.,'Save GB')]")));
		((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", driver.findElement(By.xpath("//button[contains(.,'Save GB')]")));
		driver.findElement(By.xpath("//button[contains(.,'Save GB')]")).click();

		
		if (gbTitle.equalsIgnoreCase("")) {
			if(fieldValidationMandatory("gb_create_page_title_textbox")) {
				Logger.debug("Governance Body Title is Mandatory");
				return;
	        	}
			}
		
		if (gbTimezone.equalsIgnoreCase("")) {
			if(fieldValidationMandatory("wor_create_page_priority_dropdown")) {
				Logger.debug("WOR Priority is Mandatory");
				
				
				return;
	        	}
			}
		if (driver.findElements(By.className("success-icon")).size()!=0) {
			 String entityID = driver.findElement(By.className("success-icon")).findElement(By.id("hrefElemId")).getText();
				 
			 Logger.debug("GB created successfully with Entity ID " + entityID);
			 
			 driver.findElement(By.className("success-icon")).findElement(By.id("hrefElemId")).click();
			 fluentWaitMethod(locateElementBy("gb_show_page_id"));

				 
			 String entityShowPageID = locateElementBy("gb_show_page_id").getText();

			 Assert.assertEquals(entityShowPageID, entityID);
			 Logger.debug("GB ID on show page has been verified");
			 }	    
		
       driver.get(CONFIG.getProperty("endUserURL"));
	    }
	
	@AfterMethod
	public void reportDataSetResult(ITestResult testResult) throws IOException {
		takeScreenShotOnFailure(testResult.getStatus(), this.getClass().getSimpleName());
		
			if(testResult.getStatus()==ITestResult.SKIP)
				TestUtil.reportDataSetResult(governance_body_suite_xls, this.getClass().getSimpleName(), count+2, "SKIP");

			else if(testResult.getStatus()==ITestResult.FAILURE) {
				isTestPass=false;
				TestUtil.reportDataSetResult(governance_body_suite_xls, this.getClass().getSimpleName(), count+2, "FAIL");
				result= "Fail";
				}
			
			else if (testResult.getStatus()==ITestResult.SUCCESS) {
				TestUtil.reportDataSetResult(governance_body_suite_xls, this.getClass().getSimpleName(), count+2, "PASS");
				result= "Pass";
				}

		try {
			for (int i = 2; i <= governance_body_suite_xls.getRowCount(this.getClass().getSimpleName()); i++) {
				String testCaseID = governance_body_suite_xls.getCellData(this.getClass().getSimpleName(), "testCaseID", i);
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
			TestUtil.reportDataSetResult(governance_body_suite_xls, "Test Cases", TestUtil.getRowNum(governance_body_suite_xls, this.getClass().getSimpleName()),"PASS");
		else
			TestUtil.reportDataSetResult(governance_body_suite_xls, "Test Cases", TestUtil.getRowNum(governance_body_suite_xls, this.getClass().getSimpleName()),"FAIL");
		}

	@DataProvider
	public Object[][] getTestData() {
		return TestUtil.getData(governance_body_suite_xls, this.getClass().getSimpleName());
		}
	}
