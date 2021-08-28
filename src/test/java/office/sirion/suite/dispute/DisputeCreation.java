package office.sirion.suite.dispute;

import java.io.IOException;
import java.util.List;
import office.sirion.util.DatePicker_Enhanced;
import office.sirion.util.TestUtil;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.testng.Assert;
import org.testng.ITestResult;
import org.testng.SkipException;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

public class DisputeCreation extends TestSuiteBase {
	String testCaseID;
	String result = null;
	String runmodes[] = null;
	static int count = -1;
	static boolean fail = false;
	static boolean skip = false;
	static boolean isTestPass = true;

	@BeforeTest
	public void checkTestSkip() {
		if (!TestUtil.isTestCaseRunnable(dispute_suite_xls, this.getClass().getSimpleName())) {
			Logger.debug("Skipping Test Case "+ this.getClass().getSimpleName() + " as runmode set to NO");
			throw new SkipException("Skipping Test Case "+ this.getClass().getSimpleName() + " as runmode set to NO");
			}
		runmodes = TestUtil.getDataSetRunmodes(dispute_suite_xls, this.getClass().getSimpleName());
		}
	
	@Test(dataProvider = "getTestData")
	public void DisputeCreationTest (String disputeTitle, String disputeDescription, String disputeType, String disputePriority, String disputeMilestone,
			String disputeCurrency, String disputeFinancialImpact, String disputeIdentifiedClaimValue, String disputePotentialClaimValue, String disputeValueClaimed,
			String disputeValueSettled, String disputeValueRealised, String disputeExpectedClaimValue,String disputeRealisationAmount,String disputeSavingValue,
            String disputeVFadditionalexpensesincurredbytheclaim,String disputeProcessAreaImpacted, String disputeActionTaken, String disputeResolutionRemarks,
			String disputeValueofotherdamagesincurredfromthisclaim, String disputeDeliveryCountries, String disputeTimezone, String disputeRestrictPublicAccess,
			String disputeRestrictRequesterAccess, String disputeDependentEntity, String disputeSupplierAccess, String disputeTier, String disputeDate,
			String disputePlannedcompletionDate, String disputeExternalClientEntity, String disputeExternalCompanyCode, String disputeContarctingClientEntity,
			String disputeContractingCompanyCode, String disputeFunctions, String disputeServices, String disputeManagementRegions, String disputeManagementCountries,
			String disputeContractRegions, String disputeContractCountries, String disputeProjectID, String disputeProjectLevels, String disputeInitiatives,
			String disputecomment, String disputeRequestedby, String disputeChangeRequest, String disputeActualdate, String disputeUploadFiles, String disputeSupplier,
            String disputeSourceType, String disputeSourceName)throws InterruptedException {

		count++;
		if (!runmodes[count].equalsIgnoreCase("Y")) {
			skip = true;
			throw new SkipException("Runmode for test set data set to no "+ count);
		}

		openBrowser();
	    endUserLogin(CONFIG.getProperty("endUserURL"), CONFIG.getProperty("endUserUsername"), CONFIG.getProperty("endUserPassword"));
	    Logger.debug("Executing Test Case Dispute Creation with Title --- " + disputeTitle);
	    driver.get(CONFIG.getProperty("endUserURL"));
		fluentWaitMethod(locateElementBy("disputes_quick_link"));

		locateElementBy("disputes_quick_link").click();
		fluentWaitMethod(locateElementBy("dispute_entity_listing_page_plus_button"));
	    
		locateElementBy("dispute_entity_listing_page_plus_button").click();

		addFieldValue("entity_global_create_page_supplier_dropdown", disputeSupplier.trim());

		addFieldValue("entity_global_create_page_source_type_dropdown", disputeSourceType.trim());
	    
		addFieldValue("entity_global_create_page_source_name_title_dropdown", disputeSourceName.trim());

		Assert.assertNotNull(driver.findElement(By.xpath("//button[contains(.,'Submit')]")));
		((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", driver.findElement(By.xpath("//button[contains(.,'Submit')]")));
		driver.findElement(By.xpath("//button[contains(.,'Submit')]")).click();
		fluentWaitMethod(locateElementBy("dispute_create_page_title_textbox"));
	    
		// DISPUTE - CREATE PAGE - BASIC INFORMATION
		addFieldValue("dispute_create_page_title_textbox", disputeTitle);
		addFieldValue("dispute_create_page_description_textarea", disputeDescription);
		addFieldValue("dispute_create_page_dispute_type_dropdown", disputeType);
		addFieldValue("dispute_create_page_priority_dropdown", disputePriority);
		addFieldValue("dispute_create_page_milestone_dropdown", disputeMilestone);
		addFieldValue("dispute_create_page_currency_dropdown", disputeCurrency);
		addFieldValue("dispute_create_page_financial_impact_numeric_box", disputeFinancialImpact);
		addFieldValue("dispute_create_page_indentified_claim_value_textbox", disputeIdentifiedClaimValue);
		addFieldValue("dispute_create_page_potential_claim_value_textbox", disputePotentialClaimValue);
		addFieldValue("dispute_create_page_value_claimed_textbox", disputeValueClaimed);
		addFieldValue("dispute_create_page_value_settled_textbox", disputeValueSettled);
		addFieldValue("dispute_create_page_value_relaised_textbox", disputeValueRealised);
        addFieldValue("dispute_create_page_expected_cliam_value", disputeExpectedClaimValue);
        addFieldValue("dispute_create_page_realisation_amount", disputeRealisationAmount);
        addFieldValue("dispute_create_page_savings_value", disputeSavingValue);
		addFieldValue("dispute_create_page_vf_additional_expenses_incurred_by_the_claim", disputeVFadditionalexpensesincurredbytheclaim);
		addFieldValue("dispute_create_page_value_of_other_damages_incurred_from_this_claim", disputeValueofotherdamagesincurredfromthisclaim);
		addFieldValue("dispute_create_page_delivery_countries_multi_dropdown", disputeDeliveryCountries);
		addFieldValue("dispute_create_page_timezone_dropdown", disputeTimezone);
		try {
			if (driver.findElement(By.className("success-icon")).getText().contains("Current Date is different for the selected Time Zone"))
				driver.findElement(By.xpath(".//button[contains(.,'OK')]")).click();
			} catch (Exception e){}

		addFieldValue("dispute_create_page_restrict_public_access_checkbox", disputeRestrictPublicAccess);
		addFieldValue("dispute_create_page_restrict_requester_access_checkbox", disputeRestrictRequesterAccess);
		addFieldValue("dispute_create_page_supplier_access_checkbox", disputeSupplierAccess);
		addFieldValue("dispute_create_page_tier_dropdown", disputeTier);
		addFieldValue("dispute_create_page_dependent_entity_checkbox", disputeDependentEntity);

		// DISPUTE - CREATE PAGE - CONTRACTING ENTITY
		addFieldValue("dispute_create_page_external_contracting_entity_multiselect", disputeExternalClientEntity);
		addFieldValue("dispute_create_page_external_company_code_multiselect", disputeExternalCompanyCode);
		addFieldValue("dispute_create_page_contracting_client_entity_multiselect", disputeContarctingClientEntity);
		addFieldValue("dispute_create_page_contracting_company_code_multiselect", disputeContractingCompanyCode);

		// Change Request - Create Page - IMPORTANT DATES   

		DatePicker_Enhanced date = new DatePicker_Enhanced();
		date.selectCalendar(disputeDate,"issueDate");
		date.selectCalendar(disputePlannedcompletionDate,"plannedCompletionDate");

		// DISPUTE - CREATE PAGE - FUNCTIONS
		addFieldValue("dispute_create_page_functions_multi_dropdown", disputeFunctions);
		addFieldValue("dispute_create_page_services_multi_dropdown", disputeServices);
			
		//	addFieldValue("disputes_create_page_service_category_multi_dropdown", disputeServicesCategory);
		
		// DISPUTE - CREATE PAGE - GEOGRAPHY
		addFieldValue("dispute_create_page_management_regions_multi_dropdown", disputeManagementRegions);			
		addFieldValue("dispute_create_page_management_countries_multi_dropdown", disputeManagementCountries);
	//	addFieldValue("dispute_create_page_contract_regions_multi_dropdown", disputeContractRegions);
	//	addFieldValue("dispute_create_page_contract_countries_multi_dropdown", disputeContractCountries);
		
		// DISPUTE - CREATE PAGE - PROJECT INFORMATION
		addFieldValue("dispute_create_page_project_id_multiselect", disputeProjectID);
		addFieldValue("dispute_create_page_project_levels_multiselect", disputeProjectLevels);
		addFieldValue("dispute_create_page_initiatives_multiselect", disputeInitiatives);

		//DISPUTE - CREATE PAGE - RESOLUTION INFORMATION
        addFieldValue("dispute_create_page_process_area_impacted", disputeProcessAreaImpacted);
        addFieldValue("dispute_create_page_action_taken", disputeActionTaken);
        addFieldValue("dispute_create_page_resolution_remarks", disputeResolutionRemarks);

		// Change Request - Create Page - COMMENTS AND ATTACHMENTS
		addFieldValue("entity_create_page_comments_textarea", disputecomment);
		addFieldValue("entity_create_page_requested_by_dropdown", disputeRequestedby);
				    
		date.selectCalendar(disputeActualdate,"actualDate");
				    
		addFieldValue("entity_create_page_change_request_dropdown", disputeChangeRequest);
				
		if (!disputeUploadFiles.equalsIgnoreCase("")) {
			locateElementBy("entity_create_page_upload_browse_button").sendKeys(System.getProperty("user.dir")+"\\file-upload\\CR\\" + disputeUploadFiles);
		}

		Assert.assertNotNull(driver.findElement(By.xpath("//button[contains(.,'Save Issue')]")));
		((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", driver.findElement(By.xpath("//button[contains(.,'Save Issue')]")));
		driver.findElement(By.xpath("//button[contains(.,'Save Issue')]")).click();

		// DISPUTES - CREATE PAGE - FIELD VALIDATIONS
				
		List<WebElement> validationClassElementList = driver.findElements(By.xpath(".//*[@class='errorMessageHolder']/ul/li"));
		if (!validationClassElementList.isEmpty()) {
			for (WebElement validationClassElement : validationClassElementList) {
				String validationMessage = validationClassElement.getText();
				WebElement elementParent = validationClassElement.findElement(By.xpath("..")).findElement(By.xpath("..")).findElement(By.xpath("..")).findElement(By.xpath(".."));

				if (validationMessage.equalsIgnoreCase(""))
					Logger.debug("For Actions -- " + elementParent.findElement(By.className("errorClass")).getAttribute("name").toUpperCase() + " -- is Mandatory");
					else
					Logger.debug("For Actions -- " + elementParent.findElement(By.className("errorClass")).getAttribute("name").toUpperCase() + " -- " + validationMessage);
				}

				driver.get(CONFIG.getProperty("endUserURL"));
				return;
			}

				if (driver.findElements(By.className("success-icon")).size() != 0) {
					String entityID = driver.findElement(By.className("success-icon")).findElement(By.id("hrefElemId")).getText();
					Logger.debug("Dispute created successfully with Dispute ID " + entityID);
					driver.findElement(By.className("success-icon")).findElement(By.id("hrefElemId")).click();
					fluentWaitMethod(locateElementBy("dispute_show_page_issue_id"));
					String entityShowPageID = locateElementBy("dispute_show_page_issue_id").getText();
					Assert.assertEquals(entityShowPageID, entityID);
					Logger.debug("Dispute ID on show page has been verified");
					}
				
				   driver.get(CONFIG.getProperty("endUserURL"));
			    }

	@AfterMethod
	public void reportDataSetResult(ITestResult testResult) throws IOException {
		takeScreenShotOnFailure(testResult.getStatus(), this.getClass().getSimpleName());

		if(testResult.getStatus()==ITestResult.SKIP)
			TestUtil.reportDataSetResult(dispute_suite_xls, this.getClass().getSimpleName(), count+2, "SKIP");
		else if(testResult.getStatus()==ITestResult.FAILURE) {
			isTestPass=false;
			TestUtil.reportDataSetResult(dispute_suite_xls, this.getClass().getSimpleName(), count+2, "FAIL");
			result= "Fail";
		}
		else if (testResult.getStatus()==ITestResult.SUCCESS) {
			TestUtil.reportDataSetResult(dispute_suite_xls, this.getClass().getSimpleName(), count+2, "PASS");
			result= "Pass";
		}
		try {
			for (int i = 2; i <= dispute_suite_xls.getRowCount(this.getClass().getSimpleName()); i++) {
				String testCaseID = dispute_suite_xls.getCellData(this.getClass().getSimpleName(), "testCaseID", i);
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
			TestUtil.reportDataSetResult(dispute_suite_xls, "Test Cases", TestUtil.getRowNum(dispute_suite_xls, this.getClass().getSimpleName()),"PASS");
		else
			TestUtil.reportDataSetResult(dispute_suite_xls, "Test Cases", TestUtil.getRowNum(dispute_suite_xls, this.getClass().getSimpleName()),"FAIL");
		}

	@DataProvider
	public Object[][] getTestData() {
		return TestUtil.getData(dispute_suite_xls, this.getClass().getSimpleName());
		}
	}
