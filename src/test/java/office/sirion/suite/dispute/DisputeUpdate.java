package office.sirion.suite.dispute;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.testng.Assert;
import org.testng.ITestResult;
import org.testng.SkipException;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import office.sirion.util.DatePicker_Enhanced;
import office.sirion.util.TestUtil;
import java.io.IOException;

public class DisputeUpdate extends TestSuiteBase {
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
	public void DisputeUpdateTest(String disputeTitle, String disputeDescription, String disputeType, String disputePriority, String disputeCurrency,
                                  String disputeFinancialImpact,String disputeIdentifiedClaimValue,String disputePotentialClaimValue, String disputeValueClaimed,
                                  String disputeValueSettled,String disputeFunctions, String disputeServices, String disputeServicesCategory,
                                  String disputeManagementRegions, String disputeManagementCountries, String disputeContractRegions, String disputeContractCountries,
                                  String disputeProjectID,String disputeProcessAreaImpacted, String disputeActionTaken, String disputeResolutionRemarks,
                                  String disputeValueRealised, String disputeExpectedClaimValue, String disputeRealisationAmount, String disputeSavingValue,
                                  String disputeVFadditionalexpensesincurredbytheclaim, String disputeValueofotherdamagesincurredfromthisclaim,String disputeGovernanceBody,
                                  String disputeDeliveryCountries, String disputeTimezone, String disputeRestrictPublicAccess, String disputeRestrictRequesterAccess,
                                  String disputeSupplierAccess, String disputeDependentEntity, String disputeTier,String disputedisputeDate, String disputePlannedDate,
                                  String disputeInternalContractingParty, String disputeCompanyCode, String disputeContarctingClientEntity,
                                  String disputeContractingCompanyCode, String disputeProjectLevels, String disputeInitiatives, String disputecomment,
                                  String disputeRequestedby,String disputeActualdate, String disputeChangeRequest, String disputeUploadFiles)
                                    throws InterruptedException {
		
		count++;
		if (!runmodes[count].equalsIgnoreCase("Y")) {
			skip = true;
			throw new SkipException("Runmode for test set data set to no "+ count);
		}

		Logger.debug("Executing Test Case dispute Audit Log Creation");

		openBrowser();
		endUserLogin(CONFIG.getProperty("endUserURL"),CONFIG.getProperty("endUserUsername"),CONFIG.getProperty("endUserPassword"));
		
		driver.get(CONFIG.getProperty("endUserURL"));
		Logger.debug("Executing Test Case Dispute Update with Title ---- " + disputeTitle);

		fluentWaitMethod(locateElementBy("disputes_quick_link"));

		locateElementBy("disputes_quick_link").click();
		fluentWaitMethod(locateElementBy("entity_listing_page_display_dropdown_link"));

		new Select(locateElementBy("entity_listing_page_display_dropdown_link")).selectByVisibleText("100");

		if (!disputeTitle.equalsIgnoreCase("")) {
			try {
				driver.findElement(By.xpath("//*[@id='cr']/tbody/tr[@role='row']/td[contains(.,'" + disputeTitle + "')]/preceding-sibling::td[1]/a")).click();
			}catch (NoSuchElementException e){
				locateElementBy("entity_listing_page_first_entry_link").click();
			}
		} else {
			locateElementBy("entity_listing_page_first_entry_link").click();
		}
		
		fluentWaitMethod(driver.findElement(By.xpath("//button[contains(.,'Edit')]")));
		Assert.assertNotNull(driver.findElement(By.xpath("//button[contains(.,'Edit')]")));
		((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", driver.findElement(By.xpath("//button[contains(.,'Edit')]")));
		driver.findElement(By.xpath("//button[contains(.,'Edit')]")).click();
		fluentWaitMethod(locateElementBy("dispute_show_page_id"));

		// dispute - Create Page - BASIC INFORMATION

		addFieldValue("dispute_create_page_title_textbox",disputeTitle);
		addFieldValue("dispute_create_page_dispute_type_dropdown",disputeType);
		addFieldValue("dispute_create_page_priority_dropdown",disputePriority);
		addFieldValue("dispute_create_page_currency_dropdown",disputeCurrency);
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
		addFieldValue("dispute_create_page_delivery_countries_multi_dropdown",disputeDeliveryCountries);
		addFieldValue("dispute_create_page_timezone_dropdown",disputeTimezone);
		try {
      		if (driver.findElement(By.className("success-icon")).getText().contains("Current Date is different for the selected Time Zone"))
      driver.findElement(By.xpath("//button[contains(.,'OK')]")).click();
      	} catch (Exception e) {}
		
		addFieldValue("dispute_create_page_restrict_public_access_checkbox",disputeRestrictPublicAccess);
		addFieldValue("dispute_create_page_restrict_requester_access_checkbox",disputeRestrictRequesterAccess);
		addFieldValue("dispute_create_page_supplier_access_checkbox",disputeSupplierAccess);
		addFieldValue("dispute_create_page_tier_dropdown",disputeTier);
		addFieldValue("dispute_create_page_dependent_entity_checkbox",disputeDependentEntity);

		//Dispute - Create Page - IMPORTANT DATES
        DatePicker_Enhanced date = new DatePicker_Enhanced();
        date.selectCalendar(disputedisputeDate,"issueDate");
        date.selectCalendar(disputePlannedDate,"plannedCompletionDate");

        // dispute - Create Page - Contracting Entity
		addFieldValue("dispute_create_page_external_contracting_entity_multiselect", disputeInternalContractingParty);
		addFieldValue("dispute_create_page_external_company_code_multiselect", disputeCompanyCode);
		addFieldValue("dispute_create_page_contracting_client_entity_multiselect", disputeContarctingClientEntity);
		addFieldValue("dispute_create_page_contracting_company_code_multiselect", disputeContractingCompanyCode);

        // DISPUTE - CREATE PAGE - FUNCTIONS
        addFieldValue("dispute_create_page_functions_multi_dropdown", disputeFunctions);
        addFieldValue("dispute_create_page_services_multi_dropdown", disputeServices);
        addFieldValue("disputes_create_page_service_category_multi_dropdown", disputeServicesCategory);

        // DISPUTE - CREATE PAGE - GEOGRAPHY
        addFieldValue("dispute_create_page_management_regions_multi_dropdown", disputeManagementRegions);
        addFieldValue("dispute_create_page_management_countries_multi_dropdown", disputeManagementCountries);
        addFieldValue("dispute_create_page_contract_regions_multi_dropdown", disputeContractRegions);
        addFieldValue("dispute_create_page_contract_countries_multi_dropdown", disputeContractCountries);

        // DISPUTE - CREATE PAGE - PROJECT INFORMATION
        addFieldValue("dispute_create_page_project_id_multiselect", disputeProjectID);
        addFieldValue("dispute_create_page_project_levels_multiselect", disputeProjectLevels);
        addFieldValue("dispute_create_page_initiatives_multiselect", disputeInitiatives);

        //DISPUTE - CREATE PAGE - RESOLUTION INFORMATION
        addFieldValue("dispute_create_page_process_area_impacted", disputeProcessAreaImpacted);
        addFieldValue("dispute_create_page_action_taken", disputeActionTaken);
        addFieldValue("dispute_create_page_resolution_remarks", disputeResolutionRemarks);

		Assert.assertNotNull(driver.findElement(By.xpath("//button[contains(.,'Update')]")));
		((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", driver.findElement(By.xpath("//button[contains(.,'Update')]")));
		driver.findElement(By.xpath("//button[contains(.,'Update')]")).click();

		// Change Request - Create Page - COMMENTS AND ATTACHMENTS
		addFieldValue("entity_create_page_comments_textarea", disputecomment);
		driver.findElement(By.xpath("//*[@id='mainForm']/ng-form/fieldset[10]/p")).click();
		addFieldValue("entity_create_page_requested_by_dropdown", disputeRequestedby);
		addFieldValue("entity_create_page_actual_date", disputeActualdate);
		addFieldValue("entity_create_page_change_request_dropdown", disputeChangeRequest);
		
		if (!disputeUploadFiles.equalsIgnoreCase("")) {
			locateElementBy("entity_create_page_upload_browse_button").sendKeys(System.getProperty("user.dir")+"\\file-upload\\CR\\" + disputeUploadFiles);
			Assert.assertNotNull(driver.findElement(By.xpath("//button[contains(.,'Save Comment/Attachment')]")));
			((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", driver.findElement(By.xpath("//button[contains(.,'Save Comment/Attachment')]")));
			driver.findElement(By.xpath("//button[contains(.,'Save Comment/Attachment')]")).click();
			}

		
		
		// Change Request - Create Page - VALIDATIONS
		if (disputeTitle.equalsIgnoreCase("")) {
		   	fail = true;
		   	if (fieldValidationMandatory("dispute_create_page_title_textbox")) {
		   		Logger.debug("Dispute Title is Mandatory");
		   		
		   		fail = false;
		   		driver.get(CONFIG.getProperty("endUserURL"));
		   		return;
		   		}
		   	}

		if (disputeDescription.equalsIgnoreCase("")) {
		   	fail = true;
		    	
		   	if (fieldValidationMandatory("dispute_create_page_description_textarea")) {
		   		Logger.debug("Dispute Description is Mandatory");
		   		
		   		fail = false;
		   		driver.get(CONFIG.getProperty("endUserURL"));
		   		return;
		   		}
		   	}
		
		if (disputeDeliveryCountries.equalsIgnoreCase("")) {
		   	fail = true;
		    	
		   	if (fieldValidationMandatory("dispute_create_page_delivery_countries_multi_dropdown")) {
		   		Logger.debug("Dispute Delivery Countries is Mandatory");
		   		
		   		fail = false;
		   		driver.get(CONFIG.getProperty("endUserURL"));
		   		return;
		   		}
		   	}

		if (disputeTimezone.equalsIgnoreCase("")) {
		   	fail = true;
		    	
		   	if (fieldValidationMandatory("dispute_create_page_timezone_dropdown")) {
		   		Logger.debug("Dispute Timezone is Mandatory");
		   		
		   		fail = false;
		   		driver.get(CONFIG.getProperty("endUserURL"));
		   		return;
		   		}
		   	}
		
		if (disputeTier.equalsIgnoreCase("")) {
		   	fail = true;
		    	
		   	if (fieldValidationMandatory("dispute_create_page_tier_dropdown")) {
		   		Logger.debug("Dispute Tier is Mandatory");
		   		
		   		fail = false;
		   		driver.get(CONFIG.getProperty("endUserURL"));
		   		return;
		   		}
		   	}
		
		if (disputedisputeDate.equalsIgnoreCase("")) {
		   	fail = true;
		    	
		   	if (fieldValidationMandatory("dispute_create_page_issue_date")) {
		   		Logger.debug("Dispute Date is Mandatory");
		   		
		   		fail = false;
		   		driver.get(CONFIG.getProperty("endUserURL"));
		   		return;
		   		}
		   	}
		
		if (disputePlannedDate.equalsIgnoreCase("")) {
		   	fail = true;
		    	
		   	if (fieldValidationMandatory("dispute_create_page_planned_completion_date")) {
		   		Logger.debug("Dispute Planned Completion is Mandatory");
		   		
		   		fail = false;
		   		driver.get(CONFIG.getProperty("endUserURL"));
		   		return;
		   		}
		   	}


		fluentWaitMethod(locateElementBy("dispute_show_page_id"));

		String entityShowPageName = locateElementBy("dispute_show_page_Title").getText();

		Assert.assertEquals(entityShowPageName, disputeTitle);
		Logger.debug("Dispute Title on show page has been verified");
		fail = false;

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
    public void reportTestResult(){
        if(isTestPass)
            TestUtil.reportDataSetResult(dispute_suite_xls, "Test Cases", TestUtil.getRowNum(dispute_suite_xls,this.getClass().getSimpleName()), "PASS");
        else
            TestUtil.reportDataSetResult(dispute_suite_xls, "Test Cases", TestUtil.getRowNum(dispute_suite_xls,this.getClass().getSimpleName()), "FAIL");
    
    }
    
    @DataProvider
    public Object[][] getTestData(){
        return TestUtil.getData(dispute_suite_xls, this.getClass().getSimpleName()) ;
    }

}
