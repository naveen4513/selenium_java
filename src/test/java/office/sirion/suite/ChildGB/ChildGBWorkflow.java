package office.sirion.suite.ChildGB;

import java.io.IOException;
import java.sql.SQLException;
import office.sirion.util.TestUtil;
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

public class ChildGBWorkflow extends TestSuiteBase {

	String runmodes[] = null;
	static int count = -1;
	static boolean fail = true;
	static boolean skip = false;
	static boolean isTestPass = true;
	String result=null;
	
	@BeforeTest
	public void checkTestSkip() {
		if (!TestUtil.isTestCaseRunnable(child_gb_suite_xls, this.getClass().getSimpleName())) {
			Logger.debug("Skipping Test Case " + this.getClass().getSimpleName() + " as runmode set to NO");
			throw new SkipException("Skipping Test Case " + this.getClass().getSimpleName() + " as runmode set to NO");
			}
		runmodes = TestUtil.getDataSetRunmodes(child_gb_suite_xls, this.getClass().getSimpleName());
		}
	
	@Test(dataProvider = "getTestData")
	public void ChildGbWorkflowTest(String cgbWorkflowSteps, String cgbGoal,String cgbParent) throws InterruptedException, ClassNotFoundException, SQLException {
		
		count++;
		if (!runmodes[count].equalsIgnoreCase("Y")) {
			skip = true;
			throw new SkipException("Runmode for Test Data Set to NO -- " + count);
			}
		
		openBrowser();
	    endUserLogin(CONFIG.getProperty("endUserURL"), CONFIG.getProperty("endUserUsername"), CONFIG.getProperty("endUserPassword"));
	    
	    Logger.debug("Executing Test Case Child GB Workflow with Name --- " + cgbParent);
		
	    driver.get(CONFIG.getProperty("endUserURL"));
		locateElementBy("gb_quick_link");

		locateElementBy("gb_quick_link").click();

		new Select(locateElementBy("entity_listing_page_display_dropdown_link")).selectByVisibleText("100");

		try{
		locateElementBy("entity_listing_page_first_entry_link").click();
		} catch (NullPointerException e){
			Logger.debug("Child GB Data is not available");
	    	return;
	    }
		if (!cgbParent.equalsIgnoreCase("")) {
			driver.findElement(By.xpath(".//*[@id='cr']/tbody/tr[@role='row']/td[contains(.,'" + cgbParent + "')]/preceding-sibling::td[1]/a")).click();
			} else {
				locateElementBy("entity_listing_page_first_entry_link").click();
				}

		if (!cgbWorkflowSteps.equalsIgnoreCase("")) {
		      for (String entityData : cgbWorkflowSteps.split(";")) {
		    	  try {
                      locateElementBy("cgb_show_page_status");
		    		  fluentWaitMethod(driver.findElement(By.xpath("//button[contains(.,'"+entityData.trim()+"')]")));
		    		  Assert.assertNotNull(driver.findElement(By.xpath("//button[contains(.,'"+entityData.trim()+"')]")));
		    		  driver.findElement(By.xpath("//button[contains(.,'"+entityData.trim()+"')]")).click();
		    		 
		    		  } catch (NoSuchElementException e) {
		    			  Logger.debug("No Such Element with the given Key, --- "+entityData+" --- Moving onto Next Step");
		    			  }
		    	  }
		      
		      locateElementBy("cgb_show_page_status");
		     
		      String entityShowPageStatus = locateElementBy("cgb_show_page_status").getText();

		      Assertion(entityShowPageStatus, "Meeting Completed");
		      Logger.debug("Child GB Status on show page has been verified");
		      }
	    
		
		fail = false;

       

        driver.get(CONFIG.getProperty("endUserURL"));
	    }

	  @AfterMethod
		public void reportDataSetResult(ITestResult testResult) throws IOException {
		   takeScreenShotOnFailure(testResult.getStatus(), this.getClass().getSimpleName());
		   
		   if(testResult.getStatus()==ITestResult.SKIP)
		      TestUtil.reportDataSetResult(child_gb_suite_xls, this.getClass().getSimpleName(), count+2, "SKIP");
		   else if(testResult.getStatus()==ITestResult.FAILURE) {
		      isTestPass=false;
		      TestUtil.reportDataSetResult(child_gb_suite_xls, this.getClass().getSimpleName(), count+2, "FAIL");
		      result= "Fail";
		      }
		   else if (testResult.getStatus()==ITestResult.SUCCESS) {
		      TestUtil.reportDataSetResult(child_gb_suite_xls, this.getClass().getSimpleName(), count+2, "PASS");
		      result= "Pass";
		      }
		  try {
			  for (int i = 2; i <= child_gb_suite_xls.getRowCount(this.getClass().getSimpleName()); i++) {
				  String testCaseID = child_gb_suite_xls.getCellData(this.getClass().getSimpleName(), "testCaseID", i);
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
      TestUtil.reportDataSetResult(child_gb_suite_xls, "Test Cases", TestUtil.getRowNum(child_gb_suite_xls, this.getClass().getSimpleName()), "PASS");
    else
      TestUtil.reportDataSetResult(child_gb_suite_xls, "Test Cases", TestUtil.getRowNum(child_gb_suite_xls, this.getClass().getSimpleName()), "FAIL");
  }

  @DataProvider
  public Object[][] getTestData() {
    return TestUtil.getData(child_gb_suite_xls, this.getClass().getSimpleName());
  }
}
