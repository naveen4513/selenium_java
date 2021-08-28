package office.sirion.suite.supplier;

import java.io.IOException;

import office.sirion.util.TestUtil;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.StaleElementReferenceException;
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

public class SupplierWorkflow extends TestSuiteBase {
	String result = null;
	String runmodes[] = null;
	static int count = -1;
	static boolean isTestPass = true;
	String testCaseID;

	@BeforeTest
	public void checkTestSkip() {
		if (!TestUtil.isTestCaseRunnable(supplier_suite_xls, this.getClass().getSimpleName())) {
			Logger.debug("Skipping Test Case " + this.getClass().getSimpleName() + " as runmode set to NO");
			throw new SkipException("Skipping Test Case " + this.getClass().getSimpleName() + " as runmode set to NO");
			}
		runmodes = TestUtil.getDataSetRunmodes(supplier_suite_xls, this.getClass().getSimpleName());
		}

	@Test(dataProvider = "getTestData")
	public void SupplierWorkflowTest (String WorkflowSteps, String ParentEntity) throws InterruptedException {

		count++;
		if (!runmodes[count].equalsIgnoreCase("Y"))
			throw new SkipException("Runmode for Test Data Set to NO -- " + count);


		openBrowser();
	    endUserLogin(CONFIG.getProperty("endUserURL"), CONFIG.getProperty("endUserUsername"), CONFIG.getProperty("endUserPassword"));

	    Logger.debug("Executing Test Case Supplier Workflow/Non-Workflow for -- " + ParentEntity);

	    driver.get(CONFIG.getProperty("endUserURL"));

		fluentWaitMethod(locateElementBy("suppliers_quick_link"));

		locateElementBy("suppliers_quick_link").click();

		fluentWaitMethod(locateElementBy("entity_listing_page_display_dropdown_link"));

		new Select(locateElementBy("entity_listing_page_display_dropdown_link")).selectByVisibleText(CONFIG.getProperty("maxEntityListingDropdown"));


		if (!ParentEntity.equalsIgnoreCase("") && ParentEntity.length()<=30)
			try{
			driver.findElement(By.xpath("//*[@id='cr']/tbody/tr/td[4][./text()='"+ParentEntity+"']/preceding-sibling::td[2]/a")).click();
			}catch (NoSuchElementException e){ locateElementBy("global_bulk_listing_page_first_entry_link").click();}
		else if (!ParentEntity.equalsIgnoreCase("") && ParentEntity.length()>30)
			try{
			driver.findElement(By.xpath(".//*[@id='cr']/tbody/tr/td[4][div/div[./text()='"+ParentEntity+"']]/preceding-sibling::td[2]/a")).click();
			}catch (NoSuchElementException e){ locateElementBy("global_bulk_listing_page_first_entry_link").click();}

		fluentWaitMethod(driver.findElement(By.cssSelector("ng-form[template='tab.actions']")).findElement(By.cssSelector("div.submit.freeze")));

		if (!WorkflowSteps.equalsIgnoreCase("")) {
			for (String entityData : WorkflowSteps.split(";")) {
				WebElement elementWorkflow = driver.findElement(By.cssSelector("ng-form[template='tab.actions']")).findElement(By.cssSelector("div.submit.freeze"));		
				try {
					fluentWaitMethod(driver.findElement(By.xpath(".//button[contains(.,'"+entityData.trim()+"')]")));
					Assert.assertNotNull(elementWorkflow.findElement(By.xpath(".//button[contains(.,'"+entityData.trim()+"')]")));
					elementWorkflow.findElement(By.xpath(".//button[contains(.,'"+entityData.trim()+"')]")).click();


					if (entityData.trim().equalsIgnoreCase("Delete")) {
						if (driver.findElements(By.className("success-icon")).size()!=0) {
							String entityWFMessage = driver.findElement(By.className("success-icon")).getText();

							if (entityWFMessage.equalsIgnoreCase("Are you sure you would like to delete this entity?")) {
								Assert.assertEquals(entityWFMessage, "Are you sure you would like to delete this entity?");

								Assert.assertNotNull(driver.findElement(By.xpath("//button[contains(.,'Yes')]")));

								driver.findElement(By.xpath("//button[contains(.,'Yes')]")).click();
								//
		    				  	}
							}
						}
					} catch (NoSuchElementException e) {
						Logger.debug("No Such Element with the given Key, --- "+entityData.trim()+" --- Moving onto Next Step");
						}
						catch (StaleElementReferenceException ex){
                            fluentWaitMethod(driver.findElement(By.xpath(".//button[contains(.,'"+entityData.trim()+"')]")));

                        }

		    	  fluentWaitMethod(driver.findElement(By.cssSelector("ng-form[template='tab.actions']")).findElement(By.cssSelector("div.submit.freeze")));
		    	  }
		      }

		Logger.debug("Test Case Supplier Workflow/Non-Workflow for -- " + ParentEntity + " -- is PASSED");
        driver.get(CONFIG.getProperty("endUserURL"));
	    }

	@AfterMethod
	public void reportDataSetResult(ITestResult testResult) throws IOException {
		takeScreenShotOnFailure(testResult.getStatus(), this.getClass().getSimpleName());

		if(testResult.getStatus()==ITestResult.SKIP)
			TestUtil.reportDataSetResult(supplier_suite_xls, this.getClass().getSimpleName(), count+2, "SKIP");
		else if(testResult.getStatus()==ITestResult.FAILURE) {
			isTestPass=false;
			TestUtil.reportDataSetResult(supplier_suite_xls, this.getClass().getSimpleName(), count+2, "FAIL");
			result= "Fail";
			}
		else if (testResult.getStatus()==ITestResult.SUCCESS) {
			TestUtil.reportDataSetResult(supplier_suite_xls, this.getClass().getSimpleName(), count+2, "PASS");
			result= "Pass";
			}
		try {
            for (int i = 2; i <= supplier_suite_xls.getRowCount(this.getClass().getSimpleName()); i++) {
                String testCaseID = supplier_suite_xls.getCellData(this.getClass().getSimpleName(), "testCaseID", i);
                if (!testCaseID.equalsIgnoreCase("")) {
                    updateRun(convertStringToInteger(testCaseID),new java.lang.Exception().toString(), result);
                }
            }
	   }catch(Exception e){
            Logger.debug(e);
        }
		}

	@AfterTest
	public void reportTestResult() {
		if (isTestPass)
			TestUtil.reportDataSetResult(supplier_suite_xls, "Test Cases", TestUtil.getRowNum(supplier_suite_xls, this.getClass().getSimpleName()),"PASS");
		else
			TestUtil.reportDataSetResult(supplier_suite_xls, "Test Cases", TestUtil.getRowNum(supplier_suite_xls, this.getClass().getSimpleName()),"FAIL");
		}

	@DataProvider
	public Object[][] getTestData() {
		return TestUtil.getData(supplier_suite_xls, this.getClass().getSimpleName());
		}
	}
