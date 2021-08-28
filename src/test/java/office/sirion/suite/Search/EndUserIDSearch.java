package office.sirion.suite.Search;

import office.sirion.suite.issue.TestSuiteBase;
import office.sirion.util.TestUtil;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.testng.ITestResult;
import org.testng.SkipException;
import org.testng.annotations.*;

import java.io.IOException;

public class EndUserIDSearch extends TestSuiteBase {
	String result = null;
	String runmodes[] = null;
	static int count = -1;
	static boolean fail = true;
	static boolean skip = false;
	static boolean isTestPass = true;

	@BeforeTest
	public void checkTestSkip() {
		if (!TestUtil.isTestCaseRunnable(search_suite_xls, this.getClass().getSimpleName())) {
			Logger.debug("Skipping Test Case "+ this.getClass().getSimpleName() + " as runmode set to NO");
			throw new SkipException("Skipping Test Case "+ this.getClass().getSimpleName() + " as runmode set to NO");
			}
		runmodes = TestUtil.getDataSetRunmodes(search_suite_xls, this.getClass().getSimpleName());
		}
	
	@Test(dataProvider = "getTestData")
	public void EndUserIDSearchTest (String EntityID) throws InterruptedException {

		count++;
		if (!runmodes[count].equalsIgnoreCase("Y")) {
			skip = true;
			throw new SkipException("Runmode for test set data set to no " + count);
		}
		Logger.debug("Executing Test Case ID Search");

		openBrowser();
		endUserLogin(CONFIG.getProperty("endUserURL"), CONFIG.getProperty("endUserUsername"), CONFIG.getProperty("endUserPassword"));
		Logger.debug("Executing Test Case ID Search --- ");

		driver.get(CONFIG.getProperty("endUserURL"));

		fluentWaitMethod(locateElementBy("end_user_search_link"));

		locateElementBy("end_user_search_link").sendKeys(EntityID);
		locateElementBy("end_user_matching_entityid_search").click();

		try {
			driver.findElement(By.xpath("//div[@class='modal-content']"));
			if(driver.findElement(By.xpath("//span[@class='success-icon']")).getText().
					equalsIgnoreCase("Either you do not have the required permissions or requested page does not exist anymore.") ||
					driver.findElement(By.xpath("//span[@class='success-icon']")).getText().
							equalsIgnoreCase("Server Error")) {
				Logger.debug("Unable to view show page of an Entity" + EntityID + "Might be Permission issue");
				locateElementBy("entity_show_page_permission_notification_pop_up_ok_button").click();
			}
		} catch (NoSuchElementException ex) {
			if (driver.getPageSource().contains(EntityID)) {
				System.out.println("ID displayed on Entity ID Search matched with show page");
				fail = false;
				driver.get(CONFIG.getProperty("endUserURL"));

			} else {
				System.out.println("Error: Show page of different Entity is getting displayed");
				fail = false;
				driver.get(CONFIG.getProperty("endUserURL"));

			}
		}
	}

	@AfterMethod
	public void reportDataSetResult(ITestResult testResult) throws IOException {
		takeScreenShotOnFailure(testResult.getStatus(), this.getClass().getSimpleName());

		if(testResult.getStatus()==ITestResult.SKIP)
			TestUtil.reportDataSetResult(client_admin_suite_xls, this.getClass().getSimpleName(), count+2, "SKIP");
		else if(testResult.getStatus()==ITestResult.FAILURE) {
			isTestPass=false;
			TestUtil.reportDataSetResult(client_admin_suite_xls, this.getClass().getSimpleName(), count+2, "FAIL");
			result= "Fail";
		}
		else if (testResult.getStatus()== ITestResult.SUCCESS) {
			TestUtil.reportDataSetResult(client_admin_suite_xls, this.getClass().getSimpleName(), count+2, "PASS");
			result= "Pass";
		}
		try {
			for (int i = 2; i <= search_suite_xls.getRowCount(this.getClass().getSimpleName()); i++) {
				String testCaseID = search_suite_xls.getCellData(this.getClass().getSimpleName(), "testCaseID", i);
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
			TestUtil.reportDataSetResult(search_suite_xls, "Test Cases", TestUtil.getRowNum(search_suite_xls, this.getClass().getSimpleName()), "PASS");
		else
			TestUtil.reportDataSetResult(search_suite_xls, "Test Cases", TestUtil.getRowNum(search_suite_xls, this.getClass().getSimpleName()), "FAIL");
	}

	@DataProvider
	public Object[][] getTestData() {
		return TestUtil.getData(search_suite_xls, this.getClass().getSimpleName());
	}
}
