package office.sirion.suite.myProfile;

import java.io.IOException;

import office.sirion.suite.manualReport.TestSuiteBase;
import office.sirion.util.TestUtil;
import office.sirion.util.TestlinkIntegration;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.testng.Assert;
import org.testng.ITestResult;
import org.testng.SkipException;
import org.testng.annotations.*;

import testlink.api.java.client.TestLinkAPIResults;

public class ClientAdminDelegation  extends TestSuiteBase {
	String runmodes[] = null;
	static int count = -1;
	static boolean isTestPass = true;
	String testCaseID=null;
	String result=null;

	  // Runmode of test case in a suite
	  @BeforeTest
	  public void checkTestSkip() {

	    if (!TestUtil.isTestCaseRunnable(my_profile_suite_xls, this.getClass().getSimpleName())) {
	      Logger.debug("Skipping Test Case" + this.getClass().getSimpleName() + " as runmode set to NO");// logs
	      throw new SkipException("Skipping Test Case" + this.getClass().getSimpleName() + " as runmode set to NO");// reports
	    }
	    // load the runmodes off the tests
	    runmodes = TestUtil.getDataSetRunmodes(my_profile_suite_xls, this.getClass().getSimpleName());
	  }
	  
	  @Test(dataProvider = "getTestData")
	  public void ClientAdmin(String testCaseID, String delegateUser, String entityType, String reportName, String fileToUpload) throws InterruptedException {

		  // test the runmode of current dataset
			count++;
			if(!runmodes[count].equalsIgnoreCase("Y"))
				throw new SkipException("Runmode for Test Data -- " +entityType +" set to NO " +count);

		    openBrowser();
			clientAdminLogin(CONFIG.getProperty("clientAdminURL"), CONFIG.getProperty("clientAdminUsername"), CONFIG.getProperty("clientAdminPassword"));
			
			Logger.debug("Executing Client Admin Delegation Test Case for -- " +entityType);
			
			driver.get(CONFIG.getProperty("clientAdminURL"));
			fluentWaitMethod(driver.findElement(By.linkText("User Administration")));

			driver.findElement(By.linkText("User Administration")).click();
			fluentWaitMethod(driver.findElement(By.linkText("User Configuration")));

			driver.findElement(By.className("dataTables_filter")).findElement(By.tagName("label")).findElement(By.tagName("input")).sendKeys(delegateUser);
			//

			driver.findElement(By.xpath(".//*[@id='l_com_sirionlabs_model_MasterGroup']/tbody/tr/td[1]/div[@sort='"+ delegateUser +"']/a")).click();
			driver.findElement(By.xpath("/html[1]/body[1]/div[4]/div[1]/div[1]/div[3]/div[2]/div[1]/div[1]/div[2]/div[1]/div[1]/div[4]/div[2]/table[1]/tbody[1]/div[@sort='"+ delegateUser +"']/td[1]/a")).click();
			
			
			driver.findElement(By.linkText("Delegation")).click();
			fluentWaitMethod(driver.findElement(By.className("plus")));
			
			driver.findElement(By.className("plus")).click();
				
			if (entityType.equalsIgnoreCase("Obligation")) {
				new Select(driver.findElement(By.id("_entityType.id_id"))).selectByVisibleText(entityType.trim());
				}
			
			if (!reportName.equalsIgnoreCase("")) {
				driver.findElement(By.id("_reportName_id")).clear();
				driver.findElement(By.id("_reportName_id")).sendKeys(reportName.trim());
				}
	  
	        if (!fileToUpload.equalsIgnoreCase("")) {
	        	driver.findElement(By.id("_multipartFile_id")).sendKeys(System.getProperty("user.dir") + "\\file-upload\\Manual Report\\" + fileToUpload);
	            //
	        }
		
	        if (!fileToUpload.equalsIgnoreCase("")) {
	        	driver.findElement(By.id("_multipartFile_id")).sendKeys(System.getProperty("user.dir") + "\\file-upload\\Manual Report\\" + fileToUpload);
	            //
	        }
	        
	        driver.findElement(By.className("submit")).findElement(By.tagName("input")).click();;
			//
		
			// When no entity is selected
			if (entityType.equalsIgnoreCase("")) {
				String entityErrors = driver.findElement(By.className("formErrorContent")).getText();
				Assert.assertEquals(entityErrors, "This field is required");
				Logger.debug("Entity Type Name is Mandatory");
				
				driver.get(CONFIG.getProperty("clientAdminURL"));
			    return;
			    }
			
			// When report name is more than 256 characters
			if (reportName.length() > 256) {
				String entityErrors = driver.findElement(By.className("formErrorContent")).getText();

				Assert.assertEquals(entityErrors, "Maximum 256 characters allowed");
				Logger.debug("For Report Name, -- Maximum 256 characters allowed");

				driver.get(CONFIG.getProperty("clientAdminURL"));
			    return;
			    }
			
			// When report name already exists for any entity
			if (!fileToUpload.equalsIgnoreCase("")){
			if(driver.findElements(By.id("alertdialog")).size()!=0) {
				String entityErrors = driver.findElement(By.id("errors")).getText();

				Assert.assertEquals(entityErrors, "This name already exists in the system.");
				Logger.debug("For Entity, " +reportName+" -- This name already exists in the system.");

				driver.get(CONFIG.getProperty("clientAdminURL"));
				return;
				}}
		
			// When no report file is attached
			if (fileToUpload.equalsIgnoreCase("")) {
			if(driver.findElements(By.id("alertdialog")).size()!=0) {
				String entityErrors = driver.findElement(By.id("errors")).getText();

				Assert.assertEquals(entityErrors, "No Report File attached");
				Logger.debug("For Entity, " +reportName+" -- No Report File attached.");

				driver.get(CONFIG.getProperty("clientAdminURL"));
				return;
				}}
			
			driver.get(CONFIG.getProperty("clientAdminURL"));
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
	      TestUtil.reportDataSetResult(my_profile_suite_xls, "Test Cases", TestUtil.getRowNum(my_profile_suite_xls, this.getClass().getSimpleName()), "PASS");
	    else
	      TestUtil.reportDataSetResult(my_profile_suite_xls, "Test Cases", TestUtil.getRowNum(my_profile_suite_xls, this.getClass().getSimpleName()), "FAIL");

	  }

	  @DataProvider
	  public Object[][] getTestData() {
	    return TestUtil.getData(my_profile_suite_xls, this.getClass().getSimpleName());
	  }
}
