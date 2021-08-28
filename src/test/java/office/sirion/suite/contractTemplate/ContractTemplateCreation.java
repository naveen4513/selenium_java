package office.sirion.suite.contractTemplate;

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

import testlink.api.java.client.TestLinkAPIException;
import testlink.api.java.client.TestLinkAPIResults;

public class ContractTemplateCreation extends TestSuiteBase {
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
	public void ContractTemplateCreationTest (String testCaseID, String Name, String TemplateHeader, String TableOfContent, String TransactionTypes, String Suppliers,
			String Category, String TemplateType, String ContractingEntity, String TermType, String UploadTemplate, String Functions, String Services, String Regions,
			String Countries, String IndustryTypes, String RecipientMarket, String AgreementTypes, String ContractServices, String TCVMin, String TCVMax, String RiskTypes,
			String Privacy, String Comments, String ActualDate, String RequestedBy, String ChangeRequest, String UploadFiles
			) throws InterruptedException, TestLinkAPIException {

		count++;
		if (!runmodes[count].equalsIgnoreCase("Y"))
			throw new SkipException("Runmode for Test Data Set to NO -- " + count);

		this.testCaseID = testCaseID;

		openBrowser();
	    endUserLogin(CONFIG.getProperty("endUserURL"), CONFIG.getProperty("endUserUsername"), CONFIG.getProperty("endUserPassword"));

	    Logger.debug("Executing Test Case Contract Template Creation with Name -- " + Name);

	    driver.get(CONFIG.getProperty("endUserURL"));
	    //
		fluentWaitMethod(locateElementBy("cdr_quick_link"));

		locateElementBy("cdr_quick_link").click();
	    //
	    fluentWaitMethod(driver.findElement(By.className("dropdown-toggle")));

	    new Actions(driver).moveToElement(driver.findElement(By.className("dropdown-toggle"))).clickAndHold().build().perform();

	    driver.findElement(By.linkText("View Contract Templates")).click();
	    //
		fluentWaitMethod(locateElementBy("entity_listing_page_plus_button"));

		new Actions(driver).moveToElement(locateElementBy("entity_listing_page_plus_button")).click().build().perform();

		new Actions(driver).moveToElement(driver.findElement(By.xpath(".//*[@id='drop']/ul/li/label"))).clickAndHold().build().perform();
		Thread.sleep(2000);

		driver.findElement(By.className("childlist")).findElement(By.linkText("Single")).click();
	    //
	    fluentWaitMethod(locateElementBy("contract_template_create_page_name_textbox"));

	    // CONTRACT TEMPLATE - CREATE PAGE - BASIC INFORMATION
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
			locateElementBy("entity_create_page_upload_browse_button").sendKeys(System.getProperty("user.dir")+"\\file-upload\\Contract Template\\" + UploadTemplate.trim());
			//
			}
	    
	    // CONTRACT TEMPLATE - CREATE PAGE - FUNCTIONS
		addFieldValue("contract_template_create_page_functions_multi_dropdown", Functions.trim());

		addFieldValue("contract_template_create_page_services_multi_dropdown", Services.trim());
		
	    // CONTRACT TEMPLATE - CREATE PAGE - GEOGRAPHY
		addFieldValue("contract_template_create_page_regions_multi_dropdown", Regions.trim());

		addFieldValue("contract_template_create_page_countries_multi_dropdown", Countries.trim());

	    // CONTRACT TEMPLATE - CREATE PAGE - OTHER INFORMATION
	    addFieldValue("contract_template_create_page_industry_types_multi_dropdown", IndustryTypes.trim());

	    addFieldValue("contract_template_create_page_agreement_types_multi_dropdown", AgreementTypes.trim());
	    
	    addFieldValue("contract_template_create_page_recipient_market_multi_dropdown", RecipientMarket.trim());

	    addFieldValue("contract_template_create_page_contract_services_multi_dropdown", ContractServices.trim());
	    
	    addFieldValue("contract_template_create_page_tcv_min_numeric_textbox", TCVMin.trim());
	    
	    addFieldValue("contract_template_create_page_tcv_max_numeric_textbox", TCVMax.trim());
	    
	    // CONTRACT TEMPLATE - CREATE PAGE - RISK
	    addFieldValue("contract_template_create_page_risk_types_multi_dropdown", RiskTypes.trim());

		// CONTRACT TEMPLATE - CREATE PAGE - COMMENTS AND ATTACHMENTS
		addFieldValue("entity_create_page_privacy_checkbox", Privacy.trim());

		addFieldValue("entity_create_page_comments_textarea", Comments.trim());

		addFieldValue("entity_create_page_actual_date", ActualDate.trim());

		addFieldValue("entity_create_page_requested_by_dropdown", RequestedBy.trim());

		addFieldValue("entity_create_page_change_request_dropdown", ChangeRequest.trim());
		
		if (!UploadFiles.equalsIgnoreCase("")) {
			locateElementBy("entity_create_page_upload_browse_button").sendKeys(System.getProperty("user.dir")+"\\file-upload\\Contract Template\\" + UploadFiles);
			//
			}

		Assert.assertNotNull(driver.findElement(By.xpath("//button[contains(.,'Save Template')]")));
		((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", driver.findElement(By.xpath("//button[contains(.,'Save Template')]")));
		driver.findElement(By.xpath("//button[contains(.,'Save Template')]")).click();
		//

		// CONTRACT TEMPLATE - CREATE PAGE - FIELD VALIDATIONS
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

		// CONTRACT TEMPLATE - CREATE PAGE - VALIDATIONS
		if (locateElementBy("contract_template_create_page_name_textbox").findElement(By.tagName("span")).getText().equalsIgnoreCase("Name Must Be Unique")) {
		   	Logger.debug("Contract Template Name -- "+Name+" -- already exists in the system");

		   	driver.get(CONFIG.getProperty("endUserURL"));
	   		return;
	   		}
		
		if (driver.findElements(By.className("success-icon")).size()!=0) {
			 String entityID = driver.findElement(By.className("success-icon")).findElement(By.id("hrefElemId")).getText();
				 
			 Logger.debug("CONTRACT TEMPLATE created successfully with PO ID " + entityID);
			 
			 driver.findElement(By.className("success-icon")).findElement(By.id("hrefElemId")).click();
			 //
			 fluentWaitMethod(driver.findElement(By.linkText("GENERAL")));

				driver.findElement(By.linkText("GENERAL")).click();
				//
				fluentWaitMethod(locateElementBy("contract_template_show_page_id"));

				String entityShowPageID = locateElementBy("contract_template_show_page_id").getText();

				Assert.assertEquals(entityShowPageID, entityID);

			 }	 

		Logger.debug("Test Case Contract Template Creation with Name -- " + Name + " -- is PASSED");
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
