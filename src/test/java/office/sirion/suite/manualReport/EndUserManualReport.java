package office.sirion.suite.manualReport;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import office.sirion.util.TestUtil;

import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.testng.ITestResult;
import org.testng.SkipException;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import testlink.api.java.client.TestLinkAPIException;
import testlink.api.java.client.TestLinkAPIResults;

public class EndUserManualReport extends TestSuiteBase {
	String runmodes[] = null;
	static int count = -1;
	static boolean isTestPass = true;
	String testCaseID=null;
	String result=null;

	@BeforeTest
	public void checkTestSkip() {
		if(!TestUtil.isTestCaseRunnable(manual_report_suite_xls,this.getClass().getSimpleName())) {
			Logger.debug("Skipping Test Case "+this.getClass().getSimpleName()+" as Runmode set to NO");
			throw new SkipException("Skipping Test Case "+this.getClass().getSimpleName()+" as Runmode set to NO");
			}
		runmodes = TestUtil.getDataSetRunmodes(manual_report_suite_xls, this.getClass().getSimpleName());
		}

	@Test (dataProvider="getTestData")
	public void CommonReportOpenTest (String testCaseID, String entityName, String entityManualReportName)
			throws InterruptedException, TestLinkAPIException {

		count++;
		if (!runmodes[count].equalsIgnoreCase("Y"))
			throw new SkipException("Runmode for Test Data -- " + entityName + " is set to NO " +count);

		openBrowser();
	    endUserLogin(CONFIG.getProperty("endUserURL"), CONFIG.getProperty("endUserUsername"), CONFIG.getProperty("endUserPassword"));

	    Logger.debug("Executing Test Case Report Open for entity -- " +entityName + " and manual report -- "+ entityManualReportName);

	    driver.get(CONFIG.getProperty("endUserURL"));
		//
		fluentWaitMethod(driver.findElement(By.id("header")).findElement(By.linkText("Reports")));

		driver.findElement(By.id("header")).findElement(By.linkText("Reports")).click();
		Thread.sleep(8000);
	//	fluentWaitMethod(driver.findElement(By.id("listTd1")).findElement(By.id("allLists1")));
		
		new Select(driver.findElement(By.id("allLists1"))).selectByVisibleText(entityName);
	//	new Select(driver.findElement(By.id("allLists2"))).selectByVisibleText(entityManualReportName);
    	new Select(driver.findElement(By.id("listTd2")).findElement(By.id("allLists2"))).selectByVisibleText(entityManualReportName);
	    //
/*		fluentWaitMethod(driver.findElement(By.id("downloadXls")));

		String reportNameOnShowPage = driver.findElement(By.id("downloadXls")).getText();
		Logger.debug(reportNameOnShowPage);*/


	    	
		Logger.debug("Report downloaded successfuly for -- " + entityManualReportName);
    	driver.get(CONFIG.getProperty("endUserURL"));
    	}
	    
	
	@AfterMethod
	public void reportDataSetResult(ITestResult testResult) throws IOException {
		takeScreenShotOnFailure(testResult.getStatus(), this.getClass().getSimpleName());

		if(testResult.getStatus()==ITestResult.SKIP)
			TestUtil.reportDataSetResult(manual_report_suite_xls, this.getClass().getSimpleName(), count+2, "SKIP");
		else if(testResult.getStatus()==ITestResult.FAILURE) {
			isTestPass=false;
			TestUtil.reportDataSetResult(manual_report_suite_xls, this.getClass().getSimpleName(), count+2, "FAIL");
			result= "Fail";
			}
		else if (testResult.getStatus()==ITestResult.SUCCESS) {
			TestUtil.reportDataSetResult(manual_report_suite_xls, this.getClass().getSimpleName(), count+2, "PASS");
			result= "Pass";
			}
		}

	@AfterTest
	public void reportTestResult() {
		if (isTestPass)
			TestUtil.reportDataSetResult(manual_report_suite_xls, "Test Cases", TestUtil.getRowNum(manual_report_suite_xls, this.getClass().getSimpleName()), "PASS");
		else
			TestUtil.reportDataSetResult(manual_report_suite_xls, "Test Cases", TestUtil.getRowNum(manual_report_suite_xls, this.getClass().getSimpleName()), "FAIL");
		}

	@DataProvider
	public Object[][] getTestData() {
		return TestUtil.getData(manual_report_suite_xls, this.getClass().getSimpleName());
	}
}