package office.sirion.suite.action;

import office.sirion.util.DatePicker_Enhanced;
import office.sirion.util.JSWaiter;
import office.sirion.util.TestUtil;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.testng.Assert;
import org.testng.ITestResult;
import org.testng.SkipException;
import org.testng.annotations.*;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class ActionCreation extends TestSuiteBase {
	String testCaseID;
	String result = null;
	String runmodes[] = null;
	static int count = -1;
	static boolean fail = false;
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
	public void ActionCreationTest (String actionTitle, String actionDescription, String actionType, String actionPriority, String actionResponsibility,
			String actionDeliveryCountries, String actionTimeZone, String actionCurrency, String actionSupplierAccess, String actionDependentEntity, String actionTier,
			String actionGovernanceBodyMeeting, String actionRecipientClientEntity, String actionRecipientCompanyCode, String actionContractingClientEntity,
			String actionContractingCompanyCode, String actionRequestedOn, String actionDueDate, String actionFunctions, String actionServices,
			String actionServicesCategory, String actionManagementRegions, String actionManagementCountries, String actionContractRegions, String actionContractCountries,
			String actionFinancialImpact, String actionProjectLevels, String actionInitiatives, String actionProjectID,
			String actionComments, String actionActualDate, String actionRequestedBy, String actionChangeRequest, String actionUploadFile,
			String actionSupplier, String actionSourceType, String actionSourceName
			) throws InterruptedException {
		
		count++;
		if (!runmodes[count].equalsIgnoreCase("Y")) {
			skip = true;
			throw new SkipException("Runmode for Test Data Set to NO -- " + count);
			}
		
		openBrowser();
	    endUserLogin(CONFIG.getProperty("endUserURL"), CONFIG.getProperty("endUserUsername"), CONFIG.getProperty("endUserPassword"));

        Logger.debug("Executing Test Case Action Creation with Title ---- " + actionTitle + " under Source Type ---- " + actionSourceType);

		JSWaiter jsWaiter = new JSWaiter();
		jsWaiter.waitAllRequest();

        WebElement we = driver.findElement(By.xpath("//*[@id='quick-nav']/div/ul[2]"));

        Actions ac = new Actions(driver);
        ac.moveToElement(we).build().perform();

        locateElementBy("collaboration_group_quick_link").click();
        locateElementBy("actions_quick_link").click();

        locateElementBy("action_listing_page_plus_button").click();

		addFieldValue("entity_global_create_page_supplier_dropdown", actionSupplier.trim());

		addFieldValue("entity_global_create_page_source_type_dropdown", actionSourceType.trim());
		 
		addFieldValue("entity_global_create_page_source_name_title_dropdown", actionSourceName.trim());

        locateElementBy("entity_create_page_submit_button").click();

	    // Actions - Create Page - BASIC INFORMATION
		addFieldValue("action_create_page_title_textbox", actionTitle);
		
		addFieldValue("action_create_page_description_textarea", actionDescription);

        addFieldValue("action_create_page_type_dropdown", actionType);

		addFieldValue("action_create_page_priority_dropdown", actionPriority);

		addFieldValue("action_create_page_responsibility_dropdown", actionResponsibility);
		
		addFieldValue("action_create_page_delivery_countries_multi_dropdown", actionDeliveryCountries);

		addFieldValue("action_create_page_timezone_dropdown", actionTimeZone);

			if (driver.findElements(By.className("success-icon")).size()!=0) {
                driver.findElement(By.className("success-icon")).getText().contains("Current Date is different for the selected Time Zone");
                driver.findElement(By.xpath(".//button[contains(.,'OK')]")).click();
            }

		addFieldValue("action_create_page_currency_dropdown", actionCurrency);

		addFieldValue("action_create_page_supplier_access_checkbox", actionSupplierAccess);
		
		addFieldValue("action_create_page_dependent_entity_checkbox", actionDependentEntity);
		
		addFieldValue("action_create_page_tier_dropdown", actionTier);
		
		addFieldValue("action_create_page_governance_body_meeting_dropdown", actionGovernanceBodyMeeting);

		//Actions - Create Page - Contracting Entity
		addFieldValue("action_create_page_recipient_client_entity_multi_dropdown", actionRecipientClientEntity);

		addFieldValue("action_create_page_recipient_company_code_multi_dropdown", actionRecipientCompanyCode);
		
		addFieldValue("action_create_page_contracting_client_entity_multi_dropdown", actionContractingClientEntity);
		
		addFieldValue("action_create_page_contracting_company_code_multi_dropdown", actionContractingCompanyCode);

		// Actions - Create Page - IMPORTANT DATES
		DatePicker_Enhanced date = new DatePicker_Enhanced();
		date.selectCalendar(actionRequestedOn,"requestedOn");
		date.selectCalendar(actionDueDate,"plannedCompletionDate");

		// Actions - Create Page - FUNCTIONS
		addFieldValue("actions_create_page_functions_multi_dropdown", actionFunctions);
		addFieldValue("actions_create_page_services_multi_dropdown", actionServices);
		

		// Actions - Create Page - GEOGRAPHY
		addFieldValue("actions_create_page_management_regions_multi_dropdown", actionManagementRegions);
		addFieldValue("actions_create_page_management_countries_multi_dropdown", actionManagementCountries);

		//Actions - Create Page - FINANCIAL INFORMATION
		addFieldValue("actions_create_page_financial_impact_numeric_box", actionFinancialImpact);

		//Actions Create Page - PROJECT INFORMATION
		addFieldValue("actions_create_page_projectid_multiselect", actionProjectID);
		addFieldValue("actions_create_page_project_levels_multiselect", actionProjectLevels);
		addFieldValue("actions_create_page_initiatives_multiselect", actionInitiatives);
		
		// Actions - Create Page - COMMENTS AND ATTACHMENTS
		addFieldValue("entity_create_page_comments_textarea", actionComments);
		addFieldValue("entity_create_page_requested_by_dropdown", actionRequestedBy);
		date.selectCalendar(actionActualDate,"actualDate");
		    
		addFieldValue("entity_create_page_change_request_dropdown", actionChangeRequest);
		
		if (!actionUploadFile.equalsIgnoreCase("")) {
			locateElementBy("entity_create_page_upload_browse_button").sendKeys(System.getProperty("user.dir")+"\\file-upload\\Action\\" + actionUploadFile);
            driver.manage().timeouts().implicitlyWait(15, TimeUnit.SECONDS);
			}

		// ACTIONS - CREATE PAGE - SAVE BUTTON

        WebElement element = driver.findElement(By.xpath("//button[contains(.,'Save Action')]"));
        Actions actions = new Actions(driver);
        actions.moveToElement(element).click().perform();

		// ACTIONS - CREATE PAGE - FIELD VALIDATIONS
		
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

		if (driver.findElements(By.className("success-icon")).size()!=0) {
			String entityID = driver.findElement(By.className("success-icon")).findElement(By.id("hrefElemId")).getText();

			Logger.debug("Action created successfully with Action ID " + entityID);

            driver.findElement(By.className("success-icon")).findElement(By.id("hrefElemId")).click();
			waitForPageSpinnerToDisappear();

			String entityShowPageID = locateElementBy("actions_show_page_id").getText();

			Assert.assertEquals(entityShowPageID, entityID);
			Logger.debug("Action ID on show page has been verified");
			}

		   driver.get(CONFIG.getProperty("endUserURL"));
	    }

	@AfterMethod
	public void reportDataSetResult(ITestResult testResult) throws IOException {
		takeScreenShotOnFailure(testResult.getStatus(), this.getClass().getSimpleName());

		if(testResult.getStatus()==ITestResult.SKIP)
			TestUtil.reportDataSetResult(action_suite_xls, this.getClass().getSimpleName(), count+2, "SKIP");
		else if(testResult.getStatus()==ITestResult.FAILURE) {
			isTestPass=false;
			TestUtil.reportDataSetResult(action_suite_xls, this.getClass().getSimpleName(), count+2, "FAIL");
			result= "Fail";
		}
		else if (testResult.getStatus()==ITestResult.SUCCESS) {
			TestUtil.reportDataSetResult(action_suite_xls, this.getClass().getSimpleName(), count+2, "PASS");
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
	      TestUtil.reportDataSetResult(action_suite_xls, "Test Cases", TestUtil.getRowNum(action_suite_xls, this.getClass().getSimpleName()), "PASS");
	    else
	      TestUtil.reportDataSetResult(action_suite_xls, "Test Cases", TestUtil.getRowNum(action_suite_xls, this.getClass().getSimpleName()), "FAIL");
	  }
	 

	@DataProvider
	public Object[][] getTestData() {
		return TestUtil.getData(action_suite_xls, this.getClass().getSimpleName());
	}
}