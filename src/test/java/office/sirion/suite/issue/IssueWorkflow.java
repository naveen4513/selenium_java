package office.sirion.suite.issue;

import office.sirion.util.TestUtil;
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
import java.io.IOException;

public class IssueWorkflow extends TestSuiteBase {
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
	public void IssueWorkflowTest (String issueTitle, String issueWorkflowSteps, String issueResolutionRemarks, String issueProcessAreaImpacted,
			String issueactionTaken
			) throws InterruptedException {

		count++;
		if (!runmodes[count].equalsIgnoreCase("Y")) {
			skip = true;
			throw new SkipException("Runmode for test set data set to no "
					+ count);
		}
		
		Logger.debug("Executing Test Case issue Workflow");

		openBrowser();
		endUserLogin(CONFIG.getProperty("endUserURL"), CONFIG.getProperty("endUserUsername"), CONFIG.getProperty("endUserPassword"));

		Logger.debug("Executing Test Case issue Workflow with Name --- " + issueTitle);

		driver.get(CONFIG.getProperty("endUserURL"));
		fluentWaitMethod(locateElementBy("issues_quick_link"));


		locateElementBy("issues_quick_link").click();

		fluentWaitMethod(locateElementBy("entity_listing_page_display_dropdown_link"));

		new Select(locateElementBy("entity_listing_page_display_dropdown_link")).selectByVisibleText("100");


		if (!issueTitle.equalsIgnoreCase("")) {
			driver.findElement(By.xpath("//*[@id='cr']/tbody/tr[@role='row']/td[contains(.,'" + issueTitle + "')]/preceding-sibling::td[1]/a")).click();
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

				Logger.debug("issue cloned successfully with issue ID " + entityID);

				driver.findElement(By.className("success-icon")).findElement(By.id("hrefElemId")).click();
				fluentWaitMethod(locateElementBy("issue_show_page_id"));
				

				String entityShowPageID = locateElementBy("issue_show_page_id").getText();

				Assert.assertEquals(entityShowPageID, entityID);
				Logger.debug("issue ID on show page has been verified");
			}
		}


				
		if (!issueWorkflowSteps.equalsIgnoreCase("")) {
			for (String entityData : issueWorkflowSteps.split(";")) {
				try {
					fluentWaitMethod(driver.findElement(By.xpath("//button[contains(.,'"+entityData.trim()+"')]")));
					Assert.assertNotNull(driver.findElement(By.xpath("//button[contains(.,'"+entityData.trim()+"')]")));
					driver.findElement(By.xpath("//button[contains(.,'"+entityData.trim()+"')]")).click();
					
					if(entityData.trim().equalsIgnoreCase("Submit")) {
						addFieldValue("issue_wf_mandatory_field_resolution_remarks", issueResolutionRemarks);
						locateElementBy("issue_wf_blank_area").click();
						fluentWaitMethod(driver.findElement(By.xpath("//button[contains(.,'"+entityData.trim()+"')]")));
						Assert.assertNotNull(driver.findElement(By.xpath("//button[contains(.,'"+entityData.trim()+"')]")));
						driver.findElement(By.xpath("//button[contains(.,'"+entityData.trim()+"')]")).click();
						
					} 
					if (entityData.trim().equalsIgnoreCase("Close")) {
						addFieldValue("issue_wf_mandatory_field_process_area_impacted", issueProcessAreaImpacted);
						locateElementBy("issue_wf_blank_area").click();
						addFieldValue("issue_wf_mandatory_field_action_taken", issueactionTaken);
						locateElementBy("issue_wf_blank_area").click();
						fluentWaitMethod(driver.findElement(By.xpath("//button[contains(.,'"+entityData.trim()+"')]")));
						Assert.assertNotNull(driver.findElement(By.xpath("//button[contains(.,'"+entityData.trim()+"')]")));
						driver.findElement(By.xpath("//button[contains(.,'"+entityData.trim()+"')]")).click();
					}

				} catch (NoSuchElementException e) {
					Logger.debug("No Such Element with the given Key, --- "+entityData+" --- Moving onto Next Step");
				}
			}

			String entityShowPageStatus = locateElementBy("issues_show_page_status").getText();

			Assertion(entityShowPageStatus, "Active");
			Logger.debug("issue Status on show page has been verified");
		}


		fail = false;

       driver.get(CONFIG.getProperty("endUserURL"));
	    }

	@AfterMethod
	public void reportDataSetResult(ITestResult testResult) throws IOException {
		takeScreenShotOnFailure(testResult.getStatus(), this.getClass().getSimpleName());

		if(testResult.getStatus()==ITestResult.SKIP)
			TestUtil.reportDataSetResult(issue_suite_xls, this.getClass().getSimpleName(), count+2, "SKIP");
		else if(testResult.getStatus()==ITestResult.FAILURE) {
			isTestPass=false;
			TestUtil.reportDataSetResult(issue_suite_xls, this.getClass().getSimpleName(), count+2, "FAIL");
			result= "Fail";
		}
		else if (testResult.getStatus()==ITestResult.SUCCESS) {
			TestUtil.reportDataSetResult(issue_suite_xls, this.getClass().getSimpleName(), count+2, "PASS");
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
