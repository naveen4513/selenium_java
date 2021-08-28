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

public class SLItem extends TestSuiteBase {
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
	public void SLItemCreation (String slCategory, String slSubCategory, String slItem, String slItemOp, String slItemActive) throws InterruptedException {
		count++;
		if(!runmodes[count].equalsIgnoreCase("Y")) {
			skip=true;
			throw new SkipException("Runmode for Test Data -- " +slItemOp +" set to NO " +count);
			}
		
		openBrowser();
		clientAdminLogin(CONFIG.getProperty("clientAdminURL"), CONFIG.getProperty("clientAdminUsername"), CONFIG.getProperty("clientAdminPassword"));
		
		Logger.debug("Executing Client Admin SL Item Creation Test -- "+slItemOp);
		
		driver.get(CONFIG.getProperty("clientAdminURL"));
		fluentWaitMethod(driver.findElement(By.linkText("SL Category")));
		
		driver.findElement(By.linkText("SL Category")).click();
		Thread.sleep(2000);

		driver.findElement(By.className("dataTables_filter")).findElement(By.tagName("label")).findElement(By.tagName("input")).sendKeys(slCategory);
		Thread.sleep(2000);
		
		driver.findElement(By.xpath(".//*[@id='l_com_sirionlabs_model_MasterSlacategory']/tbody/tr/td[1]/a/div[@sort='"+ slCategory +"']")).click();
		
        String entityTypeShowPage = driver.findElement(By.id("_c_com_sirionlabs_model_masterslacategory_systemSLACategory.name_id")).getText();
        Assert.assertEquals(entityTypeShowPage, slCategory, "SL Category at show page is -- " +entityTypeShowPage+ " instead of -- " +slCategory);
        
        driver.findElement(By.id("ui-id-2")).click();
        Thread.sleep(2000);
/*
		driver.findElement(By.className("dataTables_filter")).findElement(By.tagName("label")).findElement(By.tagName("input")).sendKeys(slSubCategory);
		Thread.sleep(2000);
*/
		driver.findElement(By.xpath(".//*[@id='l_com_sirionlabs_model_MasterSlasubCategory']/tbody/tr/td[1]/a/div[@sort='"+ slSubCategory +"']")).click();
		
        String entityTypeParentShowPage = driver.findElement(By.id("_c_com_sirionlabs_model_masterslasubcategory_systemSLASubCategory.name_id")).getText();
        Assert.assertEquals(entityTypeParentShowPage, slSubCategory, "SL Sub-Category Name at show page is -- " +entityTypeParentShowPage+ " instead of -- " +slSubCategory);
        
        driver.findElement(By.id("ui-id-2")).click();
		fluentWaitMethod(driver.findElement(By.className("plus")));
		
		driver.findElement(By.className("plus")).click();
		fluentWaitMethod(driver.findElement(By.id("_name_id")));

		if (!slItem.equalsIgnoreCase("")) {
            try {
                new Select(driver.findElement(By.id("_id_id"))).selectByVisibleText(slItem.trim());
            }catch (NoSuchElementException e1) {
                Logger.debug("No Option is available at UI");
            }
            try {
                new Select(driver.findElement(By.id("_id_id"))).selectByIndex(1);
            } catch (NoSuchElementException e1) {
                Logger.debug("No Option is available at UI");
            }
			}

		if (!slItemOp.equalsIgnoreCase("")) {
			driver.findElement(By.id("_name_id")).clear();
			driver.findElement(By.id("_name_id")).sendKeys(slItemOp.trim());
			}
		
		if (slItemActive.equalsIgnoreCase("Yes")) {
			if (!driver.findElement(By.id("_active_id")).isSelected())
				driver.findElement(By.id("_active_id")).click();
			}

		driver.findElement(By.id("proceed")).click();
		//
		
		if (slSubCategory.equalsIgnoreCase("")) {
			fail = true;
			
			WebElement e = driver.findElement(By.cssSelector("div._id_idformError.parentFormmasterSlaitem.formError"));
			String entityErrors = e.findElement(By.className("formErrorContent")).getText();
			
			Assert.assertEquals(entityErrors, "This field is required");
			Logger.debug("SL Item is Mandatory");
			
			fail = false;
			driver.get(CONFIG.getProperty("clientAdminURL"));
		    
		    return;
		    }
			
		if (slItemOp.equalsIgnoreCase("")) {
			fail = true;
			
			WebElement e = driver.findElement(By.cssSelector("div._name_idformError.parentFormmasterSlaitem.formError"));
			String entityErrors = e.findElement(By.className("formErrorContent")).getText();
			
			Assert.assertEquals(entityErrors, "This field is required");
			Logger.debug("SL Item Organization Preference is Mandatory");
			
			fail = false;
			driver.get(CONFIG.getProperty("clientAdminURL"));
		    
		    return;
		    }

		if (slItemOp.length() > 128) {
			fail = true;
			
			WebElement e = driver.findElement(By.cssSelector("div._name_idformError.parentFormmasterSlaitem.formError"));
			String entityErrors = e.findElement(By.className("formErrorContent")).getText();
			
			Assert.assertEquals(entityErrors, "Maximum 128 characters allowed");
			Logger.debug("For SL Item Organization Preference, --- Maximum 128 characters allowed");
			
			fail = false;
			driver.get(CONFIG.getProperty("clientAdminURL"));
		    
		    return;
		    }

		if(driver.findElements(By.id("alertdialog")).size()!=0) {
			fail = true;			
			String entityErrors = driver.findElement(By.id("errors")).getText();
			
			if (entityErrors.contains("This name already exists in the system."))
				Logger.debug("For SL Item, " +slItemOp+" --- This name already exists in the system.");
			
			fail=false;
			driver.get(CONFIG.getProperty("clientAdminURL"));

			return;
			}
		
        driver.findElement(By.id("ui-id-2")).click();
        Thread.sleep(2000);
/*
		driver.findElement(By.className("dataTables_filter")).findElement(By.tagName("label")).findElement(By.tagName("input")).sendKeys(slItem);
		Thread.sleep(2000);
*/
		driver.findElement(By.xpath(".//*[@id='l_com_sirionlabs_model_MasterSlaitem']/tbody/tr/td[1]/a/div[@sort='"+ slItem +"']")).click();
		
        String entityTypeOrgPreferenceShowPage = driver.findElement(By.id("_c_com_sirionlabs_model_MasterServiceCategory_clientpref_name_id")).getText();
        Assert.assertEquals(entityTypeOrgPreferenceShowPage, slItemOp, "SL Item Name at show page is -- " +entityTypeOrgPreferenceShowPage+ " instead of -- " +slItemOp);

        Logger.debug("SL Item created successfully, with Name -- " + slItemOp);
        
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