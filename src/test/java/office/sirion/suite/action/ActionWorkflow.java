package office.sirion.suite.action;

import office.sirion.util.TestUtil;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
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

public class ActionWorkflow extends TestSuiteBase {
	String result = null;
	String runmodes[] = null;
	static int count = -1;
	static boolean fail = true;
	static boolean skip = false;
	static boolean isTestPass = true;
	
	@BeforeTest
	public void checkTestSkip() {
		if (!TestUtil.isTestCaseRunnable(action_suite_xls, this.getClass().getSimpleName())) {
			Logger.debug("Skipping Test Case " + this.getClass().getSimpleName() + " as runmode set to NO");
			throw new SkipException("Skipping Test Case " + this.getClass().getSimpleName() + " as runmode set to NO");
			}
		runmodes = TestUtil.getDataSetRunmodes(action_suite_xls, this.getClass().getSimpleName());
		}

	@Test(dataProvider = "getTestData")
	public void ActionWorkflowTest (String actionWorkflowSteps, String actionParent) throws InterruptedException {
		
		count++;
		if (!runmodes[count].equalsIgnoreCase("Y")) {
			skip = true;
			throw new SkipException("Runmode for Test Data Set to NO -- " + count);
			}
		
		openBrowser();
	    endUserLogin(CONFIG.getProperty("endUserURL"), CONFIG.getProperty("endUserUsername"), CONFIG.getProperty("endUserPassword"));
	    
	    Logger.debug("Executing Test Case Action Workflow with Title ---- " + actionParent);
		
	    driver.get(CONFIG.getProperty("endUserURL"));

	    locateElementBy("actions_quick_link").click();;

		new Select(locateElementBy("entity_listing_page_display_dropdown_link")).selectByVisibleText("100");

		if (!actionParent.equalsIgnoreCase("")) {
			
			fluentWaitMethod(driver.findElement(By.xpath(".//*[@id='cr']/tbody/tr[@role='row']/td[contains(.,'" + actionParent + "')]/preceding-sibling::td[1]/a")));
            driver.findElement(By.xpath(".//*[@id='cr']/tbody/tr[@role='row']/td[contains(.,'" + actionParent + "')]/preceding-sibling::td[1]/a")).click();
			
		} else {
				locateElementBy("entity_listing_page_first_entry_link").click();;

			    locateElementBy("entity_clone_button").click();;
				locateElementBy("entity_save_button_after_clone").click();;

				handlePopup("actions_show_page_id");

				 }
	    if (!actionWorkflowSteps.equalsIgnoreCase("")) {
	    	
		      for (String entityData : actionWorkflowSteps.split(";")) {
		    	  try {

		    		  Assert.assertNotNull(driver.findElement(By.xpath("//button[contains(.,'"+entityData.trim()+"')]")));
		    		  clickWebElement(driver.findElement(By.xpath("//button[contains(.,'"+entityData.trim()+"')]")));
		    		  } catch (NoSuchElementException e) {
		    			  Logger.debug("No Such Element with the given Key, --- "+entityData+" --- Moving onto Next Step");
		    			  }
		    	  }
		      
		      String entityShowPageStatus = locateElementBy("actions_show_page_status").getText();

		      Assertion(entityShowPageStatus, "Newly Created");
		      Logger.debug("Actions Status on show page has been verified");
		      }
	    
		fail = false;

       

        driver.get(CONFIG.getProperty("endUserURL"));
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
		else if (testResult.getStatus()== ITestResult.SUCCESS) {
			TestUtil.reportDataSetResult(client_admin_suite_xls, this.getClass().getSimpleName(), count+2, "PASS");
			result= "Pass";
		}
		try {
			for (int i = 2; i <= action_suite_xls.getRowCount(this.getClass().getSimpleName()); i++) {
				String testCaseID = action_suite_xls.getCellData(this.getClass().getSimpleName(), "testCaseID", i);
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
			TestUtil.reportDataSetResult(action_suite_xls, "Test Cases", TestUtil.getRowNum(action_suite_xls, this.getClass().getSimpleName()), "PASS");
		else
			TestUtil.reportDataSetResult(action_suite_xls, "Test Cases", TestUtil.getRowNum(action_suite_xls, this.getClass().getSimpleName()), "FAIL");
		}

	@DataProvider
	public Object[][] getTestData() {
		return TestUtil.getData(action_suite_xls, this.getClass().getSimpleName());
		}
	}
