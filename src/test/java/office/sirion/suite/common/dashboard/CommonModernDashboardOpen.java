package office.sirion.suite.common.dashboard;

import java.io.IOException;

import office.sirion.util.TestCommonDashboard;
import office.sirion.util.TestUtil;

import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.testng.Assert;
import org.testng.ITestResult;
import org.testng.SkipException;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import testlink.api.java.client.TestLinkAPIResults;

public class CommonModernDashboardOpen extends TestSuiteBase {
	String result = null;
	String runmodes[] = null;
	static int count = -1;
	static boolean isTestPass = true;

	@BeforeTest
	public void checkTestSkip() {
		if (!TestUtil.isTestCaseRunnable(common_dashboard_suite_xls, this.getClass().getSimpleName())) {
			Logger.debug("Skipping Test Case " + this.getClass().getSimpleName() + " as Runmode set to NO");
			throw new SkipException("Skipping Test Case " + this.getClass().getSimpleName() + " as Runmode set to NO");
			}
		runmodes = TestUtil.getDataSetRunmodes(common_dashboard_suite_xls, this.getClass().getSimpleName());
		}

	@Test(dataProvider = "getTestData")
	public void CommonModernDashboardOpenTest (String dashboardID, String dashboardName, String dashboardType
			) throws InterruptedException, IOException {

		count++;
		if(!runmodes[count].equalsIgnoreCase("Y"))
			throw new SkipException("Runmode for Test Data -- " +dashboardID+ " -- is set to NO " +count);

		openBrowser();
	    endUserLogin(CONFIG.getProperty("endUserURL"), CONFIG.getProperty("endUserUsername"), CONFIG.getProperty("endUserPassword"));

	    Logger.debug("Executing Test Case Modern Dashboard Open for -- " + dashboardID);

	    driver.get(CONFIG.getProperty("endUserURL"));

	    fluentWaitMethod(driver.findElement(By.id("globalView")));
	    fluentWaitMethod(driver.findElement(By.id("priorityListModernView")));
	    driver.findElement(By.id("priorityListModernView")).click();

	    TestCommonDashboard.deSelectChartList();

	    String selectedDashboardNameOnShowPage = driver.findElement(By.className("chartSelectionBody")).findElement(By.id(dashboardID)).getText();

	    driver.findElement(By.className("chartSelectionBody")).findElement(By.id(dashboardID)).findElement(By.className("chartListClass")).click();

	    driver.findElement(By.className("chartSelectionHeader")).findElement(By.id("savePreferences")).click();
	    String dashboardIDonShowPage = dashboardID.replaceAll("li_", "");

	    fluentWaitMethod(driver.findElement(By.id("chartContainer" + dashboardIDonShowPage)));

	    if (!dashboardType.equalsIgnoreCase("TreeMap") && !dashboardType.equalsIgnoreCase("TreeMap")) {
		    fluentWaitMethod(driver.findElement(By.tagName("svg")));
	    	driver.findElement(By.tagName("svg"));
	    	}
	    else {
		    fluentWaitMethod(driver.findElement(By.id("chartContainer" + dashboardIDonShowPage)).findElement(By.tagName("div")));
		    }

	    takeScreenShotAs(this.getClass().getSimpleName(), selectedDashboardNameOnShowPage.toUpperCase());

	    Assert.assertEquals(driver.findElement(By.id("chartContainerTable")).findElement(By.className("chartTitle")).getText().toUpperCase(), selectedDashboardNameOnShowPage.toUpperCase());

	    Logger.debug("Test Case Modern Dashboard Open for -- " + selectedDashboardNameOnShowPage.toUpperCase() + " -- is PASSED");

	    driver.get(CONFIG.getProperty("endUserURL"));
	    }

	@AfterMethod
	public void reportDataSetResult(ITestResult testResult) throws IOException {
		takeScreenShotOnFailure(testResult.getStatus(), this.getClass().getSimpleName());
		if(testResult.getStatus()==ITestResult.SKIP)
			TestUtil.reportDataSetResult(common_dashboard_suite_xls, this.getClass().getSimpleName(), count+2, "SKIP");
		else if(testResult.getStatus()==ITestResult.FAILURE) {
			isTestPass=false;
			TestUtil.reportDataSetResult(common_dashboard_suite_xls, this.getClass().getSimpleName(), count+2, "FAIL");
			result= "Fail";
			}
		else if (testResult.getStatus()==ITestResult.SUCCESS) {
			TestUtil.reportDataSetResult(common_dashboard_suite_xls, this.getClass().getSimpleName(), count+2, "PASS");
			result= "Pass";
			}
		}

	@AfterTest
	public void reportTestResult() {
		if (isTestPass)
			TestUtil.reportDataSetResult(common_dashboard_suite_xls, "Test Cases", TestUtil.getRowNum(common_dashboard_suite_xls, this.getClass().getSimpleName()),"PASS");
		else
			TestUtil.reportDataSetResult(common_dashboard_suite_xls, "Test Cases", TestUtil.getRowNum(common_dashboard_suite_xls, this.getClass().getSimpleName()),"FAIL");
		}

	@DataProvider
	public Object[][] getTestData() {
		return TestUtil.getData(common_dashboard_suite_xls, this.getClass().getSimpleName());
		}
	}