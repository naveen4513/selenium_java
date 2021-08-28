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

public class CDRCommunication extends TestSuiteBase {
	String result = null;
	String runmodes[] = null;
	static int count = -1;
	static boolean isTestPass = true;
	String testCaseID;
	
	@BeforeTest
	public void checkTestSkip() {
		if (!TestUtil.isTestCaseRunnable(cdr_suite_xls, this.getClass().getSimpleName())) {
			Logger.debug("Skipping Test Case " + this.getClass().getSimpleName() + " as runmode set to NO");
			throw new SkipException("Skipping Test Case " + this.getClass().getSimpleName() + " as runmode set to NO");
			}
		runmodes = TestUtil.getDataSetRunmodes(cdr_suite_xls, this.getClass().getSimpleName());
		}
	
	@Test(dataProvider = "getTestData")
	public void CDRCommunicationTest (String testCaseID, String cdrComments, String cdrActualDate, String cdrRequestedBy, String cdrChangeRequest,
			String cdrUploadFile, String cdrParent
			) throws InterruptedException {
		
		count++;
		if (!runmodes[count].equalsIgnoreCase("Y"))
			throw new SkipException("Runmode for Test Data Set to NO -- " + count);
		
		this.testCaseID = testCaseID;

		openBrowser();
	    endUserLogin(CONFIG.getProperty("endUserURL"), CONFIG.getProperty("endUserUsername"), CONFIG.getProperty("endUserPassword"));
	    
	    Logger.debug("Executing Test Case CDR Communication Tab with Title -- " + cdrParent);

	    driver.get(CONFIG.getProperty("endUserURL"));

		fluentWaitMethod(locateElementBy("cdr_quick_link"));

		locateElementBy("cdr_quick_link").click();

		fluentWaitMethod(locateElementBy("entity_listing_page_display_dropdown_link"));

		new Select(locateElementBy("entity_listing_page_display_dropdown_link")).selectByVisibleText(CONFIG.getProperty("maxEntityListingDropdown"));


		try {
			if (!cdrParent.equalsIgnoreCase("") && cdrParent.length()<=30)
				driver.findElement(By.xpath(".//*[@id='cr']/tbody/tr/td[2][./text()='"+cdrParent+"']/preceding-sibling::td[1]/a")).click();
			else if (!cdrParent.equalsIgnoreCase("") && cdrParent.length()>30)
				driver.findElement(By.xpath(".//*[@id='cr']/tbody/tr/td[2][div/div[./text()='"+cdrParent+"']]/preceding-sibling::td[1]/a")).click();
			else
				locateElementBy("entity_listing_page_first_entry_link").click();
			} catch (Exception e) {
				locateElementBy("entity_listing_page_first_entry_link").click();				
				}

	    fluentWaitMethod(locateElementBy("entity_create_page_comments_textarea"));
		
		// CDR - CREATE PAGE - COMMENTS AND ATTACHMENTS
		addFieldValue("entity_create_page_comments_textarea", cdrComments);
		    
		addFieldValue("entity_create_page_requested_by_dropdown", cdrRequestedBy);
		    
//		addFieldValue("entity_create_page_actual_date", cdrActualDate);
		    
		addFieldValue("entity_create_page_change_request_dropdown", cdrChangeRequest);
		
		if (!cdrUploadFile.equalsIgnoreCase("")) {
			locateElementBy("entity_create_page_upload_browse_button").sendKeys(System.getProperty("user.dir")+"\\file-upload\\CDR\\" + cdrUploadFile);

			}
		
		// CDR - SHOW PAGE - Save Comment/Attachment BUTTON
	    WebElement elementSaveComment = driver.findElement(By.cssSelector("ng-form[template='tab.actions']")).findElement(By.cssSelector("div.submit.freeze"));		
		Assert.assertNotNull(elementSaveComment.findElement(By.xpath(".//button[contains(.,'Save Comment/Attachment')]")));
	    ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", elementSaveComment.findElement(By.xpath(".//button[contains(.,'Save Comment/Attachment')]")));
	    elementSaveComment.findElement(By.xpath(".//button[contains(.,'Save Comment/Attachment')]")).click();

	    fluentWaitMethod(locateElementBy("cdr_create_page_title_textbox"));

	    // CDR - SHOW PAGE - VALIDATIONS
	    if (locateElementBy("entity_create_page_error_notifications")!=null) {
	    	List<WebElement> elementErrors = driver.findElements(By.xpath(".//*[@class='genericErrors']/list/li"));
			for (WebElement element : elementErrors) {
				String entityErrors = element.getText();
				 
				if (entityErrors.contains("Please add a comment or upload files")) {
					Logger.debug("For CDR -- " +cdrParent+ " -- Please add a comment or upload files");
			        }
				}

		    driver.get(CONFIG.getProperty("endUserURL"));
		    return;
	    	}
	    fluentWaitMethod(driver.findElement(By.linkText("COMMUNICATION")));

	    driver.findElement(By.linkText("COMMUNICATION")).click();
		//
	    Logger.debug("Test Case CDR Communication Tab with Title -- " + cdrParent + " -- is STARTED");

	    verifyCommunicationTab(cdrComments, cdrUploadFile);

	    Logger.debug("Test Case CDR Communication Tab with Title -- " + cdrParent + " -- is PASSED");
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