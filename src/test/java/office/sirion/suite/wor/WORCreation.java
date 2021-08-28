package office.sirion.suite.wor;

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

public class WORCreation extends TestSuiteBase {
	String result = null;
	String runmodes[] = null;
	static int count = -1;
	static boolean isTestPass = true;

	@BeforeTest
	public void checkTestSkip() {
		if (!TestUtil.isTestCaseRunnable(wor_suite_xls, this.getClass().getSimpleName())) {
			Logger.debug("Skipping Test Case " + this.getClass().getSimpleName() + " as runmode set to NO");
			throw new SkipException("Skipping Test Case " + this.getClass().getSimpleName() + " as runmode set to NO");
			}
		runmodes = TestUtil.getDataSetRunmodes(wor_suite_xls, this.getClass().getSimpleName());
		}

	@Test(dataProvider = "getTestData")
	public void WORCreationTest (String worName, String worTitle, String worBrief, String worPriority, String worType, String worBillingType, String worResponsibility,
			String worCurrency, String worContractingEntity, String worDeliveryCountries, String worTimeZone, String worSupplierAccess, String worTier,
			String worRecipientClientEntity, String worRecipientCompanyCode, String worContractingClientEntity, String worContractingCompanyCode,
			String worEffectiveDate, String worExpirationDate, String worRequestDate, String worPlannedCompletionDate, String worFunctions, String worServices,
			String worServiceCategory, String worRegions, String worCountries, String worContractRegions, String worContractCountries, String worAdditionalTCV,
			String worAdditionalACV, String worAdditionalFACV, String worInitiatives, String worProjectLevels, String worProjectID,
			String worPrivacy, String worComments, String worActualDate, String worRequestedBy, String worChangeRequest, String worUploadFiles,
			String worSupplier, String worSourceType, String worSourceName
			) throws InterruptedException {

		count++;
		if (!runmodes[count].equalsIgnoreCase("Y"))
			throw new SkipException("Runmode for Test Data set to NO "+ count);

		openBrowser();
	    endUserLogin(CONFIG.getProperty("endUserURL"), CONFIG.getProperty("endUserUsername"), CONFIG.getProperty("endUserPassword"));

	    Logger.debug("Executing Test Case WOR Creation with Name ---- " + worName);

	    driver.get(CONFIG.getProperty("endUserURL"));

		fluentWaitMethod(locateElementBy("wor_quick_link"));

		locateElementBy("wor_quick_link").click();

		fluentWaitMethod(locateElementBy("wor_entity_listing_page_plus_button"));

		locateElementBy("wor_entity_listing_page_plus_button").click();


	    // WORK ORDER REQUESTS - GLOBAL CREATE PAGE
		addFieldValue("entity_global_create_page_supplier_dropdown", worSupplier.trim());

		addFieldValue("entity_global_create_page_source_type_dropdown", worSourceType.trim());


		addFieldValue("entity_global_create_page_source_name_title_dropdown", worSourceName.trim());

		WebElement elementSubmit = driver.findElement(By.cssSelector("ng-form[template='tab.actions']")).findElement(By.cssSelector("div.submit.freeze"));		
		Assert.assertNotNull(elementSubmit.findElement(By.xpath(".//button[contains(.,'Submit')]")));
	    ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", elementSubmit.findElement(By.xpath(".//button[contains(.,'Submit')]")));
	    elementSubmit.findElement(By.xpath(".//button[contains(.,'Submit')]")).click();

	    fluentWaitMethod(locateElementBy("wor_create_page_name_textbox"));

	    // WORK ORDER REQUESTS - CREATE PAGE - BASIC INFORMATION
	    addFieldValue("wor_create_page_name_textbox", worName.trim());

	    addFieldValue("wor_create_page_title_textbox", worTitle.trim());

	    addFieldValue("wor_create_page_brief_textarea", worBrief.trim());

        addFieldValue("wor_create_page_priority_dropdown", worPriority.trim());

        addFieldValue("wor_create_page_type_multiselect", worType.trim());

        addFieldValue("wor_create_page_billing_type_multiselect", worBillingType.trim());

        addFieldValue("wor_create_page_responsibility_dropdown", worResponsibility.trim());

        addFieldValue("wor_create_page_currency_dropdown", worCurrency.trim());

        addFieldValue("wor_create_page_contracting_entity_dropdown", worContractingEntity.trim());

        addFieldValue("wor_create_page_delivery_countries_dropdown", worDeliveryCountries.trim());

        addFieldValue("wor_create_page_timezone_dropdown", worTimeZone.trim());
		try {
			if (driver.findElement(By.className("success-icon")).getText().contains("Current Date is different for the selected Time Zone"))
				driver.findElement(By.xpath(".//button[contains(.,'OK')]")).click();
			} catch (Exception e) {
				}

        addFieldValue("wor_create_page_tier_dropdown", worTier.trim());

        // WORK ORDER REQUESTS - CREATE PAGE - CONTRACTING ENTITY		
		addFieldValue("wor_create_page_recipient_client_entities_multiselect", worRecipientClientEntity.trim());

		addFieldValue("wor_create_page_recipient_company_codes_multiselect", worRecipientCompanyCode.trim());

		addFieldValue("wor_create_page_contracting_client_entities_multiselect", worContractingClientEntity.trim());

		addFieldValue("wor_create_page_contracting_company_codes_multiselect", worContractingCompanyCode.trim());

        // WORK ORDER REQUESTS - CREATE PAGE - IMPORTANT DATES
		DatePicker_Enhanced date = new DatePicker_Enhanced();
		date.selectCalendar(worEffectiveDate,"effectiveDate");
		date.selectCalendar(worExpirationDate,"expirationDate");
		date.selectCalendar(worPlannedCompletionDate,"plannedCompletionDate");
		date.selectCalendar(worRequestDate,"requestDate");

		// WORK ORDER REQUESTS - CREATE PAGE - FUNCTIONS
		addFieldValue("wor_create_page_function_multiselect", worFunctions.trim());
		if (!worFunctions.equalsIgnoreCase(""))
			addFieldValue("wor_create_page_service_multiselect", worServices.trim());

		addFieldValue("wor_create_page_service_category_multiselect", worServiceCategory.trim());

		// WORK ORDER REQUESTS - CREATE PAGE - GEOGRAPHY
		addFieldValue("wor_create_page_management_regions_multi_dropdown", worRegions.trim());
		if (!worRegions.equalsIgnoreCase(""))
			addFieldValue("wor_create_page_management_countries_multi_dropdown", worCountries.trim());

		addFieldValue("wor_create_page_contract_regions_multi_dropdown", worContractRegions.trim());
		if (!worContractRegions.equalsIgnoreCase(""))
			addFieldValue("wor_create_page_contract_countries_multi_dropdown", worContractCountries.trim());

		// WORK ORDER REQUESTS - CREATE PAGE - FINANCIAL INFORMATION
		addFieldValue("wor_create_page_additional_TCV_textbox", worAdditionalTCV.trim());

		addFieldValue("wor_create_page_additional_ACV_textbox", worAdditionalACV.trim());

		addFieldValue("wor_create_page_additional_FACV_textbox", worAdditionalFACV.trim());

		// WORK ORDER REQUESTS - CREATE PAGE - PROJECT INFORMATION
		addFieldValue("wor_create_page_project_levels_multiselect", worProjectLevels.trim());

		addFieldValue("wor_create_page_initiatives_multiselect", worInitiatives.trim());

		addFieldValue("wor_create_page_project_id_multiselect", worProjectID.trim());

		// WORK ORDER REQUESTS - CREATE PAGE - COMMENTS AND ATTACHMENTS
		addFieldValue("entity_create_page_comments_textarea", worComments.trim());

		addFieldValue("entity_create_page_requested_by_dropdown", worRequestedBy.trim());

		date.selectCalendar(worActualDate,"actualDate");

		addFieldValue("entity_create_page_change_request_dropdown", worChangeRequest.trim());

		if (!worUploadFiles.equalsIgnoreCase("")) {
			locateElementBy("entity_create_page_upload_browse_button").sendKeys(System.getProperty("user.dir")+"\\file-upload\\WOR\\" + worUploadFiles);

			}

		WebElement elementSave = driver.findElement(By.cssSelector("ng-form[template='tab.actions']")).findElement(By.cssSelector("div.submit.freeze"));		
		Assert.assertNotNull(elementSave.findElement(By.xpath(".//button[contains(.,'Save WOR')]")));
	    ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", elementSave.findElement(By.xpath(".//button[contains(.,'Save WOR')]")));
	    elementSave.findElement(By.xpath(".//button[contains(.,'Save WOR')]")).click();


		// WORK ORDER REQUESTS - CREATE PAGE - FIELD VALIDATIONS
		List<WebElement> validationClassElementList = driver.findElements(By.xpath(".//*[@class='errorMessageHolder']/ul/li"));
		if (!validationClassElementList.isEmpty()) {
			for (WebElement validationClassElement : validationClassElementList) {
				String validationMessage = validationClassElement.getText();
				
				WebElement elementParent = validationClassElement.findElement(By.xpath("..")).findElement(By.xpath("..")).findElement(By.xpath("..")).findElement(By.xpath(".."));
				
				if (validationMessage.equalsIgnoreCase(""))
					Logger.debug("For WOR -- " + elementParent.findElement(By.className("errorClass")).getAttribute("name").toUpperCase() + " -- is Mandatory");
				else
					Logger.debug("For WOR -- " + elementParent.findElement(By.className("errorClass")).getAttribute("name").toUpperCase() + " -- " + validationMessage);
				}

			driver.get(CONFIG.getProperty("endUserURL"));
			return;
			}

		String entityID = driver.findElement(By.className("success-icon")).findElement(By.id("hrefElemId")).getText();

		Logger.debug("WOR created successfully with Entity ID " + entityID);

		driver.findElement(By.className("success-icon")).findElement(By.id("hrefElemId")).click();

		fluentWaitMethod(locateElementBy("wor_show_page_id"));

		String entityShowPageID = locateElementBy("wor_show_page_id").getText();

		Assert.assertEquals(entityShowPageID, entityID);

		Logger.debug("Test Case WOR Creation with Title -- " + worTitle + " -- is PASSED");
        driver.get(CONFIG.getProperty("endUserURL"));
	    }

