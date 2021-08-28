package office.sirion.suite.clientAdmin;

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

public class InterpretationType extends TestSuiteBase {
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
	public void InterpretationTypeCreation (String interpretationType, String interpretationTypeOp, String interpretationTypeActive) throws InterruptedException {
		count++;
		if(!runmodes[count].equalsIgnoreCase("Y")) {
			skip=true;
			throw new SkipException("Runmode for Test Data -- " +interpretationTypeOp +" set to NO " +count);
			}
		
		openBrowser();
		clientAdminLogin(CONFIG.getProperty("clientAdminURL"), CONFIG.getProperty("clientAdminUsername"), CONFIG.getProperty("clientAdminPassword"));
		
		Logger.debug("Executing Client Admin Interpretation Type Creation Test -- "+interpretationTypeOp);
		
		driver.get(CONFIG.getProperty("clientAdminURL"));
		fluentWaitMethod(driver.findElement(By.linkText("Interpretation Type")));
		
		driver.findElement(By.linkText("Interpretation Type")).click();
		fluentWaitMethod(driver.findElement(By.className("plus")));
	
		driver.findElement(By.className("plus")).click();
		fluentWaitMethod(driver.findElement(By.id("_name_id")));

		if (!interpretationType.equalsIgnoreCase("")) {
            driver.findElement(By.xpath("//span[@id='select2-_systemEntity.id_id-container']")).click();
            driver.findElement(By.xpath("//span[@class='select2-search select2-search--dropdown']//input[@type='search']")).clear();

            driver.findElement(By.xpath("//span[@class='select2-search select2-search--dropdown']//input[@type='search']")).sendKeys(interpretationType);
            driver.findElement(By.xpath("//span[@class='select2-search select2-search--dropdown']//input[@type='search']")).sendKeys(Keys.RETURN);

            if (driver.findElement(By.xpath("//li[@class='select2-results__option']")).isDisplayed()){
                driver.findElement(By.xpath("//span[@class='select2-search select2-search--dropdown']//input[@type='search']")).clear();
                driver.findElement(By.xpath("//label[@for='_systemEntity.id_id']")).click();
                driver.findElement(By.xpath("//span[@id='select2-_systemEntity.id_id-container']")).click();
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
		if (!interpretationTypeOp.equalsIgnoreCase("")) {
			driver.findElement(By.id("_name_id")).clear();
			driver.findElement(By.id("_name_id")).sendKeys(interpretationTypeOp.trim());
			}
		
		if (interpretationTypeActive.equalsIgnoreCase("Yes")) {
			if (!driver.findElement(By.id("_active_id")).isSelected())
				driver.findElement(By.id("_active_id")).click();
			}
		
        driver.findElement(By.className("submit")).findElement(By.tagName("input")).click();
		//
		
		if (interpretationType.equalsIgnoreCase("")) {
			fail = true;

			WebElement e = driver.findElement(By.cssSelector("div._systemEntity_id_idformError.parentFormciType.formError"));
			String entityErrors = e.findElement(By.className("formErrorContent")).getText();
			
			Assert.assertEquals(entityErrors, "This field is required");
			Logger.debug("Interpretation Type is Mandatory");
			
			fail = false;
			driver.get(CONFIG.getProperty("clientAdminURL"));
		    
		    return;
		    }
			
		if (interpretationTypeOp.equalsIgnoreCase("")) {
			fail = true;
			
			WebElement e = driver.findElement(By.cssSelector("div._name_idformError.parentFormciType.formError"));
			String entityErrors = e.findElement(By.className("formErrorContent")).getText();
			
			Assert.assertEquals(entityErrors, "This field is required");
			Logger.debug("Interpretation Type Organization Preference is Mandatory");
			
			fail = false;
			driver.get(CONFIG.getProperty("clientAdminURL"));
		    
		    return;
		    }

		if (interpretationTypeOp.length() > 128) {
			fail = true;
			
			WebElement e = driver.findElement(By.cssSelector("div._name_idformError.parentFormciType.formError"));
			String entityErrors = e.findElement(By.className("formErrorContent")).getText();
			
			Assert.assertEquals(entityErrors, "Maximum 128 characters allowed");
			Logger.debug("For Interpretation Type Organization Preference, --- Maximum 128 characters allowed");
			
			fail = false;
			driver.get(CONFIG.getProperty("clientAdminURL"));
		    
		    return;
		    }

		if(driver.findElements(By.id("alertdialog")).size()!=0) {
			fail = true;
			String entityErrors = driver.findElement(By.id("errors")).getText();
			
			if (entityErrors.contains("This name already exists in the system."))
				Logger.debug("For Interpretation Type, " +interpretationTypeOp+" --- This name already exists in the system.");
									
			fail=false;
			driver.get(CONFIG.getProperty("clientAdminURL"));

			return;
			}

		driver.findElement(By.className("dataTables_filter")).findElement(By.tagName("label")).findElement(By.tagName("input")).sendKeys(interpretationType);
		Thread.sleep(2000);
		
		driver.findElement(By.xpath(".//*[@id='l_com_sirionlabs_model_MasterGroup']/tbody/tr/td[1]/div[@sort='"+ interpretationType +"']/a")).click();
		
        String entityTypeShowPage = driver.findElement(By.id("_c_com_sirionlabs_model_masterinterpretationtype_name_systemEntity.name_id")).getText();
        Assert.assertEquals(entityTypeShowPage, interpretationType, "Interpretation Type at show page is -- " +entityTypeShowPage+ " instead of -- " +interpretationType);

        String entityTypeOrgPreferenceShowPage = driver.findElement(By.id("_c_com_sirionlabs_model_mastermanagementtype_clientpref_name_id")).getText();
        Assert.assertEquals(entityTypeOrgPreferenceShowPage, interpretationTypeOp, "Interpretation Type Name at show page is -- " +entityTypeOrgPreferenceShowPage+ " instead of -- " +interpretationTypeOp);

        Logger.debug("Interpretation Type created successfully, with Name -- " + interpretationTypeOp);
        
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