package office.sirion.suite.interpretation;

import java.io.IOException;
import java.sql.SQLException;
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


public class InterpretationWorkflow extends TestSuiteBase {
	String result = null;
	String runmodes[] = null;
	static int count = -1;
	static boolean fail = true;
	static boolean skip = false;
	static boolean isTestPass = true;
  
	@BeforeTest
	public void checkTestSkip() {
		if (!TestUtil.isTestCaseRunnable(int_suite_xls, this.getClass().getSimpleName())) {
			Logger.debug("Skipping Test Case " + this.getClass().getSimpleName() + " as runmode set to NO");
			throw new SkipException("Skipping Test Case " + this.getClass().getSimpleName() + " as runmode set to NO");
			}
		runmodes = TestUtil.getDataSetRunmodes(int_suite_xls, this.getClass().getSimpleName());
		}
	
	@Test(dataProvider = "getTestData")
	public void InterpretationWorkflowTest (String ipTitle, String ipWorkflowSteps
			)throws InterruptedException, ClassNotFoundException, SQLException {

		count++;
		if (!runmodes[count].equalsIgnoreCase("Y")) {
			skip = true;
			throw new SkipException("Runmode for test set data set to no "+ count);
			}
		
		Logger.debug("Executing Test Case Interpretaion Workflow");
		System.out.println("before open browser");		
		// Launch The Browser
		openBrowser();
		endUserLogin(CONFIG.getProperty("endUserURL"), CONFIG.getProperty("endUserUsername"), CONFIG.getProperty("endUserPassword"));

        driver.get(CONFIG.getProperty("endUserURL"));
	
		fluentWaitMethod(locateElementBy("ip_quick_link"));

		locateElementBy("ip_quick_link").click();

		
		fluentWaitMethod(locateElementBy("entity_listing_page_display_dropdown_link"));

		new Select(locateElementBy("entity_listing_page_display_dropdown_link")).selectByVisibleText("100");

		if (!ipTitle.equalsIgnoreCase("")) {
			driver.findElement(By.xpath("//*[@id='cr']/tbody/tr[@role='row']/td[contains(.,'" + ipTitle + "')]/preceding-sibling::td[1]/a")).click();
		} else {
			locateElementBy("ip_global_listing_page_first_link").click();


			fluentWaitMethod(driver.findElement(By.xpath("//button[contains(.,'Clone')]")));
			Assert.assertNotNull(driver.findElement(By.xpath("//button[contains(.,'Clone')]")));
			((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", driver.findElement(By.xpath("//button[contains(.,'Clone')]")));
			driver.findElement(By.xpath("//button[contains(.,'Clone')]")).click();


			fluentWaitMethod(driver.findElement(By.xpath("//button[contains(.,'Save Interpretation')]")));
			Assert.assertNotNull(driver.findElement(By.xpath("//button[contains(.,'Save Interpretation')]")));
			((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", driver.findElement(By.xpath("//button[contains(.,'Save Interpretation')]")));
			driver.findElement(By.xpath("//button[contains(.,'Save Interpretation')]")).click();

			if (driver.findElements(By.className("success-icon")).size()!=0) {
				String entityID = driver.findElement(By.className("success-icon")).findElement(By.id("hrefElemId")).getText();

				Logger.debug("IP cloned successfully with issue ID " + entityID);

				driver.findElement(By.className("success-icon")).findElement(By.id("hrefElemId")).click();
				fluentWaitMethod(locateElementBy("ip_show_page_id"));
				

				String entityShowPageID = locateElementBy("ip_show_page_id").getText();

				Assert.assertEquals(entityShowPageID, entityID);
				Logger.debug("IP ID on show page has been verified");
			}
		}


				
		if (!ipWorkflowSteps.equalsIgnoreCase("")) {
			for (String entityData : ipWorkflowSteps.split(";")) {
				try {
					fluentWaitMethod(driver.findElement(By.xpath("//button[contains(.,'"+entityData.trim()+"')]")));
					Assert.assertNotNull(driver.findElement(By.xpath("//button[contains(.,'"+entityData.trim()+"')]")));
					driver.findElement(By.xpath("//button[contains(.,'"+entityData.trim()+"')]")).click();
					fluentWaitMethod(locateElementBy("ip_show_page_id"));
				} catch (NoSuchElementException e) {
					Logger.debug("No Such Element with the given Key, --- "+entityData+" --- Moving onto Next Step");
				}
			}
			fluentWaitMethod(locateElementBy("ip_show_page_id"));
			String entityShowPageStatus = locateElementBy("ip_show_page_status").getText();

			
			Logger.debug("IP Status on show page has been verified"+entityShowPageStatus);
		}
		
		
		fail = false;

        driver.get(CONFIG.getProperty("endUserURL"));
	    }


	@AfterMethod
	public void reportDataSetResult(ITestResult testResult) throws IOException {
		takeScreenShotOnFailure(testResult.getStatus(), this.getClass().getSimpleName());

		if(testResult.getStatus()==ITestResult.SKIP)
			TestUtil.reportDataSetResult(int_suite_xls, this.getClass().getSimpleName(), count+2, "SKIP");
		else if(testResult.getStatus()==ITestResult.FAILURE) {
			isTestPass=false;
			TestUtil.reportDataSetResult(int_suite_xls, this.getClass().getSimpleName(), count+2, "FAIL");
			result= "Fail";
		}
		else if (testResult.getStatus()==ITestResult.SUCCESS) {
			TestUtil.reportDataSetResult(int_suite_xls, this.getClass().getSimpleName(), count+2, "PASS");
			result= "Pass";
		}
		try {
			for (int i = 2; i <= int_suite_xls.getRowCount(this.getClass().getSimpleName()); i++) {
				String testCaseID = int_suite_xls.getCellData(this.getClass().getSimpleName(), "testCaseID", i);
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
      TestUtil.reportDataSetResult(int_suite_xls, "Test Cases", TestUtil.getRowNum(int_suite_xls, this.getClass().getSimpleName()), "PASS");
    else
      TestUtil.reportDataSetResult(int_suite_xls, "Test Cases", TestUtil.getRowNum(int_suite_xls, this.getClass().getSimpleName()), "FAIL");

  }

  @DataProvider
  public Object[][] getTestData() {
    return TestUtil.getData(int_suite_xls, this.getClass().getSimpleName());
  }

}
