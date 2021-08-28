package office.sirion.suite.sl;

import java.io.IOException;
import java.util.List;
import office.sirion.util.DatePicker_Enhanced;
import office.sirion.util.TestUtil;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;
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

public class SLAuditLog extends TestSuiteBase {
	String result = null;
	String runmodes[] = null;
	static int count = -1;
	static boolean isTestPass = true;
	
	@BeforeTest
	public void checkTestSkip() {
		if (!TestUtil.isTestCaseRunnable(sl_suite_xls, this.getClass().getSimpleName())) {
			Logger.debug("Skipping Test Case " + this.getClass().getSimpleName() + " as runmode set to NO");
			throw new SkipException("Skipping Test Case " + this.getClass().getSimpleName() + " as runmode set to NO");
			}
		runmodes = TestUtil.getDataSetRunmodes(sl_suite_xls, this.getClass().getSimpleName());
		}
	
	@Test(dataProvider = "getTestData")
	public void SLAuditLogTest (String slTitle, String slDescription, String slSLID, String slSLCategory, String slSLSubCategory, String slSLItem, 
			String slRagApplicable, String slKPI, String slScope1, String slScope2, String slTimezone, String slDeliveryCountries, String slRegionType,
			String slCurrency, String slSupplierAccess, String slTier, String slComputationCalculationData, String slDataCalculationData, String slDataCriteria,
			String slPriority, String slReferences, String slClause, String slPageNumber, String slApplicationGroup, String slApplication, String slMinimumMaximumSelection,
			String slUnitofMeasurement, String slMinimumMaximumValue, String slExpected, String slSignificantlyMinMax, String slMeasurementWindow, String slYTDStartDate,
			String slCreditApplicable, String slCreditApplicableDate, String slCreditClause, String slCreditOfInvoice, String slSLCreditCategory,
			String slSLCreditSubCategory, String slSLCreditLineItem, String slCreditFrequency, String slEarnbackApplicable, String slEarnbackApplicableDate, String slEarnbackClause,
			String slEarnbackCategory, String slEarnbackSubCategory, String slEarnbackLineItem, String slEarnbackFrequency, String slSubjectTo,
			String slContinuousImprovementClause, String slStartDate, String slEndDate,String slFrequency, String slFrequencyType, String slComputationFrequency,
			String slPatternDate, String slEffectiveDate, String slReportingFrequencyType, String slReportingComputationFrequency, String slReportingPatternDate,
			String slReportingEffectiveDate, String slFunctions, String slServices, String slServiceCategory, String slManagementRegions, String slManagementCountries,
			String slContractRegions, String slContractCountries, String slProjectID, String slProjectLevels, String slInitiatives, String slVendorContractingParty,
			String slContractingClientEntity, String slContractingCompanyCode, String slRecipientClientEntity, String slRecipientCompanyCode, String slResponsibility,
			String slFinancialImpactApplicable, String slFinancialImpactValue, String slFinancialImpactClause, String slImpactDays, String slImpactType,
			String slCreditImpactApplicable, String slCreditImpactValue, String slCreditImpactClause, String slComments, String slActualDate, String slRequestedBy,
			String slChangeRequest, String slUploadFile, String slParent
			) throws InterruptedException {
		
		count++;
		if (!runmodes[count].equalsIgnoreCase("Y"))
			throw new SkipException("Runmode for Test Data Set to NO -- " + count);
		
		

		openBrowser();
	    endUserLogin(CONFIG.getProperty("endUserURL"), CONFIG.getProperty("endUserUsername"), CONFIG.getProperty("endUserPassword"));
	    DatePicker_Enhanced date = new DatePicker_Enhanced();
	    Logger.debug("Executing Test Case Service Level Audit Log with Title -- " + slTitle + " -- under Parent -- " + slParent);

	    driver.get(CONFIG.getProperty("endUserURL"));

		fluentWaitMethod(locateElementBy("sl_quick_link"));

		locateElementBy("sl_quick_link").click();

		fluentWaitMethod(locateElementBy("entity_listing_page_display_dropdown_link"));

		new Select(locateElementBy("entity_listing_page_display_dropdown_link")).selectByVisibleText(CONFIG.getProperty("maxEntityListingDropdown"));


		if (!slParent.equalsIgnoreCase("") && slParent.length()<=30)
			try{
			driver.findElement(By.xpath(".//*[@id='cr']/tbody/tr/td[4][./text()='"+slParent+"']/preceding-sibling::td[2]/a")).click();
			}catch (NoSuchElementException e){
				locateElementBy("entity_listing_page_first_entry_link").click();}
		else if (!slParent.equalsIgnoreCase("") && slParent.length()>30)
			try{
			driver.findElement(By.xpath(".//*[@id='cr']/tbody/tr/td[4][div/div[./text()='"+slParent+"']]/preceding-sibling::td[2]/a")).click();
			}catch (NoSuchElementException e){
				locateElementBy("entity_listing_page_first_entry_link").click();}

		fluentWaitMethod(driver.findElement(By.cssSelector("ng-form[template='tab.actions']")).findElement(By.cssSelector("div.submit.freeze")));
		
		// SERVICE LEVELS - SHOW PAGE - EDIT BUTTON
	    WebElement elementEdit = driver.findElement(By.cssSelector("ng-form[template='tab.actions']")).findElement(By.cssSelector("div.submit.freeze"));		
		Assert.assertNotNull(elementEdit.findElement(By.xpath(".//button[contains(.,'Edit')]")));
	    ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", elementEdit.findElement(By.xpath(".//button[contains(.,'Edit')]")));
	    elementEdit.findElement(By.xpath(".//button[contains(.,'Edit')]")).click();

		fluentWaitMethod(locateElementBy("sl_create_page_title_textbox"));
		
	    // SERVICE LEVELS - EDIT PAGE - BASIC INFORMATION
	    addFieldValue("sl_create_page_title_textbox", slTitle.trim());

	    addFieldValue("sl_create_page_description_textarea", slDescription.trim());
	    
	    addFieldValue("sl_create_page_sl_id_textarea", slSLID.trim());

        addFieldValue("sl_create_page_sl_category_dropdown", slSLCategory.trim());
	    
	    addFieldValue("sl_create_page_sl_sub_category_dropdown", slSLSubCategory.trim());
	    
	    addFieldValue("sl_create_page_sl_item_dropdown", slSLItem.trim());

	    addFieldValue("sl_create_page_rag_applicable_dropdown", slRagApplicable.trim());

	    addFieldValue("sl_create_page_sl_kpi_dropdown", slKPI.trim());

	    addFieldValue("sl_create_page_scope_of_service_one_dropdown", slScope1.trim());
	    
	    addFieldValue("sl_create_page_scope_of_service_two_dropdown", slScope2.trim());
	    
	    addFieldValue("sl_create_page_timezone_dropdown", slTimezone.trim());
	    try {
	    	if (driver.findElement(By.className("success-icon")).getText().contains("Current Date is different for the selected Time Zone"))
	    		driver.findElement(By.xpath(".//button[contains(.,'OK')]")).click();
	    	} catch (Exception e) {
	    		}
	    
	    addFieldValue("sl_create_page_delivery_countries_multi_dropdown", slDeliveryCountries.trim());
	    
	    addFieldValue("sl_create_page_region_type_dropdown", slRegionType.trim());

	    addFieldValue("sl_create_page_currency_dropdown", slCurrency.trim());
	    
	    addFieldValue("sl_create_page_supplier_access_checkbox", slSupplierAccess.trim());
	    
	    addFieldValue("sl_create_page_tier_dropdown", slTier.trim());
	    
	    addFieldValue("sl_create_page_performance_computation_calculation_textarea", slComputationCalculationData.trim());
	    
	    addFieldValue("sl_create_page_performance_data_calculation_textarea", slDataCalculationData.trim());

	    addFieldValue("sl_create_page_unique_criteria_textarea", slDataCriteria.trim());
	    
	    // SERVICE LEVELS - EDIT PAGE - OTHER INFORMATION
	    addFieldValue("sl_create_page_priority_dropdown", slPriority.trim());
	    
	    addFieldValue("sl_create_page_references_dropdown", slReferences.trim());
	    if (!slReferences.equalsIgnoreCase("")) {
		    addFieldValue("sl_create_page_clause_textbox", slClause.trim());

		    addFieldValue("sl_create_page_page_number_numeric_box", slPageNumber.trim());
	    	}

	    addFieldValue("sl_create_page_application_group_multi_dropdown", slApplicationGroup.trim());
	    
	    addFieldValue("sl_create_page_application_multi_dropdown", slApplication.trim());
	    
	    // SERVICE LEVELS - EDIT PAGE - PERFORMANCE
	    addFieldValue("sl_create_page_minimum_maximum_dropdown", slMinimumMaximumSelection.trim());
	    
	    addFieldValue("sl_create_page_unit_of_sl_measurement_dropdown", slUnitofMeasurement.trim());
	    
	    addFieldValue("sl_create_page_minimum_maximum_numeric_box", slMinimumMaximumValue.trim());
	    
	    addFieldValue("sl_create_page_expected_numeric_box", slExpected.trim());
	    
	    addFieldValue("sl_create_page_significantly_min_max_numeric_box", slSignificantlyMinMax.trim());
	    
	    addFieldValue("sl_create_page_measurement_window_dropdown", slMeasurementWindow.trim());
	    
	    addFieldValue("sl_create_page_ytd_start_date", slYTDStartDate.trim());
	     
	    // SERVICE LEVELS - EDIT PAGE - CREDIT EARNBACK
		addFieldValue("sl_create_page_credit_applicable_checkbox", slCreditApplicable.trim());
   		if (slCreditApplicable.equalsIgnoreCase("Yes")) {
   			addFieldValue("sl_create_page_credit_applicable_date", slCreditApplicableDate.trim());
	    	
		    addFieldValue("sl_create_page_credit_clause_textarea", slCreditClause.trim());

		    addFieldValue("sl_create_page_credit_percentage_of_invoice_numeric_box", slCreditOfInvoice.trim());
		    
		    addFieldValue("sl_create_page_sl_credit_percentage_category_numeric_box", slSLCreditCategory.trim());

		    addFieldValue("sl_create_page_sl_credit_percentage_sub_category_numeric_box", slSLCreditSubCategory.trim());
		    
		    addFieldValue("sl_create_page_sl_credit_percentage_line_item_numeric_box", slSLCreditLineItem.trim());
		    
		    addFieldValue("sl_create_page_credit_frequency_dropdown", slCreditFrequency.trim());
    		}
   		
		addFieldValue("sl_create_page_earnback_applicable_checkbox", slEarnbackApplicable.trim());
   		if (slEarnbackApplicable.equalsIgnoreCase("Yes")) {

   		  addFieldValue("sl_create_page_earnback_applicable_date", slEarnbackApplicableDate.trim());
	    	
		    addFieldValue("sl_create_page_earnback_clause_textarea", slEarnbackClause.trim());

		    addFieldValue("sl_create_page_earnback_percentage_category_numeric_box", slEarnbackCategory.trim());

		    addFieldValue("sl_create_page_earnback_percentage_sub_category_numeric_box", slEarnbackSubCategory.trim());
		    
		    addFieldValue("sl_create_page_earnback_percentage_line_item_numeric_box", slEarnbackLineItem.trim());
		    
		    addFieldValue("sl_create_page_earnback_frequency_dropdown", slEarnbackFrequency.trim());		    
    		}
	    
	    // SERVICE LEVELS - EDIT PAGE - CONTINUOUS IMPROVEMENT
		addFieldValue("sl_create_page_subject_to_continuous_improvement_checkbox", slSubjectTo.trim());
		if (slSubjectTo.equalsIgnoreCase("Yes"))
    		addFieldValue("sl_create_page_continuous_improvement_clause_textbox", slContinuousImprovementClause.trim());
	    
	    // SERVICE LEVELS - EDIT PAGE - IMPORTANT DATES
	    
		  addFieldValue("sl_create_page_end_date", slEndDate.trim());

		    addFieldValue("sl_create_page_start_date", slStartDate.trim());
		    
		    addFieldValue("sl_create_page_frequency_dropdown", slFrequency.trim());
		    
		    addFieldValue("sl_create_page_frequency_type_dropdown", slFrequencyType.trim());
		    
		    addFieldValue("sl_create_page_computation_frequency_dropdown", slComputationFrequency);
		    
		    addFieldValue("sl_create_page_pattern_date", slPatternDate.trim());
		    
		    addFieldValue("sl_create_page_effective_date", slEffectiveDate.trim());

		    addFieldValue("sl_create_page_reporting_frequency_type_dropdown", slReportingFrequencyType.trim());

		    addFieldValue("sl_create_page_reporting_frequency_dropdown", slReportingComputationFrequency);
		    
		    addFieldValue("sl_create_page_reporting_pattern_date", slReportingPatternDate.trim());
			    
			addFieldValue("sl_create_page_reporting_effective_date", slReportingEffectiveDate.trim());

	    
	    // SERVICE LEVELS - EDIT PAGE - FUNCTIONS
		addFieldValue("sl_create_page_functions_multi_dropdown", slFunctions.trim());
		if (!slFunctions.equalsIgnoreCase(""))
			addFieldValue("sl_create_page_services_multi_dropdown", slServices.trim());
		
		addFieldValue("sl_create_page_service_category_multi_dropdown", slServiceCategory.trim());
		    
	    // SERVICE LEVELS - EDIT PAGE - GEOGRAPHY
		addFieldValue("sl_create_page_management_regions_multi_dropdown", slManagementRegions.trim());
		if (!slManagementRegions.equalsIgnoreCase(""))
			addFieldValue("sl_create_page_management_countries_multi_dropdown", slManagementCountries.trim());
		
		addFieldValue("sl_create_page_contract_regions_multi_dropdown", slContractRegions.trim());
		if (!slContractRegions.equalsIgnoreCase(""))
			addFieldValue("sl_create_page_contract_countries_multi_dropdown", slContractCountries.trim());
		
	    // SERVICE LEVELS - EDIT PAGE - PROJECT INFORMATION
		addFieldValue("sl_create_page_project_id_multi_dropdown", slProjectID.trim());
			
		addFieldValue("sl_create_page_project_levels_multi_dropdown", slProjectLevels.trim());
			
		addFieldValue("sl_create_page_initiatives_multi_dropdown", slInitiatives.trim());
			
	    // SERVICE LEVELS - EDIT PAGE - CONTRACTING PARTY
		addFieldValue("sl_create_page_vendor_contracting_party_multi_dropdown", slVendorContractingParty.trim());

		addFieldValue("sl_create_page_contracting_client_entities_multi_dropdown", slContractingClientEntity.trim());
		if (!slContractingClientEntity.equalsIgnoreCase(""))
			addFieldValue("sl_create_page_contracting_company_codes_multi_dropdown", slContractingCompanyCode.trim());

		addFieldValue("sl_create_page_recipient_client_entities_multi_dropdown", slRecipientClientEntity.trim());
		if (!slRecipientClientEntity.equalsIgnoreCase(""))
			addFieldValue("sl_create_page_recipient_company_codes_multi_dropdown", slRecipientCompanyCode.trim());

		// SERVICE LEVELS - EDIT PAGE - STAKEHOLDERS
		addFieldValue("sl_create_page_responsibility_dropdown", slResponsibility.trim());
			
	    // SERVICE LEVELS - EDIT PAGE - FINANCIAL INFORMATION
	   	addFieldValue("sl_create_page_financial_impact_applicable_checkbox", slFinancialImpactApplicable.trim());
		if (slFinancialImpactApplicable.equalsIgnoreCase("Yes")) {
		   	addFieldValue("sl_create_page_financial_impact_value_numeric_box", slFinancialImpactValue.trim());

//			addFieldValue("sl_create_page_financial_impact_clause_textarea", slFinancialImpactClause.trim());

		   	addFieldValue("sl_create_page_impact_days_numeric_box", slImpactDays.trim());

		   	addFieldValue("sl_create_page_impact_type_multi_dropdown", slImpactType.trim());
		   	}
		
		addFieldValue("sl_create_page_credit_impact_applicable_checkbox", slCreditImpactApplicable.trim());
		if (slCreditImpactApplicable.equalsIgnoreCase("Yes")) {
			addFieldValue("sl_create_page_credit_impact_value_numeric_box", slCreditImpactValue.trim());
			
//			addFieldValue("sl_create_page_credit_impact_clause_textarea", slCreditImpactClause.trim());
			}
		
		// SERVICE LEVELS - EDIT PAGE - COMMENTS AND ATTACHMENTS
		addFieldValue("entity_create_page_comments_textarea", slComments.trim());
		    
		addFieldValue("entity_create_page_requested_by_dropdown", slRequestedBy.trim());
		    
		date.selectCalendar(slActualDate,"actualDate");
		    
		addFieldValue("entity_create_page_change_request_dropdown", slChangeRequest.trim());
		
		if (!slUploadFile.equalsIgnoreCase("")) {
			locateElementBy("entity_create_page_upload_browse_button").sendKeys(System.getProperty("user.dir")+"\\file-upload\\SL\\" + slUploadFile);

			}

		locateElementBy("entity_create_page_actual_date").findElement(By.tagName("input")).click();
		// SERVICE LEVELS - EDIT PAGE - UPDATE BUTTON
	    WebElement elementUpdate = driver.findElement(By.cssSelector("ng-form[template='tab.actions']")).findElement(By.cssSelector("div.submit.freeze"));		
		Assert.assertNotNull(elementUpdate.findElement(By.xpath(".//button[contains(.,'Update')]")));
	    ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", elementUpdate.findElement(By.xpath(".//button[contains(.,'Update')]")));
	    elementUpdate.findElement(By.xpath(".//button[contains(.,'Update')]")).click();


		// SERVICE LEVELS - EDIT PAGE - FIELD VALIDATIONS
		List<WebElement> validationClassElementList = driver.findElements(By.xpath(".//*[@class='errorMessageHolder']/ul/li"));
		if (!validationClassElementList.isEmpty()) {
			for (WebElement validationClassElement : validationClassElementList) {
				String validationMessage = validationClassElement.getText();
				
				WebElement elementParent = validationClassElement.findElement(By.xpath("..")).findElement(By.xpath("..")).findElement(By.xpath("..")).findElement(By.xpath(".."));
				
				if (validationMessage.equalsIgnoreCase(""))
					Logger.debug("For Service Levels -- " + elementParent.findElement(By.className("errorClass")).getAttribute("name").toUpperCase() + " -- is Mandatory");
				else
					Logger.debug("For Service Levels -- " + elementParent.findElement(By.className("errorClass")).getAttribute("name").toUpperCase() + " -- " + validationMessage);
				}

			driver.get(CONFIG.getProperty("endUserURL"));
			return;
			}
	    fluentWaitMethod(locateElementBy("sl_create_page_title_textbox"));

		String entityShowPageName = locateElementBy("sl_create_page_title_textbox").getText();

		Assert.assertEquals(entityShowPageName, slTitle);

	    driver.findElement(By.linkText("AUDIT LOG")).click();

	    Logger.debug("Test Case Service Level Audit Log with Title -- " + slTitle + " -- is STARTED");
		
		fluentWaitMethod(driver.findElement(By.className("dataTables_scrollBody")));
		WebElement elementOddRowSelected = driver.findElement(By.id("table_230")).findElements(By.className("odd")).get(0);
		
		Assert.assertEquals(elementOddRowSelected.findElement(By.cssSelector("td.col_wrap_action_name")).getText(), "Updated");

	    if (!slRequestedBy.equalsIgnoreCase(""))
			Assert.assertEquals(elementOddRowSelected.findElement(By.cssSelector("td.col_wrap_requested_by")).getText(), slRequestedBy);
	    else
	    	Assert.assertEquals(elementOddRowSelected.findElement(By.cssSelector("td.col_wrap_requested_by")).getText(), CONFIG.getProperty("endUserFullName"));

	    Assert.assertEquals(elementOddRowSelected.findElement(By.cssSelector("td.col_wrap_completed_by")).getText(), CONFIG.getProperty("endUserFullName"));

	    if (!slComments.equalsIgnoreCase(""))
		    Assert.assertEquals(elementOddRowSelected.findElement(By.cssSelector("td.col_wrap_comment")).getText(), "Yes");
	    else
		    Assert.assertEquals(elementOddRowSelected.findElement(By.cssSelector("td.col_wrap_comment")).getText(), "No");

	    if (!slUploadFile.equalsIgnoreCase(""))
		    Assert.assertEquals(elementOddRowSelected.findElement(By.cssSelector("td.col_wrap_document")).getText(), "Yes");
	    else
		    Assert.assertEquals(elementOddRowSelected.findElement(By.cssSelector("td.col_wrap_document")).getText(), "No");

	    elementOddRowSelected.findElement(By.cssSelector("td.col_wrap_history")).findElement(By.className("serverHistoryView")).click();


		List<WebElement> auditRowElementList = driver.findElement(By.id("historyDataTable")).findElement(By.id("data")).findElements(By.tagName("tr"));
	    for (int iAuditRow = 0; iAuditRow < auditRowElementList.size(); iAuditRow++) {
		      for (int jAuditCol = 0; jAuditCol < auditRowElementList.get(0).findElements(By.tagName("td")).size(); jAuditCol++) {
		    	  // SERVICE LEVELS - BASIC INFORMATION
			      if (auditRowElementList.get(iAuditRow).findElements(By.tagName("td")).get(jAuditCol).getText().equalsIgnoreCase("Title")) {
				         List<WebElement> elementDataList = auditRowElementList.get(iAuditRow).findElements(By.tagName("td"));
				         Assert.assertEquals(elementDataList.get(3).getText(), slTitle);
				         }

			      else if (auditRowElementList.get(iAuditRow).findElements(By.tagName("td")).get(jAuditCol).getText().equalsIgnoreCase("SL ID")) {
				         List<WebElement> elementDataList = auditRowElementList.get(iAuditRow).findElements(By.tagName("td"));
				         Assert.assertEquals(elementDataList.get(3).getText(), slSLID);
				         }

			      else if (auditRowElementList.get(iAuditRow).findElements(By.tagName("td")).get(jAuditCol).getText().equalsIgnoreCase("Description")) {
				         List<WebElement> elementDataList = auditRowElementList.get(iAuditRow).findElements(By.tagName("td"));
				         Assert.assertEquals(elementDataList.get(3).getText(), slDescription);
				         }   
	
			      else if (auditRowElementList.get(iAuditRow).findElements(By.tagName("td")).get(jAuditCol).getText().equalsIgnoreCase("SL Category")) {
				         List<WebElement> elementDataList = auditRowElementList.get(iAuditRow).findElements(By.tagName("td"));
				         Assert.assertEquals(elementDataList.get(3).getText(), slSLCategory);
				         }   

			      else if (auditRowElementList.get(iAuditRow).findElements(By.tagName("td")).get(jAuditCol).getText().equalsIgnoreCase("SL Sub-Category")) {
				         List<WebElement> elementDataList = auditRowElementList.get(iAuditRow).findElements(By.tagName("td"));
				         Assert.assertEquals(elementDataList.get(3).getText(), slSLSubCategory);
				         }   

			      else if (auditRowElementList.get(iAuditRow).findElements(By.tagName("td")).get(jAuditCol).getText().equalsIgnoreCase("SL")) {
				         List<WebElement> elementDataList = auditRowElementList.get(iAuditRow).findElements(By.tagName("td"));
				         Assert.assertEquals(elementDataList.get(3).getText(), slSLItem);
				         }   

			      else if (auditRowElementList.get(iAuditRow).findElements(By.tagName("td")).get(jAuditCol).getText().equalsIgnoreCase("Service Level/KPI")) {
				         List<WebElement> elementDataList = auditRowElementList.get(iAuditRow).findElements(By.tagName("td"));
				         Assert.assertEquals(elementDataList.get(3).getText(), slKPI);
				         }   

			      else if (auditRowElementList.get(iAuditRow).findElements(By.tagName("td")).get(jAuditCol).getText().equalsIgnoreCase("Scope of Service 1")) {
				         List<WebElement> elementDataList = auditRowElementList.get(iAuditRow).findElements(By.tagName("td"));
				         Assert.assertEquals(elementDataList.get(3).getText(), slScope1);
				         }   

			      else if (auditRowElementList.get(iAuditRow).findElements(By.tagName("td")).get(jAuditCol).getText().equalsIgnoreCase("Scope of Service 2")) {
				         List<WebElement> elementDataList = auditRowElementList.get(iAuditRow).findElements(By.tagName("td"));
				         Assert.assertEquals(elementDataList.get(3).getText(), slScope2);
				         }   
			      
			      else if (auditRowElementList.get(iAuditRow).findElements(By.tagName("td")).get(jAuditCol).getText().equalsIgnoreCase("Time Zone")) {
				         List<WebElement> elementDataList = auditRowElementList.get(iAuditRow).findElements(By.tagName("td"));
				         Assert.assertEquals(elementDataList.get(3).getText(), slTimezone);
				         }   

			      else if (auditRowElementList.get(iAuditRow).findElements(By.tagName("td")).get(jAuditCol).getText().equalsIgnoreCase("Supplier Access")) {
				         List<WebElement> elementDataList = auditRowElementList.get(iAuditRow).findElements(By.tagName("td"));
				         Assert.assertEquals(elementDataList.get(3).getText(), slSupplierAccess);
				         }   
			      }
		      }

	    Logger.debug("Test Case Service Level Audit Log with Title -- " + slTitle + " -- is PASSED");
        driver.get(CONFIG.getProperty("endUserURL"));
	    }
		
