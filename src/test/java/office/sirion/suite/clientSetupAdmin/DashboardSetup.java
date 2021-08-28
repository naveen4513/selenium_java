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

public class DashboardSetup extends TestSuiteBase {
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
	public void DashboardSetupTest (String clientName, String clientDashboard, String clientInsight) throws InterruptedException, TestLinkAPIException {
		count++;
		if(!runmodes[count].equalsIgnoreCase("Y"))
			throw new SkipException("Runmode for Test Data -- " +clientName +" set to NO " +count);

		openBrowser();
		clientSetupAdminLogin(CONFIG.getProperty("ClientSetupAdminURL"), CONFIG.getProperty("ClientSetupAdminUserName"), CONFIG.getProperty("ClientSetupAdminPassword"));

		Logger.debug("Executing Test Sirion Client Setup Admin Dashboard Setup for -- " + clientName);

		driver.get(CONFIG.getProperty("ClientSetupAdminURL"));
		fluentWaitMethod(driver.findElement(By.linkText("Dashboard")));

		driver.findElement(By.linkText("Dashboard")).click();
		fluentWaitMethod(driver.findElement(By.id("allClients")));

		if (!clientName.equalsIgnoreCase("")) {
			new Select(driver.findElement(By.id("allClients"))).selectByVisibleText(clientName);
			Thread.sleep(2000);
			}

		if (!clientInsight.equalsIgnoreCase("")) {
			if (clientInsight.equalsIgnoreCase("ALL")) {
				if (!driver.findElement(By.id("insightRoles")).findElement(By.className("globaltoggleall")).isSelected())
					driver.findElement(By.id("insightRoles")).findElement(By.className("globaltoggleall")).click();
				}
			}

		if (!clientDashboard.equalsIgnoreCase("")) {
			if (clientInsight.equalsIgnoreCase("ALL")) {
				if (!driver.findElement(By.id("chartRoles")).findElement(By.className("globaltoggleall")).isSelected())
					driver.findElement(By.id("chartRoles")).findElement(By.className("globaltoggleall")).click();
				}
			}
		
/*		
 		// Removed in RC1.21
		if (!clientInsight.equalsIgnoreCase("")) {
			driver.findElement(By.id("insightId")).findElement(By.cssSelector("button.ui-multiselect.ui-widget.ui-state-default.ui-corner-all")).click();
			if (clientInsight.equalsIgnoreCase("ALL"))
				if (!driver.findElement(By.xpath(".//*[@id='data-ng-app']/div[13]")).findElement(By.className("ui-multiselect-all")).isSelected())
					driver.findElement(By.xpath(".//*[@id='data-ng-app']/div[13]")).findElement(By.className("ui-multiselect-all")).click();

			else {
				for (String entityInsightTitle : clientInsight.split(";")) {
					if (!driver.findElement(By.xpath(".//*[@id='data-ng-app']/div[13]/ul/li/label/input[contains(@title,'"+entityInsightTitle.trim()+"')]")).isSelected())
						driver.findElement(By.xpath(".//*[@id='data-ng-app']/div[13]/ul/li/label/input[contains(@title,'"+entityInsightTitle.trim()+"')]")).click();
					}
					
				}				
			}
		
		if (!clientDashboard.equalsIgnoreCase("")) {
			driver.findElement(By.id("chartTd")).findElement(By.cssSelector("button.ui-multiselect.ui-widget.ui-state-default.ui-corner-all")).click();
			if (clientDashboard.equalsIgnoreCase("ALL"))
				if (!driver.findElement(By.xpath(".//*[@id='data-ng-app']/div[12]")).findElement(By.className("ui-multiselect-all")).isSelected())
					driver.findElement(By.xpath(".//*[@id='data-ng-app']/div[12]")).findElement(By.className("ui-multiselect-all")).click();

			else {
				for (String entityDashboardTitle : clientDashboard.split(";")) {
					if (!driver.findElement(By.xpath(".//*[@id='data-ng-app']/div[12]/ul/li/label/input[contains(@title,'"+entityDashboardTitle.trim()+"')]")).isSelected())
						driver.findElement(By.xpath(".//*[@id='data-ng-app']/div[12]/ul/li/label/input[contains(@title,'"+entityDashboardTitle.trim()+"')]")).click();
					}
					
				}				
			}
*/		
		driver.findElement(By.id("proceed")).click();
		Thread.sleep(2000);		

		String clientAlert = driver.findElement(By.className("alertdialog-icon")).getText();

		Assert.assertEquals(clientAlert, "Client Configuration Updated Successfully");

		driver.findElement(By.cssSelector("button.ui-button.ui-widget.ui-state-default.ui-corner-all.ui-button-text-only")).click();

		Logger.debug("Test Sirion Client Setup Admin Dashboard Setup for -- " + clientName + " -- is PASSED");
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
