package office.sirion.suite.clientAdmin;

import java.io.IOException;

import office.sirion.util.TestUtil;
import office.sirion.util.TestlinkIntegration;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
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

public class DiscrepancyReasonType extends TestSuiteBase {
	String runmodes[] = null;
	static int count = -1;
	static boolean isTestPass = true;
	String testCaseID=null;
	String result=null;

	@BeforeTest
	public void checkTestSkip() {
		if (!TestUtil.isTestCaseRunnable(client_admin_suite_xls, this.getClass().getSimpleName())) {
			Logger.debug("Skipping Test Case " + this.getClass().getSimpleName() + " as Runmode set to NO");
			throw new SkipException("Skipping Test Case " + this.getClass().getSimpleName() + " as Runmode set to NO");
			}
		runmodes = TestUtil.getDataSetRunmodes(client_admin_suite_xls, this.getClass().getSimpleName());
		}

	@Test (dataProvider="getTestData")
	public void DiscrepancyReasonTypeCreation (String discrepancyReasonType, String discrepancyReasonTypeOp, String discrepancyReasonTypActive
			) throws InterruptedException {
		count++;
		if(!runmodes[count].equalsIgnoreCase("Y"))
			throw new SkipException("Runmode for Test Data -- " +discrepancyReasonType +" set to NO " +count);
		
		openBrowser();
		clientAdminLogin(CONFIG.getProperty("clientAdminURL"), CONFIG.getProperty("clientAdminUsername"), CONFIG.getProperty("clientAdminPassword"));
		
		Logger.debug("Executing Client Admin Discrepancy Reason Type Creation Test -- "+discrepancyReasonType);
		
		driver.get(CONFIG.getProperty("clientAdminURL"));
		fluentWaitMethod(driver.findElement(By.linkText("Discrepancy Reason Type")));

		driver.findElement(By.linkText("Discrepancy Reason Type")).click();
		fluentWaitMethod(driver.findElement(By.className("plus")));
	
		driver.findElement(By.className("plus")).click();
		fluentWaitMethod(driver.findElement(By.id("_name_id")));

		if (!discrepancyReasonType.equalsIgnoreCase("")) {
            try {
                new Select(driver.findElement(By.id("_id_id"))).selectByVisibleText(discrepancyReasonType.trim());
            }catch (NoSuchElementException e1) {
                Logger.debug("No Option is available at UI");
            }
            try {
                new Select(driver.findElement(By.id("_id_id"))).selectByIndex(1);
            } catch (NoSuchElementException e1) {
                Logger.debug("No Option is available at UI");
            }
			}

		if (!discrepancyReasonTypeOp.equalsIgnoreCase("")) {
			driver.findElement(By.id("_name_id")).clear();
			driver.findElement(By.id("_name_id")).sendKeys(discrepancyReasonTypeOp.trim());
			}
		
		if (discrepancyReasonTypActive.equalsIgnoreCase("Yes")) {
			if (!driver.findElement(By.id("_active_id")).isSelected())
				driver.findElement(By.id("_active_id")).click();
			}

		driver.findElement(By.id("proceed")).click();
		//
		
		if (discrepancyReasonType.equalsIgnoreCase("")) {
			String entityErrors = driver.findElement(By.cssSelector("div._id_idformError.parentFormmasterdiscrepancyreasontype.formError")).findElement(By.className("formErrorContent")).getText();
			
			Assert.assertEquals(entityErrors, "This field is required");
			Logger.debug("Discrepancy Reason Type Name is Mandatory");
			
		    driver.get(CONFIG.getProperty("clientAdminURL"));
		    return;
		    }
			
		if (discrepancyReasonTypeOp.equalsIgnoreCase("")) {
			String entityErrors = driver.findElement(By.cssSelector("div._name_idformError.parentFormmasterdiscrepancyreasontype.formError")).findElement(By.className("formErrorContent")).getText();
			
			Assert.assertEquals(entityErrors, "This field is required");
			Logger.debug("Discrepancy Reason Type Organization Preference is Mandatory");
			
		    driver.get(CONFIG.getProperty("clientAdminURL"));
		    return;
		    }

		if (discrepancyReasonTypeOp.length() > 128) {
			String entityErrors = driver.findElement(By.cssSelector("div._name_idformError.parentFormmasterdiscrepancyreasontype.formError")).findElement(By.className("formErrorContent")).getText();
			
			Assert.assertEquals(entityErrors, "Maximum 128 characters allowed");
			Logger.debug("For Discrepancy Reason Type Organization Preference, --- Maximum 128 characters allowed");
			
		    driver.get(CONFIG.getProperty("clientAdminURL"));
		    return;
		    }	

		if(driver.findElements(By.id("alertdialog")).size()!=0) {
			String entityErrors = driver.findElement(By.id("errors")).getText();
			
			Assert.assertEquals(entityErrors, "This name already exists in the system.");
			Logger.debug("For Discrepancy Reason Type Organization Preference, " +discrepancyReasonTypeOp+" --- This name already exists in the system.");

			driver.get(CONFIG.getProperty("clientAdminURL"));
			return;
			}

		driver.findElement(By.className("dataTables_filter")).findElement(By.tagName("label")).findElement(By.tagName("input")).sendKeys(discrepancyReasonType);
		Thread.sleep(2000);

        driver.findElement(By.xpath(".//*[@id='l_com_sirionlabs_model_MasterDiscrepancyReasonType']/tbody/tr/td[1]/a/div[@sort='"+ discrepancyReasonType +"']")).click();

        String entityTypeShowPage = driver.findElement(By.id("_c_com_sirionlabs_model_masterdiscrepancyreasontype_systemDiscrepancyReasonType.name_id")).getText();
        Assert.assertEquals(entityTypeShowPage, discrepancyReasonType, "Discrepancy Reason Type at show page is -- " +entityTypeShowPage+ " instead of -- " +discrepancyReasonType);

        String entityTypeOrgPreferenceShowPage = driver.findElement(By.id("_c_com_sirionlabs_model_MasterDiscrepancyReasonType_clientpref_name_id")).getText();
        Assert.assertEquals(entityTypeOrgPreferenceShowPage, discrepancyReasonType, "Discrepancy Reason Type Name at show page is -- " +entityTypeOrgPreferenceShowPage+ " instead of -- " +discrepancyReasonType);

        Logger.debug("Discrepancy Reason Type created successfully, with Name -- " +discrepancyReasonTypeOp);
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
		if (isTestPass)
			TestUtil.reportDataSetResult(client_admin_suite_xls, "Test Cases", TestUtil.getRowNum(client_admin_suite_xls, this.getClass().getSimpleName()), "PASS");
		else
			TestUtil.reportDataSetResult(client_admin_suite_xls, "Test Cases", TestUtil.getRowNum(client_admin_suite_xls, this.getClass().getSimpleName()), "FAIL");
		}

	@DataProvider
	public Object[][] getTestData() {
		return TestUtil.getData(client_admin_suite_xls, this.getClass().getSimpleName());
		}
	}