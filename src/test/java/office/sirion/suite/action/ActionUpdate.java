package office.sirion.suite.action;

import java.io.IOException;
import java.util.List;
import office.sirion.util.DatePicker_Enhanced;
import office.sirion.util.JSWaiter;
import office.sirion.util.TestUtil;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.Select;
import org.testng.Assert;
import org.testng.ITestResult;
import org.testng.SkipException;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;



public class ActionUpdate extends TestSuiteBase {
	String testCaseID;
	String result = null;
	String runmodes[] = null;
	static int count = -1;
	static boolean fail = true;
	static boolean skip = false;
	static boolean isTestPass = true;
	
	@BeforeTest
	public void checkTestSkip() {
		if (!TestUtil.isTestCaseRunnable(action_suite_xls, this.getClass().getSimpleName())) {
			Logger.debug("Skipping Test Case " + this.getClass().getSimpleName() + " as runmode set to NO");
			throw new SkipException("Skipping Test Case " + this.getClass().getSimpleName() + " as runmode set to NO");
			}
		runmodes = TestUtil.getDataSetRunmodes(action_suite_xls, this.getClass().getSimpleName());
		}

	@Test(dataProvider = "getTestData")
	public void ActionUpdateTest (String actionTitle, String actionDescription, String actionType, String actionPriority, String actionResponsibility,
								  String actionTimeZone, String actionCurrency, String actionSupplierAccess, String actionTier, String actionGovernanceBodyMeeting, String actionRequestedOn, 
								  String actionDueDate, String actionFunctions, String actionServices,String actionServicesCategory, String actionManagementRegions, String actionManagementCountries, 
								  String actionContractRegions, String actionContractCountries,String actionProjectLevels, String actionInitiatives, String actionProjectID,String actionComments, 
								  String actionActualDate, String actionRequestedBy, String actionChangeRequest, String actionUploadFile,String actionParent) 
								  throws InterruptedException {
		
		count++;
		if (!runmodes[count].equalsIgnoreCase("Y")) {
			skip = true;
			throw new SkipException("Runmode for Test Data Set to NO -- " + count);
			}
		
		openBrowser();
	    endUserLogin(CONFIG.getProperty("endUserURL"), CONFIG.getProperty("endUserUsername"), CONFIG.getProperty("endUserPassword"));
	    
	    Logger.debug("Executing Test Case Action Update with Title ---- " + actionTitle);

        driver.get(CONFIG.getProperty("endUserURL"));

	    fluentWaitMethod(locateElementBy("actions_quick_link"));
	    locateElementBy("actions_quick_link").click();

		new Select(locateElementBy("entity_listing_page_display_dropdown_link")).selectByVisibleText("100");

		if (!actionParent.equalsIgnoreCase("")) {
		driver.findElement(By.xpath(".//*[@id='cr']/tbody/tr[@role='row']/td[contains(.,'" + actionParent + "')]/preceding-sibling::td[1]/a")).click();

		} else {
				locateElementBy("entity_listing_page_first_entry_link").click();
				}
		locateElementBy("entity_edit_button").click();


	    // Actions - Edit Page - BASIC INFORMATION
		addFieldValue("action_create_page_title_textbox", actionTitle);
		
		addFieldValue("action_create_page_description_textarea", actionDescription);
		locateElementBy("action_create_page_blank_area_label").click();
		
		addFieldValue("action_create_page_type_dropdown", actionType);

		addFieldValue("action_create_page_priority_dropdown", actionPriority);

		addFieldValue("action_create_page_responsibility_dropdown", actionResponsibility);
		
//		addFieldValue("action_create_page_delivery_countries_multi_dropdown", actionDeliveryCountries);
		
		addFieldValue("action_create_page_timezone_dropdown", actionTimeZone);
		try {
			if (driver.findElement(By.className("success-icon")).getText().contains("Current Date is different for the selected Time Zone"))
                driver.findElement(By.xpath(".//button[contains(.,'OK')]")).click();
			} catch (Exception e) {
				
			}
	
		addFieldValue("action_create_page_currency_dropdown", actionCurrency);

		addFieldValue("action_create_page_supplier_access_checkbox", actionSupplierAccess);
		
		addFieldValue("action_create_page_tier_dropdown", actionTier);
		
		addFieldValue("action_create_page_governance_body_meeting_dropdown", actionGovernanceBodyMeeting);

		// Actions - Edit Page - IMPORTANT DATES
		DatePicker_Enhanced date = new DatePicker_Enhanced();
		date.selectCalendar(actionRequestedOn,"requestedOn");
		date.selectCalendar(actionDueDate,"plannedCompletionDate");

		// Actions - Edit Page - FUNCTIONS
		addFieldValue("actions_create_page_functions_multi_dropdown", actionFunctions);
		addFieldValue("actions_create_page_services_multi_dropdown", actionServices);
		addFieldValue("actions_create_page_service_category_multi_dropdown", actionServicesCategory);
		    
		// Actions - Edit Page - GEOGRAPHY
		addFieldValue("actions_create_page_management_countries_multi_dropdown", actionManagementCountries);
		
		addFieldValue("actions_create_page_contract_countries_multi_dropdown", actionContractCountries);
		
		//Actions Edit Page - PROJECT INFORMATION
		addFieldValue("actions_create_page_projectid_multiselect", actionProjectID);

		addFieldValue("actions_create_page_project_levels_multiselect", actionProjectLevels);
		
		addFieldValue("actions_create_page_initiatives_multiselect", actionInitiatives);
		
		// Actions - Edit Page - COMMENTS AND ATTACHMENTS
		addFieldValue("entity_create_page_comments_textarea", actionComments);
		    
		addFieldValue("entity_create_page_requested_by_dropdown", actionRequestedBy);

		DatePicker_Enhanced Acdate = new DatePicker_Enhanced();
		Acdate.selectCalendar(actionActualDate,"actualDate");
		    
		addFieldValue("entity_create_page_change_request_dropdown", actionChangeRequest);
		
		if (!actionUploadFile.equalsIgnoreCase("")) {
			locateElementBy("entity_create_page_upload_browse_button").sendKeys(System.getProperty("user.dir")+"\\file-upload\\Action\\" + actionUploadFile);

			}
		

        WebElement element = locateElementBy("entity_update_button");
        Actions actions = new Actions(driver);
        actions.moveToElement(element).click().perform();

		// SUPPLIER - EDIT PAGE - FIELD VALIDATIONS

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
		 driver.get(CONFIG.getProperty("endUserURL"));
		
	    }


