package office.sirion.suite.issue;

import java.io.IOException;
import java.util.List;

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

import office.sirion.util.DatePicker_Enhanced;
import office.sirion.util.TestUtil;

public class IssueCreation extends TestSuiteBase {
	String result = null;
	String runmodes[] = null;
	static int count = -1;
	static boolean fail = false;
	static boolean skip = false;
	static boolean isTestPass = true;

	@BeforeTest
	public void checkTestSkip() {
		if (!TestUtil.isTestCaseRunnable(issue_suite_xls, this.getClass()
				.getSimpleName())) {
			Logger.debug("Skipping Test Case "
					+ this.getClass().getSimpleName() + " as runmode set to NO");
			throw new SkipException("Skipping Test Case "
					+ this.getClass().getSimpleName() + " as runmode set to NO");
		}
		runmodes = TestUtil.getDataSetRunmodes(issue_suite_xls, this.getClass()
				.getSimpleName());
	}

	@Test(dataProvider = "getTestData")
	public void IssueCreationTest(String issueTitle,
			String issueDescription, String issueType, String issuePriority,
			String issueCurrency, String issueFinancialImpact,
			String issueDeliveryCountries, String issueTimezone,
			String issueRestrictPublicAccess,
			String issueRestrictRequesterAccess, String issueSupplierAccess,
			String issueTier, String issueDependentEntity,
			String issueGovernanceBodyMeeting,
			String issueRecipientClientEntity,
			String issueRecipientCompanyCode,
			String issueContractingClientEntity,
			String issueContractingCompanyCode, String issueIssueDate,
			String issuePlannedCompletionDate, String issueFunctions,
			String issueServices, String issueServiceCategory,
			String issueManagementRegions, String issueManagementCountries,
			String issueContractRegions, String issueContractCountries,
			String issueResponsibility, String issueProjectID,
			String issueProjectLevels, String issueInitiatives,
			String issueComments, String issueActualDate,
			String issueRequestedBy, String issueChangeRequest,
			String issueUploadFile, String issueSupplier,
			String issueSourceType, String issueSourceName)
			throws InterruptedException {

		count++;
		if (!runmodes[count].equalsIgnoreCase("Y")) {
			skip = true;
			throw new SkipException("Runmode for Test Data Set to NO -- "
					+ count);
		}

		Logger.debug("Executing Test Case Issue Creation with Title ---- "
				+ issueTitle);

		// Launch The Browser
		openBrowser();
		endUserLogin(CONFIG.getProperty("endUserURL"),
				CONFIG.getProperty("endUserUsername"),
				CONFIG.getProperty("endUserPassword"));

		Logger.debug("Executing Test Case issues Creation with Title ---- "
				+ issueTitle + " under Contract ---- " + issueSourceName);

		driver.get(CONFIG.getProperty("endUserURL"));
		fluentWaitMethod(locateElementBy("issues_quick_link"));


		locateElementBy("issues_quick_link").click();

		fluentWaitMethod(locateElementBy("issue_listing_page_plus_button"));

		locateElementBy("issue_listing_page_plus_button").click();


		addFieldValue("entity_global_create_page_supplier_dropdown",
				issueSupplier.trim());

		addFieldValue("entity_global_create_page_source_type_dropdown",
				issueSourceType.trim());

		addFieldValue("entity_global_create_page_source_name_title_dropdown",
				issueSourceName.trim());

		Assert.assertNotNull(driver.findElement(By
				.xpath("//button[contains(.,'Submit')]")));
		((JavascriptExecutor) driver).executeScript(
				"arguments[0].scrollIntoView(true);",
				driver.findElement(By.xpath("//button[contains(.,'Submit')]")));
		driver.findElement(By.xpath("//button[contains(.,'Submit')]")).click();

		fluentWaitMethod(locateElementBy("issue_create_page_Title_textbox"));

		// issues - Create Page - BASIC INFORMATION

		addFieldValue("issue_create_page_Title_textbox", issueTitle);
		addFieldValue("issue_create_page_Description_textarea",
				issueDescription);
		locateElementBy("issue_create_page_blank_area_label").click();
		addFieldValue("issue_create_page_Type_dropdown", issueType);
		addFieldValue("issue_create_page_Priority_dropdown", issuePriority);
		addFieldValue("issue_create_page_Currency_dropdown", issueCurrency);
		addFieldValue("issue_create_page_FinancialImpact_textbox",
				issueFinancialImpact);
		addFieldValue("issue_create_page_DeliveryCountries_multiselect",
				issueDeliveryCountries);
		addFieldValue("issue_create_page_Timezone_dropdown", issueTimezone);
		try {
			if (driver
					.findElement(By.className("success-icon"))
					.getText()
					.contains(
							"Current Date is different for the selected Time Zone"))
				driver.findElement(By.xpath(".//button[contains(.,'OK')]"))
						.click();
		} catch (Exception e) {
		}
		addFieldValue("issue_create_page_RestrictPublicAccess_checkbox",
				issueRestrictPublicAccess);
		addFieldValue("issue_create_page_RestrictRequesterAccess_checkbox",
				issueRestrictRequesterAccess);
		addFieldValue("issue_create_page_SupplierAccess_checkbox",
				issueSupplierAccess);
		addFieldValue("issue_create_page_Tier_dropdown", issueTier);
		addFieldValue("issue_create_page_DependentEntity_checkbox",
				issueDependentEntity);
		addFieldValue("issue_create_page_GovernanceBodyMeeting_dropdown",
				issueGovernanceBodyMeeting);

		// issues - Create Page - Contrating Entity

		addFieldValue("issue_create_page_RecipientClientEntity_multiselect",
				issueRecipientClientEntity);
		addFieldValue("issue_create_page_RecipientCompanyCode_multiselect",
				issueRecipientCompanyCode);
		addFieldValue("issue_create_page_ContractingClientEntity_multiselect",
				issueContractingClientEntity);
		addFieldValue("issue_create_page_ContractingCompanyCode_multiselect",
				issueContractingCompanyCode);

		// issues - Create Page - IMPORTANT DATES

		DatePicker_Enhanced date = new DatePicker_Enhanced();
		date.selectCalendar(issueIssueDate,"issueDate");
		date.selectCalendar(issuePlannedCompletionDate,"plannedCompletionDate");

		// Issues - Create Page - FUNCTIONS
		addFieldValue("issue_create_page_Functions_multiselect", issueFunctions);
		addFieldValue("issue_create_page_Services_multiselect", issueServices);

		// addFieldValue("issue_create_page_ServiceCategory_multiselect",
		// issueServicesCategory);

		// Issues - Create Page - GEOGRAPHY
		addFieldValue("issue_create_page_ManagementRegions_multiselect",
				issueManagementRegions);
		addFieldValue("issue_create_page_ManagementCountries_multiselect",
				issueManagementCountries);
		addFieldValue("issue_create_page_ContractRegions_multiselect",
				issueContractRegions);
		addFieldValue("issue_create_page_ContractCountries_multiselect",
				issueContractCountries);

		// issues - Create Page - STAKEHOLDERS

		addFieldValue("issue_create_page_Responsibility_dropdown",
				issueResponsibility);

		// issues Create Page - PROJECT INFORMATION

		addFieldValue("issue_create_page_ProjectID_multiselect", issueProjectID);
		addFieldValue("issue_create_page_ProjectLevels_multiselect",
				issueProjectLevels);
		addFieldValue("issue_create_page_Initiatives_multiselect",
				issueInitiatives);

		// Issues - Create Page - COMMENTS AND ATTACHMENTS
		addFieldValue("entity_create_page_comments_textarea", issueComments);
		driver.findElement(By.xpath("//*[@id='mainForm']/ng-form/fieldset[8]/p")).click();
		addFieldValue("entity_create_page_requested_by_dropdown",
				issueRequestedBy);
		addFieldValue("issue_comment_actual_date", issueActualDate);

		addFieldValue("entity_create_page_change_request_dropdown",
				issueChangeRequest);

		if (!issueUploadFile.equalsIgnoreCase("")) {
			locateElementBy("entity_create_page_upload_browse_button")
					.sendKeys(
							System.getProperty("user.dir")
									+ "\\file-upload\\Issue\\"
									+ issueUploadFile);

		}

		Assert.assertNotNull(driver.findElement(By.xpath("//button[contains(.,'Save Issue')]")));
		((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", driver.findElement(By.xpath("//button[contains(.,'Save Issue')]")));
		driver.findElement(By.xpath("//button[contains(.,'Save Issue')]")).click();


		// ISSUES - CREATE PAGE - FIELD VALIDATIONS

		List<WebElement> validationClassElementList = driver.findElements(By
				.xpath(".//*[@class='errorMessageHolder']/ul/li"));
		if (!validationClassElementList.isEmpty()) {
			for (WebElement validationClassElement : validationClassElementList) {
				String validationMessage = validationClassElement.getText();

				WebElement elementParent = validationClassElement
						.findElement(By.xpath(".."))
						.findElement(By.xpath(".."))
						.findElement(By.xpath(".."))
						.findElement(By.xpath(".."));

				if (validationMessage.equalsIgnoreCase(""))
					Logger.debug("For Issues -- "
							+ elementParent
									.findElement(By.className("errorClass"))
									.getAttribute("name").toUpperCase()
							+ " -- is Mandatory");
				else
					Logger.debug("For Issues -- "
							+ elementParent
									.findElement(By.className("errorClass"))
									.getAttribute("name").toUpperCase()
							+ " -- " + validationMessage);
			}

			driver.get(CONFIG.getProperty("endUserURL"));
			return;
		}

		if (driver.findElements(By.className("success-icon")).size() != 0) {
			String entityID = driver.findElement(By.className("success-icon"))
					.findElement(By.id("hrefElemId")).getText();

			Logger.debug("Issue created successfully with Issue ID "
					+ entityID);

			driver.findElement(By.className("success-icon"))
					.findElement(By.id("hrefElemId")).click();
			waitF.until(ExpectedConditions
					.elementToBeClickable(locateElementBy("issue_show_page_id")));


			String entityShowPageID = locateElementBy("issue_show_page_id")
					.getText();

			Assert.assertEquals(entityShowPageID, entityID);
			Logger.debug("Issue ID on show page has been verified");
		}

		driver.get(CONFIG.getProperty("endUserURL"));
	}

	@AfterMethod
	public void reportDataSetResult(ITestResult testResult) throws IOException {
		takeScreenShotOnFailure(testResult.getStatus(), this.getClass().getSimpleName());

		if(testResult.getStatus()==ITestResult.SKIP)
			TestUtil.reportDataSetResult(client_admin_suite_xls, this.getClass().getSimpleName(), count+2, "SKIP");
		else if(testResult.getStatus()== ITestResult.FAILURE) {
			isTestPass=false;
			TestUtil.reportDataSetResult(client_admin_suite_xls, this.getClass().getSimpleName(), count+2, "FAIL");
			result= "Fail";
		}
		else if (testResult.getStatus()==ITestResult.SUCCESS) {
			TestUtil.reportDataSetResult(client_admin_suite_xls, this.getClass().getSimpleName(), count+2, "PASS");
			result= "Pass";
		}
		try {
			for (int i = 2; i <= issue_suite_xls.getRowCount(this.getClass().getSimpleName()); i++) {
				String testCaseID = issue_suite_xls.getCellData(this.getClass().getSimpleName(), "testCaseID", i);
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
			TestUtil.reportDataSetResult(issue_suite_xls, "Test Cases",
					TestUtil.getRowNum(issue_suite_xls, this.getClass()
							.getSimpleName()), "PASS");
		else
			TestUtil.reportDataSetResult(issue_suite_xls, "Test Cases",
					TestUtil.getRowNum(issue_suite_xls, this.getClass()
							.getSimpleName()), "FAIL");
	}

	@DataProvider
	public Object[][] getTestData() {
		return TestUtil.getData(issue_suite_xls, this.getClass()
				.getSimpleName());
	}
}
