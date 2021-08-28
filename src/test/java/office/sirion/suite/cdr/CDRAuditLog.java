package office.sirion.suite.cdr;

import java.io.IOException;
import java.util.List;

import office.sirion.util.TestUtil;
import office.sirion.util.TestlinkIntegration;

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

import testlink.api.java.client.TestLinkAPIException;
import testlink.api.java.client.TestLinkAPIResults;

public class CDRAuditLog extends TestSuiteBase {
	String result = null;
	String runmodes[] = null;
	static int count = -1;
	static boolean isTestPass = true;
	String testCaseID;
	
	@BeforeTest
	public void checkTestSkip() {
		if (!TestUtil.isTestCaseRunnable(cdr_suite_xls, this.getClass().getSimpleName())) {
			Logger.debug("Skipping Test Case" + this.getClass().getSimpleName() + " as runmode set to NO");
			throw new SkipException("Skipping Test Case" + this.getClass().getSimpleName() + " as runmode set to NO");
			}
		runmodes = TestUtil.getDataSetRunmodes(cdr_suite_xls, this.getClass().getSimpleName());
		}
	
	@Test(dataProvider = "getTestData")
	public void CDRAuditLogTest (String testCaseID, String cdrTitle, String cdrClientContractingEntity, String cdrCounterPartyContractingEntity, String cdrSuppliers,
			String cdrCounterPartyAddress, String cdrPriority,	String cdrPaperType, String cdrTimezone, String cdrNatureOfTransaction,	String cdrAgreementType,
			String cdrIndustryTypes, String cdrFunctions, String cdrServices, String cdrTermType, String cdrBusinessUnits, String cdrBusinessLines, String cdrDealOverview,
			String cdrCurrency, String cdrACV, String cdrTCV, String cdrRegions, String cdrCountries, String cdrEffectiveDate, String cdrExpirationDate,
			String cdrReqSubmissionDate, String cdrExpCompletionDate, String cdrStartDate, String cdrCompletionDate, String cdrCreatedFor, String cdrRecipientMarket,
			String cdrComments, String cdrActualDate, String cdrRequestedBy, String cdrChangeRequest, String cdrUploadFile, String cdrParent
			) throws InterruptedException {
		
		count++;
		if (!runmodes[count].equalsIgnoreCase("Y"))
			throw new SkipException("Runmode for Test Data Set to NO -- " + count);
		
		this.testCaseID = testCaseID;

		openBrowser();
	    endUserLogin(CONFIG.getProperty("endUserURL"), CONFIG.getProperty("endUserUsername"), CONFIG.getProperty("endUserPassword"));
	    
	    Logger.debug("Executing Test Case CDR Update with Title -- " + cdrTitle);
		
	    driver.get(CONFIG.getProperty("endUserURL"));

		fluentWaitMethod(locateElementBy("cdr_quick_link"));

		locateElementBy("cdr_quick_link").click();

		fluentWaitMethod(locateElementBy("entity_listing_page_plus_button"));

		new Select(locateElementBy("entity_listing_page_display_dropdown_link")).selectByVisibleText(CONFIG.getProperty("maxEntityListingDropdown"));


		if (!cdrParent.equalsIgnoreCase("") && cdrParent.length()<=30)
			driver.findElement(By.xpath(".//*[@id='cr']/tbody/tr/td[2][./text()='"+cdrParent+"']/preceding-sibling::td[1]/a")).click();
		else if (!cdrParent.equalsIgnoreCase("") && cdrParent.length()>30)
			driver.findElement(By.xpath(".//*[@id='cr']/tbody/tr/td[2][div/div[./text()='"+cdrParent+"']]/preceding-sibling::td[1]/a")).click();

		fluentWaitMethod(driver.findElement(By.cssSelector("ng-form[template='tab.actions']")).findElement(By.cssSelector("div.submit.freeze")));
		
		// CDR - SHOW PAGE - EDIT BUTTON
	    WebElement elementEdit = driver.findElement(By.cssSelector("ng-form[template='tab.actions']")).findElement(By.cssSelector("div.submit.freeze"));		
		Assert.assertNotNull(elementEdit.findElement(By.xpath(".//button[contains(.,'Edit')]")));
	    ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", elementEdit.findElement(By.xpath(".//button[contains(.,'Edit')]")));
	    elementEdit.findElement(By.xpath(".//button[contains(.,'Edit')]")).click();

		fluentWaitMethod(locateElementBy("cdr_create_page_title_textbox"));
		
	    // CDR - EDIT PAGE - BASIC INFORMATION
	    addFieldValue("cdr_create_page_title_textbox", cdrTitle.trim());
	    
	    addFieldValue("cdr_create_page_client_contracting_entity_dropdown", cdrClientContractingEntity.trim());

	    addFieldValue("cdr_create_page_counter_party_contracting_entity_textbox", cdrCounterPartyContractingEntity.trim());
	    
	    addFieldValue("cdr_create_page_suppliers_multi_dropdown", cdrSuppliers.trim());
	    
	    addFieldValue("cdr_create_page_counter_party_address_textarea", cdrCounterPartyAddress.trim());

	    addFieldValue("cdr_create_page_priority_dropdown", cdrPriority.trim());
	    
	    addFieldValue("cdr_create_page_paper_type_dropdown", cdrPaperType.trim());
	    
	    addFieldValue("cdr_create_page_timezone_dropdown", cdrTimezone.trim());

	    addFieldValue("cdr_create_page_nature_of_transaction_dropdown", cdrNatureOfTransaction.trim());
	    
	    addFieldValue("cdr_create_page_agreement_type_dropdown", cdrAgreementType.trim());
	    
	    addFieldValue("cdr_create_page_industry_types_multi_dropdown", cdrIndustryTypes.trim());

	    // CDR - EDIT PAGE - FUNCTIONS
	    addFieldValue("cdr_create_page_functions_multi_dropdown", cdrFunctions.trim());

	    addFieldValue("cdr_create_page_services_multi_dropdown", cdrServices.trim());
		
	    // CDR - EDIT PAGE - OTHER INFORMATION
	    addFieldValue("cdr_create_page_term_type_dropdown", cdrTermType.trim());

	    addFieldValue("cdr_create_page_business_units_multi_dropdown", cdrBusinessUnits.trim());
	    
	    addFieldValue("cdr_create_page_business_lines_multi_dropdown", cdrBusinessLines.trim());
	    
	    addFieldValue("cdr_create_page_deal_overview_textarea", cdrDealOverview.trim());
	    
	    // CDR - EDIT PAGE - FINANCIAL INFORMATION
	    addFieldValue("cdr_create_page_currency_dropdown", cdrCurrency.trim());

	    addFieldValue("cdr_create_page_acv_numeric_textbox", cdrACV.trim());

	    addFieldValue("cdr_create_page_tcv_numeric_textbox", cdrTCV.trim());

	    // CDR - EDIT PAGE - GEOGRAPHY
	    addFieldValue("cdr_create_page_regions_multi_dropdown", cdrRegions.trim());

	    addFieldValue("cdr_create_page_countries_multi_dropdown", cdrCountries);
		
	    // CDR - EDIT PAGE - INFORMATION DATES
	    addFieldValue("cdr_create_page_effective_date", cdrEffectiveDate.trim());
		
	    addFieldValue("cdr_create_page_expiration_date", cdrExpirationDate.trim());
		
	    addFieldValue("cdr_create_page_submission_date", cdrReqSubmissionDate.trim());
		
	    addFieldValue("cdr_create_page_expected_completion_date", cdrExpCompletionDate.trim());
	    
	    addFieldValue("cdr_create_page_start_date", cdrStartDate.trim());
	    
	    addFieldValue("cdr_create_page_completion_date", cdrCompletionDate.trim());
	    
	    // CDR - EDIT PAGE - CONTRACTING PARTY
	    addFieldValue("cdr_create_page_created_for_multi_dropdown", cdrCreatedFor.trim());

	    addFieldValue("cdr_create_page_recipient_markets_multi_dropdown", cdrRecipientMarket.trim());
	    
		// CDR - EDIT PAGE - COMMENTS AND ATTACHMENTS
		addFieldValue("entity_create_page_comments_textarea", cdrComments.trim());
		    
		addFieldValue("entity_create_page_requested_by_dropdown", cdrRequestedBy.trim());
		    
//		addFieldValue("entity_create_page_actual_date", cdrActualDate.trim());
		    
		addFieldValue("entity_create_page_change_request_dropdown", cdrChangeRequest.trim());
		
		if (!cdrUploadFile.equalsIgnoreCase("")) {
			locateElementBy("entity_create_page_upload_browse_button").sendKeys(System.getProperty("user.dir")+"\\file-upload\\CDR\\" + cdrUploadFile.trim());

			}

		// CDR - EDIT PAGE - UPDATE BUTTON
	    WebElement elementUpdate = driver.findElement(By.cssSelector("ng-form[template='tab.actions']")).findElement(By.cssSelector("div.submit.freeze"));		
		Assert.assertNotNull(elementUpdate.findElement(By.xpath(".//button[contains(.,'Update')]")));
	    ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", elementUpdate.findElement(By.xpath(".//button[contains(.,'Update')]")));
	    elementUpdate.findElement(By.xpath(".//button[contains(.,'Update')]")).click();


		// CDR - EDIT PAGE - FIELD VALIDATIONS
		List<WebElement> validationClassElementList = driver.findElements(By.xpath(".//*[@class='errorMessageHolder']/ul/li"));
		if (!validationClassElementList.isEmpty()) {
			for (WebElement validationClassElement : validationClassElementList) {
				String validationMessage = validationClassElement.getText();
				
				WebElement elementParent = validationClassElement.findElement(By.xpath("..")).findElement(By.xpath("..")).findElement(By.xpath("..")).findElement(By.xpath(".."));
				
				if (validationMessage.equalsIgnoreCase(""))
					Logger.debug("For CDR -- " + elementParent.findElement(By.className("errorClass")).getAttribute("name").toUpperCase() + " -- is Mandatory");
				else
					Logger.debug("For CDR -- " + elementParent.findElement(By.className("errorClass")).getAttribute("name").toUpperCase() + " -- " + validationMessage);
				}

			driver.get(CONFIG.getProperty("endUserURL"));
			return;
			}

	    fluentWaitMethod(locateElementBy("cdr_create_page_title_textbox"));

		String entityShowPageName = locateElementBy("cdr_create_page_title_textbox").getText();

		Assert.assertEquals(entityShowPageName, cdrTitle.trim());

	    driver.findElement(By.linkText("AUDIT LOG")).click();

	    Logger.debug("Executing Test Case CDR Update with Title -- " + cdrTitle + " -- is STARTED");
		
		WebElement elementOddRowSelected = driver.findElement(By.id("table_3505")).findElements(By.className("odd")).get(0);
		
		Assert.assertEquals(elementOddRowSelected.findElement(By.cssSelector("td.col_wrap_action_name")).getText(), "Updated");

	    if (!cdrRequestedBy.equalsIgnoreCase(""))
			Assert.assertEquals(elementOddRowSelected.findElement(By.cssSelector("td.col_wrap_requested_by")).getText(), cdrRequestedBy);
	    else
	    	Assert.assertEquals(elementOddRowSelected.findElement(By.cssSelector("td.col_wrap_requested_by")).getText(), CONFIG.getProperty("endUserFullName"));

	    Assert.assertEquals(elementOddRowSelected.findElement(By.cssSelector("td.col_wrap_completed_by")).getText(), CONFIG.getProperty("endUserFullName"));

	    if (!cdrComments.equalsIgnoreCase(""))
		    Assert.assertEquals(elementOddRowSelected.findElement(By.cssSelector("td.col_wrap_comment")).getText(), "Yes");
	    else
		    Assert.assertEquals(elementOddRowSelected.findElement(By.cssSelector("td.col_wrap_comment")).getText(), "No");

	    if (!cdrUploadFile.equalsIgnoreCase(""))
		    Assert.assertEquals(elementOddRowSelected.findElement(By.cssSelector("td.col_wrap_document")).getText(), "Yes");
	    else
		    Assert.assertEquals(elementOddRowSelected.findElement(By.cssSelector("td.col_wrap_document")).getText(), "No");

	    elementOddRowSelected.findElement(By.cssSelector("td.col_wrap_history")).findElement(By.className("serverHistoryView")).click();
	    Thread.sleep(2000);

		List<WebElement> auditRowElementList = driver.findElement(By.id("historyDataTable")).findElement(By.id("data")).findElements(By.tagName("tr"));
	    for (int iAuditRow = 0; iAuditRow < auditRowElementList.size(); iAuditRow++) {
		      for (int jAuditCol = 0; jAuditCol < auditRowElementList.get(0).findElements(By.tagName("td")).size(); jAuditCol++) {
		    	  // CDR - BASIC INFORMATION
			      if (auditRowElementList.get(iAuditRow).findElements(By.tagName("td")).get(jAuditCol).getText().equalsIgnoreCase("Title")) {
				         List<WebElement> elementDataList = auditRowElementList.get(iAuditRow).findElements(By.tagName("td"));
				         Assert.assertEquals(elementDataList.get(3).getText(), cdrTitle.trim());
				         }

			      else if (auditRowElementList.get(iAuditRow).findElements(By.tagName("td")).get(jAuditCol).getText().equalsIgnoreCase("Client Contracting Entity")) {
				         List<WebElement> elementDataList = auditRowElementList.get(iAuditRow).findElements(By.tagName("td"));
				         Assert.assertEquals(elementDataList.get(3).getText(), cdrClientContractingEntity.trim());
				         }

			      else if (auditRowElementList.get(iAuditRow).findElements(By.tagName("td")).get(jAuditCol).getText().equalsIgnoreCase("Counter Party Contracting Entity")) {
				         List<WebElement> elementDataList = auditRowElementList.get(iAuditRow).findElements(By.tagName("td"));
				         Assert.assertEquals(elementDataList.get(3).getText(), cdrCounterPartyContractingEntity);
				         }   
	
			      else if (auditRowElementList.get(iAuditRow).findElements(By.tagName("td")).get(jAuditCol).getText().equalsIgnoreCase("Counter Party Address")) {
				         List<WebElement> elementDataList = auditRowElementList.get(iAuditRow).findElements(By.tagName("td"));
				         Assert.assertEquals(elementDataList.get(3).getText(), cdrCounterPartyAddress.trim());
				         }   

			      else if (auditRowElementList.get(iAuditRow).findElements(By.tagName("td")).get(jAuditCol).getText().equalsIgnoreCase("Priority")) {
				         List<WebElement> elementDataList = auditRowElementList.get(iAuditRow).findElements(By.tagName("td"));
				         Assert.assertEquals(elementDataList.get(3).getText(), cdrPriority.trim());
				         }   

			      else if (auditRowElementList.get(iAuditRow).findElements(By.tagName("td")).get(jAuditCol).getText().equalsIgnoreCase("Paper Type")) {
				         List<WebElement> elementDataList = auditRowElementList.get(iAuditRow).findElements(By.tagName("td"));
				         Assert.assertEquals(elementDataList.get(3).getText(), cdrPaperType.trim());
				         }   

			      else if (auditRowElementList.get(iAuditRow).findElements(By.tagName("td")).get(jAuditCol).getText().equalsIgnoreCase("Time Zone")) {
				         List<WebElement> elementDataList = auditRowElementList.get(iAuditRow).findElements(By.tagName("td"));
				         Assert.assertEquals(elementDataList.get(3).getText(), cdrTimezone.trim());
				         }   

			      else if (auditRowElementList.get(iAuditRow).findElements(By.tagName("td")).get(jAuditCol).getText().equalsIgnoreCase("Agreement Type")) {
				         List<WebElement> elementDataList = auditRowElementList.get(iAuditRow).findElements(By.tagName("td"));
				         Assert.assertEquals(elementDataList.get(3).getText(), cdrAgreementType.trim());
				         }   
			      }
		      }

	    Logger.debug("Executing Test Case CDR Update with Title -- " + cdrTitle + " -- is PASSED");
        driver.get(CONFIG.getProperty("endUserURL"));
	    }

