package office.sirion.suite.sirionAdmin;

import java.io.IOException;

import office.sirion.util.TestUtil;
import office.sirion.util.TestlinkIntegration;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
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

public class UpdateFunction extends TestSuiteBase {
	String runmodes[]=null;
	static int count=-1;
	static boolean isTestPass=true;
	String testCaseID=null;
	String result=null;

	@BeforeTest
	public void checkTestSkip() {
		if(!TestUtil.isTestCaseRunnable(sirion_admin_suite_xls,this.getClass().getSimpleName())) {
			Logger.debug("Skipping Test Case "+this.getClass().getSimpleName()+" as runmode set to NO");
			throw new SkipException("Skipping Test Case "+this.getClass().getSimpleName()+" as runmode set to NO");
			}
		runmodes=TestUtil.getDataSetRunmodes(sirion_admin_suite_xls, this.getClass().getSimpleName());
		}

	@Test(dataProvider="getTestData")
	public void UpdateFunctionTest(String function, String functionUpdate, String functionAlias, String functionAliasUpdate) throws InterruptedException {
		count++;
		if(!runmodes[count].equalsIgnoreCase("Y"))
			throw new SkipException("Runmode for Test Data -- " +function +"-- set to NO " +count);
	
		openBrowser();
		sirionAdminLogin(CONFIG.getProperty("sirionAdminURL"), CONFIG.getProperty("sirionAdminUsername"), CONFIG.getProperty("sirionAdminPassword"));
		
		Logger.debug("Executing Sirion Admin Function Update Test -- "+function);
		
		driver.get(CONFIG.getProperty("sirionAdminURL"));
		fluentWaitMethod(driver.findElement(By.linkText("Function")));

		driver.findElement(By.linkText("Function")).click();
		Thread.sleep(2000);

		driver.findElement(By.className("dataTables_filter")).findElement(By.tagName("label")).findElement(By.tagName("input")).sendKeys(function);
		Thread.sleep(2000);
        
        driver.findElement(By.xpath(".//*[@id='l_com_sirionlabs_model_MasterContractType']/tbody/tr/td[1]/a/div[@sort='"+ function +"']")).click();

        String entityTypeShowPage = driver.findElement(By.id("_s_com_sirionlabs_model_MasterContractType_name_name_id")).getText();
        Assert.assertEquals(entityTypeShowPage, function, "Function Name at show page is -- " +entityTypeShowPage+ " instead of -- " +function);
                
        String entityTypeAliasShowPage = driver.findElement(By.id("_c_com_sirionlabs_model_MasterContractType_alias_alias_id")).getText();
        Assert.assertEquals(entityTypeAliasShowPage, functionAlias, "Function Alias at show page is -- " +entityTypeAliasShowPage+ " instead of -- " +functionAlias);
        
        driver.findElement(By.className("submit")).findElement(By.tagName("input")).click();
		fluentWaitMethod(driver.findElement(By.id("_name_id")));
        
		if (!functionUpdate.equalsIgnoreCase("")) {
			driver.findElement(By.id("_name_id")).clear();
			driver.findElement(By.id("_name_id")).sendKeys(functionUpdate.trim());
			}
		
		if (!functionAliasUpdate.equalsIgnoreCase("")) {
			driver.findElement(By.id("_alias_id")).clear();
			driver.findElement(By.id("_alias_id")).sendKeys(functionAliasUpdate.trim());
			}

		driver.findElement(By.id("proceed")).click();
		//
		
		if (functionUpdate.length() > 128) {
			WebElement e = driver.findElement(By.cssSelector("div._name_idformError.parentFormmasterContractType.formError"));
			String entityErrors = e.findElement(By.className("formErrorContent")).getText();
			
			Assert.assertEquals(entityErrors, "Maximum 128 characters allowed");
			Logger.debug("For Function, --- Maximum 128 characters allowed");
			
		    driver.get(CONFIG.getProperty("sirionAdminURL"));
		    return;
		    }
			
		if (functionAliasUpdate.length() > 5) {
			WebElement e = driver.findElement(By.cssSelector("div._alias_idformError.parentFormmasterContractType.formError"));
			String entityErrors = e.findElement(By.className("formErrorContent")).getText();
			
			Assert.assertEquals(entityErrors, "Maximum 5 characters allowed");
			Logger.debug("For Function Alias, --- Maximum 5 characters allowed");
			
		    driver.get(CONFIG.getProperty("sirionAdminURL"));
		    return;
		    }

		if(driver.findElements(By.id("alertdialog")).size()!=0) {
			String entityErrors = driver.findElement(By.id("errors")).getText();
			
			if (entityErrors.contains("This name already exists in the system."))
				Logger.debug("For Function, " +functionUpdate+" --- This name already exists in the system.");
			if (entityErrors.contains("This alias already exists in the system."))
				Logger.debug("For Function Alias, " +functionAliasUpdate+" --- This alias already exists in the system.");
									
			driver.get(CONFIG.getProperty("sirionAdminURL"));
			return;
			}

        String entityTypeUpdateShowPage = driver.findElement(By.id("_s_com_sirionlabs_model_MasterContractType_name_name_id")).getText();
        Assert.assertEquals(entityTypeUpdateShowPage, functionUpdate, "Function Name at show page is -- " +entityTypeUpdateShowPage+ " instead of -- " +functionUpdate);

        String entityTypeAliasUpdateShowPage = driver.findElement(By.id("_c_com_sirionlabs_model_MasterContractType_alias_alias_id")).getText();
        Assert.assertEquals(entityTypeAliasUpdateShowPage, functionAliasUpdate, "Function Alias at show page is -- " +entityTypeAliasUpdateShowPage+ 
        		" instead of -- " +functionAliasUpdate);

        Logger.debug("Function updated successfully, with Name -- " +functionUpdate +", and Alias -- "+functionAliasUpdate);
        
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