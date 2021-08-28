package office.sirion.suite.cdr;

import java.io.IOException;
import java.util.List;
import office.sirion.util.TestUtil;
import office.sirion.util.TestlinkIntegration;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.testng.Assert;
import org.testng.ITestResult;
import org.testng.SkipException;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import testlink.api.java.client.TestLinkAPIResults;

public class CDRCreation extends TestSuiteBase {
	private String result = null;
    private String runmodes[] = null;
    private static int count = -1;
    private static boolean isTestPass = true;
    private String testCaseID;

	@BeforeTest
	public void checkTestSkip() {
		if (!TestUtil.isTestCaseRunnable(cdr_suite_xls, this.getClass().getSimpleName())) {
			Logger.debug("Skipping Test Case" + this.getClass().getSimpleName() + " as runmode set to NO");
			throw new SkipException("Skipping Test Case" + this.getClass().getSimpleName() + " as runmode set to NO");
			}
		runmodes = TestUtil.getDataSetRunmodes(cdr_suite_xls, this.getClass().getSimpleName());
		}

	@Test(dataProvider = "getTestData")
	public void CDRCreationTest (String testCaseID, String cdrTitle, String cdrClientContractingEntity, String cdrCounterPartyContractingEntity,
                                 String cdrSuppliers, String cdrCustomer, String cdrCounterPartyAddress, String cdrPriority, String cdrPaperType,
                                 String cdrTimezone, String cdrNatureOfTransaction, String cdrSpendType, String cdrAgreementType,
                                 String cdrContractPortingStatus, String cdrContractDraftRequestType, String cdrIndustryTypes,
                                 String cdrFunctions, String cdrServices, String cdrTermType, String cdrBusinessUnits, String cdrBusinessLines,
                                 String cdrDealOverview, String cdrCurrency, String cdrACV, String cdrTCV, String cdrRegions, String cdrCountries,
                                 String cdrEffectiveDate, String cdrExpirationDate, String cdrReqSubmissionDate, String cdrExpCompletionDate,
                                 String cdrStartDate, String cdrCompletionDate, String cdrInternalContractingParty, String cdrContractingPartyCompanyCode,
                                 String cdrDeliveryRecipient, String cdrDeliveryRecipientCompanyCode, String cdrVendorContractingParty,
                                 String cdrCreatedFor, String cdrMarket, String cdrComments, String cdrActualDate, String cdrRequestedBy,
                                 String cdrChangeRequest, String cdrUploadFile) throws InterruptedException {

		count++;
		if (!runmodes[count].equalsIgnoreCase("Y"))
			throw new SkipException("Runmode for Test Data Set to NO -- " + count);

		this.testCaseID = testCaseID;

		openBrowser();
	    endUserLogin(CONFIG.getProperty("endUserURL"), CONFIG.getProperty("endUserUsername"), CONFIG.getProperty("endUserPassword"));

	    Logger.debug("Executing Test Case CDR Creation with Title -- " + cdrTitle);

	    driver.get(CONFIG.getProperty("endUserURL"));
	    //
		fluentWaitMethod(locateElementBy("cdr_quick_link"));

		locateElementBy("cdr_quick_link").click();
	    //
		fluentWaitMethod(locateElementBy("cdr_entity_listing_page_plus_button"));

		locateElementBy("cdr_entity_listing_page_plus_button").click();
	    //
	    fluentWaitMethod(locateElementBy("cdr_create_page_title_textbox"));

	    // CDR - CREATE PAGE - BASIC INFORMATION
	    addFieldValue("cdr_create_page_title_textbox", cdrTitle.trim());

	    addFieldValue("cdr_create_page_client_contracting_entity_dropdown", cdrClientContractingEntity.trim());

	    addFieldValue("cdr_create_page_counter_party_contracting_entity_textbox", cdrCounterPartyContractingEntity.trim());

	    addFieldValue("cdr_create_page_suppliers_multi_dropdown", cdrSuppliers.trim());

	    //Code for Autocomplete

        Actions actions = new Actions(driver);
        actions.moveToElement(driver.findElement(By.xpath("//span[@id='elem_12335']")));
        actions.click();
        actions.sendKeys(cdrCustomer.trim());
        actions.build().perform();
        List<WebElement> autoSuggest = driver.findElements(By.id("ui-id-33"));
        System.out.println("Size of the AutoSuggets is = " + autoSuggest.size());

        for (WebElement a : autoSuggest)
            System.out.println("Values are = " + a.getText());
        Thread.sleep(5000);
        autoSuggest.get(0).click();

        addFieldValue("cdr_create_page_counter_party_address_textarea", cdrCounterPartyAddress.trim());

	    addFieldValue("cdr_create_page_priority_dropdown", cdrPriority.trim());

	    addFieldValue("cdr_create_page_paper_type_dropdown", cdrPaperType.trim());

	    addFieldValue("cdr_create_page_timezone_dropdown", cdrTimezone.trim());

	    addFieldValue("cdr_create_page_nature_of_transaction_dropdown", cdrNatureOfTransaction.trim());

        addFieldValue("cdr_create_page_spend_type_dropdown", cdrSpendType.trim());

        addFieldValue("cdr_create_page_agreement_type_dropdown", cdrAgreementType.trim());

        addFieldValue("cdr_create_page_contract_porting_status_dropdown", cdrContractPortingStatus.trim());

        addFieldValue("cdr_create_page_contract_draft_request_type_dropdown", cdrContractDraftRequestType.trim());

        addFieldValue("cdr_create_page_industry_types_multi_dropdown", cdrIndustryTypes.trim());

	    // CDR - CREATE PAGE - FUNCTIONS
	    addFieldValue("cdr_create_page_functions_multi_dropdown", cdrFunctions.trim());

	    addFieldValue("cdr_create_page_services_multi_dropdown", cdrServices.trim());

	    // CDR - CREATE PAGE - OTHER INFORMATION
	    addFieldValue("cdr_create_page_term_type_dropdown", cdrTermType.trim());

	    addFieldValue("cdr_create_page_business_units_multi_dropdown", cdrBusinessUnits.trim());

	    addFieldValue("cdr_create_page_business_lines_multi_dropdown", cdrBusinessLines.trim());

	    addFieldValue("cdr_create_page_deal_overview_textarea", cdrDealOverview.trim());

	    // CDR - CREATE PAGE - FINANCIAL INFORMATION
	    addFieldValue("cdr_create_page_currency_dropdown", cdrCurrency.trim());

	    addFieldValue("cdr_create_page_acv_numeric_textbox", cdrACV.trim());

	    addFieldValue("cdr_create_page_tcv_numeric_textbox", cdrTCV.trim());

	    // CDR - CREATE PAGE - GEOGRAPHY
	    addFieldValue("cdr_create_page_regions_multi_dropdown", cdrRegions.trim());

	    addFieldValue("cdr_create_page_countries_multi_dropdown", cdrCountries);

	    // CDR - CREATE PAGE - INFORMATION DATES
	    addFieldValue("cdr_create_page_effective_date", cdrEffectiveDate.trim());

	    addFieldValue("cdr_create_page_expiration_date", cdrExpirationDate.trim());

	    addFieldValue("cdr_create_page_submission_date", cdrReqSubmissionDate.trim());

	    addFieldValue("cdr_create_page_expected_completion_date", cdrExpCompletionDate.trim());

	    addFieldValue("cdr_create_page_start_date", cdrStartDate.trim());

	    addFieldValue("cdr_create_page_completion_date", cdrCompletionDate.trim());

	    // CDR - CREATE PAGE - CONTRACTING PARTY

        addFieldValue("cdr_create_page_internal_contracting_party_dropdown", cdrInternalContractingParty.trim());

        addFieldValue("cdr_create_page_contracting_party_company_code_dropdown", cdrContractingPartyCompanyCode.trim());

        addFieldValue("cdr_create_page_delivery_recipient_dropdown", cdrDeliveryRecipient.trim());

        addFieldValue("cdr_create_page_delivery_recipient_company_code_dropdown", cdrDeliveryRecipientCompanyCode.trim());

        addFieldValue("cdr_create_page_vendor_contracting_party_dropdown", cdrVendorContractingParty.trim());

        addFieldValue("cdr_create_page_created_for_multi_dropdown", cdrCreatedFor.trim());

	    addFieldValue("cdr_create_page_recipient_markets_multi_dropdown", cdrMarket.trim());

	    // CDR - CREATE PAGE - COMMENTS AND ATTACHMENTS
		addFieldValue("entity_create_page_comments_textarea", cdrComments.trim());

		addFieldValue("entity_create_page_requested_by_dropdown", cdrRequestedBy.trim());

		addFieldValue("entity_create_page_actual_date", cdrActualDate.trim());

		addFieldValue("entity_create_page_change_request_dropdown", cdrChangeRequest.trim());

		if (!cdrUploadFile.equalsIgnoreCase("")) {
			locateElementBy("entity_create_page_upload_browse_button").sendKeys(System.getProperty("user.dir")+"\\file-upload\\CDR\\" + cdrUploadFile.trim());
			//
			}

		Assert.assertNotNull(driver.findElement(By.xpath("//button[contains(.,'Save CDR')]")));
		((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", driver.findElement(By.xpath("//button[contains(.,'Save CDR')]")));
		driver.findElement(By.xpath("//button[contains(.,'Save CDR')]")).click();
		//

		// CDR - CREATE PAGE - FIELD VALIDATIONS
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

		if (driver.findElements(By.className("success-icon")).size()!=0) {
			 String entityID = driver.findElement(By.className("success-icon")).findElement(By.id("hrefElemId")).getText();

			 Logger.debug("CDR created successfully with Entity ID " + entityID);

			 driver.findElement(By.className("success-icon")).findElement(By.id("hrefElemId")).click();
			 //
			 fluentWaitMethod(locateElementBy("cdr_show_page_id"));

			 String entityShowPageID = locateElementBy("cdr_show_page_id").getText();

			 Assert.assertEquals(entityShowPageID, entityID);
			 }

		Logger.debug("Test Case CDR Creation with Title -- " + cdrTitle + " -- is PASSED");
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
                 Logger.debug("Not Test-Case ID has been mapped");
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
