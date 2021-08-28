package office.sirion.suite.clientAdmin;

import java.awt.event.KeyEvent;
import java.io.IOException;
import java.util.List;

import office.sirion.util.TestUtil;
import office.sirion.util.TestlinkIntegration;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
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

public class ServiceSubCategory extends TestSuiteBase {
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
	public void ServiceSubCategoryCreation (String serviceCategory, String serviceSubCategory, String serviceSubCategoryOp, String serviceSubCategoryActive
			) throws InterruptedException {
		count++;
		if(!runmodes[count].equalsIgnoreCase("Y")) {
			skip=true;
			throw new SkipException("Runmode for Test Data -- " +serviceSubCategoryOp +" set to NO " +count);
			}
		
		openBrowser();
		clientAdminLogin(CONFIG.getProperty("clientAdminURL"), CONFIG.getProperty("clientAdminUsername"), CONFIG.getProperty("clientAdminPassword"));
		
		Logger.debug("Executing Client Admin Service Sub-Category Creation Test -- "+serviceSubCategoryOp);
		
		driver.get(CONFIG.getProperty("clientAdminURL"));
		fluentWaitMethod(driver.findElement(By.linkText("Service Category")));
		
		driver.findElement(By.linkText("Service Category")).click();
		Thread.sleep(2000);

		driver.findElement(By.className("dataTables_filter")).findElement(By.tagName("label")).findElement(By.tagName("input")).sendKeys(serviceCategory);
		Thread.sleep(2000);
		
		driver.findElement(By.xpath(".//*[@id='l_com_sirionlabs_model_MasterServiceCategory']/tbody/tr/td[1]/a/div[@sort='"+ serviceCategory +"']")).click();

        String entityTypeShowPage = driver.findElement(By.id("_c_com_sirionlabs_model_MasterContractSubType_systemServiceCategory.name_id")).getText();
        Assert.assertEquals(entityTypeShowPage, serviceCategory, "Service Category at show page is -- " +entityTypeShowPage+ " instead of -- " +serviceCategory);
        
        driver.findElement(By.id("ui-id-2")).click();
		fluentWaitMethod(driver.findElement(By.className("plus")));
		
		driver.findElement(By.className("plus")).click();
		fluentWaitMethod(driver.findElement(By.id("_name_id")));

		if (!serviceSubCategory.equalsIgnoreCase("")) {
            driver.findElement(By.xpath("//span[@id='select2-_id_id-container']")).click();
            driver.findElement(By.xpath("//span[@class='select2-search select2-search--dropdown']//input[@type='search']")).clear();

                driver.findElement(By.xpath("//span[@class='select2-search select2-search--dropdown']//input[@type='search']")).sendKeys(serviceCategory);
                driver.findElement(By.xpath("//span[@class='select2-search select2-search--dropdown']//input[@type='search']")).sendKeys(Keys.RETURN);

                if (driver.findElement(By.xpath("//li[@class='select2-results__option']")).isDisplayed()){
                    driver.findElement(By.xpath("//span[@class='select2-search select2-search--dropdown']//input[@type='search']")).clear();
                    driver.findElement(By.xpath("//label[@for='_id_id']")).click();
                    driver.findElement(By.xpath("//span[@id='select2-_id_id-container']")).click();
                    try {
                        WebElement autoOptions = driver.findElement(By.xpath("//span[@class='select2-dropdown select2-dropdown--below']//span[@class='select2-results']/ul[@class='select2-results__options']"));
                        waitF.until(ExpectedConditions.visibilityOf(autoOptions));

                        List<WebElement> optionsToSelect = autoOptions.findElements(By.tagName("li"));
                        for(WebElement option : optionsToSelect){
                            if(!option.getText().equals("-Select-")) {
                                System.out.println("Trying to select: "+option.getText().toString());
                                option.click();
                                break;
                            }
                        }
                    } catch (NoSuchElementException e1) {
                        System.out.println(e1.getStackTrace());
                    }
                }
		}

		if (!serviceSubCategoryOp.equalsIgnoreCase("")) {
			driver.findElement(By.id("_name_id")).clear();
			driver.findElement(By.id("_name_id")).sendKeys(serviceSubCategoryOp.trim());
			}
		
		if (serviceSubCategoryActive.equalsIgnoreCase("Yes")) {
			if (!driver.findElement(By.id("_active_id")).isSelected())
				driver.findElement(By.id("_active_id")).click();
			}

		driver.findElement(By.id("proceed")).click();
		//
		
		if (serviceSubCategory.equalsIgnoreCase("")) {
			fail = true;
			
			WebElement e = driver.findElement(By.cssSelector("div._id_idformError.parentFormmasterservicesubcategory.formError"));
			String entityErrors = e.findElement(By.className("formErrorContent")).getText();
			
			Assert.assertEquals(entityErrors, "This field is required");
			Logger.debug("Service Sub-Category is Mandatory");
			
			fail = false;
			driver.get(CONFIG.getProperty("clientAdminURL"));
		    
		    return;
		    }
			
		if (serviceSubCategoryOp.equalsIgnoreCase("")) {
			fail = true;
			
			WebElement e = driver.findElement(By.cssSelector("div._name_idformError.parentFormmasterservicesubcategory.formError"));
			String entityErrors = e.findElement(By.className("formErrorContent")).getText();
			
			Assert.assertEquals(entityErrors, "This field is required");
			Logger.debug("Service Sub-Category Organization Preference is Mandatory");
			
			fail = false;
			driver.get(CONFIG.getProperty("clientAdminURL"));
		    
		    return;
		    }

		if (serviceSubCategoryOp.length() > 128) {
			fail = true;
			
			WebElement e = driver.findElement(By.cssSelector("div._name_idformError.parentFormmasterservicesubcategory.formError"));
			String entityErrors = e.findElement(By.className("formErrorContent")).getText();
			
			Assert.assertEquals(entityErrors, "Maximum 128 characters allowed");
			Logger.debug("For Service Sub-Category Organization Preference, --- Maximum 128 characters allowed");
			
			fail = false;
			driver.get(CONFIG.getProperty("clientAdminURL"));
		    
		    return;
		    }

		if(driver.findElements(By.id("alertdialog")).size()!=0) {
			fail = true;			
			String entityErrors = driver.findElement(By.id("errors")).getText();
			
			if (entityErrors.contains("This name already exists in the system."))
				Logger.debug("For Service Sub-Category, " +serviceSubCategoryOp+" --- This name already exists in the system.");
			
			fail=false;
			driver.get(CONFIG.getProperty("clientAdminURL"));

			return;
			}
		
        driver.findElement(By.id("ui-id-2")).click();
        Thread.sleep(2000);
/*
		driver.findElement(By.className("dataTables_filter")).findElement(By.tagName("label")).findElement(By.tagName("input")).sendKeys(serviceSubCategoryOp);
		Thread.sleep(2000);
*/
		driver.findElement(By.xpath(".//*[@id='l_com_sirionlabs_model_MasterServiceSubCategory']/tbody/tr/td[1]/a/div[@sort='"+ serviceSubCategory +"']")).click();
		
        String entityTypeOrgPreferenceShowPage = driver.findElement(By.id("_s_com_sirionlabs_model_MasterServiceSubCategory_clientpref_name_id")).getText();
        Assert.assertEquals(entityTypeOrgPreferenceShowPage, serviceSubCategoryOp, "Service Sub-Category Name at show page is -- " +entityTypeOrgPreferenceShowPage+ " instead of -- " +serviceSubCategoryOp);

        Logger.debug("Service Sub-Category created successfully, with Name -- " + serviceSubCategoryOp);
        
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