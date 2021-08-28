package office.sirion.suite.cr;

import office.sirion.util.TestUtil;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
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
import java.io.IOException;

public class CRWorkflow extends TestSuiteBase {
	String result = null;
	String runmodes[] = null;
	static int count = -1;
	static boolean fail = false;
	static boolean skip = false;
	static boolean isTestPass = true;
	
	@BeforeTest
	public void checkTestSkip() {
		if (!TestUtil.isTestCaseRunnable(cr_suite_xls, this.getClass().getSimpleName())) {
			Logger.debug("Skipping Test Case " + this.getClass().getSimpleName() + " as runmode set to NO");
			throw new SkipException("Skipping Test Case " + this.getClass().getSimpleName() + " as runmode set to NO");
			}
		runmodes = TestUtil.getDataSetRunmodes(cr_suite_xls, this.getClass().getSimpleName());
		}

  @Test(dataProvider = "getTestData")
  public void CRWorkflowTest (String crWorkflowSteps, String crParent
		  ) throws InterruptedException {
	  
		count++;
		if (!runmodes[count].equalsIgnoreCase("Y")) {
			skip = true;
			throw new SkipException("Runmode for Test Data Set to NO -- " + count);
			}
		
		openBrowser();
	    endUserLogin(CONFIG.getProperty("endUserURL"), CONFIG.getProperty("endUserUsername"), CONFIG.getProperty("endUserPassword"));
	    
	    Logger.debug("Executing Test Case CR Workflow with Name --- " + crParent);
		
	    driver.get(CONFIG.getProperty("endUserURL"));

		fluentWaitMethod(locateElementBy("cr_quick_link"));

	    
	    locateElementBy("cr_quick_link").click();

		fluentWaitMethod(locateElementBy("entity_listing_page_display_dropdown_link"));
		
		new Select(locateElementBy("entity_listing_page_display_dropdown_link")).selectByVisibleText("100");

		
		if (!crParent.equalsIgnoreCase("")) {
			try {
				driver.findElement(By.xpath(".//*[@id='cr']/tbody/tr[@role='row']/td[contains(.,'" + crParent + "')]/preceding-sibling::td[2]/a")).click();
			}catch (NoSuchElementException e){
				locateElementBy("entity_listing_page_first_entry_link_cr").click();

			}
			} else {
			locateElementBy("entity_listing_page_first_entry_link_cr").click();

		}
                fluentWaitMethod(locateElementBy("co_show_page_bottom_tab").findElement(By.cssSelector("div.submit.freeze")));
				fluentWaitMethod(driver.findElement(By.xpath("//button[contains(.,'Clone')]")));
			    Assert.assertNotNull(driver.findElement(By.xpath("//button[contains(.,'Clone')]")));
			    ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", driver.findElement(By.xpath("//button[contains(.,'Clone')]")));
			    driver.findElement(By.xpath("//button[contains(.,'Clone')]")).click();

                fluentWaitMethod(locateElementBy("co_show_page_bottom_tab").findElement(By.cssSelector("div.submit.freeze")));
				fluentWaitMethod(driver.findElement(By.xpath("//button[contains(.,'Create CR')]")));
				Assert.assertNotNull(driver.findElement(By.xpath("//button[contains(.,'Create CR')]")));
				((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", driver.findElement(By.xpath("//button[contains(.,'Create CR')]")));
				driver.findElement(By.xpath("//button[contains(.,'Create CR')]")).click();

					
				if (driver.findElements(By.className("success-icon")).size()!=0) {
					 String entityID = driver.findElement(By.className("success-icon")).findElement(By.id("hrefElemId")).getText();
						 
					 Logger.debug("CR cloned successfully with CR ID " + entityID);
					 
					 driver.findElement(By.className("success-icon")).findElement(By.id("hrefElemId")).click();
					 fluentWaitMethod(locateElementBy("cr_show_page_id"));

						 
					 String entityShowPageID = locateElementBy("cr_show_page_id").getText();

					 Assert.assertEquals(entityShowPageID, entityID);
					 Logger.debug("CR ID on show page has been verified");
					 }

	    if (!crWorkflowSteps.equalsIgnoreCase("")) {
		      for (String entityData : crWorkflowSteps.split(";")) {
		    	  try {
		    		  fluentWaitMethod(driver.findElement(By.xpath("//button[contains(.,'"+entityData.trim()+"')")));

		    		  Assert.assertNotNull(driver.findElement(By.xpath("//button[contains(.,'"+entityData.trim()+"')]")));
		    		  driver.findElement(By.xpath("//button[contains(.,'"+entityData.trim()+"')]")).click();


					  String entityShowPageStatus = locateElementBy("cr_show_page_status").getText();

					  Assertion(entityShowPageStatus, entityData);
					  Logger.debug("CR Status on show page has been verified");

		    		  } catch (NoSuchElementException e) {
		    			  Logger.debug("No Such Element with the given Key, --- "+entityData+" --- Moving onto Next Step");
		    			  }
		    	  }
		      

		      }
		
		fail = false;
	
		driver.get(CONFIG.getProperty("endUserURL"));
	    }

	@AfterMethod
	public void reportDataSetResult(ITestResult testResult) throws IOException {
		takeScreenShotOnFailure(testResult.getStatus(), this.getClass().getSimpleName());

		if(testResult.getStatus()==ITestResult.SKIP)
			TestUtil.reportDataSetResult(cr_suite_xls, this.getClass().getSimpleName(), count+2, "SKIP");
		else if(testResult.getStatus()==ITestResult.FAILURE) {
			isTestPass=false;
			TestUtil.reportDataSetResult(cr_suite_xls, this.getClass().getSimpleName(), count+2, "FAIL");
			result= "Fail";
		}
		else if (testResult.getStatus()== ITestResult.SUCCESS) {
			TestUtil.reportDataSetResult(cr_suite_xls, this.getClass().getSimpleName(), count+2, "PASS");
			result= "Pass";
		}
		try {
			for (int i = 2; i <= cr_suite_xls.getRowCount(this.getClass().getSimpleName()); i++) {
				String testCaseID = cr_suite_xls.getCellData(this.getClass().getSimpleName(), "testCaseID", i);
				if (!testCaseID.equalsIgnoreCase("")) {
					updateRun(convertStringToInteger(testCaseID), new java.lang.Exception().toString(), result);
				}
			}
		}catch(Exception e){
			Logger.debug(e);
		}
	}
  
  @AfterTest
  public void reportTestResult() {
	  if (isTestPass)
		  TestUtil.reportDataSetResult(cr_suite_xls, "Test Cases", TestUtil.getRowNum(cr_suite_xls, this.getClass().getSimpleName()), "PASS");
	  else
		  TestUtil.reportDataSetResult(cr_suite_xls, "Test Cases", TestUtil.getRowNum(cr_suite_xls, this.getClass().getSimpleName()), "FAIL");
	  }
  
  @DataProvider
  public Object[][] getTestData() {
    return TestUtil.getData(cr_suite_xls, this.getClass().getSimpleName());
    }
  }