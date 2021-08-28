package office.sirion.suite.cdr;

import java.io.IOException;

import office.sirion.util.TestUtil;
import office.sirion.util.TestlinkIntegration;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.NoSuchElementException;
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

public class CDRWorkflow extends TestSuiteBase {
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
	public void CDRWorkflowTest (String testCaseID, String cdrWorkflowSteps, String cdrParent
			) throws InterruptedException {

		count++;
		if (!runmodes[count].equalsIgnoreCase("Y"))
			throw new SkipException("Runmode for Test Data Set to NO -- " + count);
		
		this.testCaseID = testCaseID;

		openBrowser();
	    endUserLogin(CONFIG.getProperty("endUserURL"), CONFIG.getProperty("endUserUsername"), CONFIG.getProperty("endUserPassword"));
	    
	    Logger.debug("Executing Test Case CDR Workflow with Title -- " + cdrParent);

	    driver.get(CONFIG.getProperty("endUserURL"));
	    //
		fluentWaitMethod(locateElementBy("cdr_quick_link"));

		locateElementBy("cdr_quick_link").click();
	    //
		fluentWaitMethod(locateElementBy("entity_listing_page_display_dropdown_link"));
		
		new Select(locateElementBy("entity_listing_page_display_dropdown_link")).selectByVisibleText(CONFIG.getProperty("maxEntityListingDropdown"));
		//
		
		if (!cdrParent.equalsIgnoreCase("") && cdrParent.length()<=30)
			driver.findElement(By.xpath(".//*[@id='cr']/tbody/tr/td[2][./text()='"+cdrParent+"']/preceding-sibling::td[1]/a")).click();
		else if (!cdrParent.equalsIgnoreCase("") && cdrParent.length()>30)
			driver.findElement(By.xpath(".//*[@id='cr']/tbody/tr/td[2][div/div[./text()='"+cdrParent+"']]/preceding-sibling::td[1]/a")).click();
		else {
			locateElementBy("entity_listing_page_first_entry_link").click();
			//
			fluentWaitMethod(driver.findElement(By.cssSelector("ng-form[template='tab.actions']")).findElement(By.cssSelector("div.submit.freeze")));

			WebElement elementEdit = driver.findElement(By.cssSelector("ng-form[template='tab.actions']")).findElement(By.cssSelector("div.submit.freeze"));		
			Assert.assertNotNull(elementEdit.findElement(By.xpath(".//button[contains(.,'Clone')]")));
			((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", elementEdit.findElement(By.xpath(".//button[contains(.,'Clone')]")));
			elementEdit.findElement(By.xpath(".//button[contains(.,'Clone')]")).click();
			//
			fluentWaitMethod(driver.findElement(By.xpath("//button[contains(.,'Save CDR')]")));

			Assert.assertNotNull(driver.findElement(By.xpath("//button[contains(.,'Save CDR')]")));
			((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", driver.findElement(By.xpath("//button[contains(.,'Save CDR')]")));
			driver.findElement(By.xpath("//button[contains(.,'Save CDR')]")).click();
			//

			if (driver.findElements(By.className("success-icon")).size()!=0) {
				String entityID = driver.findElement(By.className("success-icon")).findElement(By.id("hrefElemId")).getText();

				Logger.debug("CDR created successfully with Entity ID " + entityID);

				driver.findElement(By.className("success-icon")).findElement(By.id("hrefElemId")).click();
				//
				fluentWaitMethod(locateElementBy("cdr_show_page_id"));

				String entityShowPageID = locateElementBy("cdr_show_page_id").getText();

				Assert.assertEquals(entityShowPageID, entityID);
				}
			}
		//
		fluentWaitMethod(driver.findElement(By.cssSelector("ng-form[template='tab.actions']")).findElement(By.cssSelector("div.submit.freeze")));

		if (!cdrWorkflowSteps.equalsIgnoreCase("")) {
			for (String entityData : cdrWorkflowSteps.split(";")) {
				WebElement elementWorkflow = driver.findElement(By.cssSelector("ng-form[template='tab.actions']")).findElement(By.cssSelector("div.submit.freeze"));		
				try {
					fluentWaitMethod(elementWorkflow.findElement(By.xpath(".//button[contains(.,'"+entityData.trim()+"')]")));
					Assert.assertNotNull(elementWorkflow.findElement(By.xpath(".//button[contains(.,'"+entityData.trim()+"')]")));
					elementWorkflow.findElement(By.xpath(".//button[contains(.,'"+entityData.trim()+"')]")).click();
					//
					} catch (NoSuchElementException e) {
						Logger.info("No Such Element with the given Key, -- "+entityData+" -- Moving onto Next Step");
		    			}
				}
			}

	    Logger.debug("Test Case CDR Workflow with Title -- " + cdrParent + " -- is PASSED");
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