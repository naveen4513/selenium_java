package office.sirion.suite.clientAdmin.workflowConfiguration;

import java.io.IOException;

import office.sirion.util.TestUtil;
import office.sirion.util.TestlinkIntegration;

import org.openqa.selenium.By;
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

import testlink.api.java.client.TestLinkAPIResults;

public class EmailCreation extends TestSuiteBase {
	String runmodes[] = null;
	static int count = -1;
	static boolean isTestPass = true;
	String testCaseID=null;
	String result=null;

	
	@BeforeTest
	public void checkTestSkip() {
		if (!TestUtil.isTestCaseRunnable(client_workflow_configuration_suite_xls, this.getClass().getSimpleName())) {
			Logger.debug("Skipping Test Case " + this.getClass().getSimpleName() + " as runmode set to NO");
			throw new SkipException("Skipping Test Case " + this.getClass().getSimpleName() + " as runmode set to NO");
		  	}
		runmodes = TestUtil.getDataSetRunmodes(client_workflow_configuration_suite_xls, this.getClass().getSimpleName());
	  	}
	
	@Test(dataProvider = "getTestData")
	public void EmailCreationTest(String emailEntity, String emailName) throws InterruptedException {
		count++;
		if (!runmodes[count].equalsIgnoreCase("Y"))
			throw new SkipException("Runmode for Test data -- " + emailName + " set to NO " + count);
	  
		openBrowser();
		clientAdminLogin(CONFIG.getProperty("clientAdminURL"), CONFIG.getProperty("clientAdminUsername"), CONFIG.getProperty("clientAdminPassword"));
	
		Logger.debug("Executing Client Admin Email Creation Test -- " +emailName);
	
		driver.get(CONFIG.getProperty("clientAdminURL"));
		fluentWaitMethod(driver.findElement(By.linkText("Email Creation")));
	  
		driver.findElement(By.linkText("Email Creation")).click();
		fluentWaitMethod(driver.findElement(By.id("_entityTypeId_id")));
	  
		if (!emailEntity.equalsIgnoreCase(""))
			new Select(driver.findElement(By.id("_entityTypeId_id"))).selectByVisibleText(emailEntity);
		Thread.sleep(2000);
	  
		if (!emailName.equalsIgnoreCase("")) {
			driver.findElement(By.id("_name_id")).clear();
			driver.findElement(By.id("_name_id")).sendKeys(emailName.trim());
		  	}
		
		driver.findElement(By.id("proceed")).click();
		//
		
		if (driver.findElements(By.id("alertdialog")).size() != 0) {
			String entityErrors = driver.findElement(By.id("errors")).getText();
		  
			if (entityErrors.contains("This name already exists in the system."))
				Logger.debug("For Email, " + emailName + " --- This name already exists in the system.");
		  
			driver.get(CONFIG.getProperty("clientAdminURL"));
			return;
		  	}
	  
		if (driver.findElements(By.className("alertdialog-icon")).size()!=0) {
			String userAlert = driver.findElement(By.className("alertdialog-icon")).getText();
		  
			Assert.assertEquals(userAlert, "Email Configuration updated successfully");
			Logger.debug("Email has been created successfully with Name -- " +emailName);
			
			driver.findElement(By.cssSelector("button.ui-button.ui-widget.ui-state-default.ui-corner-all.ui-button-text-only")).click();
		  	}
		
		driver.get(CONFIG.getProperty("clientAdminURL"));
		}
	
	@AfterMethod
	public void reportDataSetResult(ITestResult testResult) throws IOException {
	   takeScreenShotOnFailure(testResult.getStatus(), this.getClass().getSimpleName());
	   
	   if(testResult.getStatus()==ITestResult.SKIP)
	      TestUtil.reportDataSetResult(client_workflow_configuration_suite_xls, this.getClass().getSimpleName(), count+2, "SKIP");
	   else if(testResult.getStatus()==ITestResult.FAILURE) {
	      isTestPass=false;
	      TestUtil.reportDataSetResult(client_workflow_configuration_suite_xls, this.getClass().getSimpleName(), count+2, "FAIL");
	      result= "Fail";
	      }
	   else if (testResult.getStatus()==ITestResult.SUCCESS) {
	      TestUtil.reportDataSetResult(client_workflow_configuration_suite_xls, this.getClass().getSimpleName(), count+2, "PASS");
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
			TestUtil.reportDataSetResult(client_workflow_configuration_suite_xls, "Test Cases", TestUtil.getRowNum(client_workflow_configuration_suite_xls, this.getClass().getSimpleName()), "PASS");
		else
			TestUtil.reportDataSetResult(client_workflow_configuration_suite_xls, "Test Cases", TestUtil.getRowNum(client_workflow_configuration_suite_xls, this.getClass().getSimpleName()), "FAIL");
		}

	@DataProvider
	public Object[][] getTestData() {
		return TestUtil.getData(client_workflow_configuration_suite_xls, this.getClass().getSimpleName());
		}
	}