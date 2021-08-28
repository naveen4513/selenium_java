package office.sirion.suite.sirionAdmin;

import java.io.IOException;

import office.sirion.util.TestUtil;
import office.sirion.util.TestlinkIntegration;

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

public class SLItem extends TestSuiteBase {
	String runmodes[] = null;
	static int count = -1;
	static boolean isTestPass = true;
	String testCaseID=null;
	String result=null;

	@BeforeTest
	public void checkTestSkip() {
		if (!TestUtil.isTestCaseRunnable(sirion_admin_suite_xls, this.getClass().getSimpleName())) {
			Logger.debug("Skipping Test Case " + this.getClass().getSimpleName() + " as Runmode set to NO");
			throw new SkipException("Skipping Test Case " + this.getClass().getSimpleName() + " as Runmode set to NO");
			}
		runmodes = TestUtil.getDataSetRunmodes(sirion_admin_suite_xls, this.getClass().getSimpleName());
		}

	@Test (dataProvider="getTestData")
	public void SLItemCreation (String slItem
			) throws InterruptedException {

		count++;
		if(!runmodes[count].equalsIgnoreCase("Y"))
			throw new SkipException("Runmode for Test Data -- " +slItem +" set to NO " +count);

		openBrowser();
		sirionAdminLogin(CONFIG.getProperty("sirionAdminURL"), CONFIG.getProperty("sirionAdminUsername"), CONFIG.getProperty("sirionAdminPassword"));

		Logger.debug("Executing Test Sirion Admin SL Item Creation for -- " + slItem);

		driver.get(CONFIG.getProperty("sirionAdminURL"));
		//
		fluentWaitMethod(driver.findElement(By.linkText("SL Item")));

		driver.findElement(By.linkText("SL Item")).click();
		//
		fluentWaitMethod(driver.findElement(By.className("plus")));
	
		driver.findElement(By.className("plus")).click();
		//
		fluentWaitMethod(driver.findElement(By.id("_name_id")));

		if (!slItem.equalsIgnoreCase("")) {
			driver.findElement(By.id("_name_id")).clear();
			driver.findElement(By.id("_name_id")).sendKeys(slItem.trim());
			}

		driver.findElement(By.id("proceed")).click();
		//
		
		if (slItem.equalsIgnoreCase("")) {
			String entityErrors = driver.findElement(By.className("formErrorContent")).getText();
			
			Assert.assertEquals(entityErrors, "This field is required");
			Logger.debug("SL Item Name is Mandatory");
			
		    driver.get(CONFIG.getProperty("sirionAdminURL"));
		    return;
		    }
			
		if (slItem.length() > 256) {
			String entityErrors = driver.findElement(By.className("formErrorContent")).getText();
			
			Assert.assertEquals(entityErrors, "Maximum 256 characters allowed");
			Logger.debug("For SL Item, -- Maximum 256 characters allowed");
			
		    driver.get(CONFIG.getProperty("sirionAdminURL"));
		    return;
		    }	

		if(driver.findElements(By.id("errorDialog")).size()!=0 || driver.findElements(By.id("alertdialog")).size()!=0) {
			String entityErrors = driver.findElement(By.id("errors")).getText();
			
			Assert.assertEquals(entityErrors, "This name already exists in the system.");
			Logger.debug("For SL Item, " +slItem+" -- This name already exists in the system.");
						
			driver.get(CONFIG.getProperty("sirionAdminURL"));
			return;
			}

		driver.findElement(By.className("dataTables_filter")).findElement(By.tagName("label")).findElement(By.tagName("input")).sendKeys(slItem);
		//

        driver.findElement(By.xpath(".//*[@id='l_com_sirionlabs_model_MasterSlaitem']/tbody/tr/td[1]/a/div[@sort='"+ slItem +"']")).click();
		Thread.sleep(2000);

		String entityTypeShowPage = driver.findElement(By.id("_s_com_sirionlabs_model_MasterSlaitem_name_name_id")).getText();
        Assert.assertEquals(entityTypeShowPage, slItem, "SL Item Name at show page is -- " +entityTypeShowPage+ " instead of -- " +slItem);

		Logger.debug("Test Sirion Admin SL Item Creation for -- " + slItem + " is PASSED");
		driver.get(CONFIG.getProperty("sirionAdminURL"));
		}

	@AfterMethod
	public void reportDataSetResult(ITestResult testResult) throws IOException {
	   takeScreenShotOnFailure(testResult.getStatus(), this.getClass().getSimpleName());
	   
	   if(testResult.getStatus()==ITestResult.SKIP)
	      TestUtil.reportDataSetResult(sirion_admin_suite_xls, this.getClass().getSimpleName(), count+2, "SKIP");
	   else if(testResult.getStatus()==ITestResult.FAILURE) {
	      isTestPass=false;
	      TestUtil.reportDataSetResult(sirion_admin_suite_xls, this.getClass().getSimpleName(), count+2, "FAIL");
	      result= "Fail";
	      }
	   else if (testResult.getStatus()==ITestResult.SUCCESS) {
	      TestUtil.reportDataSetResult(sirion_admin_suite_xls, this.getClass().getSimpleName(), count+2, "PASS");
	      result= "Pass";
	      }
	   try {
	      if (!testCaseID.equalsIgnoreCase(""))
	         TestlinkIntegration.updateTestLinkResult(testCaseID,"",result);
	      } catch (Exception e) {
	                   Logger.debug(e);
	         }
	   }

	@AfterTest
	public void reportTestResult() {
		if (isTestPass)
			TestUtil.reportDataSetResult(sirion_admin_suite_xls, "Test Cases", TestUtil.getRowNum(sirion_admin_suite_xls, this.getClass().getSimpleName()), "PASS");
		else
			TestUtil.reportDataSetResult(sirion_admin_suite_xls, "Test Cases", TestUtil.getRowNum(sirion_admin_suite_xls, this.getClass().getSimpleName()), "FAIL");
		}

	@DataProvider
	public Object[][] getTestData() {
		return TestUtil.getData(sirion_admin_suite_xls, this.getClass().getSimpleName());
		}
	}