	@AfterMethod
   	public void reportDataSetResult(ITestResult testResult) throws IOException {
   		takeScreenShotOnFailure(testResult.getStatus(), this.getClass().getSimpleName());

   		if(testResult.getStatus()==ITestResult.SKIP)
   			TestUtil.reportDataSetResult(sl_suite_xls, this.getClass().getSimpleName(), count+2, "SKIP");
   		else if(testResult.getStatus()==ITestResult.FAILURE) {
   			isTestPass=false;
   			TestUtil.reportDataSetResult(sl_suite_xls, this.getClass().getSimpleName(), count+2, "FAIL");
   			result= "Fail";
   			}
   		else if (testResult.getStatus()==ITestResult.SUCCESS) {
   			TestUtil.reportDataSetResult(sl_suite_xls, this.getClass().getSimpleName(), count+2, "PASS");
   			result= "Pass";
   			}
   		try {
             for (int i = 2; i <= sl_suite_xls.getRowCount(this.getClass().getSimpleName()); i++) {
                 String testCaseID = sl_suite_xls.getCellData(this.getClass().getSimpleName(), "testCaseID", i);
                 if (!testCaseID.equalsIgnoreCase("")) {
                     updateRun(convertStringToInteger(testCaseID),new java.lang.Exception().toString(), result);
                 }
             }
   	   }catch(Exception e){
             Logger.debug(e);
         }
   		}

	@AfterTest
	public void reportTestResult() {
		if (isTestPass)
			TestUtil.reportDataSetResult(sl_suite_xls, "Test Cases", TestUtil.getRowNum(sl_suite_xls, this.getClass().getSimpleName()),"PASS");
		else
			TestUtil.reportDataSetResult(sl_suite_xls, "Test Cases", TestUtil.getRowNum(sl_suite_xls, this.getClass().getSimpleName()),"FAIL");
		}

	@DataProvider
	public Object[][] getTestData() {
		return TestUtil.getData(sl_suite_xls, this.getClass().getSimpleName());
		}
	}