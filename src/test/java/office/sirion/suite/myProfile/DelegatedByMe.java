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

public class DelegatedByMe extends TestSuiteBase {
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
	public void Delegation (String testCaseID)
			throws InterruptedException, TestLinkAPIException {

		count++;
		if (!runmodes[count].equalsIgnoreCase("Y"))
			throw new SkipException("Runmode for Test Data -- is set to NO " +count);

		openBrowser();
	    endUserLogin(CONFIG.getProperty("endUserURL"), CONFIG.getProperty("endUserUsername"), CONFIG.getProperty("endUserPassword"));

	    Logger.debug("Executing Test Case: Delegated By Me");

	    driver.get(CONFIG.getProperty("endUserURL"));
		//
	
		// Click on Account section
		driver.findElement(By.xpath("//li[@id='gl-account-section']")).click();
		Thread.sleep(2000);
	    driver.findElement(By.xpath("//div[@class='middles display_inline_block']")).click();
	    Thread.sleep(5000);
	    //Click on delegation link
	    driver.findElement(By.xpath("/html[1]/body[1]/div[4]/div[1]/ng-view[1]/div[1]/div[3]/div[2]/div[1]/div[2]/div[2]/ul[1]/div[1]/div[1]/div[1]/li[2]/a[1]")).click();
	    Thread.sleep(5000);
	    
	    
	    //Click on the first entity id in delegated by me section
	    String delegatedByMe = driver.findElement(By.xpath("/html[1]/body[1]/div[4]/div[1]/ng-view[1]/div[1]/div[3]/div[2]/div[1]/div[2]/div[2]/div[1]/div[1]/div[1]/div[1]/div[4]/div[2]/table[1]/tbody[1]/tr[1]/td[2]/a[1]")).getText();

	    String delegatedByMeSplit = delegatedByMe.substring(0,2);
    
	    driver.findElement(By.xpath("/html[1]/body[1]/div[4]/div[1]/ng-view[1]/div[1]/div[3]/div[2]/div[1]/div[2]/div[2]/div[1]/div[1]/div[1]/div[1]/div[4]/div[2]/table[1]/tbody[1]/tr[1]/td[2]/a[1]")).click();
	    
	    Thread.sleep(5000);

	    String showPageId = null;
	    if (delegatedByMeSplit.equalsIgnoreCase("RL")){
	    showPageId = driver.findElement(By.id("elem_6018")).getText();
	    }
	    
	    if (delegatedByMeSplit.equalsIgnoreCase("SP")){
	    showPageId = driver.findElement(By.id("elem_501")).getText();
	    }
	    
	    if (delegatedByMeSplit.equalsIgnoreCase("CO")){
	    showPageId = driver.findElement(By.id("elem_98")).getText();
	    }
	    
	    if (delegatedByMeSplit.equalsIgnoreCase("PO")){
	    showPageId = driver.findElement(By.id("elem_11443")).getText();
	    }
	       
	    if (delegatedByMeSplit.equalsIgnoreCase("SL")){
	    showPageId = driver.findElement(By.id("elem_285")).getText();
	    }
	    
	    if (delegatedByMeSplit.equalsIgnoreCase("AC")){
	    showPageId = driver.findElement(By.id("elem_443")).getText();
	    }
	    
	    if (delegatedByMeSplit.equalsIgnoreCase("IS")){
	    showPageId = driver.findElement(By.id("elem_148")).getText();
	    }
	    
	    if (delegatedByMeSplit.equalsIgnoreCase("DS")){
	    showPageId = driver.findElement(By.id("elem_11179")).getText();
	    }
	    
	    if (delegatedByMeSplit.equalsIgnoreCase("CR")){
	    showPageId = driver.findElement(By.id("elem_841")).getText();
	    }
	    
	    if (delegatedByMeSplit.equalsIgnoreCase("IP")){
	    showPageId = driver.findElement(By.id("elem_746")).getText();
	    }

	    
	    Assert.assertEquals(delegatedByMe, showPageId);
	    
	    Logger.debug("Delegated to me open succesfully for entity id -- " +delegatedByMe);
	    
	    
    	driver.get(CONFIG.getProperty("endUserURL"));
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