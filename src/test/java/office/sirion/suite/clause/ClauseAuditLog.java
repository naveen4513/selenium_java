package office.sirion.suite.clause;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import office.sirion.util.DatabaseQueryChange;
import office.sirion.util.TestAuditLogUtil;
import office.sirion.util.TestUtil;
import office.sirion.util.TestlinkIntegration;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
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

public class ClauseAuditLog extends TestSuiteBase {
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
	public void ClauseAuditLogTest(String testCaseID, String Name, String HeaderLabel, String SubHeader, String ClauseType, String Suppliers, String ContractingEntity,
			String TermType, String Type, String Category, String Functions, String Services, String ClauseContent, String RiskType, String IndustryTypes,
			String CompanyPosition, String AgreementTypes, String TransactionTypes, String ContractServices, String TCVMin, String TCVMax, String RecipientMarket,
			String Regions, String Countries, String Privacy, String Comments, String ActualDate, String RequestedBy, String ChangeRequest, String UploadFiles,
			String ParentEntity
			) throws InterruptedException, TestLinkAPIException, ClassNotFoundException, SQLException {

		count++;
		if (!runmodes[count].equalsIgnoreCase("Y"))
			throw new SkipException("Runmode for Test Data Set to NO -- " + count);

		this.testCaseID = testCaseID;

		openBrowser();
	    endUserLogin(CONFIG.getProperty("endUserURL"), CONFIG.getProperty("endUserUsername"), CONFIG.getProperty("endUserPassword"));

	    Logger.debug("Executing Test Case Clause Audit Log with Name -- " + ParentEntity);

	    driver.get(CONFIG.getProperty("endUserURL"));
	    //
		fluentWaitMethod(driver.findElement(By.name("searchtext")));

		String clauseID = DatabaseQueryChange.getClauseDatabaseID("select client_entity_seq_id from clause where name ='" +ParentEntity.trim()+ "' and deleted = false and client_id in (select id from client  where alias ilike'"+CONFIG.getProperty("clientSupplierAlias").trim()+"')" );

		if (clauseID==null)
	    	Assert.fail("There is no Such Clause Name as " + ParentEntity);

		driver.findElement(By.name("searchtext")).sendKeys(clauseID);
	    driver.findElement(By.name("searchtext")).sendKeys(Keys.ENTER);
	    //
	    if (driver.findElements(By.className("alertdialog-icon")).size()!=0) {
	    	if (driver.findElement(By.className("alertdialog-icon")).getText().equalsIgnoreCase("Either you do not have the required permissions or this Entity has been deleted or doesn't exist anymore.")) {
	    		Logger.debug("Either you do not have the required permissions or this Entity has been deleted or doesn't exist anymore. for -- " + ParentEntity);

	    		driver.findElement(By.cssSelector("button.ui-button.ui-widget.ui-state-default.ui-corner-all.ui-button-text-only.ui-state-hover")).click();

	    		driver.get(CONFIG.getProperty("endUserURL"));
	    	    return;
	    		}
	    	}
		fluentWaitMethod(driver.findElement(By.linkText("GENERAL")));

		driver.findElement(By.linkText("GENERAL")).click();
	    //
		fluentWaitMethod(driver.findElement(By.cssSelector("ng-form[template='tab.actions']")).findElement(By.cssSelector("div.submit.freeze")));

		// CLAUSE - SHOW PAGE - EDIT BUTTON
	    WebElement elementBeforeEdit = driver.findElement(By.cssSelector("ng-form[template='tab.actions']")).findElement(By.cssSelector("div.submit.freeze"));

	    try {
	    	elementBeforeEdit.findElement(By.xpath(".//button[contains(.,'Edit')]"));
	    	} catch(NoSuchElementException e) {
	    	    Assert.assertNotNull(elementBeforeEdit.findElement(By.xpath(".//button[contains(.,'Inactivate')]")));
	    	    elementBeforeEdit.findElement(By.xpath(".//button[contains(.,'Inactivate')]")).click();
	    	    //
	    		fluentWaitMethod(driver.findElement(By.linkText("GENERAL")));

	    	    driver.findElement(By.linkText("GENERAL")).click();
	    	    //
	    	    }

	    WebElement elementAfterEdit = driver.findElement(By.cssSelector("ng-form[template='tab.actions']")).findElement(By.cssSelector("div.submit.freeze"));
	    Assert.assertNotNull(elementAfterEdit.findElement(By.xpath(".//button[contains(.,'Edit')]")));
	    ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", elementAfterEdit.findElement(By.xpath(".//button[contains(.,'Edit')]")));
	    elementAfterEdit.findElement(By.xpath(".//button[contains(.,'Edit')]")).click();
	    //
		fluentWaitMethod(locateElementBy("clause_create_page_name_textbox"));

		Map<String, Map<String, List<String>>> entityMapBeforeUpdatePage = TestAuditLogUtil.getEntityMapBeforeUpdate();

		// CLAUSE LIBRARY - EDIT PAGE - BASIC INFORMATION
		addFieldValue("clause_create_page_name_textbox", Name.trim());

	    addFieldValue("clause_create_page_header_label_textbox", HeaderLabel.trim());

	    addFieldValue("clause_create_page_sub_header_textbox", SubHeader.trim());

	    addFieldValue("clause_create_page_clause_clause_type_dropdown", ClauseType.trim());

	    addFieldValue("clause_create_page_suppliers_multi_dropdown", Suppliers.trim());

	    addFieldValue("clause_create_page_contracting_entity_dropdown", ContractingEntity.trim());
	    
	    addFieldValue("clause_create_page_term_type_dropdown", TermType.trim());

	    String selectedClauseType = new Select(locateElementBy("clause_create_page_clause_type_dropdown").findElement(By.tagName("select"))).getFirstSelectedOption().getText();
	    if (selectedClauseType.equalsIgnoreCase("-Select-") || selectedClauseType.equalsIgnoreCase(Type.trim()))
		    addFieldValue("clause_create_page_clause_type_dropdown", Type.trim());
	    else {
			Logger.debug("You Cannot Change the Clause Type After Creation for -- " + Name);

			driver.get(CONFIG.getProperty("endUserURL"));
    	    return;
    	    }

	    if (Type.trim().equalsIgnoreCase("Clause"))
		    addFieldValue("clause_create_page_clause_category_dropdown", Category.trim());
	    else if (Type.trim().equalsIgnoreCase("Definition"))
		    addFieldValue("clause_create_page_defined_terms_dropdown", Category.trim());

	    // CLAUSE LIBRARY - EDIT PAGE - FUNCTIONS
		addFieldValue("clause_create_page_functions_multi_dropdown", Functions.trim());

		addFieldValue("clause_create_page_services_multi_dropdown", Services.trim());

		// CLAUSE LIBRARY - EDIT PAGE - CLAUSE CONTENT
	    if (!ClauseContent.trim().equalsIgnoreCase("")) {
	    	driver.findElement(By.name("editTextClause")).click();
	    	//
	    	
	    	WebElement clauseContentFrameParent = driver.findElement(By.cssSelector("fieldset.fieldset_2206.popOutside.pop")).findElement(By.id("elem_7386"));
			driver.switchTo().frame(clauseContentFrameParent.findElement(By.tagName("iframe")));

			driver.findElement(By.cssSelector("div.note-editable.panel-body")).clear();
			driver.findElement(By.cssSelector("div.note-editable.panel-body")).sendKeys(ClauseContent.trim());
			
			driver.switchTo().defaultContent();
			new Actions(driver).moveToElement(driver.findElement(By.className("saveClause"))).build().perform();
			driver.findElement(By.className("saveClause")).click();
	    	}

	    // CLAUSE LIBRARY - EDIT PAGE - RISKS
	    addFieldValue("clause_create_page_risk_types_multi_dropdown", RiskType.trim());
		
	    // CLAUSE LIBRARY - EDIT PAGE - OTHER INFORMATION
	    addFieldValue("clause_create_page_industry_types_multi_dropdown", IndustryTypes.trim());

	    addFieldValue("clause_create_page_company_position_dropdown", CompanyPosition.trim());
	    
	    addFieldValue("clause_create_page_agreement_types_multi_dropdown", AgreementTypes.trim());
	    
	    addFieldValue("clause_create_page_clause_transaction_type_dropdown", TransactionTypes.trim());
	    
	    addFieldValue("clause_create_page_contract_services_multi_dropdown", ContractServices.trim());

	    addFieldValue("clause_create_page_tcv_min_numeric_textbox", TCVMin.trim());

	    addFieldValue("clause_create_page_tcv_max_numeric_textbox", TCVMax.trim());
	    
	    addFieldValue("clause_create_page_recipient_market_multi_dropdown", RecipientMarket.trim());

	    // CLAUSE LIBRARY - EDIT PAGE - GEOGRAPHY
		addFieldValue("clause_create_page_regions_multi_dropdown", Regions.trim());

		addFieldValue("clause_create_page_countries_multi_dropdown", Countries.trim());

		// CLAUSE LIBRARY - EDIT PAGE - COMMENTS AND ATTACHMENTS
		addFieldValue("entity_create_page_privacy_checkbox", Privacy.trim());

		addFieldValue("entity_create_page_comments_textarea", Comments.trim());

		addFieldValue("entity_create_page_actual_date", ActualDate.trim());

		addFieldValue("entity_create_page_requested_by_dropdown", RequestedBy.trim());

		addFieldValue("entity_create_page_change_request_dropdown", ChangeRequest.trim());
		
		if (!UploadFiles.equalsIgnoreCase("")) {
			locateElementBy("entity_create_page_upload_browse_button").sendKeys(System.getProperty("user.dir")+"\\file-upload\\Clause\\" + UploadFiles.trim());
			//
			}

		Map<String, Map<String, List<String>>> entityMapAfterUpdatePage = TestAuditLogUtil.getEntityMapAfterUpdate(entityMapBeforeUpdatePage);

		// CLAUSE - EDIT PAGE - UPDATE BUTTON
	    WebElement elementUpdate = driver.findElement(By.cssSelector("ng-form[template='tab.actions']")).findElement(By.cssSelector("div.submit.freeze"));		
		Assert.assertNotNull(elementUpdate.findElement(By.xpath(".//button[contains(.,'Update')]")));
	    ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", elementUpdate.findElement(By.xpath(".//button[contains(.,'Update')]")));
	    elementUpdate.findElement(By.xpath(".//button[contains(.,'Update')]")).click();
		//

		// CLAUSE - EDIT PAGE - FIELD VALIDATIONS
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

		// CLAUSE - EDIT PAGE - VALIDATIONS
		if (locateElementBy("clause_create_page_name_textbox").findElement(By.tagName("span")).getText().equalsIgnoreCase("Name Must Be Unique")) {
		   	Logger.debug("Clause Name -- " + Name + " -- already exists in the system");

		   	driver.get(CONFIG.getProperty("endUserURL"));
	   		return;
	   		}
		 fluentWaitMethod(driver.findElement(By.linkText("GENERAL")));

		 driver.findElement(By.linkText("GENERAL")).click();
		 //
		 fluentWaitMethod(locateElementBy("clause_create_page_name_textbox"));

		 String entityNameOnShowPage = locateElementBy("clause_create_page_name_textbox").getText();

		 Assert.assertEquals(entityNameOnShowPage, Name);

		 driver.findElement(By.linkText("AUDIT LOG")).click();
		 //
		 Logger.debug("Test Case CLAUSE Audit Log with Name -- " + ParentEntity + " --  is STARTED");
			
		 WebElement elementOddRowSelected = driver.findElement(By.id("table_2272")).findElements(By.className("odd")).get(0);
			
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

		 Logger.debug("Test Case CLAUSE Audit Log with Name -- " + ParentEntity + " -- is PASSED");
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
