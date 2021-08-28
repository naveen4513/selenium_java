package office.sirion.suite.po;

import office.sirion.util.TestUtil;
import java.io.IOException;
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

public class POWorkflow extends TestSuiteBase {
	String result = null;
	String runmodes[] = null;
	static int count = -1;
	static boolean fail = false;
	static boolean skip = false;
	static boolean isTestPass = true;
	
	@BeforeTest
	public void checkTestSkip() {
		if (!TestUtil.isTestCaseRunnable(po_suite_xls, this.getClass().getSimpleName())) {
			Logger.debug("Skipping Test Case " + this.getClass().getSimpleName() + " as runmode set to NO");
			throw new SkipException("Skipping Test Case " + this.getClass().getSimpleName() + " as runmode set to NO");
			}
		runmodes = TestUtil.getDataSetRunmodes(po_suite_xls, this.getClass().getSimpleName());
		}

  @Test(dataProvider = "getTestData")
  public void POWorkflowTest (String poWorkflowSteps, String poParent
		  ) throws InterruptedException {
	  
		count++;
		if (!runmodes[count].equalsIgnoreCase("Y")) {
			skip = true;
			throw new SkipException("Runmode for Test Data Set to NO -- " + count);
			}
		
		openBrowser();
	    endUserLogin(CONFIG.getProperty("endUserURL"), CONFIG.getProperty("endUserUsername"), CONFIG.getProperty("endUserPassword"));
	    
	    Logger.debug("Executing Test Case PO Workflow with Name --- " + poParent);
		
	    driver.get(CONFIG.getProperty("endUserURL"));
		fluentWaitMethod(locateElementBy("po_quick_link"));

	    
	    locateElementBy("po_quick_link").click();

		fluentWaitMethod(locateElementBy("entity_listing_page_display_dropdown_link"));
		
		new Select(locateElementBy("entity_listing_page_display_dropdown_link")).selectByVisibleText("100");

		
		if (!poParent.equalsIgnoreCase("")) {
			driver.findElement(By.xpath("//*[@id='cr']/tbody/tr[@role='row']/td[contains(.,'" + poParent + "')]/preceding-sibling::td[2]/a")).click();
			} else {
				locateElementBy("po_listing_page_first_link").click();
				}
				fluentWaitMethod(locateElementBy("pc_clone_entity"));
				locateElementBy("pc_clone_entity").click();
				fluentWaitMethod(locateElementBy("po_clone_entity_save_button"));
                locateElementBy("po_clone_entity_save_button").click();
		

		if (!poWorkflowSteps.equalsIgnoreCase("")) {
			for (String entityData : poWorkflowSteps.split(";")) {
				WebElement elementWorkflow = driver.findElement(By.cssSelector("ng-form[template='tab.actions']")).findElement(By.cssSelector("div.submit.freeze"));		
				try {
					fluentWaitMethod(elementWorkflow.findElement(By.xpath(".//button[contains(.,'"+entityData.trim()+"')]")));
					Assert.assertNotNull(elementWorkflow.findElement(By.xpath(".//button[contains(.,'"+entityData.trim()+"')]")));
					elementWorkflow.findElement(By.xpath("//button[contains(.,'"+entityData.trim()+"')]")).click();

					} catch (NoSuchElementException e) {
						Logger.debug("No Such Element with the given Key, --- "+entityData+" --- Moving onto Next Step");
		    			}

				if (entityData.trim().equalsIgnoreCase("Delete")) {
					if (driver.findElements(By.className("success-icon")).size()!=0) {
						String entityDeleteWFMessage = driver.findElement(By.className("success-icon")).getText();
						
						if (entityDeleteWFMessage.equalsIgnoreCase("Are you sure you would like to delete this entity?")) {
							Assert.assertEquals(entityDeleteWFMessage, "Are you sure you would like to delete this entity?");
							
							Assert.assertNotNull(driver.findElement(By.xpath("//button[contains(.,'Yes')]")));
							driver.findElement(By.xpath("//button[contains(.,'Yes')]")).click();

							}
						}
					}
				if (entityData.trim().equalsIgnoreCase("Approve")) {
					String entityShowPageStatus = locateElementBy("po_show_page_status").getText();

					Assertion(entityShowPageStatus, "Approved");
					Logger.debug("SL Status on show page has been verified");
					}

				try{
				fluentWaitMethod(driver.findElement(By.cssSelector("ng-form[template='tab.actions']")).findElement(By.cssSelector("div.submit.freeze")));
				}catch (NoSuchElementException e) {fluentWaitMethod(driver.findElement(By.cssSelector("ng-form[template='tab.actions']")).findElement(By.cssSelector("div.submit.freeze")));}
				}
			}
		fail = false;
		
		driver.get(CONFIG.getProperty("endUserURL"));
	    }
	
  
  @AfterMethod
  public void reportDataSetResult(ITestResult testResult) throws IOException {
		takeScreenShotOnFailure(testResult.getStatus(), this.getClass().getSimpleName());

		if(testResult.getStatus()==ITestResult.SKIP)
			TestUtil.reportDataSetResult(po_suite_xls, this.getClass().getSimpleName(), count+2, "SKIP");
		else if(testResult.getStatus()==ITestResult.FAILURE) {
			isTestPass=false;
			TestUtil.reportDataSetResult(po_suite_xls, this.getClass().getSimpleName(), count+2, "FAIL");
			result= "Fail";
			}
		else if (testResult.getStatus()==ITestResult.SUCCESS) {
			TestUtil.reportDataSetResult(po_suite_xls, this.getClass().getSimpleName(), count+2, "PASS");
			result= "Pass";
			}
		try {
          for (int i = 2; i <= po_suite_xls.getRowCount(this.getClass().getSimpleName()); i++) {
              String testCaseID = po_suite_xls.getCellData(this.getClass().getSimpleName(), "testCaseID", i);
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
		  TestUtil.reportDataSetResult(po_suite_xls, "Test Cases", TestUtil.getRowNum(po_suite_xls, this.getClass().getSimpleName()), "PASS");
	  else
		  TestUtil.reportDataSetResult(po_suite_xls, "Test Cases", TestUtil.getRowNum(po_suite_xls, this.getClass().getSimpleName()), "FAIL");
	  }
  
  @DataProvider
  public Object[][] getTestData() {
    return TestUtil.getData(po_suite_xls, this.getClass().getSimpleName());
    }
  }