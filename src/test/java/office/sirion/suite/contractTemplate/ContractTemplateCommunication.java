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

public class ContractTemplateCommunication extends TestSuiteBase {
	String result = null;
	String runmodes[] = null;
	static int count = -1;
	static boolean isTestPass = true;
	String testCaseID;

	@BeforeTest
	public void checkTestSkip() {
		if (!TestUtil.isTestCaseRunnable(contract_template_suite_xls, this.getClass().getSimpleName())) {
			Logger.debug("Skipping Test Case " + this.getClass().getSimpleName() + " as runmode set to NO");
			throw new SkipException("Skipping Test Case " + this.getClass().getSimpleName() + " as runmode set to NO");
			}
		runmodes = TestUtil.getDataSetRunmodes(contract_template_suite_xls, this.getClass().getSimpleName());
		}

	@Test(dataProvider = "getTestData")
	public void ContractTemplateCommunicationTest (String testCaseID, String Privacy, String Comments, String ActualDate, String RequestedBy, String ChangeRequest,
			String UploadFiles, String ParentEntity
			) throws InterruptedException, TestLinkAPIException {

		count++;
		if (!runmodes[count].equalsIgnoreCase("Y"))
			throw new SkipException("Runmode for Test Data Set to NO -- " + count);

		this.testCaseID = testCaseID;

		openBrowser();
	    endUserLogin(CONFIG.getProperty("endUserURL"), CONFIG.getProperty("endUserUsername"), CONFIG.getProperty("endUserPassword"));

	    Logger.debug("Executing Test Case CONTRACT TEMPLATE Communication Tab with Title ---- " + ParentEntity);

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

		driver.findElement(By.linkText("GENERAL")).click();
		//

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

		// CONTRACT TEMPLATE - SHOW PAGE - Save Comment/Attachment BUTTON
	    WebElement elementSaveComment = driver.findElement(By.cssSelector("ng-form[template='tab.actions']")).findElement(By.cssSelector("div.submit.freeze"));		
		Assert.assertNotNull(elementSaveComment.findElement(By.xpath(".//button[contains(.,'Save Comment/Attachment')]")));
	    ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", elementSaveComment.findElement(By.xpath(".//button[contains(.,'Save Comment/Attachment')]")));
	    elementSaveComment.findElement(By.xpath(".//button[contains(.,'Save Comment/Attachment')]")).click();
		//

		// CONTRACT TEMPLATE - SHOW PAGE - VALIDATIONS
	    if (locateElementBy("entity_create_page_error_notifications")!=null) {
	    	List<WebElement> elementErrors = driver.findElements(By.xpath(".//*[@class='genericErrors']/list/li"));
			for (WebElement element : elementErrors) {
				String entityErrors = element.getText();
				 
				if (entityErrors.contains("Please add a comment or upload files"))
					Logger.debug("For CONTRACT TEMPLATE -- " +ParentEntity+ " -- Please add a comment or upload files");
				}

			driver.get(CONFIG.getProperty("endUserURL"));
		    return;
	    	}
	    //
	    fluentWaitMethod(driver.findElement(By.linkText("COMMUNICATION")));

	    driver.findElement(By.linkText("COMMUNICATION")).click();
		//
	    Logger.debug("Test Case CONTRACT TEMPLATE Communication Tab with Title -- " + ParentEntity + " -- is STARTED");

	    verifyCommunicationTab(Comments, UploadFiles);

	    Logger.debug("Test Case CONTRACT TEMPLATE Communication Tab with Title -- " + ParentEntity + " -- is PASSED");
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
