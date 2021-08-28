package office.sirion.suite.interpretation;

import java.io.IOException;
import java.sql.SQLException;
import office.sirion.util.DatePicker_Enhanced;
import office.sirion.util.TestUtil;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
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

public class InterpretationUpdate extends TestSuiteBase {
	String runmodes[] = null;
	String result = null;
	static int count = -1;
	static boolean fail = true;
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
	public void InterpretationUpdateTest (String ipTitle, String ipBackground, String ipType, String ipPriority, 
			String ipTimezone, String ipTier, String ipSupplierAccess, String ipDependentEntity, String ipQuestion, String ipIncludeInFAQ, String ContractReference, 
			String ReferenceClause, String ReferencePageNumber, String ipRequestedDateDate, String ipPlannedSubmissionDateDate, String ipFunction, 
			String ipService, String ipServiceCategory, String ipManagementRegions, String ipManagementCountries, String ipContractRegions, String ipContractCountries)throws InterruptedException, ClassNotFoundException, SQLException {

		count++;
		if (!runmodes[count].equalsIgnoreCase("Y")) {
			skip = true;
			throw new SkipException("Runmode for test set data set to no "
					+ count);
		}
		openBrowser();
		endUserLogin(CONFIG.getProperty("endUserURL"), CONFIG.getProperty("endUserUsername"), CONFIG.getProperty("endUserPassword"));
		Logger.debug("Executing Test Case IP Update with Title ---- " + ipTitle);

        driver.get(CONFIG.getProperty("endUserURL"));

        fluentWaitMethod(locateElementBy("ip_quick_link"));

		locateElementBy("ip_quick_link").click();

        fluentWaitMethod(locateElementBy("entity_listing_page_display_dropdown_link"));

		new Select(locateElementBy("entity_listing_page_display_dropdown_link")).selectByVisibleText("100");

        fluentWaitMethod(locateElementBy("entity_listing_page_first_entry_link_ip"));

		locateElementBy("entity_listing_page_first_entry_link_ip").click();


        fluentWaitMethod(driver.findElement(By.xpath("//button[contains(.,'Edit')]")));
		Assert.assertNotNull(driver.findElement(By.xpath("//button[contains(.,'Edit')]")));
		((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", driver.findElement(By.xpath("//button[contains(.,'Edit')]")));
		driver.findElement(By.xpath("//button[contains(.,'Edit')]")).click();
        fluentWaitMethod(locateElementBy("ip_show_page_id"));

		// issues - update Page - BASIC INFORMATION

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
		addFieldValue("ip_create_page_tier_dropdown", ipTier);
		addFieldValue("ip_create_page_depentity_dropdown", ipDependentEntity);
		
		//Interpretation - Create Page - QUESTIONS
		
		addFieldValue("ip_create_page_question_textarea", ipQuestion);
		addFieldValue("ip_create_page_includeinfaq_checkbox", ipIncludeInFAQ);
		
		
		 if (!ContractReference.equalsIgnoreCase("")) {
		    	new Select(locateElementBy("sl_create_page_references_dropdown")).selectByVisibleText(ContractReference.trim());

		    	addFieldValue("sl_create_page_clause_textbox", ReferenceClause);
		    	
		    	addFieldValue("sl_create_page_page_number_numeric_box", ReferencePageNumber);
		    	}
		
		// issues - Create Page - IMPORTANT DATES
		
		
		/*Interpretation Create Page - Interpretation Date*/

		 DatePicker_Enhanced date = new DatePicker_Enhanced();
		date.selectCalendar(ipRequestedDateDate,"requestDate");
		date.selectCalendar(ipPlannedSubmissionDateDate,"plannedSubmissionDate");
		    		
		// Interpretations - Create Page - FUNCTIONS
		if (!ipFunction.equalsIgnoreCase("")) {
			for (String entityData : ipFunction.split(";")) {
				addFieldValue("ip_create_page_functions_multiselect",entityData.trim());
			  	}
			addFieldValue("ip_create_page_services_multiselect", ipService);
			}
		
//		addFieldValue("ip_create_page_service_category_multiselect", ipServiceCategory);
		    
		// Interpretations - Create Page - GEOGRAPHY
		if (locateElementBy("ip_create_page_management_regions_multiselect")!=null) {
			if (!ipManagementRegions.equalsIgnoreCase("")) {
				for (String entityData : ipManagementRegions.split(";")) {
					addFieldValue("ip_create_page_management_regions_multiselect",entityData.trim());
					}
				addFieldValue("ip_create_page_management_countries_multiselect", ipManagementCountries);
				}
			}
		
		if (locateElementBy("ip_create_page_contract_regions_multiselect")!=null) {
			if (!ipContractRegions.equalsIgnoreCase("")) {
				for (String entityData : ipContractRegions.split(";")) {
					addFieldValue("ip_create_page_contract_regions_multiselect",entityData.trim());
					}
			    addFieldValue("ip_create_page_contract_countries_multiselect", ipContractCountries);
				}
			}
		

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

		Assert.assertNotNull(driver.findElement(By.xpath("//button[contains(.,'Update')]")));
		((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", driver.findElement(By.xpath("//button[contains(.,'Update')]")));
		driver.findElement(By.xpath("//button[contains(.,'Update')]")).click();


        fluentWaitMethod(locateElementBy("ip_show_page_id"));


		String entityShowPageName = locateElementBy("ip_show_page_title").getText();

		Assert.assertEquals(entityShowPageName, ipTitle);
		Logger.debug("IP Title on show page has been verified");

		fail = false;

		driver.get(CONFIG.getProperty("endUserURL"));
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
			TestUtil.reportDataSetResult(int_suite_xls, "Test Cases",
					TestUtil.getRowNum(int_suite_xls, this.getClass()
							.getSimpleName()), "PASS");
		else
			TestUtil.reportDataSetResult(int_suite_xls, "Test Cases",
					TestUtil.getRowNum(int_suite_xls, this.getClass()
							.getSimpleName()), "FAIL");

	}

	@DataProvider
	public Object[][] getTestData() {
		return TestUtil.getData(int_suite_xls, this.getClass().getSimpleName());
	}

}