package office.sirion.suite.interpretation;

import java.io.IOException;
import java.util.List;
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
import office.sirion.util.DatePicker_Enhanced;
import office.sirion.util.TestUtil;

public class InterpretationCreation extends TestSuiteBase {
	String result = null;
	String runmodes[] = null;
	static int count = -1;
	static boolean fail = false;
	static boolean skip = false;
	static boolean isTestPass = true;

	@BeforeTest
	public void checkTestSkip() {
		if (!TestUtil.isTestCaseRunnable(int_suite_xls, this.getClass().getSimpleName())) {
			Logger.debug("Skipping Test Case " + this.getClass().getSimpleName() + " as runmode set to NO");
			throw new SkipException("Skipping Test Case " + this.getClass().getSimpleName() + " as runmode set to NO");
			}
		runmodes = TestUtil.getDataSetRunmodes(int_suite_xls, this.getClass().getSimpleName());
		}
	
	@Test(dataProvider = "getTestData")
	public void InterpretationCreationTest (String ipTitle, String ipBackground, String ipType, String ipPriority, String ipTimezone, String ipTier,
			String ipSupplierAccess, String ipDependentEntity, String ipQuestion, String ipIncludeInFAQ, String ContractReference, String ReferenceClause,
			String ReferencePageNumber, String ipRecipientClientEntity, String ipRecipientCompanyCode, String ipContractingClientEntity, String ipContractingCompanyCode,
			String ipRequestedDateDate, String ipPlannedSubmissionDateDate, String ipFunction, String ipService, String ipServiceCategory, String ipManagementRegions,
			String ipManagementCountries, String ipContractRegions, String ipContractCountries, String ipProjectID, String ipProjectLevels, String ipInitiatives,
			String ipComments, String ipActualDate, String ipRequestedBy, String ipChangeRequest, String ipUploadFile,
			String ipSupplier, String ipSourceType, String ipSourceName
			) throws InterruptedException {
		
		count++;
		if (!runmodes[count].equalsIgnoreCase("Y")) {
			skip = true;
			throw new SkipException("Runmode for Test Data Set to NO -- " + count);
			}

		openBrowser();
	    endUserLogin(CONFIG.getProperty("endUserURL"), CONFIG.getProperty("endUserUsername"), CONFIG.getProperty("endUserPassword"));
	    
	    Logger.debug("Executing Test Case Interpretation Creation with Title ---- " + ipTitle + " under Source Type ---- " + ipSourceType);
		
	    driver.get(CONFIG.getProperty("endUserURL"));
		fluentWaitMethod(locateElementBy("ip_quick_link"));

	    
	    locateElementBy("ip_quick_link").click();

		fluentWaitMethod(locateElementBy("ip_plus_icon"));
	    
	    locateElementBy("ip_plus_icon").click();


		addFieldValue("entity_global_create_page_supplier_dropdown", ipSupplier.trim());

		addFieldValue("entity_global_create_page_source_type_dropdown", ipSourceType.trim());
	    
		addFieldValue("entity_global_create_page_source_name_title_dropdown", ipSourceName.trim());

	    Assert.assertNotNull(driver.findElement(By.xpath("//button[contains(.,'Submit')]")));
	    ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", driver.findElement(By.xpath("//button[contains(.,'Submit')]")));
	    driver.findElement(By.xpath("//button[contains(.,'Submit')]")).click();

	    fluentWaitMethod(locateElementBy("ip_create_page_title_textbox"));

		// Interpretation - Create Page - BASIC INFORMATION

		addFieldValue("ip_create_page_title_textbox", ipTitle);
		addFieldValue("ip_create_page_background_textarea", ipBackground);
		locateElementBy("issue_create_page_blank_area_label").click();
		addFieldValue("ip_create_page_type_dropdown", ipType);
		addFieldValue("ip_create_page_priority_dropdown", ipPriority);
		addFieldValue("ip_create_page_timezone_dropdown", ipTimezone);
		try {
			if (driver.findElement(By.className("success-icon")).getText().contains("Current Date is different for the selected Time Zone"))
				driver.findElement(By.xpath(".//button[contains(.,'OK')]")).click();
		} catch (Exception e) {
		}
		addFieldValue("ip_create_page_supplieraccess_checkbox", ipSupplierAccess);
		try {
            addFieldValue("ip_create_page_tier_dropdown", ipTier);
        }catch (NoSuchElementException e){
		     new Select (locateElementBy("ip_create_page_tier_dropdown")).selectByIndex(1);
        }
		addFieldValue("ip_create_page_depentity_dropdown", ipDependentEntity);
		
		//Interpretation - Create Page - QUESTIONS
		
		addFieldValue("ip_create_page_question_textarea", ipQuestion);
		addFieldValue("ip_create_page_includeinfaq_checkbox", ipIncludeInFAQ);
		
		
		 if (!ContractReference.equalsIgnoreCase("")) {
		    	new Select(locateElementBy("sl_create_page_references_dropdown")).selectByVisibleText(ContractReference.trim());

		    	addFieldValue("sl_create_page_clause_textbox", ReferenceClause);
		    	
		    	addFieldValue("sl_create_page_page_number_numeric_box", ReferencePageNumber);
		    	}
		
		//Interpretation - Create Page - Contrating Entity

		addFieldValue("ip_create_page_recipient_client_entity_multiselect", ipRecipientClientEntity);
		addFieldValue("ip_create_page_recipient_company_code_multiselect", ipRecipientCompanyCode);
		addFieldValue("ip_create_page_contracting_client_entity_multiselect", ipContractingClientEntity);
		addFieldValue("ip_create_page_contracting_company_code_multiselect", ipContractingCompanyCode);

		// Interpretation - Create Page - IMPORTANT DATES
		
		DatePicker_Enhanced date = new DatePicker_Enhanced();
		date.selectCalendar(ipRequestedDateDate,"requestDate");
		date.selectCalendar(ipPlannedSubmissionDateDate,"plannedSubmissionDate");

		// Interpretations - Create Page - FUNCTIONS
		addFieldValue("ip_create_page_functions_multiselect", ipFunction);
		addFieldValue("ip_create_page_services_multiselect", ipService);
		
//		addFieldValue("ip_create_page_service_category_multiselect", ipServiceCategory);
		    
		// Interpretations - Create Page - GEOGRAPHY
		addFieldValue("ip_create_page_management_regions_multiselect", ipManagementRegions);
		addFieldValue("ip_create_page_management_countries_multiselect", ipManagementCountries);
		addFieldValue("ip_create_page_contract_regions_multiselect", ipContractRegions);
		addFieldValue("ip_create_page_contract_countries_multiselect", ipContractCountries);

		//issues Create Page - PROJECT INFORMATION
		addFieldValue("ip_create_page_project_id_multiselect", ipProjectID);
		addFieldValue("ip_create_page_project_levels_multiselect", ipProjectLevels);
		addFieldValue("ip_create_page_initiatives_multiselect", ipInitiatives);

		if (!ContractReference.equalsIgnoreCase("") && !ReferencePageNumber.equalsIgnoreCase("")) {
			if (locateElementBy("ip_create_page_page_no_textbox").getAttribute("maxvalue").equalsIgnoreCase("UNINDEXED")) {
				String Error = locateElementBy("ip_create_page_references_dropdown_errors").getText();

		        Assert.assertEquals(Error, "Document is yet to be indexed. Please try attaching this document after sometime.");

		        Logger.debug("Reference Document is yet to be indexed. Please try attaching this document after sometime.");

		        fail = false;
		        driver.get(CONFIG.getProperty("endUserURL"));

		        return;
		        }
			
			Double double_PageNumber_temp = Double.parseDouble(ReferencePageNumber);
		    int int_PageNumber_temp = double_PageNumber_temp.intValue();

		    if (int_PageNumber_temp > Integer.valueOf(locateElementBy("ip_create_page_page_number_numeric_box").getAttribute("maxvalue"))) {
		    	String slError = locateElementBy("ip_create_page_page_number_numeric_box_errors").getText();

		    	Assert.assertEquals(slError, "This page number does not exist in the selected document");

		    	Logger.debug("Page Number does not exist in the selected document");

		    	fail = false;
		    	driver.get(CONFIG.getProperty("endUserURL"));

		    	return;
		    	}
		    }

		Assert.assertNotNull(driver.findElement(By.xpath("//button[contains(.,'Save Interpretation')]")));
		((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", driver.findElement(By.xpath("//button[contains(.,'Save Interpretation')]")));
		driver.findElement(By.xpath("//button[contains(.,'Save Interpretation')]")).click();

		
		if (driver.findElements(By.className("success-icon")).size() != 0) {
			String entityID = driver.findElement(By.className("success-icon")).findElement(By.id("hrefElemId")).getText();

			Logger.debug("IP created successfully with issue ID " + entityID);

			driver.findElement(By.className("success-icon")).findElement(By.id("hrefElemId")).click();
			fluentWaitMethod(locateElementBy("ip_show_page_id"));


			String entityShowPageID = locateElementBy("ip_show_page_id").getText();

			Assert.assertEquals(entityShowPageID, entityID);
			Logger.debug("IP ID on show page has been verified");
		}

		fail = false;		
		fail = false;


		// Assertion code shifted down
		
		try {
			if (driver.findElement(By.className("success-icon")).getText().contains("Either you do not have the required permissions or requested page does not exist anymore.")) {
				driver.findElement(By.xpath("//button[contains(.,'OK')]")).click();

			}

		} catch (Exception e) {

		}

		
		// Interpretation - CREATE PAGE - FIELD VALIDATIONS
				List<WebElement> validationClassElementList = driver.findElements(By.xpath(".//*[@class='errorMessageHolder']/ul/li"));
				if (!validationClassElementList.isEmpty()) {
					for (WebElement validationClassElement : validationClassElementList) {
						String validationMessage = validationClassElement.getText();
						
						WebElement elementParent = validationClassElement.findElement(By.xpath("..")).findElement(By.xpath("..")).findElement(By.xpath("..")).findElement(By.xpath(".."));
						
						if (validationMessage.equalsIgnoreCase(""))
							Logger.debug("For Interpretation -- " + elementParent.findElement(By.className("errorClass")).getAttribute("name").toUpperCase() + " -- is Mandatory");
						else
							Logger.debug("For Interpretation -- " + elementParent.findElement(By.className("errorClass")).getAttribute("name").toUpperCase() + " -- " + validationMessage);
						}

					driver.get(CONFIG.getProperty("endUserURL"));
					return;
					}

				if (driver.findElements(By.className("success-icon")).size()!=0) {
					 String entityID = driver.findElement(By.className("success-icon")).findElement(By.id("hrefElemId")).getText();

					 Logger.debug("Interpretation created successfully with Entity ID " + entityID);

					 driver.findElement(By.className("success-icon")).findElement(By.id("hrefElemId")).click();

					 fluentWaitMethod(locateElementBy("ip_show_page_id"));
						 
					 String entityShowPageID = locateElementBy("ip_show_page_id").getText();

					 Assert.assertEquals(entityShowPageID, entityID);

				}
	
			  //Assertion code shifted down- comment put at its place String
			  String ipIdFromShowPage = locateElementBy("ip_show_page_id").getText();
			  
			  Logger.debug("Interpretation show page open successfully with action id " + ipIdFromShowPage);
			  
			 
			  
			    }

	@AfterMethod
	public void reportDataSetResult(ITestResult testResult) throws IOException {
		takeScreenShotOnFailure(testResult.getStatus(), this.getClass().getSimpleName());

		if(testResult.getStatus()==ITestResult.SKIP)
			TestUtil.reportDataSetResult(int_suite_xls, this.getClass().getSimpleName(), count+2, "SKIP");
		else if(testResult.getStatus()==ITestResult.FAILURE) {
			isTestPass=false;
			TestUtil.reportDataSetResult(int_suite_xls, this.getClass().getSimpleName(), count+2, "FAIL");
			result= "Fail";
		}
		else if (testResult.getStatus()==ITestResult.SUCCESS) {
			TestUtil.reportDataSetResult(int_suite_xls, this.getClass().getSimpleName(), count+2, "PASS");
			result= "Pass";
		}
		try {
			for (int i = 2; i <= int_suite_xls.getRowCount(this.getClass().getSimpleName()); i++) {
				String testCaseID = int_suite_xls.getCellData(this.getClass().getSimpleName(), "testCaseID", i);
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
			TestUtil.reportDataSetResult(int_suite_xls, "Test Cases", TestUtil.getRowNum(int_suite_xls, this.getClass().getSimpleName()),"PASS");
		else
			TestUtil.reportDataSetResult(int_suite_xls, "Test Cases", TestUtil.getRowNum(int_suite_xls, this.getClass().getSimpleName()),"FAIL");

	}

	@DataProvider
	public Object[][] getTestData() {
		return TestUtil.getData(int_suite_xls, this.getClass().getSimpleName());
	}

}