	@AfterMethod
	public void reportDataSetResult(ITestResult testResult) throws IOException {
		takeScreenShotOnFailure(testResult.getStatus(), this.getClass().getSimpleName());
		
			if(testResult.getStatus()==ITestResult.SKIP)
				TestUtil.reportDataSetResult(cdr_suite_xls, this.getClass().getSimpleName(), count+2, "SKIP");

			else if(testResult.getStatus()==ITestResult.FAILURE) {
				isTestPass=false;
				TestUtil.reportDataSetResult(cdr_suite_xls, this.getClass().getSimpleName(), count+2, "FAIL");
				result= "Fail";
				}
			
			else if (testResult.getStatus()==ITestResult.SUCCESS) {
				TestUtil.reportDataSetResult(cdr_suite_xls, this.getClass().getSimpleName(), count+2, "PASS");
				result= "Pass";
				}
			
			try {
				if (!testCaseID.equalsIgnoreCase(""))
					TestlinkIntegration.updateTestLinkResult(testCaseID,"",result);
				} catch (Exception e) {
				
				}
			}

	@AfterTest
	public void reportTestResult() {
		if (isTestPass)
			TestUtil.reportDataSetResult(cdr_suite_xls, "Test Cases", TestUtil.getRowNum(cdr_suite_xls, this.getClass().getSimpleName()),"PASS");
		else
			TestUtil.reportDataSetResult(cdr_suite_xls, "Test Cases", TestUtil.getRowNum(cdr_suite_xls, this.getClass().getSimpleName()),"FAIL");
		}

	@DataProvider
	public Object[][] getTestData() {
		return TestUtil.getData(cdr_suite_xls, this.getClass().getSimpleName());
		}
	}