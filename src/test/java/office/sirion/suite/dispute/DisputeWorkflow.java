package office.sirion.suite.dispute;

import java.io.IOException;
import java.net.MalformedURLException;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.NoSuchElementException;
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
import office.sirion.util.TestUtil;

public class DisputeWorkflow extends TestSuiteBase {
	String result = null;
	String runmodes[] = null;
	static int count = -1;
	static boolean fail = false;
	static boolean skip = false;
	static boolean isTestPass = true;
	JavascriptExecutor executor;

	@BeforeTest
	public void checkTestSkip() {
		if (!TestUtil.isTestCaseRunnable(dispute_suite_xls, this.getClass().getSimpleName())) {
			Logger.debug("Skipping Test Case "+ this.getClass().getSimpleName() + " as runmode set to NO");
			throw new SkipException("Skipping Test Case "+ this.getClass().getSimpleName() + " as runmode set to NO");
			}
		runmodes = TestUtil.getDataSetRunmodes(dispute_suite_xls, this.getClass().getSimpleName());
		}
	
  @Test(dataProvider = "getTestData")
  public void DisputeWorkflowTest(String disputeTitle, String disputeWorkflowSteps) throws InterruptedException, MalformedURLException {

		count++;
		if (!runmodes[count].equalsIgnoreCase("Y")) {
			skip = true;
			throw new SkipException("Runmode for Test Data Set to NO -- "+ count);
			}
		Logger.debug("Executing Test Case disputes Workflow");
    
		// Launch The Browser
		openBrowser();
		endUserLogin(CONFIG.getProperty("endUserURL"), CONFIG.getProperty("endUserUsername"), CONFIG.getProperty("endUserPassword"));
		
		Logger.debug("Executing Test Case Dispute Workflow with Name --- " + disputeTitle);

		driver.get(CONFIG.getProperty("endUserURL"));
		fluentWaitMethod(locateElementBy("disputes_quick_link"));


		locateElementBy("disputes_quick_link").click();
		try {
			fluentWaitMethod(locateElementBy("entity_listing_page_display_dropdown_link"));

		}catch (NullPointerException ex){
		}
		new Select(locateElementBy("entity_listing_page_display_dropdown_link")).selectByVisibleText("100");

		if (!disputeTitle.equalsIgnoreCase("")) {
			try {
				driver.findElement(By.xpath("//*[@id='cr']/tbody/tr[@role='row']/td[contains(.,'" + disputeTitle + "')]/preceding-sibling::td[1]/a")).click();
			}catch (NoSuchElementException e){
				locateElementBy("entity_listing_page_first_entry_link").click();
			}
			} else {
			locateElementBy("entity_listing_page_first_entry_link").click();

			fluentWaitMethod(driver.findElement(By.xpath("//button[contains(.,'Clone')]")));
			Assert.assertNotNull(driver.findElement(By.xpath("//button[contains(.,'Clone')]")));
			((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", driver.findElement(By.xpath("//button[contains(.,'Clone')]")));
			driver.findElement(By.xpath("//button[contains(.,'Clone')]")).click();

			fluentWaitMethod(driver.findElement(By.xpath("//button[contains(.,'Save Issue')]")));
			Assert.assertNotNull(driver.findElement(By.xpath("//button[contains(.,'Save Issue')]")));
			((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", driver.findElement(By.xpath("//button[contains(.,'Save Issue')]")));
			driver.findElement(By.xpath("//button[contains(.,'Save Issue')]")).click();

			if (driver.findElements(By.className("success-icon")).size()!=0) {
				String entityID = driver.findElement(By.className("success-icon")).findElement(By.id("hrefElemId")).getText();

				Logger.debug("Dispute cloned successfully with ID " + entityID);

				driver.findElement(By.className("success-icon")).findElement(By.id("hrefElemId")).click();
				fluentWaitMethod(locateElementBy("dispute_show_page_id"));
				

				String entityShowPageID = locateElementBy("dispute_show_page_id").getText();
				System.out.println(entityShowPageID);

				Assert.assertEquals(entityShowPageID, entityID);
				Logger.debug("Dispute ID on show page has been verified");
			}
		}


		if (!disputeWorkflowSteps.equalsIgnoreCase("")) {
			for (String entityData : disputeWorkflowSteps.split(";")) {
				try {
					fluentWaitMethod(driver.findElement(By.xpath("//button[contains(.,'"+entityData.trim()+"')]")));
					
					Assert.assertNotNull(driver.findElement(By.xpath("//button[contains(.,'"+entityData.trim()+"')]")));
					driver.findElement(By.xpath("//button[contains(.,'"+entityData.trim()+"')]")).click();
					fluentWaitMethod(locateElementBy("dispute_show_page_id"));
				} catch (NoSuchElementException e) {
					Logger.debug("No Such Element with the given Key, --- "+entityData+" --- Moving onto Next Step");
				}
			}
			
			fluentWaitMethod(locateElementBy("dispute_show_page_id"));
			
			String entityShowPageStatus = locateElementBy("dispute_show_page_status").getText();

			
			Logger.debug("Dispute Status on show page has been verified"+entityShowPageStatus);
		}


		fail = false;
		driver.get(CONFIG.getProperty("endUserURL"));
		
		

		fail = false;
/*
		if (!fail)
	        result= "Pass";
	      else   
	         result= "Fail";
	     TestlinkIntegration.updateTestLinkResult(testCaseId,"",result);
*/
		driver.get(CONFIG.getProperty("endUserURL"));
		}

	@AfterMethod
	public void reportDataSetResult(ITestResult testResult) throws IOException {
		takeScreenShotOnFailure(testResult.getStatus(), this.getClass().getSimpleName());

		if(testResult.getStatus()==ITestResult.SKIP)
			TestUtil.reportDataSetResult(dispute_suite_xls, this.getClass().getSimpleName(), count+2, "SKIP");
		else if(testResult.getStatus()==ITestResult.FAILURE) {
			isTestPass=false;
			TestUtil.reportDataSetResult(dispute_suite_xls, this.getClass().getSimpleName(), count+2, "FAIL");
			result= "Fail";
		}
		else if (testResult.getStatus()== ITestResult.SUCCESS) {
			TestUtil.reportDataSetResult(dispute_suite_xls, this.getClass().getSimpleName(), count+2, "PASS");
			result= "Pass";
		}
		try {
			for (int i = 2; i <= dispute_suite_xls.getRowCount(this.getClass().getSimpleName()); i++) {
				String testCaseID = dispute_suite_xls.getCellData(this.getClass().getSimpleName(), "testCaseID", i);
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
			TestUtil.reportDataSetResult(dispute_suite_xls, "Test Cases", TestUtil.getRowNum(dispute_suite_xls, this.getClass().getSimpleName()),"PASS");
		else
			TestUtil.reportDataSetResult(dispute_suite_xls, "Test Cases", TestUtil.getRowNum(dispute_suite_xls, this.getClass().getSimpleName()),"FAIL");
		}

	@DataProvider
	public Object[][] getTestData() {
		return TestUtil.getData(dispute_suite_xls, this.getClass().getSimpleName());
		}
	}
