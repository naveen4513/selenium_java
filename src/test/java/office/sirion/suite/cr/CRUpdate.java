package office.sirion.suite.cr;

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

public class CRUpdate extends TestSuiteBase {
	String result = null;
	String runmodes[] = null;
	static int count = -1;
	static boolean fail = false;
	static boolean skip = false;
	static boolean isTestPass = true;
	
	@BeforeTest
	public void checkTestSkip() {
		if (!TestUtil.isTestCaseRunnable(cr_suite_xls, this.getClass().getSimpleName())) {
			Logger.debug("Skipping Test Case " + this.getClass().getSimpleName() + " as runmode set to NO");
			throw new SkipException("Skipping Test Case " + this.getClass().getSimpleName() + " as runmode set to NO");
			}
		runmodes = TestUtil.getDataSetRunmodes(cr_suite_xls, this.getClass().getSimpleName());
		}

  @Test(dataProvider = "getTestData")
  public void CRUpdateTest (String crTitle, String crDescription, String crContractReferences, String crTimezone, String crClass, String crType,
		  String crPriority, String crResponsibility, String crAssumptions, String crID, String crContractingEntity, String crSupplierAccess, String crTier,
		  String crDependentEntity, String crRecipientClientEntity, String crRecipientCompanyCode, String crContractingClientEntity, String crContractingCompanyCode,
		  String crCRDate, String crEffectiveDate, String crPlannedCompletionDate, String crFunctions, String crServices, String crServiceCategory,
		  String crManagementRegions, String crManagementCountries, String crContractRegions, String crContractCountries, String crOriginalTCV, String crOriginalTCVCurrency,  String crRevisedTCV,
		  String crRevisedTCVCurrency, String crVarianceTCV, String crVarianceTCVCurrency, String crOriginalACV, String crOriginalACVCurrency, String crRevisedACV,
		  String crRevisedACVCurrency, String crVarianceACV, String crVarianceACVCurrency, String crOriginalFACV, String crOriginalFACVCurrency, String crRevisedFACV,
		  String crRevisedFACVCurrency, String crVarianceFACV, String crVarianceFACVCurrency, String crProjectID, String crProjectLevels, String crInitiatives,
		  String crComments, String crActualDate, String crRequestedBy, String crChangeRequest, String crUploadFile,
		  String crParent) throws InterruptedException {
	  
		count++;
		if (!runmodes[count].equalsIgnoreCase("Y")) {
			skip = true;
			throw new SkipException("Runmode for Test Data Set to NO -- " + count);
			}
		
		openBrowser();
	    endUserLogin(CONFIG.getProperty("endUserURL"), CONFIG.getProperty("endUserUsername"), CONFIG.getProperty("endUserPassword"));
	    
	    Logger.debug("Executing Test Case CR Update with Title ---- " + crTitle + " under CR Parent ---- " + crParent);
		
	    driver.get(CONFIG.getProperty("endUserURL"));
		fluentWaitMethod(locateElementBy("cr_quick_link"));

	    
	    locateElementBy("cr_quick_link").click();

		fluentWaitMethod(locateElementBy("entity_listing_page_display_dropdown_link"));
		
		new Select(locateElementBy("entity_listing_page_display_dropdown_link")).selectByVisibleText("100");

		
		if (!crParent.equalsIgnoreCase("")) {
			try {
				driver.findElement(By.xpath(".//*[@id='cr']/tbody/tr[@role='row']/td[contains(.,'" + crParent + "')]/preceding-sibling::td[2]/a")).click();
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
	    fluentWaitMethod(locateElementBy("cr_create_page_title_textbox"));
    

	    // Change Request - Edit Page - BASIC INFORMATION
	    addFieldValue("cr_create_page_title_textbox", crTitle);
	    addFieldValue("cr_create_page_description_textarea", crDescription);
	    addFieldValue("cr_create_page_contract_references_textarea", crContractReferences);
	    addFieldValue("cr_create_page_timezone_dropdown", crTimezone);
	    try {
	    	if (driver.findElement(By.className("success-icon")).getText().contains("Current Date is different for the selected Time Zone"))
	    		driver.findElement(By.xpath(".//button[contains(.,'OK')]")).click();
	    	} catch (Exception e) {
	    			}
	    
	    addFieldValue("cr_create_page_cr_class_dropdown", crClass);
	    addFieldValue("cr_create_page_cr_type_dropdown", crType);
	    addFieldValue("cr_create_page_priority_dropdown", crPriority);
	    addFieldValue("cr_create_page_responsibility_dropdown", crResponsibility);
	    addFieldValue("cr_create_page_assumptions_textarea", crAssumptions);
	    addFieldValue("cr_create_page_cr_id_textbox", crID);
	    addFieldValue("cr_create_page_contracting_entity_dropdown", crContractingEntity);
	    addFieldValue("cr_create_page_supplier_access_checkbox", crSupplierAccess);
	    addFieldValue("cr_create_page_tier_dropdown", crTier);
	    addFieldValue("cr_create_page_dependent_entity_dropdown", crDependentEntity);
	    
		// Change Request - Edit Page - CONTRACTING ENTITY
		addFieldValue("cr_create_page_recipient_client_entities_multi_dropdown", crRecipientClientEntity);
		addFieldValue("cr_create_page_recipient_company_codes_multi_dropdown", crRecipientCompanyCode);
		addFieldValue("cr_create_page_contracting_client_entities_multi_dropdown", crContractingClientEntity);
		addFieldValue("cr_create_page_contracting_company_codes_multi_dropdown", crContractingCompanyCode);

	    // Change Request - Edit Page - IMPORTANT DATES   
		DatePicker_Enhanced date = new DatePicker_Enhanced();
		date.selectCalendar(crEffectiveDate,"effectiveDate");
		date.selectCalendar(crPlannedCompletionDate,"plannedCompletionDate");
		date.selectCalendar(crCRDate,"crDate");
	    
		// Change Request - Edit Page - FUNCTIONS
		addFieldValue("cr_create_page_functions_multi_dropdown", crFunctions);	  	
		addFieldValue("cr_create_page_services_multi_dropdown", crServices);
		addFieldValue("cr_create_page_service_category_multi_dropdown", crServiceCategory);
		    
		// Change Request - Edit Page - GEOGRAPHY
		addFieldValue("cr_create_page_management_regions_multi_dropdown", crManagementRegions);		
		addFieldValue("cr_create_page_management_countries_multi_dropdown", crManagementCountries);
		addFieldValue("cr_create_page_contract_regions_multi_dropdown", crContractRegions);		
		addFieldValue("cr_create_page_contract_countries_multi_dropdown", crContractCountries);
		
		// Change Request - Edit Page - FINANCIAL INFORMATION
	   	addFieldValue("cr_create_page_original_tcv_numeric", crOriginalTCV);
		addFieldValue("cr_create_page_original_tcv_currency_dropdown", crOriginalTCVCurrency);
	   	addFieldValue("cr_create_page_revised_tcv_numeric", crRevisedTCV);
		addFieldValue("cr_create_page_revised_tcv_currency_dropdown", crRevisedTCVCurrency);
	   	addFieldValue("cr_create_page_variance_tcv_numeric", crVarianceTCV);
		addFieldValue("cr_create_page_variance_tcv_currency_dropdown", crVarianceTCVCurrency);
	   	addFieldValue("cr_create_page_original_acv_numeric", crOriginalACV);
		addFieldValue("cr_create_page_original_acv_currency_dropdown", crOriginalACVCurrency);
	   	addFieldValue("cr_create_page_revised_acv_numeric", crRevisedACV);
		addFieldValue("cr_create_page_revised_acv_currency_dropdown", crRevisedACVCurrency);
	   	addFieldValue("cr_create_page_variance_acv_numeric", crVarianceACV);
		addFieldValue("cr_create_page_variance_acv_currency_dropdown", crVarianceACVCurrency);	
	   	addFieldValue("cr_create_page_original_facv_numeric", crOriginalFACV);
		addFieldValue("cr_create_page_original_facv_currency_dropdown", crOriginalFACVCurrency);
	   	addFieldValue("cr_create_page_revised_facv_numeric", crRevisedFACV);
		addFieldValue("cr_create_page_revised_facv_currency_dropdown", crRevisedFACVCurrency);	
	   	addFieldValue("cr_create_page_variance_facv_numeric", crVarianceFACV);
		addFieldValue("cr_create_page_variance_facv_currency_dropdown", crVarianceFACVCurrency);

		// Change Request - Edit Page - PROJECT INFORMATION
		addFieldValue("cr_create_page_project_id_multi_dropdown", crProjectID);
		addFieldValue("cr_create_page_project_levels_multi_dropdown", crProjectLevels);
		addFieldValue("cr_create_page_initiatives_multi_dropdown", crInitiatives);

		// Change Request - Edit Page - COMMENTS AND ATTACHMENTS
		addFieldValue("entity_create_page_comments_textarea", crComments);  
		addFieldValue("entity_create_page_requested_by_dropdown", crRequestedBy);
		    
		date.selectCalendar(crActualDate,"actualDate");
		    
		addFieldValue("entity_create_page_change_request_dropdown", crChangeRequest);
		
		if (!crUploadFile.equalsIgnoreCase("")) {
			locateElementBy("entity_create_page_upload_browse_button").sendKeys(System.getProperty("user.dir")+"\\file-upload\\CR\\" + crUploadFile);

			}
		
	    Assert.assertNotNull(driver.findElement(By.xpath("//button[contains(.,'Update')]")));
	    ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", driver.findElement(By.xpath("//button[contains(.,'Update')]")));
	    driver.findElement(By.xpath("//button[contains(.,'Update')]")).click();

		
	 // Change Request - Update Page - VALIDATIONS
	 		if (crTitle.equalsIgnoreCase("")) {
	 		   	fail = true;
	 		   	if (fieldValidationMandatory("cr_create_page_title_textbox")) {
	 		   	Logger.debug("CR Title is Mandatory");
	 		   		
	 		   		fail = false;
	 		   		driver.get(CONFIG.getProperty("endUserURL"));
	 		   		return;
	 		   		}
	 		   	}

	 		if (crFunctions.equalsIgnoreCase("")) {
	 		   	fail = true;
	 		    	
	 		   	if (fieldValidationMandatory("cr_create_page_functions_multi_dropdown")) {
	 		   	Logger.debug("CR Function is Mandatory");
	 		   		
	 		   		fail = false;
	 		   		driver.get(CONFIG.getProperty("endUserURL"));
	 		   		return;
	 		   		}
	 		   	}
	 		
	 		if (crServices.equalsIgnoreCase("")) {
	 		   	fail = true;
	 		    	
	 		   	if (fieldValidationMandatory("cr_create_page_services_multi_dropdown")) {
	 		   	Logger.debug("CR Service is Mandatory");
	 		   		
	 		   		fail = false;
	 		   		driver.get(CONFIG.getProperty("endUserURL"));
	 		   		return;
	 		   		}
	 		   	}

	 		if (crManagementRegions.equalsIgnoreCase("")) {
	 		   	fail = true;
	 		    	
	 		   	if (fieldValidationMandatory("cr_create_page_management_regions_multi_dropdown")) {
	 		   	Logger.debug("CR Region is Mandatory");
	 		   		
	 		   		fail = false;
	 		   		driver.get(CONFIG.getProperty("endUserURL"));
	 		   		return;
	 		   		}
	 		   	}
	 		
	 		if (crManagementCountries.equalsIgnoreCase("")) {
	 		   	fail = true;
	 		    	
	 		   	if (fieldValidationMandatory("cr_create_page_management_countries_multi_dropdown")) {
	 		   	Logger.debug("CR Countries is Mandatory");
	 		   		
	 		   		fail = false;
	 		   		driver.get(CONFIG.getProperty("endUserURL"));
	 		   		return;
	 		   		}
	 		   	}
	 		
	 		List<WebElement> validationClassElementList = driver.findElements(By.xpath(".//*[@class='errorMessageHolder']/ul/li"));
			if (!validationClassElementList.isEmpty()) {
				for (WebElement validationClassElement : validationClassElementList) {
					String validationMessage = validationClassElement.getText();

					WebElement elementParent = validationClassElement.findElement(By.xpath("..")).findElement(By.xpath("..")).findElement(By.xpath("..")).findElement(By.xpath(".."));

					if (validationMessage.equalsIgnoreCase(""))
						Logger.debug("For CR -- " + elementParent.findElement(By.className("errorClass")).getAttribute("name").toUpperCase() + " -- is Mandatory");
					else
						Logger.debug("For CR -- " + elementParent.findElement(By.className("errorClass")).getAttribute("name").toUpperCase() + " -- " + validationMessage);
					
					if(validationMessage.equalsIgnoreCase("Please select a date in past")) {
						Logger.debug("For CR -- " + elementParent.findElement(By.className("errorClass")).getAttribute("name").toUpperCase() + " -- Please select date in past");
					}else {
						Logger.debug("For CR -- " + elementParent.findElement(By.className("errorClass")).getAttribute("name").toUpperCase() + " -- "+validationMessage);
					}
					}
			}
	    
	    fluentWaitMethod(locateElementBy("cr_show_page_title"));

		String entityShowPageName = locateElementBy("cr_show_page_title").getText();

		Assert.assertEquals(entityShowPageName, crTitle);
		Logger.debug("CR Title on show page has been verified");
		
	
		driver.get(CONFIG.getProperty("endUserURL"));
	    }

  @AfterMethod
	public void reportDataSetResult(ITestResult testResult) throws IOException {
		takeScreenShotOnFailure(testResult.getStatus(), this.getClass().getSimpleName());

		if(testResult.getStatus()==ITestResult.SKIP)
			TestUtil.reportDataSetResult(cr_suite_xls, this.getClass().getSimpleName(), count+2, "SKIP");
		else if(testResult.getStatus()==ITestResult.FAILURE) {
			isTestPass=false;
			TestUtil.reportDataSetResult(cr_suite_xls, this.getClass().getSimpleName(), count+2, "FAIL");
			result= "Fail";
		}
		else if (testResult.getStatus()== ITestResult.SUCCESS) {
			TestUtil.reportDataSetResult(cr_suite_xls, this.getClass().getSimpleName(), count+2, "PASS");
			result= "Pass";
		}
		try {
			for (int i = 2; i <= cr_suite_xls.getRowCount(this.getClass().getSimpleName()); i++) {
				String testCaseID = cr_suite_xls.getCellData(this.getClass().getSimpleName(), "testCaseID", i);
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
		  TestUtil.reportDataSetResult(cr_suite_xls, "Test Cases", TestUtil.getRowNum(cr_suite_xls, this.getClass().getSimpleName()), "PASS");
	  else
		  TestUtil.reportDataSetResult(cr_suite_xls, "Test Cases", TestUtil.getRowNum(cr_suite_xls, this.getClass().getSimpleName()), "FAIL");
	  }
  
  @DataProvider
  public Object[][] getTestData() {
    return TestUtil.getData(cr_suite_xls, this.getClass().getSimpleName());
  }
}