	 @AfterMethod
		public void reportDataSetResult(ITestResult testResult) throws IOException {
			takeScreenShotOnFailure(testResult.getStatus(), this.getClass().getSimpleName());

			if(testResult.getStatus()==ITestResult.SKIP)
				TestUtil.reportDataSetResult(wor_suite_xls, this.getClass().getSimpleName(), count+2, "SKIP");
			else if(testResult.getStatus()==ITestResult.FAILURE) {
				isTestPass=false;
				TestUtil.reportDataSetResult(wor_suite_xls, this.getClass().getSimpleName(), count+2, "FAIL");
				result= "Fail";
			}
			else if (testResult.getStatus()==ITestResult.SUCCESS) {
				TestUtil.reportDataSetResult(wor_suite_xls, this.getClass().getSimpleName(), count+2, "PASS");
				result= "Pass";
			}
			try {
				for (int i = 2; i <= wor_suite_xls.getRowCount(this.getClass().getSimpleName()); i++) {
					String testCaseID = wor_suite_xls.getCellData(this.getClass().getSimpleName(), "testCaseID", i);
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
			TestUtil.reportDataSetResult(wor_suite_xls, "Test Cases", TestUtil.getRowNum(wor_suite_xls, this.getClass().getSimpleName()), "PASS");
		else 
			TestUtil.reportDataSetResult(wor_suite_xls, "Test Cases", TestUtil.getRowNum(wor_suite_xls, this.getClass().getSimpleName()), "FAIL");
		}

	@DataProvider
	public Object[][] getTestData() {
		return TestUtil.getData(wor_suite_xls, this.getClass().getSimpleName());
		}
	}
