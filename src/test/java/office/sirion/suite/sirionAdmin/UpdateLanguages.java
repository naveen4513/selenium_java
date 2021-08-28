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

public class UpdateLanguages extends TestSuiteBase {
	String runmodes[]=null;
	static int count=-1;
	static boolean isTestPass=true;
	String testCaseID=null;
	String result=null;

	@BeforeTest
	public void checkTestSkip() {
		if(!TestUtil.isTestCaseRunnable(sirion_admin_suite_xls,this.getClass().getSimpleName())) {
			Logger.debug("Skipping Test Case "+this.getClass().getSimpleName()+" as Runmode set to NO");
			throw new SkipException("Skipping Test Case "+this.getClass().getSimpleName()+" as Runmode set to NO");
			}
		runmodes = TestUtil.getDataSetRunmodes(sirion_admin_suite_xls, this.getClass().getSimpleName());
		}

	@Test (dataProvider="getTestData")
	public void UpdateLanguagesTest (String languagesName, String languagesFile
			) throws InterruptedException {

		count++;
		if(!runmodes[count].equalsIgnoreCase("Y"))
			throw new SkipException("Runmode for Test Data -- " +languagesName +" set to NO " +count);

		openBrowser();
		sirionAdminLogin(CONFIG.getProperty("sirionAdminURL"), CONFIG.getProperty("sirionAdminUsername"), CONFIG.getProperty("sirionAdminPassword"));

		Logger.debug("Executing Test Sirion Admin Languages Update for -- "+languagesName);

		driver.get(CONFIG.getProperty("sirionAdminURL"));
		//
		fluentWaitMethod(driver.findElement(By.linkText("Languages")));

		driver.findElement(By.linkText("Languages")).click();
		//
		fluentWaitMethod(driver.findElement(By.className("dataTables_filter")).findElement(By.tagName("label")).findElement(By.tagName("input")));
		
		driver.findElement(By.className("dataTables_filter")).findElement(By.tagName("label")).findElement(By.tagName("input")).sendKeys(languagesName);
		//
        
        driver.findElement(By.xpath(".//*[@id='l_com_sirionlabs_model_MasterGroup']/tbody/tr[@role='row']/td[contains(.,'"+ languagesName +"')]/preceding-sibling::td[1]/a")).click();

        String entityTypeShowPage = driver.findElement(By.id("_c_com_sirionlabs_model_language_name_name_id")).getText();
        Assert.assertEquals(entityTypeShowPage, languagesName, "Languages Name at show page is -- " +entityTypeShowPage+ " instead of -- " +languagesName);

		if (!languagesFile.equalsIgnoreCase("")) {
			driver.findElement(By.id("_multipartFile_id")).clear();
			driver.findElement(By.id("_multipartFile_id")).sendKeys(System.getProperty("user.dir") + "//" + languagesFile);
			//
			}
		
		driver.findElement(By.xpath(".//*[@id='tabs-inner-sec']/div[2]/div/input")).click();
		//
		
		if(driver.findElements(By.id("alertdialog")).size()!=0) {
			String entityErrors = driver.findElement(By.id("errors")).getText();
			
			if (entityErrors.contains("Please upload file"))
				Logger.debug("For Language, " +languagesName+" --- Please upload file");
			if (entityErrors.contains("Error occurred while parsing workbook."))
				Logger.debug("For Language, " +languagesName+" --- Error occurred while parsing workbook.");
			if (entityErrors.contains("All system label entries not present in workbook."))
				Logger.debug("For Language, " +languagesName+" --- All system label entries not present in workbook.");		
						
			driver.get(CONFIG.getProperty("sirionAdminURL"));
			return;
			}
		
		driver.findElement(By.className("dataTables_filter")).findElement(By.tagName("label")).findElement(By.tagName("input")).sendKeys(languagesName);
		//
        
        driver.findElement(By.xpath(".//*[@id='l_com_sirionlabs_model_MasterGroup']/tbody/tr[@role='row']/td[contains(.,'"+ languagesName +"')]/preceding-sibling::td[1]/a")).click();

        String entityTypeUpdateShowPage = driver.findElement(By.id("_c_com_sirionlabs_model_language_name_name_id")).getText();
        Assert.assertEquals(entityTypeUpdateShowPage, languagesName, "Languages Name at show page is -- " +entityTypeUpdateShowPage+ " instead of -- " +languagesName);

		Logger.debug("Test Sirion Admin Languages Update for -- " + languagesName + " is PASSED");
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