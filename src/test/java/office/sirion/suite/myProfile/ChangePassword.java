package office.sirion.suite.myProfile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import office.sirion.util.TestUtil;

import org.apache.commons.lang3.StringUtils;
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

import com.google.common.base.Splitter;

import testlink.api.java.client.TestLinkAPIException;
import testlink.api.java.client.TestLinkAPIResults;

public class ChangePassword extends TestSuiteBase {
	String runmodes[] = null;
	static int count = -1;
	static boolean isTestPass = true;
	String testCaseID=null;
	String result=null;

	@BeforeTest
	public void checkTestSkip() {
		if(!TestUtil.isTestCaseRunnable(my_profile_suite_xls,this.getClass().getSimpleName())) {
			Logger.debug("Skipping Test Case "+this.getClass().getSimpleName()+" as Runmode set to NO");
			throw new SkipException("Skipping Test Case "+this.getClass().getSimpleName()+" as Runmode set to NO");
			}
		runmodes = TestUtil.getDataSetRunmodes(my_profile_suite_xls, this.getClass().getSimpleName());
		}

	@Test (dataProvider="getTestData")
	public void UpdatePassword (String testCaseID, String oldPassword, String newPassword, String confirmPassword)
			throws InterruptedException, TestLinkAPIException {

		count++;
		if (!runmodes[count].equalsIgnoreCase("Y"))
			throw new SkipException("Runmode for Test Data -- is set to NO " +count);

		openBrowser();
	    endUserLogin(CONFIG.getProperty("endUserURL"), CONFIG.getProperty("endUserUsername"), CONFIG.getProperty("endUserPassword"));

	    Logger.debug("Executing Test Case: Change Password");

	    driver.get(CONFIG.getProperty("endUserURL"));
		//
	
		// Click on Account section
		driver.findElement(By.xpath("//li[@id='gl-account-section']")).click();
		Thread.sleep(2000);
	    driver.findElement(By.xpath("//div[@class='middles display_inline_block']")).click();
	    Thread.sleep(5000);
	    
	    
	    
	   // Click on Change Password Button 
	    driver.findElement(By.xpath("//input[@value='Change Password']")).click();
	    Thread.sleep(5000);
	    
	 //   addFieldValue("password_old", oldPassword.trim());
	    
	    driver.findElement(By.id("_currentPassword_1_id")).sendKeys(oldPassword);
	    driver.findElement(By.id("_newPassword_1_id")).sendKeys(newPassword);
	    driver.findElement(By.id("_repeatPassword_1_id")).sendKeys(confirmPassword);
	    
	    driver.findElement(By.className("submit")).findElement(By.id("proceed")).click();
	    
	    //Click on Update button of password page
	    driver.findElement(By.className("ui-dialog-buttonset")).findElement(By.className("ui-button-text")).click();
	    
    	driver.get(CONFIG.getProperty("endUserURL"));
    	Logger.debug("Password Updated Successfully");
    	}
	    
	
	@AfterMethod
	public void reportDataSetResult(ITestResult testResult) throws IOException {
		takeScreenShotOnFailure(testResult.getStatus(), this.getClass().getSimpleName());

		if(testResult.getStatus()==ITestResult.SKIP)
			TestUtil.reportDataSetResult(my_profile_suite_xls, this.getClass().getSimpleName(), count+2, "SKIP");
		else if(testResult.getStatus()==ITestResult.FAILURE) {
			isTestPass=false;
			TestUtil.reportDataSetResult(my_profile_suite_xls, this.getClass().getSimpleName(), count+2, "FAIL");
			result= "Fail";
			}
		else if (testResult.getStatus()==ITestResult.SUCCESS) {
			TestUtil.reportDataSetResult(my_profile_suite_xls, this.getClass().getSimpleName(), count+2, "PASS");
			result= "Pass";
			}
		}

	@AfterTest
	public void reportTestResult() {
		if (isTestPass)
			TestUtil.reportDataSetResult(my_profile_suite_xls, "Test Cases", TestUtil.getRowNum(my_profile_suite_xls, this.getClass().getSimpleName()), "PASS");
		else
			TestUtil.reportDataSetResult(my_profile_suite_xls, "Test Cases", TestUtil.getRowNum(my_profile_suite_xls, this.getClass().getSimpleName()), "FAIL");
		}

	@DataProvider
	public Object[][] getTestData() {
		return TestUtil.getData(my_profile_suite_xls, this.getClass().getSimpleName());
	}
}