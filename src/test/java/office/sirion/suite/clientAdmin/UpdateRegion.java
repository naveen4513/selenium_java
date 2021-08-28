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

public class UpdateRegion extends TestSuiteBase {
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
	public void UpdateRegionTest (String geographyName, String regionName, String regionCurrency, String regionTimezone, String regionNameUpdate,
			String regionActive, String regionCountries, String regionAlias) throws InterruptedException {
		count++;
		if(!runmodes[count].equalsIgnoreCase("Y")) {
			skip=true;
			throw new SkipException("Runmode for Test Data -- " +regionName +" set to NO " +count);
			}
		openBrowser();
		clientAdminLogin(CONFIG.getProperty("clientAdminURL"), CONFIG.getProperty("clientAdminUsername"), CONFIG.getProperty("clientAdminPassword"));
		
		Logger.debug("Executing Client Admin Region Update Test -- "+regionName);
		
		driver.get(CONFIG.getProperty("clientAdminURL"));
		fluentWaitMethod(driver.findElement(By.linkText("Geography")));
		
		driver.findElement(By.linkText("Geography")).click();
		
		driver.findElement(By.className("dataTables_filter")).findElement(By.tagName("label")).findElement(By.tagName("input")).sendKeys(geographyName);
		Thread.sleep(2000);
		
		driver.findElement(By.xpath(".//*[@id='l_com_sirionlabs_model_RegionCountryStructure']/tbody/tr/td[1]/a/div[@sort='"+ geographyName +"']")).click();
		
        String entityTypeShowPage = driver.findElement(By.id("_s_com_sirionlabs_model_RegionCountryStructure_name_name_id")).getText();
        Assert.assertEquals(entityTypeShowPage, geographyName, "Geography Name at show page is -- " +entityTypeShowPage+ " instead of -- " +geographyName);
        
        driver.findElement(By.id("ui-id-2")).click();
        
		driver.findElement(By.className("dataTables_filter")).findElement(By.tagName("label")).findElement(By.tagName("input")).sendKeys(regionName);
		Thread.sleep(2000);
		
		driver.findElement(By.xpath(".//*[@id='l_com_sirionlabs_model_MasterRegion']/tbody/tr/td[1]/a/div[@sort='"+ regionName +"']")).click();
		
        String entityTypeNameShowPage = driver.findElement(By.id("_s_com_sirionlabs_model_MasterRegion_name_name_id")).getText();
        Assert.assertEquals(entityTypeNameShowPage, regionName, "Region Name at show page is -- " +entityTypeNameShowPage+ " instead of -- " +regionName);

        driver.findElement(By.className("submit")).findElement(By.tagName("input")).click();
		fluentWaitMethod(driver.findElement(By.id("_name_id")));

		if (!regionCurrency.equalsIgnoreCase("")) {
			new Select(driver.findElement(By.id("_currency.id_id"))).selectByVisibleText(regionCurrency.trim());
			}
        
		if (!regionTimezone.equalsIgnoreCase("")) {
			new Select(driver.findElement(By.id("_timeZone.id_id"))).selectByVisibleText(regionTimezone.trim());
			}
        
		if (!regionNameUpdate.equalsIgnoreCase("")) {
			driver.findElement(By.id("_name_id")).clear();
			driver.findElement(By.id("_name_id")).sendKeys(regionNameUpdate.trim());
			}
		
		if (regionActive.equalsIgnoreCase("Yes")) {
			if (!driver.findElement(By.id("_active_id")).isSelected())
				driver.findElement(By.id("_active_id")).click();
		} else if (regionActive.equalsIgnoreCase("No")) {
			if (driver.findElement(By.id("_active_id")).isSelected())
				driver.findElement(By.id("_active_id")).click();
		}

		if(!regionCountries.equalsIgnoreCase("")) {
			for(String entityData : regionCountries.split(";")) {
		        new Select(driver.findElement(By.id("_countries_id"))).selectByVisibleText(entityData.trim());
		        }
			}
        
		if (!regionAlias.equalsIgnoreCase("")) {
			driver.findElement(By.id("_alias_id")).clear();
			driver.findElement(By.id("_alias_id")).sendKeys(regionAlias.trim());
			}

		driver.findElement(By.id("proceed")).click();
		//

		if(driver.findElements(By.id("alertdialog")).size()!=0) {
			fail = true;			
			String entityErrors = driver.findElement(By.id("errors")).getText();
			
			if (entityErrors.contains("This name already exists in the system."))
				Logger.debug("For Region, " +regionNameUpdate+" --- This name already exists in the system.");
									
			fail=false;
			driver.get(CONFIG.getProperty("clientAdminURL"));

			return;
			}
		
        String entityTypeUpdateShowPage = driver.findElement(By.id("_s_com_sirionlabs_model_MasterRegion_name_name_id")).getText();
        Assert.assertEquals(entityTypeUpdateShowPage, regionNameUpdate, "Region Name at show page is -- " +entityTypeUpdateShowPage+ " instead of -- " +regionNameUpdate);

        String entityTypeAliasShowPage = driver.findElement(By.id("_s_com_sirionlabs_model_MasterRegion_alias_alias_id")).getText();
        Assert.assertEquals(entityTypeAliasShowPage, regionAlias, "Region Alias at show page is -- " +entityTypeAliasShowPage+ " instead of -- " +regionAlias);

        Logger.debug("Region updated successfully, with Name -- " + regionNameUpdate + " and Alias --- " + regionAlias);
        
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