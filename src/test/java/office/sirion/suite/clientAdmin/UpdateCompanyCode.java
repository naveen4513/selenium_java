package office.sirion.suite.clientAdmin;

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

public class UpdateCompanyCode extends TestSuiteBase {
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
	public void UpdateCompanyCodeTest (String regionHub, String market, String internalContractingParty, String companyCode, String companyCodeOp,
			String companyCodeECCID, String companyCodeActive) throws InterruptedException {
		count++;
		if(!runmodes[count].equalsIgnoreCase("Y")) {
			skip=true;
			throw new SkipException("Runmode for Test Data -- " +companyCodeOp +" set to NO " +count);
			}
		
		openBrowser();
		clientAdminLogin(CONFIG.getProperty("clientAdminURL"), CONFIG.getProperty("clientAdminUsername"), CONFIG.getProperty("clientAdminPassword"));
		
		Logger.debug("Executing Client Admin Company Code Update Test -- "+companyCodeOp);
		
		driver.get(CONFIG.getProperty("clientAdminURL"));
		fluentWaitMethod(driver.findElement(By.linkText("Region/Hub")));
		
		driver.findElement(By.linkText("Region/Hub")).click();
		Thread.sleep(2000);

		driver.findElement(By.className("dataTables_filter")).findElement(By.tagName("label")).findElement(By.tagName("input")).sendKeys(regionHub);
		Thread.sleep(2000);
		
        driver.findElement(By.xpath(".//*[@id='l_com_sirionlabs_model_hub']/tbody/tr[@role='row']/td[contains(.,'"+ regionHub +"')]/preceding-sibling::td[1]/a")).click();

        String entityTypeShowPage = driver.findElement(By.id("_c_com_sirionlabs_model_hub_systemHub.name_id")).getText();
        Assert.assertEquals(entityTypeShowPage, regionHub, "Region/Hub at show page is -- " +entityTypeShowPage+ " instead of -- " +regionHub);
        
        driver.findElement(By.id("ui-id-2")).click();
        Thread.sleep(2000);

		driver.findElement(By.className("dataTables_filter")).findElement(By.tagName("label")).findElement(By.tagName("input")).sendKeys(market);
		Thread.sleep(2000);

        driver.findElement(By.xpath(".//*[@id='l_com_sirionlabs_model_market']/tbody/tr[@role='row']/td[contains(.,'"+ market +"')]/preceding-sibling::td[1]/a")).click();
		
        String entityTypeParentShowPage = driver.findElement(By.id("_c_com_sirionlabs_model_market_name_systemMarket.name_id")).getText();
        Assert.assertEquals(entityTypeParentShowPage, market, "Company Code Name at show page is -- " +entityTypeParentShowPage+ " instead of -- " +market);

        driver.findElement(By.id("ui-id-2")).click();
        Thread.sleep(2000);

		driver.findElement(By.className("dataTables_filter")).findElement(By.tagName("label")).findElement(By.tagName("input")).sendKeys(internalContractingParty);
		Thread.sleep(2000);

        driver.findElement(By.xpath(".//*[@id='l_com_sirionlabs_model_icp']/tbody/tr[@role='row']/td[contains(.,'"+ internalContractingParty +"')]/preceding-sibling::td[1]/a")).click();
		
        String entityTypeParentParentShowPage = driver.findElement(By.id("_c_com_sirionlabs_model_icp_name_systemInternalContractingParty.name_id")).getText();
        Assert.assertEquals(entityTypeParentParentShowPage, internalContractingParty, "Company Code Name at show page is -- " +entityTypeParentParentShowPage+ " instead of -- " +internalContractingParty);

        driver.findElement(By.id("ui-id-2")).click();
        Thread.sleep(2000);

		driver.findElement(By.className("dataTables_filter")).findElement(By.tagName("label")).findElement(By.tagName("input")).sendKeys(companyCode);
		Thread.sleep(2000);

        driver.findElement(By.xpath(".//*[@id='l_com_sirionlabs_model_companycode']/tbody/tr[@role='row']/td[contains(.,'"+ companyCode +"')]/preceding-sibling::td[1]/a")).click();
		
        String entityTypeParentParentParentShowPage = driver.findElement(By.id("_c_com_sirionlabs_model_companycode_name_systemCompanyCode.name_id")).getText();
        Assert.assertEquals(entityTypeParentParentParentShowPage, companyCode, "Company Code Name at show page is -- " +entityTypeParentParentParentShowPage+ " instead of -- " +companyCode);

        driver.findElement(By.className("submit")).findElement(By.tagName("input")).click();
		fluentWaitMethod(driver.findElement(By.id("_name_id")));
        
		if (!companyCodeOp.equalsIgnoreCase("")) {
			driver.findElement(By.id("_name_id")).clear();
			driver.findElement(By.id("_name_id")).sendKeys(companyCodeOp.trim());
			}

		if (!companyCodeECCID.equalsIgnoreCase("")) {
			driver.findElement(By.id("_eccId_id")).clear();
			driver.findElement(By.id("_eccId_id")).sendKeys(companyCodeECCID.trim());
			}
		
		if (companyCodeActive.equalsIgnoreCase("Yes")) {
			if (!driver.findElement(By.id("_active_id")).isSelected())
				driver.findElement(By.id("_active_id")).click();
			} else if (companyCodeActive.equalsIgnoreCase("No")) {
				if (driver.findElement(By.id("_active_id")).isSelected())
					driver.findElement(By.id("_active_id")).click();
				}

		driver.findElement(By.id("proceed")).click();
		//
		
		if (companyCodeOp.length() > 128) {
			fail = true;
			
			WebElement e = driver.findElement(By.cssSelector("div._name_idformError.parentFormmasterCompanyCode.formError"));
			String entityErrors = e.findElement(By.className("formErrorContent")).getText();
			
			Assert.assertEquals(entityErrors, "Maximum 128 characters allowed");
			Logger.debug("For Company Code Organization Preference, --- Maximum 128 characters allowed");
			
			fail = false;
			driver.get(CONFIG.getProperty("clientAdminURL"));
		    
		    return;
		    }

		if(driver.findElements(By.id("alertdialog")).size()!=0) {
			fail = true;			
			String entityErrors = driver.findElement(By.id("errors")).getText();
			
			if (entityErrors.contains("This name already exists in the system."))
				Logger.debug("For Company Code, " +companyCodeOp+" --- This name already exists in the system.");
			
			fail=false;
			driver.get(CONFIG.getProperty("clientAdminURL"));

			return;
			}
		
        String entityTypeOrgPreferenceShowPage = driver.findElement(By.id("_s_com_sirionlabs_model_companycode_clientpref_name_id")).getText();
        Assert.assertEquals(entityTypeOrgPreferenceShowPage, companyCodeOp, "Company Code Name at show page is -- " +entityTypeOrgPreferenceShowPage+ " instead of -- " +companyCodeOp);

        Logger.debug("Company Code updated successfully, with Name -- " + companyCodeOp);
        
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