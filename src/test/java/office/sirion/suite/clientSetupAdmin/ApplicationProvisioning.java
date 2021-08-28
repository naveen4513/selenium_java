package office.sirion.suite.clientSetupAdmin;

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

import testlink.api.java.client.TestLinkAPIException;
import testlink.api.java.client.TestLinkAPIResults;

public class ApplicationProvisioning extends TestSuiteBase {
	String runmodes[]=null;
	static int count=-1;
	static boolean isTestPass=true;
	String testCaseID=null;
	String result=null;
	

	@BeforeTest
	public void checkTestSkip() {
		if(!TestUtil.isTestCaseRunnable(client_setup_admin_suite_xls,this.getClass().getSimpleName())) {
			Logger.debug("Skipping Test Case "+this.getClass().getSimpleName()+" as Runmode set to NO");
			throw new SkipException("Skipping Test Case "+this.getClass().getSimpleName()+" as Runmode set to NO");
			}
		runmodes = TestUtil.getDataSetRunmodes(client_setup_admin_suite_xls, this.getClass().getSimpleName());
		}
	
	@Test (dataProvider="getTestData")
	public void ApplicationProvisioningTest (String clientName) throws InterruptedException, TestLinkAPIException {
		count++;
		if(!runmodes[count].equalsIgnoreCase("Y"))
			throw new SkipException("Runmode for Test Data -- " +clientName +" set to NO " +count);
		
		openBrowser();
		clientSetupAdminLogin(CONFIG.getProperty("ClientSetupAdminURL"), CONFIG.getProperty("ClientSetupAdminUserName"), CONFIG.getProperty("ClientSetupAdminPassword"));

		Logger.debug("Executing Sirion Client Setup Admin Application Provisioning Setup Test For -- "+clientName);
		
		driver.get(CONFIG.getProperty("ClientSetupAdminURL"));
		fluentWaitMethod(driver.findElement(By.linkText("Application Provisioning")));

		driver.findElement(By.linkText("Application Provisioning")).click();
		fluentWaitMethod(driver.findElement(By.id("allClients")));

		if (!clientName.equalsIgnoreCase(""))
			new Select(driver.findElement(By.id("allClients"))).selectByVisibleText(clientName);
		
		driver.findElement(By.id("proceed")).click();
		//
		fluentWaitMethod(driver.findElement(By.id("ui-accordion-1-header-0")).findElement(By.className("globaltoggleall")));
		
		if (!driver.findElement(By.id("ui-accordion-1-header-0")).findElement(By.className("globaltoggleall")).isSelected())
			driver.findElement(By.id("ui-accordion-1-header-0")).findElement(By.className("globaltoggleall")).click();
		
		driver.findElement(By.className("submit")).findElement(By.id("proceed")).click();
		//
		
		if (driver.findElements(By.className("success-icon")).size()!=0) {
			String clientAlert = driver.findElement(By.className("success-icon")).getText();

			Assert.assertEquals(clientAlert, "Updated Successfully!");
			Logger.debug("Client Application Provisioning Configuration Updated Successfully!!");
			
			driver.findElement(By.cssSelector("button.ui-button.ui-widget.ui-state-default.ui-corner-all.ui-button-text-only")).click();
			Thread.sleep(2000);
			}

	     driver.get(CONFIG.getProperty("ClientSetupAdminURL"));
	     }
	
	@AfterMethod
	public void reportDataSetResult(ITestResult testResult) throws IOException {
	   takeScreenShotOnFailure(testResult.getStatus(), this.getClass().getSimpleName());
	   
	   if(testResult.getStatus()==ITestResult.SKIP)
	      TestUtil.reportDataSetResult(client_setup_admin_suite_xls, this.getClass().getSimpleName(), count+2, "SKIP");
	   else if(testResult.getStatus()==ITestResult.FAILURE) {
	      isTestPass=false;
	      TestUtil.reportDataSetResult(client_setup_admin_suite_xls, this.getClass().getSimpleName(), count+2, "FAIL");
	      result= "Fail";
	      }
	   else if (testResult.getStatus()==ITestResult.SUCCESS) {
	      TestUtil.reportDataSetResult(client_setup_admin_suite_xls, this.getClass().getSimpleName(), count+2, "PASS");
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
			TestUtil.reportDataSetResult(client_setup_admin_suite_xls, "Test Cases", TestUtil.getRowNum(client_setup_admin_suite_xls, this.getClass().getSimpleName()), "PASS");
		else
			TestUtil.reportDataSetResult(client_setup_admin_suite_xls, "Test Cases", TestUtil.getRowNum(client_setup_admin_suite_xls, this.getClass().getSimpleName()), "FAIL");
		}

	@DataProvider
	public Object[][] getTestData() {
		return TestUtil.getData(client_setup_admin_suite_xls, this.getClass().getSimpleName());
		}
	}