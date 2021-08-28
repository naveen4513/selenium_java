package office.sirion.suite.obligation;

import java.io.IOException;
import java.util.List;
import office.sirion.util.DatePicker_Enhanced;
import office.sirion.util.TestUtil;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
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

public class OBAuditLog extends TestSuiteBase {
	String result = null;
	String runmodes[] = null;
	static int count = -1;
	static boolean isTestPass = true;
   
	@BeforeTest
	public void checkTestSkip() {
		if (!TestUtil.isTestCaseRunnable(obligation_suite_xls, this.getClass().getSimpleName())) {
			Logger.debug("Skipping Test Case " + this.getClass().getSimpleName() + " as runmode set to NO");
			throw new SkipException("Skipping Test Case " + this.getClass().getSimpleName() + " as runmode set to NO");
			}
		runmodes = TestUtil.getDataSetRunmodes(obligation_suite_xls, this.getClass().getSimpleName());
		}

	@Test(dataProvider = "getTestData")
	public void OBAuditLogTest (String obTitle, String obDescription, String obPerformanceType, String obCategory, String obSubCategory,
			String obTimezone, String obDeliveryCountries, String obCurrency,	String obSupplierAccess, String obTier,	String obPriority, String obPhase,
			String obTriggered, String obReferences, String obClause,	String obPageNumber, String obFrequencyType, String obFrequency, String obWeekType,
			String obStartDate, String obEndDate,	String obIncludeStartDate, String obIncludeEndDate,	String obPatternDate, String obEffectiveDate,
			String obFunctions, String obServices, String obServiceCategory, String obManagementRegions, String obManagementCountries, String obContractRegions,
			String obContractCountries, String obVendorContractingParty, String obContractingClientEntity, String obContractingCompanyCode,
			String obRecipientClientEntity, String obRecipientCompanyCode, String obResponsibility, String obFinancialImpactApplicable, String obFinancialImpactValue,
			String obFinancialImpactClause, String obImpactDays, String obImpactType,	String obCreditImpactApplicable, String obCreditImpactValue,
			String obCreditImpactClause, String obProjectID, String obProjectLevels, String obInitiatives,
			String obComments, String obActualDate, String obRequestedBy, String obChangeRequest, String obUploadFile, String obParent
			) throws InterruptedException {
		
		count++;
		if (!runmodes[count].equalsIgnoreCase("Y"))
			throw new SkipException("Runmode for Test Data Set to NO -- " + count);
		
	
		openBrowser();
	    endUserLogin(CONFIG.getProperty("endUserURL"), CONFIG.getProperty("endUserUsername"), CONFIG.getProperty("endUserPassword"));
	    
	    Logger.debug("Executing Test Case Obligation Audit Log with Title -- " + obTitle + " under Obligation Parent -- " + obParent);
		
        driver.get(CONFIG.getProperty("endUserURL"));

        fluentWaitMethod(locateElementBy("ob_quick_link"));

        locateElementBy("ob_quick_link").click();

        fluentWaitMethod(driver.findElement(By.id("rightLinks")).findElement(By.linkText("VIEW OBLIGATIONS")));

        driver.findElement(By.id("rightLinks")).findElement(By.linkText("VIEW OBLIGATIONS")).click();

		fluentWaitMethod(locateElementBy("entity_listing_page_display_dropdown_link"));

		new Select(locateElementBy("entity_listing_page_display_dropdown_link")).selectByVisibleText(CONFIG.getProperty("maxEntityListingDropdown"));


		if (!obParent.equalsIgnoreCase("") && obParent.length()<=30)
			driver.findElement(By.xpath("//*[@id='cr']/tbody/tr/td[3]/a[./text()='"+obParent+"']")).click();
		else if (!obParent.equalsIgnoreCase("") && obParent.length()>30)
			driver.findElement(By.xpath("//*[@id='cr']/tbody/tr/td[3]/div/div/a[./text()='"+obParent+"']")).click();

		fluentWaitMethod(driver.findElement(By.cssSelector("ng-form[template='tab.actions']")).findElement(By.cssSelector("div.submit.freeze")));

		// OBLIGATIONS - SHOW PAGE - EDIT BUTTON
	    WebElement elementEdit = driver.findElement(By.cssSelector("ng-form[template='tab.actions']")).findElement(By.cssSelector("div.submit.freeze"));		
		Assert.assertNotNull(elementEdit.findElement(By.xpath(".//button[contains(.,'Edit')]")));
	    ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", elementEdit.findElement(By.xpath(".//button[contains(.,'Edit')]")));
	    elementEdit.findElement(By.xpath(".//button[contains(.,'Edit')]")).click();

		fluentWaitMethod(locateElementBy("ob_create_page_title_textbox"));
	    
        // OBLIGATIONS - EDIT PAGE - BASIC INFORMATION
        addFieldValue("ob_create_page_title_textbox", obTitle.trim());

        addFieldValue("ob_create_page_description_textarea", obDescription.trim());

        addFieldValue("ob_create_page_performance_type_dropdown", obPerformanceType.trim());

        addFieldValue("ob_create_page_category_dropdown", obCategory.trim());

        addFieldValue("ob_create_page_sub_category_dropdown", obSubCategory.trim());

        addFieldValue("ob_create_page_timezone_dropdown", obTimezone.trim());
        try {
        	if (driver.findElement(By.className("success-icon")).getText().contains("Current Date is different for the selected Time Zone"))
        		driver.findElement(By.xpath(".//button[contains(.,'OK')]")).click();
        	} catch (Exception e) {
        		}

        addFieldValue("ob_create_page_delivery_countries_multi_dropdown", obDeliveryCountries.trim());

        addFieldValue("ob_create_page_currency_dropdown", obCurrency.trim());

        addFieldValue("ob_create_page_supplier_access_checkbox", obSupplierAccess.trim());

        addFieldValue("ob_create_page_tier_dropdown", obTier.trim());

        // OBLIGATIONS - EDIT PAGE - OTHER INFORMATION
        addFieldValue("ob_create_page_priority_dropdown", obPriority.trim());

        addFieldValue("ob_create_page_phase_dropdown", obPhase.trim());

        addFieldValue("ob_create_page_references_dropdown", obReferences.trim());

        addFieldValue("ob_create_page_clause_textbox", obClause.trim());

        addFieldValue("ob_create_page_page_number_numeric_box", obPageNumber.trim());

        addFieldValue("ob_create_page_triggered_checkbox", obTriggered.trim());
        
        DatePicker_Enhanced date = new DatePicker_Enhanced();
        
        if (!obTriggered.equalsIgnoreCase("Yes")) {
           
        	// OBLIGATIONS - EDIT PAGE - IMPORTANT DATES
        	addFieldValue("ob_create_page_frequency_type_dropdown", obFrequencyType.trim());

            addFieldValue("ob_create_page_frequency_dropdown", obFrequency.trim());

            addFieldValue("ob_create_page_week_type_dropdown", obWeekType.trim());

            addFieldValue("ob_create_page_start_date", obStartDate.trim());

            addFieldValue("ob_create_page_end_date", obEndDate.trim());

            addFieldValue("ob_create_page_include_start_date_checkbox", obIncludeStartDate.trim());

            addFieldValue("ob_create_page_include_end_date_checkbox", obIncludeEndDate.trim());

            addFieldValue("ob_create_page_pattern_date", obPatternDate.trim());

            addFieldValue("ob_create_page_effective_date", obEffectiveDate.trim());

                        }

        // OBLIGATIONS - EDIT PAGE - FUNCTIONS
        addFieldValue("ob_create_page_functions_multi_dropdown", obFunctions.trim());

        addFieldValue("ob_create_page_services_multi_dropdown", obServices.trim());

        addFieldValue("ob_create_page_service_category_multi_dropdown", obServiceCategory.trim());

        // OBLIGATIONS - EDIT PAGE - GEOGRAPHY
        addFieldValue("ob_create_page_management_regions_multi_dropdown", obManagementRegions.trim());

        addFieldValue("ob_create_page_management_countries_multi_dropdown", obManagementCountries.trim());

        addFieldValue("ob_create_page_contract_regions_multi_dropdown", obContractRegions.trim());

        addFieldValue("ob_create_page_contract_countries_multi_dropdown", obContractCountries.trim());

        // OBLIGATIONS - EDIT PAGE - CONTRACTING PARTY
        addFieldValue("ob_create_page_vendor_contracting_party_multi_dropdown", obVendorContractingParty.trim());

        addFieldValue("ob_create_page_recipient_client_entities_multi_dropdown", obRecipientClientEntity.trim());

        addFieldValue("ob_create_page_recipient_company_codes_multi_dropdown", obRecipientCompanyCode.trim());

        addFieldValue("ob_create_page_contracting_client_entities_multi_dropdown", obContractingClientEntity.trim());

        addFieldValue("ob_create_page_contracting_company_codes_multi_dropdown", obContractingCompanyCode.trim());

        // OBLIGATIONS - EDIT PAGE - STAKEHOLDERS
        addFieldValue("ob_create_page_responsibility_dropdown", obResponsibility.trim());

        // OBLIGATIONS - EDIT PAGE - FINANCIAL INFORMATION
        addFieldValue("ob_create_page_financial_impact_applicable_checkbox", obFinancialImpactApplicable.trim());
        if (obFinancialImpactApplicable.equalsIgnoreCase("Yes")) {
            addFieldValue("ob_create_page_financial_impact_value_numeric_box", obFinancialImpactValue.trim());

	        addFieldValue("ob_create_page_financial_impact_clause_textarea", obFinancialImpactClause.trim());

            addFieldValue("ob_create_page_impact_days_numeric_box", obImpactDays.trim());

            addFieldValue("ob_create_page_impact_type_multi_dropdown", obImpactType.trim());
        	}

        addFieldValue("ob_create_page_credit_impact_applicable_checkbox", obCreditImpactApplicable.trim());
        if (obCreditImpactApplicable.equalsIgnoreCase("Yes")) {
            addFieldValue("ob_create_page_credit_impact_value_numeric_box", obCreditImpactValue.trim());

            addFieldValue("ob_create_page_credit_impact_clause_textarea", obCreditImpactClause.trim());
        	}

        // OBLIGATIONS - EDIT PAGE - PROJECT INFORMATION
        addFieldValue("ob_create_page_project_id_multi_dropdown", obProjectID.trim());

        addFieldValue("ob_create_page_project_levels_multi_dropdown", obProjectLevels.trim());

        addFieldValue("ob_create_page_initiatives_multi_dropdown", obInitiatives.trim());

        // OBLIGATIONS - EDIT PAGE - COMMENTS AND ATTACHMENTS
        addFieldValue("entity_create_page_comments_textarea", obComments.trim());

        addFieldValue("entity_create_page_requested_by_dropdown", obRequestedBy.trim());

        date.selectCalendar(obActualDate,"actualDate");

        addFieldValue("entity_create_page_change_request_dropdown", obChangeRequest.trim());

        if (!obUploadFile.equalsIgnoreCase("")) {
            locateElementBy("entity_create_page_upload_browse_button").sendKeys(System.getProperty("user.dir")+"\\file-upload\\Obligation\\" + obUploadFile);

        	}
        locateElementBy("entity_create_page_privacy_checkbox").findElement(By.tagName("input")).click();;

		// OBLIGATIONS - EDIT PAGE - UPDATE BUTTON
	    WebElement elementUpdate = driver.findElement(By.cssSelector("ng-form[template='tab.actions']")).findElement(By.cssSelector("div.submit.freeze"));		
		Assert.assertNotNull(elementUpdate.findElement(By.xpath(".//button[contains(.,'Update')]")));
	    ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", elementUpdate.findElement(By.xpath(".//button[contains(.,'Update')]")));
	    elementUpdate.findElement(By.xpath(".//button[contains(.,'Update')]")).click();


		// OBLIGATIONS - EDIT PAGE - FIELD VALIDATIONS
		List<WebElement> validationClassElementList = driver.findElements(By.xpath(".//*[@class='errorMessageHolder']/ul/li"));
		if (!validationClassElementList.isEmpty()) {
			for (WebElement validationClassElement : validationClassElementList) {
				String validationMessage = validationClassElement.getText();
				
				WebElement elementParent = validationClassElement.findElement(By.xpath("..")).findElement(By.xpath("..")).findElement(By.xpath("..")).findElement(By.xpath(".."));
				
				if (validationMessage.equalsIgnoreCase(""))
					Logger.debug("For Obligations -- " + elementParent.findElement(By.className("errorClass")).getAttribute("name").toUpperCase() + " -- is Mandatory");
				else
					Logger.debug("For Obligations -- " + elementParent.findElement(By.className("errorClass")).getAttribute("name").toUpperCase() + " -- " + validationMessage);
				}

			driver.get(CONFIG.getProperty("endUserURL"));
			return;
			}
	    fluentWaitMethod(locateElementBy("ob_create_page_title_textbox"));

		String entityShowPageName = locateElementBy("ob_create_page_title_textbox").getText();

		Assert.assertEquals(entityShowPageName, obTitle);

	    driver.findElement(By.linkText("AUDIT LOG")).click();

	    Logger.debug("Test Case Obligation Audit Log with Title -- " + obTitle + " -- is STARTED");

		fluentWaitMethod(driver.findElement(By.id("table_330")));
		WebElement elementOddRowSelected = driver.findElement(By.id("table_330")).findElements(By.className("odd")).get(0);
		
		Assert.assertEquals(elementOddRowSelected.findElement(By.cssSelector("td.col_wrap_action_name")).getText(), "Updated");

	    if (!obRequestedBy.equalsIgnoreCase(""))
			Assert.assertEquals(elementOddRowSelected.findElement(By.cssSelector("td.col_wrap_requested_by")).getText(), obRequestedBy);
	    else
	    	Assert.assertEquals(elementOddRowSelected.findElement(By.cssSelector("td.col_wrap_requested_by")).getText(), CONFIG.getProperty("endUserFullName"));

	    Assert.assertEquals(elementOddRowSelected.findElement(By.cssSelector("td.col_wrap_completed_by")).getText(), CONFIG.getProperty("endUserFullName"));

	    if (!obComments.equalsIgnoreCase(""))
		    Assert.assertEquals(elementOddRowSelected.findElement(By.cssSelector("td.col_wrap_comment")).getText(), "Yes");
	    else
		    Assert.assertEquals(elementOddRowSelected.findElement(By.cssSelector("td.col_wrap_comment")).getText(), "No");

	    if (!obUploadFile.equalsIgnoreCase(""))
		    Assert.assertEquals(elementOddRowSelected.findElement(By.cssSelector("td.col_wrap_document")).getText(), "Yes");
	    else
		    Assert.assertEquals(elementOddRowSelected.findElement(By.cssSelector("td.col_wrap_document")).getText(), "No");

	    elementOddRowSelected.findElement(By.cssSelector("td.col_wrap_history")).findElement(By.className("serverHistoryView")).click();


		List<WebElement> auditRowElementList = driver.findElement(By.id("historyDataTable")).findElement(By.id("data")).findElements(By.tagName("tr"));
	    for (int iAuditRow = 0; iAuditRow < auditRowElementList.size(); iAuditRow++) {
		      for (int jAuditCol = 0; jAuditCol < auditRowElementList.get(0).findElements(By.tagName("td")).size(); jAuditCol++) {
		    	  // OBLIGATIONS - BASIC INFORMATION
			      if (auditRowElementList.get(iAuditRow).findElements(By.tagName("td")).get(jAuditCol).getText().equalsIgnoreCase("Title")) {
				         List<WebElement> elementDataList = auditRowElementList.get(iAuditRow).findElements(By.tagName("td"));
				         Assert.assertEquals(elementDataList.get(3).getText(), obTitle);
				         }

			      else if (auditRowElementList.get(iAuditRow).findElements(By.tagName("td")).get(jAuditCol).getText().equalsIgnoreCase("Description")) {
				         List<WebElement> elementDataList = auditRowElementList.get(iAuditRow).findElements(By.tagName("td"));
				         Assert.assertEquals(elementDataList.get(3).getText(), obDescription);
				         }   
	
			      else if (auditRowElementList.get(iAuditRow).findElements(By.tagName("td")).get(jAuditCol).getText().equalsIgnoreCase("Category")) {
				         List<WebElement> elementDataList = auditRowElementList.get(iAuditRow).findElements(By.tagName("td"));
				         Assert.assertEquals(elementDataList.get(3).getText(), obCategory);
				         }   

			      else if (auditRowElementList.get(iAuditRow).findElements(By.tagName("td")).get(jAuditCol).getText().equalsIgnoreCase("Sub-Category")) {
				         List<WebElement> elementDataList = auditRowElementList.get(iAuditRow).findElements(By.tagName("td"));
				         Assert.assertEquals(elementDataList.get(3).getText(), obSubCategory);
				         }   

			      else if (auditRowElementList.get(iAuditRow).findElements(By.tagName("td")).get(jAuditCol).getText().equalsIgnoreCase("Performance Type")) {
				         List<WebElement> elementDataList = auditRowElementList.get(iAuditRow).findElements(By.tagName("td"));
				         Assert.assertEquals(elementDataList.get(3).getText(), obPerformanceType);
				         }   

			      else if (auditRowElementList.get(iAuditRow).findElements(By.tagName("td")).get(jAuditCol).getText().equalsIgnoreCase("Time Zone")) {
				         List<WebElement> elementDataList = auditRowElementList.get(iAuditRow).findElements(By.tagName("td"));
				         Assert.assertEquals(elementDataList.get(3).getText(), obTimezone);
				         }   

			      else if (auditRowElementList.get(iAuditRow).findElements(By.tagName("td")).get(jAuditCol).getText().equalsIgnoreCase("Supplier Access")) {
				         List<WebElement> elementDataList = auditRowElementList.get(iAuditRow).findElements(By.tagName("td"));
				         Assert.assertEquals(elementDataList.get(3).getText(), obSupplierAccess);
				         }   

			      else if (auditRowElementList.get(iAuditRow).findElements(By.tagName("td")).get(jAuditCol).getText().equalsIgnoreCase("Tier")) {
				         List<WebElement> elementDataList = auditRowElementList.get(iAuditRow).findElements(By.tagName("td"));
				         Assert.assertEquals(elementDataList.get(3).getText(), obTier);
				         }   
		      		}
		      }

	    Logger.debug("Test Case Obligation Audit Log with Title -- " + obTitle + " -- is PASSED");
        driver.get(CONFIG.getProperty("endUserURL"));
	    }
	
