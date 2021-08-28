package office.sirion.suite.clientAdmin;

import java.io.IOException;

import office.sirion.util.TestUtil;
import office.sirion.util.TestlinkIntegration;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;
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

public class AgreementType extends TestSuiteBase {
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
	public void AgreementTypeCreation (String agreementType, String agreementTypeOp, String agreementTypeActive) throws InterruptedException {
		count++;
		if(!runmodes[count].equalsIgnoreCase("Y")) {
			skip=true;
			throw new SkipException("Runmode for Test Data -- " +agreementTypeOp +" set to NO " +count);
			}
		
		openBrowser();
		clientAdminLogin(CONFIG.getProperty("clientAdminURL"), CONFIG.getProperty("clientAdminUsername"), CONFIG.getProperty("clientAdminPassword"));
		
		Logger.debug("Executing Client Admin Agreement Type Creation Test -- "+agreementTypeOp);
		
		driver.get(CONFIG.getProperty("clientAdminURL"));
		fluentWaitMethod(driver.findElement(By.linkText("Agreement Type")));
		
		driver.findElement(By.linkText("Agreement Type")).click();
		fluentWaitMethod(driver.findElement(By.className("plus")));
	
		driver.findElement(By.className("plus")).click();
		fluentWaitMethod(driver.findElement(By.id("_name_id")));

		if (!agreementType.equalsIgnoreCase("")) {
            try {
                new Select(driver.findElement(By.id("_systemEntity.id_id"))).selectByVisibleText(agreementType.trim());
            }catch (NoSuchElementException e1) {
                Logger.debug("No Option is available at UI");
            }
            try {
                new Select(driver.findElement(By.id("_systemEntity.id_id"))).selectByIndex(1);
            } catch (NoSuchElementException e1) {
                Logger.debug("No Option is available at UI");
            }
			}

		if (!agreementTypeOp.equalsIgnoreCase("")) {
			driver.findElement(By.id("_name_id")).clear();
			driver.findElement(By.id("_name_id")).sendKeys(agreementTypeOp.trim());
			}
		
		if (agreementTypeActive.equalsIgnoreCase("Yes")) {
			if (!driver.findElement(By.id("_active_id")).isSelected())
				driver.findElement(By.id("_active_id")).click();
			}
		
        driver.findElement(By.className("submit")).findElement(By.tagName("input")).click();
		//
		
		if (agreementType.equalsIgnoreCase("")) {
			fail = true;
			
			WebElement e = driver.findElement(By.cssSelector("div._systemEntity_id_idformError.parentFormagreementType.formError"));
			String entityErrors = e.findElement(By.className("formErrorContent")).getText();
			
			Assert.assertEquals(entityErrors, "This field is required");
			Logger.debug("Agreement Type is Mandatory");
			
			fail = false;
			driver.get(CONFIG.getProperty("clientAdminURL"));
		    
		    return;
		    }
			
		if (agreementTypeOp.equalsIgnoreCase("")) {
			fail = true;
			
			WebElement e = driver.findElement(By.cssSelector("div._name_idformError.parentFormagreementType.formError"));
			String entityErrors = e.findElement(By.className("formErrorContent")).getText();
			
			Assert.assertEquals(entityErrors, "This field is required");
			Logger.debug("Agreement Type Organization Preference is Mandatory");
			
			fail = false;
			driver.get(CONFIG.getProperty("clientAdminURL"));
		    
		    return;
		    }

		if (agreementTypeOp.length() > 128) {
			fail = true;
			
			WebElement e = driver.findElement(By.cssSelector("div._name_idformError.parentFormagreementType.formError"));
			String entityErrors = e.findElement(By.className("formErrorContent")).getText();
			
			Assert.assertEquals(entityErrors, "Maximum 128 characters allowed");
			Logger.debug("For Agreement Type Organization Preference, --- Maximum 128 characters allowed");
			
			fail = false;
			driver.get(CONFIG.getProperty("clientAdminURL"));
		    
		    return;
		    }

		if(driver.findElements(By.id("alertdialog")).size()!=0) {
			fail = true;			
			String entityErrors = driver.findElement(By.id("errors")).getText();
			
			if (entityErrors.contains("This name already exists in the system."))
				Logger.debug("For Agreement Type, " +agreementTypeOp+" --- This name already exists in the system.");
									
			fail=false;
			driver.get(CONFIG.getProperty("clientAdminURL"));

			return;
			}

		driver.findElement(By.className("dataTables_filter")).findElement(By.tagName("label")).findElement(By.tagName("input")).sendKeys(agreementType);
		Thread.sleep(2000);
		
		driver.findElement(By.xpath(".//*[@id='l_com_sirionlabs_model_MasterGroup']/tbody/tr/td[1]/div[@sort='"+ agreementType +"']/a")).click();
		
        String entityTypeShowPage = driver.findElement(By.id("_c_com_sirionlabs_model_agreementtype_name_systemEntity.name_id")).getText();
        Assert.assertEquals(entityTypeShowPage, agreementType, "Agreement Type at show page is -- " +entityTypeShowPage+ " instead of -- " +agreementType);

        String entityTypeOrgPreferenceShowPage = driver.findElement(By.id("_c_com_sirionlabs_model_agreementtype_clientpref_name_id")).getText();
        Assert.assertEquals(entityTypeOrgPreferenceShowPage, agreementTypeOp, "Agreement Type Name at show page is -- " +entityTypeOrgPreferenceShowPage+ " instead of -- " +agreementTypeOp);

        Logger.debug("Agreement Type created successfully, with Name -- " + agreementTypeOp);
        
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