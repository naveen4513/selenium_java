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

public class WorkflowCreation extends TestSuiteBase {
	String runmodes[]=null;
	static int count=-1;
	static boolean isTestPass=true;
	String testCaseID=null;
	String result=null;

	@BeforeTest
	public void checkTestSkip() {
		if(!TestUtil.isTestCaseRunnable(client_workflow_configuration_suite_xls,this.getClass().getSimpleName())) {
			Logger.debug("Skipping Test Case "+this.getClass().getSimpleName()+" as runmode set to NO");
			throw new SkipException("Skipping Test Case "+this.getClass().getSimpleName()+" as runmode set to NO");
			}
		runmodes=TestUtil.getDataSetRunmodes(client_workflow_configuration_suite_xls, this.getClass().getSimpleName());
		}

	@Test (dataProvider="getTestData")
	public void WorkflowCreationTest (String workflowName, String entityType, String filePath, String workflowSupplier
			) throws InterruptedException {

		count++;
		if(!runmodes[count].equalsIgnoreCase("Y"))
			throw new SkipException("Runmode for Test data -- " +workflowName +" set to NO " +count);
		
		openBrowser();
		clientAdminLogin(CONFIG.getProperty("clientAdminURL"), CONFIG.getProperty("clientAdminUsername"), CONFIG.getProperty("clientAdminPassword"));

		Logger.debug("Executing Test Client Admin Workflow Creation for -- " +workflowName);
		
		driver.get(CONFIG.getProperty("clientAdminURL"));
		//
		fluentWaitMethod(driver.findElement(By.linkText("Workflow Creation")));

		driver.findElement(By.linkText("Workflow Creation")).click();
		//
		fluentWaitMethod(driver.findElement(By.className("plus")));

		driver.findElement(By.className("plus")).click();
		//
		fluentWaitMethod(driver.findElement(By.xpath("//*[@id='_name_id']")));

		if (!workflowName.equalsIgnoreCase("")) {
			driver.findElement(By.id("_name_id")).clear();
			driver.findElement(By.id("_name_id")).sendKeys(workflowName.trim());
			}

		if (!entityType.equalsIgnoreCase(""))
			new Select(driver.findElement(By.id("_entityTypeId_id"))).selectByVisibleText(entityType.trim());

		if (!filePath.equalsIgnoreCase("")) {
			driver.findElement(By.id("file")).sendKeys(System.getProperty("user.dir")+"\\workflows\\" + filePath);
			//
			}

		if (!workflowSupplier.equalsIgnoreCase("")) {
			if(driver.findElements(By.id("_relationId_id")).size()!=0)
				new Select(driver.findElement(By.id("_relationId_id"))).selectByVisibleText(workflowSupplier);
			}

		driver.findElement(By.className("submit")).findElement(By.tagName("input")).click();
		//

		if(driver.findElements(By.id("alertdialog")).size()!=0) {
			String entityErrors = driver.findElement(By.id("errors")).getText();

			if (entityErrors.contains("This name already exists in the system."))
				Logger.debug("For Workflow, " +workflowName+" -- This name already exists in the system.");

		//	Assert.fail(entityErrors);

			driver.get(CONFIG.getProperty("clientAdminURL"));
			return;
			}

		fluentWaitMethod(driver.findElement(By.className("success-icon")));
		String userAlert = driver.findElement(By.className("success-icon")).getText();

		Assert.assertEquals(userAlert, "Workflow created successfully");

		driver.findElement(By.cssSelector("button.ui-button.ui-widget.ui-state-default.ui-corner-all.ui-button-text-only")).click();

		Logger.debug("Test Client Admin Workflow Creation for -- " + workflowName + " -- is PASSED");
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