package office.sirion.suite.clientAdmin;

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

public class UpdateGeography extends TestSuiteBase {
	String runmodes[]=null;
	static int count=-1;
	static boolean fail=true;
	static boolean skip=false;
	static boolean isTestPass=true;
	String testCaseID=null;
	String result=null;

	@BeforeTest
	public void checkTestSkip() {
		if(!TestUtil.isTestCaseRunnable(client_admin_suite_xls,this.getClass().getSimpleName())) {
			Logger.debug("Skipping Test Case "+this.getClass().getSimpleName()+" as Runmode set to NO");
			throw new SkipException("Skipping Test Case "+this.getClass().getSimpleName()+" as Runmode set to NO");
			}
		runmodes = TestUtil.getDataSetRunmodes(client_admin_suite_xls, this.getClass().getSimpleName());
		}
	
	@Test (dataProvider="getTestData")
	public void UpdateGeographyTest (String geographyType, String geographyTypeUpdate, String geographyName, String geographyNameUpdate, String geographyCurrency,
			String geographyTimezone, String geographyActive) throws InterruptedException {
		count++;
		if(!runmodes[count].equalsIgnoreCase("Y")) {
			skip=true;
			throw new SkipException("Runmode for Test Data -- " +geographyNameUpdate +" set to NO " +count);
			}
		openBrowser();
		clientAdminLogin(CONFIG.getProperty("clientAdminURL"), CONFIG.getProperty("clientAdminUsername"), CONFIG.getProperty("clientAdminPassword"));
		
		Logger.debug("Executing Client Admin Geography Update Test -- "+geographyNameUpdate);
		
		driver.get(CONFIG.getProperty("clientAdminURL"));
		fluentWaitMethod(driver.findElement(By.linkText("Geography")));
		
		driver.findElement(By.linkText("Geography")).click();
		
		driver.findElement(By.className("dataTables_filter")).findElement(By.tagName("label")).findElement(By.tagName("input")).sendKeys(geographyName);
		Thread.sleep(2000);
		
		driver.findElement(By.xpath(".//*[@id='l_com_sirionlabs_model_RegionCountryStructure']/tbody/tr/td[1]/a/div[@sort='"+ geographyName +"']")).click();
		
        String entityTypeShowPage = driver.findElement(By.id("_s_com_sirionlabs_model_RegionCountryStructure_type_type_id")).getText();
        Assert.assertEquals(entityTypeShowPage, geographyType, "Geography Type at show page is -- " +entityTypeShowPage+ " instead of -- " +geographyType);

        String entityTypeNameShowPage = driver.findElement(By.id("_s_com_sirionlabs_model_RegionCountryStructure_name_name_id")).getText();
        Assert.assertEquals(entityTypeNameShowPage, geographyName, "Geography Name at show page is -- " +entityTypeNameShowPage+ " instead of -- " +geographyName);

        driver.findElement(By.className("submit")).findElement(By.tagName("input")).click();
		fluentWaitMethod(driver.findElement(By.id("_name_id")));
        
		if (!geographyTypeUpdate.equalsIgnoreCase("")) {
			new Select(driver.findElement(By.id("_type_id"))).selectByVisibleText(geographyTypeUpdate.trim());
			}

		if (!geographyNameUpdate.equalsIgnoreCase("")) {
			driver.findElement(By.id("_name_id")).clear();
			driver.findElement(By.id("_name_id")).sendKeys(geographyNameUpdate.trim());
			}

		if (!geographyCurrency.equalsIgnoreCase("")) {
			new Select(driver.findElement(By.id("_currency.id_id"))).selectByVisibleText(geographyCurrency.trim());
			}

		if (!geographyTimezone.equalsIgnoreCase("")) {
			new Select(driver.findElement(By.id("_timeZone.id_id"))).selectByVisibleText(geographyTimezone.trim());
			}

        if(geographyTypeUpdate.equalsIgnoreCase("LOCAL")) {
    		if (geographyActive.equalsIgnoreCase("Yes")) {
    			if (!driver.findElement(By.id("_active_id")).isSelected())
    				driver.findElement(By.id("_active_id")).click();
    			} else if (geographyActive.equalsIgnoreCase("No")) {
    				if (driver.findElement(By.id("_active_id")).isSelected())
    					driver.findElement(By.id("_active_id")).click();
    				}
    		}
        
		driver.findElement(By.id("proceed")).click();
		//
        
		if(driver.findElements(By.id("alertdialog")).size()!=0) {
			fail = true;			
			String entityErrors = driver.findElement(By.id("errors")).getText();
			
			if (entityErrors.contains("This name already exists in the system."))
				Logger.debug("For Geography, " +geographyName+" --- This name already exists in the system.");
			if (entityErrors.contains("More than one global structure is not allowed."))
				Logger.debug("For Geography, More than one global structure is not allowed.");
									
			fail=false;
			driver.get(CONFIG.getProperty("clientAdminURL"));

			return;
			}

        String entityTypeUpdateShowPage = driver.findElement(By.id("_s_com_sirionlabs_model_RegionCountryStructure_type_type_id")).getText();
        Assert.assertEquals(entityTypeUpdateShowPage, geographyTypeUpdate, "Geography Type at show page is -- " +entityTypeUpdateShowPage+ " instead of -- " +geographyTypeUpdate);

        String entityTypeNameUpdateShowPage = driver.findElement(By.id("_s_com_sirionlabs_model_RegionCountryStructure_name_name_id")).getText();
        Assert.assertEquals(entityTypeNameUpdateShowPage, geographyNameUpdate, "Geography Name at show page is -- " +entityTypeNameUpdateShowPage+ " instead of -- " +geographyNameUpdate);

        Logger.debug("Geography updated successfully, with Name -- " + geographyNameUpdate + " and Type --- " + geographyTypeUpdate);
        
		fail=false;
        driver.get(CONFIG.getProperty("clientAdminURL"));
		}
	
	@AfterMethod
	public void reportDataSetResult(ITestResult testResult) throws IOException {
	   takeScreenShotOnFailure(testResult.getStatus(), this.getClass().getSimpleName());
	   
	   if(testResult.getStatus()==ITestResult.SKIP)
	      TestUtil.reportDataSetResult(client_admin_suite_xls, this.getClass().getSimpleName(), count+2, "SKIP");
	   else if(testResult.getStatus()==ITestResult.FAILURE) {
	      isTestPass=false;
	      TestUtil.reportDataSetResult(client_admin_suite_xls, this.getClass().getSimpleName(), count+2, "FAIL");
	      result= "Fail";
	      }
	   else if (testResult.getStatus()==ITestResult.SUCCESS) {
	      TestUtil.reportDataSetResult(client_admin_suite_xls, this.getClass().getSimpleName(), count+2, "PASS");
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
		if(isTestPass)
			TestUtil.reportDataSetResult(client_admin_suite_xls, "Test Cases", TestUtil.getRowNum(client_admin_suite_xls,this.getClass().getSimpleName()), "PASS");
		else
			TestUtil.reportDataSetResult(client_admin_suite_xls, "Test Cases", TestUtil.getRowNum(client_admin_suite_xls,this.getClass().getSimpleName()), "FAIL");
		}
	
	@DataProvider
	public Object[][] getTestData() {
		return TestUtil.getData(client_admin_suite_xls, this.getClass().getSimpleName());
		}
	}