	@AfterMethod
	public void reportDataSetResult(ITestResult testResult) throws IOException {
		takeScreenShotOnFailure(testResult.getStatus(), this.getClass().getSimpleName());

		if(testResult.getStatus()==ITestResult.SKIP)
			TestUtil.reportDataSetResult(client_admin_suite_xls, this.getClass().getSimpleName(), count+2, "SKIP");
		else if(testResult.getStatus()==ITestResult.FAILURE) {
			isTestPass=false;
			TestUtil.reportDataSetResult(client_admin_suite_xls, this.getClass().getSimpleName(), count+2, "FAIL");
			result= "Fail";
		}
		else if (testResult.getStatus()==ITestResult.SUCCESS) {
			TestUtil.reportDataSetResult(client_admin_suite_xls, this.getClass().getSimpleName(), count+2, "PASS");
			result= "Pass";
		}
		try {
			for (int i = 2; i <= action_suite_xls.getRowCount(this.getClass().getSimpleName()); i++) {
				String testCaseID = action_suite_xls.getCellData(this.getClass().getSimpleName(), "testCaseID", i);
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
			TestUtil.reportDataSetResult(action_suite_xls, "Test Cases",
					TestUtil.getRowNum(action_suite_xls, this.getClass()
							.getSimpleName()), "PASS");
		else
			TestUtil.reportDataSetResult(action_suite_xls, "Test Cases",
					TestUtil.getRowNum(action_suite_xls, this.getClass()
							.getSimpleName()), "FAIL");

	}

	@DataProvider
	public Object[][] getTestData() {
		return TestUtil.getData(action_suite_xls, this.getClass().getSimpleName());
	}

}