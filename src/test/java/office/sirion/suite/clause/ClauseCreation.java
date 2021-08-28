package office.sirion.suite.clause;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
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

import testlink.api.java.client.TestLinkAPIException;
import testlink.api.java.client.TestLinkAPIResults;

public class ClauseCreation extends TestSuiteBase {
	String result = null;
	String runmodes[] = null;
	static int count = -1;
	static boolean isTestPass = true;
	String testCaseID;

	@BeforeTest
	public void checkTestSkip() {
		if (!TestUtil.isTestCaseRunnable(clause_suite_xls, this.getClass().getSimpleName())) {
			Logger.debug("Skipping Test Case" + this.getClass().getSimpleName() + " as runmode set to NO");
			throw new SkipException("Skipping Test Case" + this.getClass().getSimpleName() + " as runmode set to NO");
			}
		runmodes = TestUtil.getDataSetRunmodes(clause_suite_xls, this.getClass().getSimpleName());
		}

	@Test(dataProvider = "getTestData")
	public void ClauseCreationTest(String testCaseID ,String clauseName ,String clauseHeaderLabel ,String clauseSubHeader
			,String clauseType ,String clauseSuppliers ,String clauseContractingEntity ,String Type ,String clauseTermType
			,String ClauseContent,String clauseFunctions ,String clauseServices ,String clauseRiskTypes ,String clauseIndustryType
			,String clauseCompanyPosition ,String clauseAgreementType ,String clauseTransactionType
			,String clauseContractServices ,String clauseTCVMin ,String clauseTCVMax ,String clauseRecipientMarket
			,String clauseRegions ,String clauseCountries ,String clausePrivacy,String clauseComments ,String clauseActualDate
			,String clauseRequestedBy ,String clauseChangeRequest ,String clauseUploadFile)
			throws InterruptedException, TestLinkAPIException {

		count++;
		if (!runmodes[count].equalsIgnoreCase("Y"))
			throw new SkipException("Runmode for Test Data Set to NO -- " + count);

		this.testCaseID = testCaseID;

		openBrowser();
	    endUserLogin(CONFIG.getProperty("endUserURL"), CONFIG.getProperty("endUserUsername"), CONFIG.getProperty("endUserPassword"));

	    Logger.debug("Executing Test Case Clause Creation with Name -- " + clauseName);

	    driver.get(CONFIG.getProperty("endUserURL"));
	    //
		fluentWaitMethod(locateElementBy("cdr_quick_link"));

		locateElementBy("cdr_quick_link").click();
	    //
	    fluentWaitMethod(driver.findElement(By.className("dropdown-toggle")));

	    new Actions(driver).moveToElement(driver.findElement(By.className("dropdown-toggle"))).clickAndHold().build().perform();

	    driver.findElement(By.linkText("View Clause Library")).click();
	    //
		fluentWaitMethod(locateElementBy("entity_listing_page_plus_button"));

		new Actions(driver).moveToElement(locateElementBy("entity_listing_page_plus_button")).click().build().perform();
		
		new Actions(driver).moveToElement(driver.findElement(By.xpath(".//*[@id='drop']/ul/li/label"))).clickAndHold().build().perform();
		Thread.sleep(2000);

		driver.findElement(By.className("childlist")).findElement(By.linkText("Single")).click();
	    //
		fluentWaitMethod(locateElementBy("clause_create_page_name_textbox"));

		// CLAUSE LIBRARY - CREATE PAGE - BASIC INFORMATION
		if (CONFIG.getProperty("entityDataCreation").equalsIgnoreCase("Yes")) {
			Date date = new Date();
			SimpleDateFormat sdf = new SimpleDateFormat("ss");
			String datestr = sdf.format(date);

			addFieldValue("clause_create_page_name_textbox", clauseName.trim() + datestr);
			}
		else
			addFieldValue("clause_create_page_name_textbox", clauseName.trim());

	    addFieldValue("clause_create_page_header_label_textbox", clauseHeaderLabel.trim());

	    addFieldValue("clause_create_page_sub_header_textbox", clauseSubHeader.trim());

	    addFieldValue("clause_create_page_clause_clause_type_dropdown", clauseType.trim());

	    addFieldValue("clause_create_page_suppliers_multi_dropdown", clauseSuppliers.trim());

	    addFieldValue("clause_create_page_contracting_entity_dropdown", clauseContractingEntity.trim());
	    
	    addFieldValue("clause_create_page_term_type_dropdown", clauseTermType.trim());

	    addFieldValue("clause_create_page_clause_type_dropdown", Type.trim());

	    if (Type.trim().equalsIgnoreCase("Clause"))
		    addFieldValue("clause_create_page_clause_category_dropdown", clauseTermType.trim());
	    else if (Type.trim().equalsIgnoreCase("Definition"))
		    addFieldValue("clause_create_page_defined_terms_dropdown", clauseTermType.trim());

	    // CLAUSE LIBRARY - CREATE PAGE - FUNCTIONS
		addFieldValue("clause_create_page_functions_multi_dropdown", clauseFunctions.trim());

		addFieldValue("clause_create_page_services_multi_dropdown", clauseServices.trim());

		// CLAUSE LIBRARY - CREATE PAGE - CLAUSE CONTENT
	    if (!ClauseContent.trim().equalsIgnoreCase("")) {
	    	driver.findElement(By.name("addTextClause")).click();
	    	Thread.sleep(2000);

	    	WebElement clauseContentFrameParent = driver.findElement(By.cssSelector("fieldset.fieldset_2206.popOutside.pop")).findElement(By.id("elem_7386"));
			driver.switchTo().frame(clauseContentFrameParent.findElement(By.tagName("iframe")));

			driver.findElement(By.cssSelector("div.note-editable.panel-body")).clear();
			driver.findElement(By.cssSelector("div.note-editable.panel-body")).sendKeys(ClauseContent.trim());

			driver.switchTo().defaultContent();
			new Actions(driver).moveToElement(driver.findElement(By.className("saveClause"))).build().perform();
			driver.findElement(By.className("saveClause")).click();
	    	}

	    // CLAUSE LIBRARY - CREATE PAGE - RISKS
	    addFieldValue("clause_create_page_risk_types_multi_dropdown", clauseRiskTypes.trim());
		
	    // CLAUSE LIBRARY - CREATE PAGE - OTHER INFORMATION
	    addFieldValue("clause_create_page_industry_types_multi_dropdown", clauseIndustryType.trim());

	    addFieldValue("clause_create_page_company_position_dropdown", clauseCompanyPosition.trim());
	    
	    addFieldValue("clause_create_page_agreement_types_multi_dropdown", clauseAgreementType.trim());
	    
	    addFieldValue("clause_create_page_clause_transaction_type_dropdown", clauseTransactionType.trim());
	    
	    addFieldValue("clause_create_page_contract_services_multi_dropdown", clauseContractServices.trim());

	    addFieldValue("clause_create_page_tcv_min_numeric_textbox", clauseTCVMin.trim());

	    addFieldValue("clause_create_page_tcv_max_numeric_textbox", clauseTCVMax.trim());
	    
	    addFieldValue("clause_create_page_recipient_market_multi_dropdown", clauseRecipientMarket.trim());

	    // CLAUSE LIBRARY - CREATE PAGE - GEOGRAPHY
		addFieldValue("clause_create_page_regions_multi_dropdown", clauseRegions.trim());

		addFieldValue("clause_create_page_countries_multi_dropdown", clauseCountries.trim());

		// CLAUSE LIBRARY - CREATE PAGE - COMMENTS AND ATTACHMENTS
		addFieldValue("entity_create_page_privacy_checkbox", clausePrivacy.trim());

		addFieldValue("entity_create_page_comments_textarea", clauseComments.trim());

		addFieldValue("entity_create_page_actual_date", clauseActualDate.trim());

		addFieldValue("entity_create_page_requested_by_dropdown", clauseRequestedBy.trim());

		addFieldValue("entity_create_page_change_request_dropdown", clauseChangeRequest.trim());
		
		if (!clauseUploadFile.equalsIgnoreCase("")) {
			locateElementBy("entity_create_page_upload_browse_button").sendKeys(System.getProperty("user.dir")+"\\file-upload\\Clause\\" + clauseUploadFile.trim());
			//
			}

		Assert.assertNotNull(driver.findElement(By.xpath("//button[contains(.,'Save Clause')]")));
		((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", driver.findElement(By.xpath("//button[contains(.,'Save Clause')]")));
		driver.findElement(By.xpath("//button[contains(.,'Save Clause')]")).click();
		//

		// CLAUSE - CREATE PAGE - FIELD VALIDATIONS
		List<WebElement> validationClassElementList = driver.findElements(By.xpath(".//*[@class='errorMessageHolder']/ul/li"));
		if (!validationClassElementList.isEmpty()) {
			for (WebElement validationClassElement : validationClassElementList) {
				String validationMessage = validationClassElement.getText();

				WebElement elementParent = validationClassElement.findElement(By.xpath("..")).findElement(By.xpath("..")).findElement(By.xpath("..")).findElement(By.xpath(".."));

				if (validationMessage.equalsIgnoreCase(""))
					Logger.debug("For Clause -- " + elementParent.findElement(By.className("errorClass")).getAttribute("name").toUpperCase() + " -- is Mandatory");
				else
					Logger.debug("For Clause -- " + elementParent.findElement(By.className("errorClass")).getAttribute("name").toUpperCase() + " -- " + validationMessage);
				}

			driver.get(CONFIG.getProperty("endUserURL"));
			return;
			}

		// CLAUSE - CREATE PAGE - VALIDATIONS
		if (locateElementBy("clause_create_page_name_textbox").findElement(By.tagName("span")).getText().equalsIgnoreCase("Name Must Be Unique")) {
		   	Logger.debug("Clause Name -- "+clauseName+" -- already exists in the system");

		   	driver.get(CONFIG.getProperty("endUserURL"));
	   		return;
	   		}

		String entityID = driver.findElement(By.className("success-icon")).findElement(By.id("hrefElemId")).getText();

		Logger.debug("CLAUSE created successfully with Entity ID " + entityID);

		driver.findElement(By.className("success-icon")).findElement(By.id("hrefElemId")).click();
		//
		fluentWaitMethod(driver.findElement(By.linkText("GENERAL")));

		driver.findElement(By.linkText("GENERAL")).click();
		//
		fluentWaitMethod(locateElementBy("clause_show_page_id"));

		String entityShowPageID = locateElementBy("clause_show_page_id").getText();

		Assert.assertEquals(entityShowPageID, entityID);

	    Logger.debug("Test Case Clause Creation with Name -- " + clauseName + " -- is PASSED");
		driver.get(CONFIG.getProperty("endUserURL"));
	    }

	@AfterMethod
	public void reportDataSetResult(ITestResult testResult) throws IOException {
		takeScreenShotOnFailure(testResult.getStatus(), this.getClass().getSimpleName());

		if(testResult.getStatus()==ITestResult.SKIP)
			TestUtil.reportDataSetResult(clause_suite_xls, this.getClass().getSimpleName(), count+2, "SKIP");
		else if(testResult.getStatus()==ITestResult.FAILURE) {
			isTestPass=false;
			TestUtil.reportDataSetResult(clause_suite_xls, this.getClass().getSimpleName(), count+2, "FAIL");
			result= "Fail";
			}
		else if (testResult.getStatus()==ITestResult.SUCCESS) {
			TestUtil.reportDataSetResult(clause_suite_xls, this.getClass().getSimpleName(), count+2, "PASS");
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
			TestUtil.reportDataSetResult(clause_suite_xls, "Test Cases", TestUtil.getRowNum(clause_suite_xls, this.getClass().getSimpleName()),"PASS");
		else
			TestUtil.reportDataSetResult(clause_suite_xls, "Test Cases", TestUtil.getRowNum(clause_suite_xls, this.getClass().getSimpleName()),"FAIL");
		}

	@DataProvider
	public Object[][] getTestData() {
		return TestUtil.getData(clause_suite_xls, this.getClass().getSimpleName());
		}
	}
