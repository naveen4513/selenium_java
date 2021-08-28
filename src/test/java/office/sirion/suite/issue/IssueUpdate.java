package office.sirion.suite.issue;

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
import java.io.IOException;
import java.util.List;

public class IssueUpdate extends TestSuiteBase {
	String result = null;
	String runmodes[] = null;
	static int count = -1;
	static boolean fail = true;
	static boolean skip = false;
	static boolean isTestPass = true;

	@BeforeTest
	public void checkTestSkip() {
		if (!TestUtil.isTestCaseRunnable(issue_suite_xls, this.getClass().getSimpleName())) {
			Logger.debug("Skipping Test Case "+ this.getClass().getSimpleName() + " as runmode set to NO");
			throw new SkipException("Skipping Test Case "+ this.getClass().getSimpleName() + " as runmode set to NO");
			}
		runmodes = TestUtil.getDataSetRunmodes(issue_suite_xls, this.getClass().getSimpleName());
		}
	
	@Test(dataProvider = "getTestData")
	public void IssueUpdateTest (String issueTitle, String issueDescription, String issueType, String issuePriority, String issueCurrency,
			String issueFinancialImpact, String issueDeliveryCountries, String issueTimezone, String issueRestrictPublicAccess, String issueRestrictRequesterAccess,
			String issueSupplierAccess, String issueTier, String issueDependentEntity, String issueGovernanceBodyMeeting, String issueRecipientClientEntity,
			String issueRecipientCompanyCode, String issueContractingClientEntity, String issueContractingCompanyCode, String issueIssueDate,
			String issuePlannedCompletionDate, String issueFunctions, String issueServices, String issueServiceCategory, String issueManagementRegions,
			String issueManagementCountries, String issueContractRegions, String issueContractCountries, String issueResponsibility, String issueProjectID,
			String issueProjectLevels, String issueInitiatives, String issueComments, String issueActualDate, String issueRequestedBy, String issueChangeRequest,
			String issueUploadFile, String issueParent) throws InterruptedException {

		count++;
		if (!runmodes[count].equalsIgnoreCase("Y")) {
			skip = true;
			throw new SkipException("Runmode for test set data set to no "
					+ count);
		}
		openBrowser();
		endUserLogin(CONFIG.getProperty("endUserURL"), CONFIG.getProperty("endUserUsername"), CONFIG.getProperty("endUserPassword"));
		Logger.debug("Executing Test Case Issue Update with Title ---- " + issueTitle);

        driver.get(CONFIG.getProperty("endUserURL"));

		fluentWaitMethod(locateElementBy("issues_quick_link"));


		locateElementBy("issues_quick_link").click();

		fluentWaitMethod(locateElementBy("entity_listing_page_display_dropdown_link"));

		new Select(locateElementBy("entity_listing_page_display_dropdown_link")).selectByVisibleText("100");


		locateElementBy("entity_listing_page_first_entry_link").click();
		

		fluentWaitMethod(driver.findElement(By.xpath("//button[contains(.,'Edit')]")));
		Assert.assertNotNull(driver.findElement(By.xpath("//button[contains(.,'Edit')]")));
		((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", driver.findElement(By.xpath("//button[contains(.,'Edit')]")));
		driver.findElement(By.xpath("//button[contains(.,'Edit')]")).click();
		fluentWaitMethod(locateElementBy("issue_show_page_id"));

		// issues - update Page - BASIC INFORMATION

				addFieldValue("issue_update_page_Title_textbox", issueTitle);
				addFieldValue("issue_update_page_Description_textarea", issueDescription);
				locateElementBy("issue_update_page_blank_area_label").click();
				addFieldValue("issue_update_page_Type_dropdown", issueType);
				addFieldValue("issue_update_page_Priority_dropdown", issuePriority);
				addFieldValue("issue_update_page_Currency_dropdown", issueCurrency);
				addFieldValue("issue_update_page_Timezone_dropdown", issueTimezone);
				try {
					if (driver.findElement(By.className("success-icon")).getText().contains("Current Date is different for the selected Time Zone"))
						driver.findElement(By.xpath(".//button[contains(.,'OK')]")).click();
				} catch (Exception e) {
				}
				addFieldValue("issue_update_page_RestrictPublicAccess_checkbox", issueRestrictPublicAccess);
				addFieldValue("issue_update_page_RestrictRequesterAccess_checkbox", issueRestrictRequesterAccess);
				addFieldValue("issue_update_page_SupplierAccess_checkbox", issueSupplierAccess);
				addFieldValue("issue_update_page_Tier_dropdown", issueTier);
				addFieldValue("issue_update_page_DependentEntity_checkbox", issueDependentEntity);
				
			
				//issues - update Page - Contrating Entity

				addFieldValue("issue_update_page_RecipientClientEntity_multiselect", issueRecipientClientEntity);
				addFieldValue("issue_update_page_RecipientCompanyCode_multiselect", issueRecipientCompanyCode);
				addFieldValue("issue_update_page_ContractingClientEntity_multiselect", issueContractingClientEntity);
				addFieldValue("issue_update_page_ContractingCompanyCode_multiselect", issueContractingCompanyCode);

				// issues - update Page - IMPORTANT DATES
				
				DatePicker_Enhanced date = new DatePicker_Enhanced();
				date.selectCalendar(issueIssueDate,"issueDate");
				date.selectCalendar(issuePlannedCompletionDate,"plannedCompletionDate");
				
				// Issues - Create Page - FUNCTIONS
				if (!issueFunctions.equalsIgnoreCase("")) {
					for (String entityData : issueFunctions.split(";")) {
						addFieldValue("issue_create_page_Functions_multiselect",entityData.trim());
					  	}
						addFieldValue("issue_create_page_Services_multiselect", issueServices);
					}
				
				addFieldValue("issue_create_page_ServiceCategory_multiselect", issueServiceCategory);
				    
				// Issues - Create Page - GEOGRAPHY
				if (locateElementBy("issue_create_page_ManagementRegions_multiselect")!=null) {
					if (!issueManagementRegions.equalsIgnoreCase("")) {
						for (String entityData : issueManagementRegions.split(";")) {
							addFieldValue("issue_create_page_ManagementRegions_multiselect",entityData.trim());
							}
						addFieldValue("issue_create_page_ManagementCountries_multiselect", issueManagementCountries);
						}
					}
				
				if (locateElementBy("issue_create_page_ContractRegions_multiselect")!=null) {
					if (!issueContractRegions.equalsIgnoreCase("")) {
						for (String entityData : issueContractRegions.split(";")) {
							addFieldValue("issue_create_page_ContractRegions_multiselect",entityData.trim());
							}
					    addFieldValue("issue_create_page_ContractCountries_multiselect", issueContractCountries);
						}
					}

				//issues - update Page - STAKEHOLDERS
				
				addFieldValue("issue_update_page_Responsibility_dropdown", issueResponsibility);

				//issues update Page - PROJECT INFORMATION

				addFieldValue("issue_update_page_ProjectID_multiselect", issueProjectID);
				addFieldValue("issue_update_page_ProjectLevels_multiselect", issueProjectLevels);
				addFieldValue("issue_update_page_Initiatives_multiselect", issueInitiatives);

		Assert.assertNotNull(driver.findElement(By.xpath("//button[contains(.,'Update')]")));
		((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", driver.findElement(By.xpath("//button[contains(.,'Update')]")));
		driver.findElement(By.xpath("//button[contains(.,'Update')]")).click();


		
		// ISSUE - EDIT PAGE - FIELD VALIDATIONS
		List<WebElement> validationClassElementList = driver.findElements(By.xpath(".//*[@class='errorMessageHolder']/ul/li"));
		if (!validationClassElementList.isEmpty()) {
			for (WebElement validationClassElement : validationClassElementList) {
				String validationMessage = validationClassElement.getText();

				WebElement elementParent = validationClassElement.findElement(By.xpath("..")).findElement(By.xpath("..")).findElement(By.xpath("..")).findElement(By.xpath(".."));

				if (validationMessage.equalsIgnoreCase(""))
					Logger.debug("For Actions -- " + elementParent.findElement(By.className("errorClass")).getAttribute("name").toUpperCase() + " -- is Mandatory");
				else
					Logger.debug("For Actions -- " + elementParent.findElement(By.className("errorClass")).getAttribute("name").toUpperCase() + " -- " + validationMessage);
				
				if(validationMessage.equalsIgnoreCase("Please select a date in past")) {
					Logger.debug("For Actions -- " + elementParent.findElement(By.className("errorClass")).getAttribute("name").toUpperCase() + " -- Please select date in past");
				}else {
					Logger.debug("For Actions -- " + elementParent.findElement(By.className("errorClass")).getAttribute("name").toUpperCase() + " -- "+validationMessage);
				}
				}
			
				

			driver.get(CONFIG.getProperty("endUserURL"));
			return;
			}

		fluentWaitMethod(locateElementBy("issue_show_page_id"));


		String entityShowPageName = locateElementBy("issues_show_page_title").getText();

		Assert.assertEquals(entityShowPageName, issueTitle);
		Logger.debug("issue Title on show page has been verified");

		fail = false;

       driver.get(CONFIG.getProperty("endUserURL"));
	    }



	@AfterMethod
	public void reportDataSetResult(ITestResult testResult) throws IOException {
		takeScreenShotOnFailure(testResult.getStatus(), this.getClass().getSimpleName());

		if(testResult.getStatus()== ITestResult.SKIP)
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
		return TestUtil.getData(issue_suite_xls, this.getClass().getSimpleName());
	}

}