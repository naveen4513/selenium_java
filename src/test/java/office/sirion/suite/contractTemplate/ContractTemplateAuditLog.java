package office.sirion.suite.contractTemplate;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import office.sirion.util.TestAuditLogUtil;
import office.sirion.util.TestUtil;
import office.sirion.util.TestlinkIntegration;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
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

public class ContractTemplateAuditLog extends TestSuiteBase {
	String result = null;
	String runmodes[] = null;
	static int count = -1;
	static boolean isTestPass = true;
	String testCaseID;
	
	@BeforeTest
	public void checkTestSkip() {
		if (!TestUtil.isTestCaseRunnable(contract_template_suite_xls, this.getClass().getSimpleName())) {
			Logger.debug("Skipping Test Case" + this.getClass().getSimpleName() + " as runmode set to NO");
			throw new SkipException("Skipping Test Case" + this.getClass().getSimpleName() + " as runmode set to NO");
			}
		runmodes = TestUtil.getDataSetRunmodes(contract_template_suite_xls, this.getClass().getSimpleName());
		}
	
	@Test(dataProvider = "getTestData")
	public void ContractTemplateAuditLogTest (String testCaseID, String Name, String TemplateHeader, String TableOfContent, String TransactionTypes, String Suppliers,
			String Category, String TemplateType, String ContractingEntity, String TermType, String UploadTemplate, String Functions, String Services, String Regions,
			String Countries, String IndustryTypes, String RecipientMarket, String AgreementTypes, String ContractServices, String TCVMin, String TCVMax, String RiskTypes,
			String Privacy, String Comments, String ActualDate, String RequestedBy, String ChangeRequest, String UploadFiles, String ParentEntity
			) throws InterruptedException, TestLinkAPIException {
		
		count++;
		if (!runmodes[count].equalsIgnoreCase("Y"))
			throw new SkipException("Runmode for Test Data Set to NO -- " + count);
		
		this.testCaseID = testCaseID;

		openBrowser();
	    endUserLogin(CONFIG.getProperty("endUserURL"), CONFIG.getProperty("endUserUsername"), CONFIG.getProperty("endUserPassword"));
	    
	    Logger.debug("Executing Test Case CONTRACT TEMPLATE Audit Log with Name -- " + ParentEntity);
		
	    driver.get(CONFIG.getProperty("endUserURL"));
	    //
		fluentWaitMethod(locateElementBy("cdr_quick_link"));

	    locateElementBy("cdr_quick_link").click();
	    //
	    fluentWaitMethod(driver.findElement(By.className("dropdown-toggle")));

	    new Actions(driver).moveToElement(driver.findElement(By.className("dropdown-toggle"))).clickAndHold().build().perform();
	    
	    driver.findElement(By.linkText("View Contract Templates")).click();
	    //
		fluentWaitMethod(locateElementBy("entity_listing_page_display_dropdown_link"));

		new Select(locateElementBy("entity_listing_page_display_dropdown_link")).selectByVisibleText(CONFIG.getProperty("maxEntityListingDropdown"));
		//
		
		if (!ParentEntity.equalsIgnoreCase("") && ParentEntity.length()<=30)
			driver.findElement(By.xpath(".//*[@id='cr']/tbody/tr/td[2][./text()='"+ParentEntity+"']/preceding-sibling::td[1]/a")).click();
		else if (!ParentEntity.equalsIgnoreCase("") && ParentEntity.length()>30)
			driver.findElement(By.xpath(".//*[@id='cr']/tbody/tr/td[2][div/div[./text()='"+ParentEntity+"']]/preceding-sibling::td[1]/a")).click();
		//
		fluentWaitMethod(driver.findElement(By.linkText("GENERAL")));

		// CONTRACT TEMPLATE - SHOW PAGE - EDIT BUTTON
		driver.findElement(By.linkText("GENERAL")).click();
		//
		
		// CONTRACT TEMPLATE - SHOW PAGE - EDIT BUTTON
	    WebElement elementBeforeEdit = driver.findElement(By.cssSelector("ng-form[template='tab.actions']")).findElement(By.cssSelector("div.submit.freeze"));
	    try {
	    	elementBeforeEdit.findElement(By.xpath(".//button[contains(.,'Edit')]"));
	    	} catch(NoSuchElementException e) {
	    	    Assert.assertNotNull(elementBeforeEdit.findElement(By.xpath(".//button[contains(.,'Inactivate')]")));
	    	    elementBeforeEdit.findElement(By.xpath(".//button[contains(.,'Inactivate')]")).click();
	    	    Thread.sleep(20000);
	    		fluentWaitMethod(driver.findElement(By.linkText("GENERAL")));

	    	    driver.findElement(By.linkText("GENERAL")).click();
	    	    //
	    	    }

		WebElement elementEdit = driver.findElement(By.cssSelector("ng-form[template='tab.actions']")).findElement(By.cssSelector("div.submit.freeze"));		
		Assert.assertNotNull(elementEdit.findElement(By.xpath(".//button[contains(.,'Edit')]")));
	    ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", elementEdit.findElement(By.xpath(".//button[contains(.,'Edit')]")));
	    elementEdit.findElement(By.xpath(".//button[contains(.,'Edit')]")).click();
		//
		fluentWaitMethod(locateElementBy("contract_template_create_page_name_textbox"));

		Map<String, Map<String, List<String>>> entityMapBeforeUpdatePage = TestAuditLogUtil.getEntityMapBeforeUpdate();

	    // CONTRACT TEMPLATE - EDIT PAGE - BASIC INFORMATION
	    addFieldValue("contract_template_create_page_name_textbox", Name.trim());

	    addFieldValue("contract_template_create_page_template_header_textarea", TemplateHeader.trim());

	    addFieldValue("contract_template_create_page_table_of_content_checkbox", TableOfContent.trim());
	    
	    addFieldValue("contract_template_create_page_transaction_type_dropdown", TransactionTypes.trim());

	    addFieldValue("contract_template_create_page_suppliers_multi_dropdown", Suppliers.trim());
	    
	    addFieldValue("contract_template_create_page_category_dropdown", Category.trim());
	    
	    addFieldValue("contract_template_create_page_template_type_dropdown", TemplateType.trim());

	    addFieldValue("contract_template_create_page_contracting_entity_dropdown", ContractingEntity.trim());

	    addFieldValue("contract_template_create_page_term_type_dropdown", TermType.trim());
	    
		if (!UploadTemplate.equalsIgnoreCase("")) {
			locateElementBy("entity_create_page_upload_browse_button").findElement(By.tagName("input")).sendKeys(System.getProperty("user.dir")+"\\file-upload\\Contract Template\\" + UploadTemplate.trim());
			//
			}
	    
	    // CONTRACT TEMPLATE - EDIT PAGE - FUNCTIONS
		addFieldValue("contract_template_create_page_functions_multi_dropdown", Functions.trim());

		addFieldValue("contract_template_create_page_services_multi_dropdown", Services.trim());
		
	    // CONTRACT TEMPLATE - EDIT PAGE - GEOGRAPHY
		addFieldValue("contract_template_create_page_regions_multi_dropdown", Regions.trim());

		addFieldValue("contract_template_create_page_countries_multi_dropdown", Countries.trim());

	    // CONTRACT TEMPLATE - EDIT PAGE - OTHER INFORMATION
	    addFieldValue("contract_template_create_page_industry_types_multi_dropdown", IndustryTypes.trim());

	    addFieldValue("contract_template_create_page_agreement_types_multi_dropdown", AgreementTypes.trim());
	    
	    addFieldValue("contract_template_create_page_recipient_market_multi_dropdown", RecipientMarket.trim());

	    addFieldValue("contract_template_create_page_contract_services_multi_dropdown", ContractServices.trim());
	    
	    addFieldValue("contract_template_create_page_tcv_min_numeric_textbox", TCVMin.trim());
	    
	    addFieldValue("contract_template_create_page_tcv_max_numeric_textbox", TCVMax.trim());
	    
	    // CONTRACT TEMPLATE - EDIT PAGE - RISK
	    addFieldValue("contract_template_create_page_risk_types_multi_dropdown", RiskTypes.trim());

		// CONTRACT TEMPLATE - EDIT PAGE - COMMENTS AND ATTACHMENTS
		addFieldValue("entity_create_page_privacy_checkbox", Privacy.trim());

		addFieldValue("entity_create_page_comments_textarea", Comments.trim());

		addFieldValue("entity_create_page_actual_date", ActualDate.trim());

		addFieldValue("entity_create_page_requested_by_dropdown", RequestedBy.trim());

		addFieldValue("entity_create_page_change_request_dropdown", ChangeRequest.trim());

		if (!UploadFiles.equalsIgnoreCase("")) {
			locateElementBy("entity_create_page_upload_browse_button").sendKeys(System.getProperty("user.dir")+"\\file-upload\\Contract Template\\" + UploadFiles);
			//
			}

		Map<String, Map<String, List<String>>> entityMapAfterUpdatePage = TestAuditLogUtil.getEntityMapAfterUpdate(entityMapBeforeUpdatePage);

		// CONTRACT TEMPLATE - EDIT PAGE - UPDATE BUTTON
	    WebElement elementUpdate = driver.findElement(By.cssSelector("ng-form[template='tab.actions']")).findElement(By.cssSelector("div.submit.freeze"));		
		Assert.assertNotNull(elementUpdate.findElement(By.xpath(".//button[contains(.,'Update')]")));
	    ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", elementUpdate.findElement(By.xpath(".//button[contains(.,'Update')]")));
	    elementUpdate.findElement(By.xpath(".//button[contains(.,'Update')]")).click();
		//

		// CONTRACT TEMPLATE - EDIT PAGE - FIELD VALIDATIONS
		List<WebElement> validationClassElementList = driver.findElements(By.xpath(".//*[@class='errorMessageHolder']/ul/li"));
		if (!validationClassElementList.isEmpty()) {
			for (WebElement validationClassElement : validationClassElementList) {
				String validationMessage = validationClassElement.getText();
				
				WebElement elementParent = validationClassElement.findElement(By.xpath("..")).findElement(By.xpath("..")).findElement(By.xpath("..")).findElement(By.xpath(".."));
				
				if (validationMessage.equalsIgnoreCase(""))
					Logger.debug("For CONTRACT TEMPLATE -- " + elementParent.findElement(By.className("errorClass")).getAttribute("name").toUpperCase() + " -- is Mandatory");
				else
					Logger.debug("For CONTRACT TEMPLATE -- " + elementParent.findElement(By.className("errorClass")).getAttribute("name").toUpperCase() + " -- " + validationMessage);
				}

			driver.get(CONFIG.getProperty("endUserURL"));
			return;
			}

		// CONTRACT TEMPLATE - EDIT PAGE - VALIDATIONS
		if (locateElementBy("contract_template_create_page_name_textbox").findElement(By.tagName("span")).getText().equalsIgnoreCase("Name Must Be Unique")) {
		   	Logger.debug("Contract Template Name -- " + Name + " -- already exists in the system");

		   	driver.get(CONFIG.getProperty("endUserURL"));
	   		return;
	   		}
		 //
		 fluentWaitMethod(driver.findElement(By.linkText("GENERAL")));

		 driver.findElement(By.linkText("GENERAL")).click();
		 //
		 fluentWaitMethod(locateElementBy("contract_template_create_page_name_textbox"));

		 String entityShowPageName = locateElementBy("contract_template_create_page_name_textbox").getText();

		 Assert.assertEquals(entityShowPageName, Name);

		 driver.findElement(By.linkText("AUDIT LOG")).click();
		 //
		 Logger.debug("Test Case CONTRACT TEMPLATE Audit Log with Name -- " + ParentEntity + " --  is STARTED");
			
		 WebElement elementOddRowSelected = driver.findElement(By.id("table_2215")).findElements(By.className("odd")).get(0);
			
		 Assert.assertEquals(elementOddRowSelected.findElement(By.cssSelector("td.col_wrap_action_name")).getText(), "Updated");

		 if (!RequestedBy.equalsIgnoreCase(""))
			 Assert.assertEquals(elementOddRowSelected.findElement(By.cssSelector("td.col_wrap_requested_by")).getText(), RequestedBy);
		 else
			 Assert.assertEquals(elementOddRowSelected.findElement(By.cssSelector("td.col_wrap_requested_by")).getText(), CONFIG.getProperty("endUserFullName"));

		 Assert.assertEquals(elementOddRowSelected.findElement(By.cssSelector("td.col_wrap_completed_by")).getText(), CONFIG.getProperty("endUserFullName"));

		 if (!Comments.equalsIgnoreCase(""))
			 Assert.assertEquals(elementOddRowSelected.findElement(By.cssSelector("td.col_wrap_comment")).getText(), "Yes");
		 else
			 Assert.assertEquals(elementOddRowSelected.findElement(By.cssSelector("td.col_wrap_comment")).getText(), "No");

		 if (!UploadFiles.equalsIgnoreCase(""))
			 Assert.assertEquals(elementOddRowSelected.findElement(By.cssSelector("td.col_wrap_document")).getText(), "Yes");
		 else
			 Assert.assertEquals(elementOddRowSelected.findElement(By.cssSelector("td.col_wrap_document")).getText(), "No");

		 elementOddRowSelected.findElement(By.cssSelector("td.col_wrap_history")).findElement(By.className("serverHistoryView")).click();
		 Thread.sleep(2000);

		 Map<String, Map<String, String>> auditLogDataMap = TestAuditLogUtil.getAuditLogViewHistoryData();

		 TestAuditLogUtil.isAuditLogWorking(entityMapAfterUpdatePage, auditLogDataMap);

		 try {
			 driver.findElement(By.linkText("GENERAL")).click();
			 //

			 WebElement elementPublish = driver.findElement(By.cssSelector("ng-form[template='tab.actions']")).findElement(By.cssSelector("div.submit.freeze"));		
			 Assert.assertNotNull(elementPublish.findElement(By.xpath(".//button[contains(.,'Publish')]")));
			 elementPublish.findElement(By.xpath(".//button[contains(.,'Publish')]")).click();
			 //
			 fluentWaitMethod(driver.findElement(By.linkText("GENERAL")));
		 	} catch(NoSuchElementException e) {
		 		}

		 Logger.debug("Test Case CONTRACT TEMPLATE Audit Log with Name -- " + ParentEntity + " --  is PASSED");
		 driver.get(CONFIG.getProperty("endUserURL"));
		 }

