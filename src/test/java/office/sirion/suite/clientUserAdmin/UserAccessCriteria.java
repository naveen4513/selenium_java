package office.sirion.suite.clientUserAdmin;

import java.io.IOException;

import office.sirion.util.TestUtil;
import office.sirion.util.TestlinkIntegration;

import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.testng.ITestResult;
import org.testng.SkipException;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import testlink.api.java.client.TestLinkAPIResults;

public class UserAccessCriteria extends TestSuiteBase {
	String runmodes[]=null;
	static int count=-1;
	static boolean isTestPass=true;
	String testCaseID=null;
	String result=null;

	@BeforeTest
	public void checkTestSkip() {
		if(!TestUtil.isTestCaseRunnable(client_user_admin_suite_xls,this.getClass().getSimpleName())) {
			Logger.debug("Skipping Test Case "+this.getClass().getSimpleName()+" as Runmode set to NO");
			throw new SkipException("Skipping Test Case "+this.getClass().getSimpleName()+" as Runmode set to NO");
			}
		runmodes = TestUtil.getDataSetRunmodes(client_user_admin_suite_xls, this.getClass().getSimpleName());
		}
	
	@Test (dataProvider="getTestData")
	public void UserAccessCriteriaCreation (String userAccessCriteriaName, String userAccessCriteriaActive, String userAccessCriteriaSystemAccess) throws InterruptedException {
		count++;
		if(!runmodes[count].equalsIgnoreCase("Y"))
			throw new SkipException("Runmode for Test Data -- User Access Criteria -- set to NO " +count);
		
		openBrowser();
		clientAdminLogin(CONFIG.getProperty("clientAdminURL"), CONFIG.getProperty("clientAdminUsername"), CONFIG.getProperty("clientAdminPassword"));
		
		Logger.debug("Executing Client Admin User Access Criteria Test -- User Access Criteria");
		
		driver.get(CONFIG.getProperty("clientAdminURL"));
		fluentWaitMethod(driver.findElement(By.linkText("User Administration")));

		driver.findElement(By.linkText("User Administration")).click();
		fluentWaitMethod(driver.findElement(By.linkText("User Access Criteria")));

		driver.findElement(By.linkText("User Access Criteria")).click();
		fluentWaitMethod(driver.findElement(By.className("plus")));
		
		driver.findElement(By.className("plus")).click();
		fluentWaitMethod(driver.findElement(By.id("_name_id")));
		
		if (!userAccessCriteriaName.equalsIgnoreCase("")) {
			driver.findElement(By.id("_name_id")).clear();
			driver.findElement(By.id("_name_id")).sendKeys(userAccessCriteriaName.trim());
			}
		
		if (userAccessCriteriaActive.equalsIgnoreCase("Yes")) {
			if (!driver.findElement(By.id("_active_id")).isSelected())
				driver.findElement(By.id("_active_id")).click();
			}
		
		if (userAccessCriteriaSystemAccess.equalsIgnoreCase("Yes")) {
			if (!driver.findElement(By.name("dataAccesses[0].readAdminAccess")).isSelected())
				driver.findElement(By.name("dataAccesses[0].readAdminAccess")).click();

			if (!driver.findElement(By.name("dataAccesses[0].writeAdminAccess")).isSelected())
				driver.findElement(By.name("dataAccesses[0].writeAdminAccess")).click();
			}
		
		driver.findElement(By.id("checkPost")).click();
		//

		if(driver.findElements(By.id("alertdialog")).size()!=0) {
			String entityErrors = driver.findElement(By.id("errors")).getText();
			
			if (entityErrors.contains("This name already exists in the system."))
				Logger.debug("For User Access Criteria, " +userAccessCriteriaName+" --- This name already exists in the system.");
									
			driver.get(CONFIG.getProperty("clientAdminURL"));
			return;
			}
/*		
		String entityTypeShowPage = driver.findElement(By.id("_name_id")).getText();
        Assert.assertEquals(entityTypeShowPage, userAccessCriteriaName, "User Access Criteria at show page is -- " +entityTypeShowPage+ " instead of -- " +userAccessCriteriaName);
*/        
        Logger.debug("User Access Criteria created successfully, with Name -- " + userAccessCriteriaName);

		driver.get(CONFIG.getProperty("clientAdminURL"));
		}

	@AfterMethod
	public void reportDataSetResult(ITestResult testResult) throws IOException {
	   takeScreenShotOnFailure(testResult.getStatus(), this.getClass().getSimpleName());
	   
	   if(testResult.getStatus()==ITestResult.SKIP)
	      TestUtil.reportDataSetResult(client_user_admin_suite_xls, this.getClass().getSimpleName(), count+2, "SKIP");
	   else if(testResult.getStatus()==ITestResult.FAILURE) {
	      isTestPass=false;
	      TestUtil.reportDataSetResult(client_user_admin_suite_xls, this.getClass().getSimpleName(), count+2, "FAIL");
	      result= "Fail";
	      }
	   else if (testResult.getStatus()==ITestResult.SUCCESS) {
	      TestUtil.reportDataSetResult(client_user_admin_suite_xls, this.getClass().getSimpleName(), count+2, "PASS");
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
			TestUtil.reportDataSetResult(client_user_admin_suite_xls, "Test Cases", TestUtil.getRowNum(client_user_admin_suite_xls, this.getClass().getSimpleName()), "PASS");
		else
			TestUtil.reportDataSetResult(client_user_admin_suite_xls, "Test Cases", TestUtil.getRowNum(client_user_admin_suite_xls, this.getClass().getSimpleName()), "FAIL");
		}

	@DataProvider
	public Object[][] getTestData() {
		return TestUtil.getData(client_user_admin_suite_xls, this.getClass().getSimpleName());
		}
	}