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

public class GeneralTab extends TestSuiteBase {
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
	public void Delegation (String testCaseID, String generalFirstName, String	generalLastName, String	generalContactNumber,
			String generalSecurityQuestion, String	generalAnswer, String	generalLanguage, String	generalTimeZone, 
			String	generalDefaultTier, String	generalCurrentTier, String	generalShowContractDocument, String	generalEditContractDocument, 
			String	generalFontSize, String	generalDefaultLandingPage)
			throws InterruptedException, TestLinkAPIException {

		count++;
		if (!runmodes[count].equalsIgnoreCase("Y"))
			throw new SkipException("Runmode for Test Data -- is set to NO " +count);

		openBrowser();
	    endUserLogin(CONFIG.getProperty("endUserURL"), CONFIG.getProperty("endUserUsername"), CONFIG.getProperty("endUserPassword"));

	    Logger.debug("Executing Test Case General Tab for user" +generalFirstName);

	    driver.get(CONFIG.getProperty("endUserURL"));
		//
	
		// Click on Account section
		driver.findElement(By.xpath("//li[@id='gl-account-section']")).click();
		Thread.sleep(15000);
	    driver.findElement(By.xpath("//div[@class='middles display_inline_block']")).click();
	    //
	   
	    //Account Information
	    //addFieldValue("end_user_ge_first_name_textarea", generalFirstName.trim());
	    driver.findElement(By.xpath("//input[@id='_firstName_id']")).clear();
	    driver.findElement(By.xpath("//input[@id='_firstName_id']")).sendKeys(generalFirstName);
	    
	    driver.findElement(By.xpath("//input[@id='_lastName_id']")).clear();
	    driver.findElement(By.xpath("//input[@id='_lastName_id']")).sendKeys(generalLastName);
	    
	    driver.findElement(By.xpath("//input[@id='_contactNo_id']")).clear();
	    driver.findElement(By.xpath("//input[@id='_contactNo_id']")).sendKeys(generalContactNumber);
	    
	    new Select(driver.findElement(By.id("_questionId_id"))).selectByVisibleText(generalSecurityQuestion);
	    
	    driver.findElement(By.xpath("//input[@id='_answer_id']")).clear();
	    driver.findElement(By.xpath("//input[@id='_answer_id']")).sendKeys(generalAnswer);
	    
	    new Select(driver.findElement(By.id("_language.id_id"))).selectByVisibleText(generalLanguage);
	    
	    new Select(driver.findElement(By.id("_timeZone.id_id"))).selectByVisibleText(generalTimeZone);
	    
	    new Select(driver.findElement(By.id("_tierId_id"))).selectByVisibleText(generalDefaultTier);
	    
	    new Select(driver.findElement(By.id("_sessionTierId_id"))).selectByVisibleText(generalCurrentTier);
	    
		if (generalShowContractDocument.equalsIgnoreCase("Yes")) {
			driver.findElement(By.xpath("//input[@id='_showContractDocumentOnShowpage_id']")).click();
			}
		
		if (generalEditContractDocument.equalsIgnoreCase("Yes")) {
			driver.findElement(By.xpath("//input[@id='_showContractDocumentOnEditpage_id']")).click();
			}
	    
		new Select(driver.findElement(By.id("_fontSize_id"))).selectByVisibleText(generalFontSize);
		
		new Select(driver.findElement(By.id("_defaultHomePage_id"))).selectByVisibleText(generalDefaultLandingPage);
		
	    Thread.sleep(5000);
	    
	    //Click on update button
	    driver.findElement(By.xpath("//input[@id='proceed']")).click();
	    
    	driver.get(CONFIG.getProperty("endUserURL"));
    	Logger.debug("Executing Test Case General Tab for user");
    	
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