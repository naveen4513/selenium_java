package office.sirion.suite.childSL;

import java.io.IOException;
import java.sql.SQLException;
import office.sirion.util.DatePicker_Enhanced;
import office.sirion.util.TestUtil;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
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

public class ChildSLUpdate extends TestSuiteBase {
	String result = null;
	String runmodes[] = null;
	static int count = -1;
	static boolean fail = true;
	static boolean skip = false;
	static boolean isTestPass = true;
	
	@BeforeTest
	public void checkTestSkip() {
		if (!TestUtil.isTestCaseRunnable(child_sl_suite_xls, this.getClass().getSimpleName())) {
			Logger.debug("Skipping Test Case " + this.getClass().getSimpleName() + " as runmode set to NO");
			throw new SkipException("Skipping Test Case " + this.getClass().getSimpleName() + " as runmode set to NO");
			}
		runmodes = TestUtil.getDataSetRunmodes(child_sl_suite_xls, this.getClass().getSimpleName());
		}
	
	@Test(dataProvider = "getTestData")
	public void ChildSLUpdateTest(String cslSupplierNumerator, String cslActualNumerator, String cslSupplierDenominator, String cslActualDenominator,
			String cslSupplierCalculation, String cslActualPerformance, String cslDiscrepancy, String cslDiscrepancyResolutionStatus, String cslFinalNumerator,
			String cslFinalDenominator, String cslFinalPerformance, String cslComments, String cslActualDate, String cslRequestedBy, String cslChangeRequest,
			String cslUploadFile, String cslParent) throws InterruptedException, ClassNotFoundException, SQLException {
		
		count++;
		if (!runmodes[count].equalsIgnoreCase("Y")) {
			skip = true;
			throw new SkipException("Runmode for Test Set Data Set to NO " + count);
			}
	
		openBrowser();
	    endUserLogin(CONFIG.getProperty("endUserURL"), CONFIG.getProperty("endUserUsername"), CONFIG.getProperty("endUserPassword"));
	    
	    Logger.debug("Executing Test Case Child SL Update ---- ");
		
	    driver.get(CONFIG.getProperty("endUserURL"));
	locateElementBy("sl_quick_link");

	    
	    locateElementBy("sl_quick_link").click();

	    locateElementBy("csl_link_listing_page");
	    locateElementBy("csl_link_listing_page").click();


		new Select(locateElementBy("entity_listing_page_display_dropdown_link")).selectByVisibleText("100");

		locateElementBy("entity_listing_page_first_entry_link");
		
		if (!cslParent.equalsIgnoreCase("")) {
			driver.findElement(By.xpath(".//*[@id='cr']/tbody/tr[@role='row']/td[contains(.,'" + cslParent + "')]/preceding-sibling::td[1]/a")).click();
			} else {
				locateElementBy("entity_listing_page_first_entry_link").click();
				}

		locateElementBy("csl_edit_button");

		Assert.assertNotNull(locateElementBy("csl_edit_button"));
	    ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", locateElementBy("csl_edit_button"));
        locateElementBy("csl_edit_button").click();

        locateElementBy("csl_edit_page_supplier_numerator_numeric_box");
	    
	    addFieldValue("csl_edit_page_supplier_numerator_numeric_box", cslSupplierNumerator);
	    
	    addFieldValue("csl_edit_page_actual_numerator_numeric_box", cslActualNumerator);
	    
	    addFieldValue("csl_edit_page_supplier_denominator_numeric_box", cslSupplierDenominator);
	    
	    addFieldValue("csl_edit_page_actual_denominator_numeric_box", cslActualDenominator);
	    
	    addFieldValue("csl_edit_page_supplier_calculation_numeric_box", cslSupplierCalculation);

	    addFieldValue("csl_edit_page_actual_performance_numeric_box", cslActualPerformance);
	    
	    addFieldValue("csl_edit_page_discrepancy_textarea", cslDiscrepancy);
	    
	    addFieldValue("csl_edit_page_discrepancy_resolution_status_textarea", cslDiscrepancyResolutionStatus);
	    
	    addFieldValue("csl_edit_page_final_numerator_numeric_box", cslFinalNumerator);
	    
	    addFieldValue("csl_edit_page_final_denominator_numeric_box", cslFinalDenominator);

	    addFieldValue("csl_edit_page_final_performance_numeric_box", cslFinalPerformance);

	    locateElementBy("csl_edit_page_final_performance_numeric_box").sendKeys(Keys.TAB);

		// Child Service Levels - Edit Page - COMMENTS AND ATTACHMENTS
		addFieldValue("entity_create_page_comments_textarea", cslComments);
		    
		addFieldValue("entity_create_page_requested_by_dropdown", cslRequestedBy);
		    
		DatePicker_Enhanced date = new DatePicker_Enhanced();
		date.selectCalendar(cslActualDate,"actualDate");
		    
		addFieldValue("entity_create_page_change_request_dropdown", cslChangeRequest);
		
		if (!cslUploadFile.equalsIgnoreCase("")) {
			locateElementBy("entity_create_page_upload_browse_button").sendKeys(System.getProperty("user.dir")+"\\file-upload\\Child SL\\" + cslUploadFile);
			//
			}

	    Assert.assertNotNull(locateElementBy("csl_update_button"));
	    ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", locateElementBy("csl_update_button"));
        locateElementBy("csl_update_button").click();


	    
	    fail = false;

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
