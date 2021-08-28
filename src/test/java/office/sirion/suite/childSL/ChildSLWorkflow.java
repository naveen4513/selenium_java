package office.sirion.suite.childSL;

import java.io.IOException;
import java.sql.SQLException;
import office.sirion.util.TestUtil;
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

public class ChildSLWorkflow extends TestSuiteBase {

	String runmodes[] = null;
	static int count = -1;
	static boolean fail = true;
	static boolean skip = false;
	static boolean isTestPass = true;
	  String testCaseID=null;
	  String result=null;
	
	@BeforeTest
	public void checkTestSkip() {
		if (!TestUtil.isTestCaseRunnable(child_sl_suite_xls, this.getClass().getSimpleName())) {
			Logger.debug("Skipping Test Case " + this.getClass().getSimpleName() + " as runmode set to NO");
			throw new SkipException("Skipping Test Case " + this.getClass().getSimpleName() + " as runmode set to NO");
			}
		runmodes = TestUtil.getDataSetRunmodes(child_sl_suite_xls, this.getClass().getSimpleName());
		}
	
	@Test(dataProvider = "getTestData")
	public void ChildSLWorkflowTest(String cslWorkflowSteps, String cslParent
			) throws InterruptedException, ClassNotFoundException, SQLException {
		
		count++;
		if (!runmodes[count].equalsIgnoreCase("Y")) {
			skip = true;
			throw new SkipException("Runmode for Test Data Set to NO -- " + count);
			}
		
		openBrowser();
	    endUserLogin(CONFIG.getProperty("endUserURL"), CONFIG.getProperty("endUserUsername"), CONFIG.getProperty("endUserPassword"));
	    
	    Logger.debug("Executing Test Case Child SL Workflow with Name --- " + cslParent);
		
	    driver.get(CONFIG.getProperty("endUserURL"));
		locateElementBy("sl_quick_link");

	    
	    locateElementBy("sl_quick_link").click();

	    locateElementBy("csl_link_listing_page");

	    locateElementBy("csl_link_listing_page").click();

	    new Select(locateElementBy("entity_listing_page_display_dropdown_link")).selectByVisibleText("100");

	    locateElementBy("entity_listing_page_first_entry_link");
		
		if (!cslParent.equalsIgnoreCase("") ) {
			try{
			driver.findElement(By.xpath(".//*[@id='cr']/tbody/tr[@role='row']/td[contains(.,'" + cslParent + "')]/preceding-sibling::td[1]/a")).click();
			}catch (NoSuchElementException e){
				locateElementBy("entity_listing_page_first_entry_link").click();
			}
			} else {
				locateElementBy("entity_listing_page_first_entry_link").click();
				}


		if (!cslWorkflowSteps.equalsIgnoreCase("")) {
			for (String entityData : cslWorkflowSteps.split(";")) {
				WebElement elementWorkflow = locateElementBy("csl_show_page_bottom_tab").findElement(By.cssSelector("div.submit.freeze"));
				try {
					fluentWaitMethod(elementWorkflow.findElement(By.xpath(".//button[contains(.,'"+entityData.trim()+"')]")));
					Assert.assertNotNull(elementWorkflow.findElement(By.xpath(".//button[contains(.,'"+entityData.trim()+"')]")));
					elementWorkflow.findElement(By.xpath("//button[contains(.,'"+entityData.trim()+"')]")).click();

					} catch (NoSuchElementException e) {
						Logger.debug("No Such Element with the given Key, --- "+entityData+" --- Moving onto Next Step");
		    			}

				if (entityData.trim().equalsIgnoreCase("Delete")) {
					if (driver.findElements(By.className("success-icon")).size()!=0) {
						String entityDeleteWFMessage = locateElementBy("csl_pop_up_id").getText();
						
						if (entityDeleteWFMessage.equalsIgnoreCase("Are you sure you would like to delete this entity?")) {
							Assert.assertEquals(entityDeleteWFMessage, "Are you sure you would like to delete this entity?");
							
							Assert.assertNotNull(locateElementBy("csl_pop_up_yes_option"));
                            locateElementBy("csl_pop_up_yes_option").click();

							}
						}
					}

				if (entityData.trim().equalsIgnoreCase("Publish")) {
					String entityShowPageStatus = locateElementBy("csl_show_page_status").getText();

					Assert.assertEquals(entityShowPageStatus, "Active");
					Logger.debug("SL Status on show page has been verified");
					}

				locateElementBy("csl_show_page_bottom_tab").findElement(By.cssSelector("div.submit.freeze"));
				}
			}

		Logger.debug("Test Case SL Workflow with Name -- " + cslParent + " -- is PASSED");
		driver.get(CONFIG.getProperty("endUserURL"));
	    }

	@AfterMethod
	public void reportDataSetResult(ITestResult testResult) throws IOException {
		takeScreenShotOnFailure(testResult.getStatus(), this.getClass().getSimpleName());

		if(testResult.getStatus()==ITestResult.SKIP)
			TestUtil.reportDataSetResult(child_sl_suite_xls, this.getClass().getSimpleName(), count+2, "SKIP");
		else if(testResult.getStatus()==ITestResult.FAILURE) {
			isTestPass=false;
			TestUtil.reportDataSetResult(child_sl_suite_xls, this.getClass().getSimpleName(), count+2, "FAIL");
			result= "Fail";
			}
		else if (testResult.getStatus()==ITestResult.SUCCESS) {
			TestUtil.reportDataSetResult(child_sl_suite_xls, this.getClass().getSimpleName(), count+2, "PASS");
			result= "Pass";
			}
		try {
            for (int i = 2; i <= child_sl_suite_xls.getRowCount(this.getClass().getSimpleName()); i++) {
                String testCaseID = child_sl_suite_xls.getCellData(this.getClass().getSimpleName(), "testCaseID", i);
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
      TestUtil.reportDataSetResult(child_sl_suite_xls, "Test Cases", TestUtil.getRowNum(child_sl_suite_xls, this.getClass().getSimpleName()), "PASS");
    else
      TestUtil.reportDataSetResult(child_sl_suite_xls, "Test Cases", TestUtil.getRowNum(child_sl_suite_xls, this.getClass().getSimpleName()), "FAIL");
  }

  @DataProvider
  public Object[][] getTestData() {
    return TestUtil.getData(child_sl_suite_xls, this.getClass().getSimpleName());
  }
}