	 @AfterMethod
		public void reportDataSetResult(ITestResult testResult) throws IOException {
			takeScreenShotOnFailure(testResult.getStatus(), this.getClass().getSimpleName());

			if(testResult.getStatus()==ITestResult.SKIP)
				TestUtil.reportDataSetResult(obligation_suite_xls, this.getClass().getSimpleName(), count+2, "SKIP");
			else if(testResult.getStatus()==ITestResult.FAILURE) {
				isTestPass=false;
				TestUtil.reportDataSetResult(obligation_suite_xls, this.getClass().getSimpleName(), count+2, "FAIL");
				result= "Fail";
				}
			else if (testResult.getStatus()==ITestResult.SUCCESS) {
				TestUtil.reportDataSetResult(obligation_suite_xls, this.getClass().getSimpleName(), count+2, "PASS");
				result= "Pass";
				}
			try {
	          for (int i = 2; i <= obligation_suite_xls.getRowCount(this.getClass().getSimpleName()); i++) {
	              String testCaseID = obligation_suite_xls.getCellData(this.getClass().getSimpleName(), "testCaseID", i);
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
			TestUtil.reportDataSetResult(obligation_suite_xls, "Test Cases", TestUtil.getRowNum(obligation_suite_xls, this.getClass().getSimpleName()),"PASS");
		else
			TestUtil.reportDataSetResult(obligation_suite_xls, "Test Cases", TestUtil.getRowNum(obligation_suite_xls, this.getClass().getSimpleName()),"FAIL");
		}

	@DataProvider
	public Object[][] getTestData() {
		return TestUtil.getData(obligation_suite_xls, this.getClass().getSimpleName());
		}
	}