	@AfterMethod
	public void reportDataSetResult(ITestResult testResult) throws IOException {
		takeScreenShotOnFailure(testResult.getStatus(), this.getClass().getSimpleName());

		if(testResult.getStatus()==ITestResult.SKIP)
			TestUtil.reportDataSetResult(contract_template_suite_xls, this.getClass().getSimpleName(), count+2, "SKIP");
		else if(testResult.getStatus()==ITestResult.FAILURE) {
			isTestPass=false;
			TestUtil.reportDataSetResult(contract_template_suite_xls, this.getClass().getSimpleName(), count+2, "FAIL");
			result= "Fail";
			}
		else if (testResult.getStatus()==ITestResult.SUCCESS) {
			TestUtil.reportDataSetResult(contract_template_suite_xls, this.getClass().getSimpleName(), count+2, "PASS");
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
			TestUtil.reportDataSetResult(contract_template_suite_xls, "Test Cases", TestUtil.getRowNum(contract_template_suite_xls, this.getClass().getSimpleName()),"PASS");
		else
			TestUtil.reportDataSetResult(contract_template_suite_xls, "Test Cases", TestUtil.getRowNum(contract_template_suite_xls, this.getClass().getSimpleName()),"FAIL");
		}

	@DataProvider
	public Object[][] getTestData() {
		return TestUtil.getData(contract_template_suite_xls, this.getClass().getSimpleName());
		}
	}
