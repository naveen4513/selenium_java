package office.sirion.suite.sl;

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

public class SLCreation extends TestSuiteBase {
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
	public void SLCreationTest(String slTitle, String slDescription, String slSLID, String slSLCategory, String slSLSubCategory, String slSLItem, 
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
			String slChangeRequest, String slUploadFile, String slSupplier, String slSource, String slSourceName
			) throws InterruptedException {

		count++;
		if (!runmodes[count].equalsIgnoreCase("Y"))
			throw new SkipException("Runmode for Test Data Set to NO -- " + count);

		

		openBrowser();



	    endUserLogin(CONFIG.getProperty("endUserURL"), CONFIG.getProperty("endUserUsername"), CONFIG.getProperty("endUserPassword"));

	    Logger.debug("Executing Test Case Service Level Creation with Title -- " + slTitle + " -- under Contract -- " + slSourceName);

	    driver.get(CONFIG.getProperty("endUserURL"));

		fluentWaitMethod(locateElementBy("sl_quick_link"));

		locateElementBy("sl_quick_link").click();

		fluentWaitMethod(locateElementBy("sl_entity_listing_page_plus_button"));

		locateElementBy("sl_entity_listing_page_plus_button").click();


	    // SERVICE LEVELS - GLOBAL CREATE PAGE
		addFieldValue("entity_global_create_page_supplier_dropdown", slSupplier.trim());

		addFieldValue("entity_global_create_page_source_type_dropdown", slSource.trim());


		addFieldValue("entity_global_create_page_source_name_title_dropdown", slSourceName.trim());

		WebElement elementSubmit = driver.findElement(By.cssSelector("ng-form[template='tab.actions']")).findElement(By.cssSelector("div.submit.freeze"));		
		Assert.assertNotNull(elementSubmit.findElement(By.xpath(".//button[contains(.,'Submit')]")));
	    ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", elementSubmit.findElement(By.xpath(".//button[contains(.,'Submit')]")));
	    elementSubmit.findElement(By.xpath(".//button[contains(.,'Submit')]")).click();

	    fluentWaitMethod(locateElementBy("sl_create_page_title_textbox"));

	    // SERVICE LEVELS - CREATE PAGE - BASIC INFORMATION
	    addFieldValue("sl_create_page_title_textbox", slTitle.trim());

	    addFieldValue("sl_create_page_description_textarea", slDescription.trim());
	    
	    addFieldValue("sl_create_page_sl_id_textarea", slSLID.trim());
        Assert.assertEquals(locateElementBy("sl_create_page_supplier_link").getText().trim(), slSupplier.trim());

        Assert.assertEquals(locateElementBy("sl_create_page_contract_link").getText().trim(), slSourceName.trim().split(" \\(")[0]);
	    
        Assert.assertEquals(locateElementBy("sl_create_page_source_name_link").getText().trim(), slSourceName.trim().split(" \\(")[0]);

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
	    
	    // SERVICE LEVELS - CREATE PAGE - OTHER INFORMATION
	    addFieldValue("sl_create_page_priority_dropdown", slPriority.trim());
	    
	    addFieldValue("sl_create_page_references_dropdown", slReferences.trim());
	    if (!slReferences.equalsIgnoreCase("")) {
		    addFieldValue("sl_create_page_clause_textbox", slClause.trim());

		    addFieldValue("sl_create_page_page_number_numeric_box", slPageNumber.trim());
	    	}

	    addFieldValue("sl_create_page_application_group_multi_dropdown", slApplicationGroup.trim());
	    
	    addFieldValue("sl_create_page_application_multi_dropdown", slApplication.trim());
	    
	    // SERVICE LEVELS - CREATE PAGE - PERFORMANCE

		if(!slRagApplicable.equalsIgnoreCase("No")) {
			addFieldValue("sl_create_page_minimum_maximum_dropdown", slMinimumMaximumSelection.trim());

			addFieldValue("sl_create_page_unit_of_sl_measurement_dropdown", slUnitofMeasurement.trim());

			addFieldValue("sl_create_page_minimum_maximum_numeric_box", slMinimumMaximumValue.trim());

			addFieldValue("sl_create_page_expected_numeric_box", slExpected.trim());

		}
	    //addFieldValue("sl_create_page_significantly_min_max_numeric_box", slSignificantlyMinMax.trim());
	    
	    addFieldValue("sl_create_page_measurement_window_dropdown", slMeasurementWindow.trim());
	    DatePicker_Enhanced ytddate = new DatePicker_Enhanced();
	    ytddate.selectCalendar(slYTDStartDate,"ytdStartDate");
	    

	    // SERVICE LEVELS - CREATE PAGE - CREDIT EARNBACK
		addFieldValue("sl_create_page_credit_applicable_checkbox", slCreditApplicable.trim());
   		if (slCreditApplicable.equalsIgnoreCase("Yes")) {

   			DatePicker_Enhanced creditdate = new DatePicker_Enhanced();
   			creditdate.selectCalendar(slCreditApplicableDate,"creditApplicableDate");
	    	
		    addFieldValue("sl_create_page_credit_clause_textarea", slCreditClause.trim());

		    addFieldValue("sl_create_page_credit_percentage_of_invoice_numeric_box", slCreditOfInvoice.trim());
		    
		    addFieldValue("sl_create_page_sl_credit_percentage_category_numeric_box", slSLCreditCategory.trim());

		    addFieldValue("sl_create_page_sl_credit_percentage_sub_category_numeric_box", slSLCreditSubCategory.trim());
		    
		    addFieldValue("sl_create_page_sl_credit_percentage_line_item_numeric_box", slSLCreditLineItem.trim());
		    
		    addFieldValue("sl_create_page_credit_frequency_dropdown", slCreditFrequency.trim());
    		}
   		
		addFieldValue("sl_create_page_earnback_applicable_checkbox", slEarnbackApplicable.trim());
   		if (slEarnbackApplicable.equalsIgnoreCase("Yes")) {
   			DatePicker_Enhanced earnbackdate = new DatePicker_Enhanced();
   			earnbackdate.selectCalendar(slEarnbackApplicableDate,"earnbackApplicableDate");

		    addFieldValue("sl_create_page_earnback_clause_textarea", slEarnbackClause.trim());

		    addFieldValue("sl_create_page_earnback_percentage_category_numeric_box", slEarnbackCategory.trim());

		    addFieldValue("sl_create_page_earnback_percentage_sub_category_numeric_box", slEarnbackSubCategory.trim());
		    
		    addFieldValue("sl_create_page_earnback_percentage_line_item_numeric_box", slEarnbackLineItem.trim());
		    
		    addFieldValue("sl_create_page_earnback_frequency_dropdown", slEarnbackFrequency.trim());		    
    		}
	    
	    // SERVICE LEVELS - CREATE PAGE - CONTINUOUS IMPROVEMENT
		addFieldValue("sl_create_page_subject_to_continuous_improvement_checkbox", slSubjectTo.trim());
		if (slSubjectTo.equalsIgnoreCase("Yes"))
    		addFieldValue("sl_create_page_continuous_improvement_clause_textbox", slContinuousImprovementClause.trim());
	    
	    // SERVICE LEVELS - CREATE PAGE - IMPORTANT DATES
		addFieldValue("sl_create_page_frequency_dropdown", slFrequency.trim());
	    addFieldValue("sl_create_page_frequency_type_dropdown", slFrequencyType.trim());
	    addFieldValue("sl_create_page_computation_frequency_dropdown", slComputationFrequency);

	    addFieldValue("sl_create_page_reporting_frequency_type_dropdown", slReportingFrequencyType.trim());
	    addFieldValue("sl_create_page_reporting_frequency_dropdown", slReportingComputationFrequency);
	    driver.findElement(By.xpath("//*[@id='mainForm']/ng-form/fieldset[7]/p")).click();
	    DatePicker_Enhanced date = new DatePicker_Enhanced();
	    //date.selectCalendar(slStartDate,"startDate");
		date.selectCalendar(slEndDate,"expDate");
		date.selectCalendar(slEffectiveDate,"effectiveDate");
		date.selectCalendar(slPatternDate,"patternDate");
		date.selectCalendar(slReportingPatternDate,"reportingPatternDate");
		date.selectCalendar(slReportingEffectiveDate,"reportingEffectiveDate");
		
		
	    
	    // SERVICE LEVELS - CREATE PAGE - FUNCTIONS
		addFieldValue("sl_create_page_functions_multi_dropdown", slFunctions.trim());
		if (!slFunctions.equalsIgnoreCase(""))
			addFieldValue("sl_create_page_services_multi_dropdown", slServices.trim());
		
		addFieldValue("sl_create_page_service_category_multi_dropdown", slServiceCategory.trim());
		    
	    // SERVICE LEVELS - CREATE PAGE - GEOGRAPHY
		addFieldValue("sl_create_page_management_regions_multi_dropdown", slManagementRegions.trim());
		if (!slManagementRegions.equalsIgnoreCase(""))
			addFieldValue("sl_create_page_management_countries_multi_dropdown", slManagementCountries.trim());
		
		addFieldValue("sl_create_page_contract_regions_multi_dropdown", slContractRegions.trim());
		if (!slContractRegions.equalsIgnoreCase(""))
			addFieldValue("sl_create_page_contract_countries_multi_dropdown", slContractCountries.trim());

		// SERVICE LEVELS - CREATE PAGE - PROJECT INFORMATION
		addFieldValue("sl_create_page_project_id_multi_dropdown", slProjectID.trim());
			
		addFieldValue("sl_create_page_project_levels_multi_dropdown", slProjectLevels.trim());
			
		addFieldValue("sl_create_page_initiatives_multi_dropdown", slInitiatives.trim());
			
	    // SERVICE LEVELS - CREATE PAGE - CONTRACTING PARTY
		addFieldValue("sl_create_page_vendor_contracting_party_multi_dropdown", slVendorContractingParty.trim());

		addFieldValue("sl_create_page_contracting_client_entities_multi_dropdown", slContractingClientEntity.trim());
		if (!slContractingClientEntity.equalsIgnoreCase(""))
			addFieldValue("sl_create_page_contracting_company_codes_multi_dropdown", slContractingCompanyCode.trim());

		addFieldValue("sl_create_page_recipient_client_entities_multi_dropdown", slRecipientClientEntity.trim());
		if (!slRecipientClientEntity.equalsIgnoreCase(""))
			addFieldValue("sl_create_page_recipient_company_codes_multi_dropdown", slRecipientCompanyCode.trim());

		// SERVICE LEVELS - CREATE PAGE - STAKEHOLDERS
		addFieldValue("sl_create_page_responsibility_dropdown", slResponsibility.trim());
			
	    // SERVICE LEVELS - CREATE PAGE - FINANCIAL INFORMATION
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
		
		// SERVICE LEVELS - CREATE PAGE - COMMENTS AND ATTACHMENTS
		addFieldValue("entity_create_page_comments_textarea", slComments.trim());
		    
		addFieldValue("entity_create_page_requested_by_dropdown", slRequestedBy.trim());
		DatePicker_Enhanced Acdate = new DatePicker_Enhanced();
		Acdate.selectCalendar(slActualDate,"actualDate");
		    
		addFieldValue("entity_create_page_change_request_dropdown", slChangeRequest.trim());
		
		if (!slUploadFile.equalsIgnoreCase("")) {
			locateElementBy("entity_create_page_upload_browse_button").sendKeys(System.getProperty("user.dir")+"\\file-upload\\SL\\" + slUploadFile);

			}

		WebElement elementSave = driver.findElement(By.cssSelector("ng-form[template='tab.actions']")).findElement(By.cssSelector("div.submit.freeze"));		
		Assert.assertNotNull(elementSave.findElement(By.xpath(".//button[contains(.,'Create SL')]")));
	    ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", elementSave.findElement(By.xpath(".//button[contains(.,'Create SL')]")));
	    elementSave.findElement(By.xpath(".//button[contains(.,'Create SL')]")).click();


		// SERVICE LEVELS - CREATE PAGE - FIELD VALIDATIONS
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

		if (driver.findElements(By.className("success-icon")).size()!=0) {
			 String entityID = driver.findElement(By.className("success-icon")).findElement(By.id("hrefElemId")).getText();

			 Logger.debug("SL created successfully with Entity ID " + entityID);

			 driver.findElement(By.className("success-icon")).findElement(By.id("hrefElemId")).click();

			 fluentWaitMethod(locateElementBy("sl_show_page_id"));
				 
			 String entityShowPageID = locateElementBy("sl_show_page_id").getText();

			 Assert.assertEquals(entityShowPageID, entityID);
			 }
		
	    Logger.debug("Test Case Service Level Creation with Title -- " + slTitle + " -- is PASSED");
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