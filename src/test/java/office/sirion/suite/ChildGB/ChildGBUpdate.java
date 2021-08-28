package office.sirion.suite.ChildGB;

import java.io.IOException;
import java.sql.SQLException;
import office.sirion.util.DatePicker_Enhanced;
import office.sirion.util.TestUtil;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
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

public class ChildGBUpdate extends TestSuiteBase {
	String result = null;
	String runmodes[] = null;
	static int count = -1;
	static boolean fail = true;
	static boolean skip = false;
	static boolean isTestPass = true;
	
	@BeforeTest
	public void checkTestSkip() {
		if (!TestUtil.isTestCaseRunnable(child_gb_suite_xls, this.getClass().getSimpleName())) {
			Logger.debug("Skipping Test Case " + this.getClass().getSimpleName() + " as runmode set to NO");
			throw new SkipException("Skipping Test Case " + this.getClass().getSimpleName() + " as runmode set to NO");
			}
		runmodes = TestUtil.getDataSetRunmodes(child_gb_suite_xls, this.getClass().getSimpleName());
		}
	
	@Test(dataProvider = "getTestData")
	public void ChildGBUpdateTest(String cgbTitle, String cgbDescription, String cgbOccuranceDate, String cgbStartTime,
			String cgbDuration, String cgbComments, String cgbActualDate, String cgbRequestedBy, String cgbChangeRequest,
			String cgbUploadFile) throws InterruptedException, ClassNotFoundException, SQLException {
		
		count++;
		if (!runmodes[count].equalsIgnoreCase("Y")) {
			skip = true;
			throw new SkipException("Runmode for Test Set Data Set to NO " + count);
			}
	
		openBrowser();
	    endUserLogin(CONFIG.getProperty("endUserURL"), CONFIG.getProperty("endUserUsername"), CONFIG.getProperty("endUserPassword"));
	    
	    Logger.debug("Executing Test Case Child GB Update ---- ");
		
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
		if (!cgbTitle.equalsIgnoreCase("")) {
			driver.findElement(By.xpath("//*[@id='cr']/tbody/tr[@role='row']/td[contains(.,'" + cgbTitle + "')]/preceding-sibling::td[1]/a")).click();
			} else {
				locateElementBy("entity_listing_page_first_entry_link").click();
				}

		locateElementBy("cgb_update_button").click();

		Assert.assertNotNull(locateElementBy("cgb_update_button"));
	    ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", locateElementBy("cgb_update_button"));
	    locateElementBy("cgb_update_button").click();
	    locateElementBy("cgb_update_page_title_textbox");
	    
	    addFieldValue("cgb_update_page_title_textbox", cgbTitle);
	    
	    addFieldValue("cgb_update_page_description_textarea", cgbDescription);
	    
	    locateElementBy("cgb_blank_area").click();
	    
	    addFieldValue("cgb_update_page_start_time_dropdown", cgbStartTime);
	    
	    addFieldValue("cgb_update_page_duration_dropdown", cgbDuration);
	    
	    String[] cgbOccurancedate = cgbOccuranceDate.split("-");
	    String cgbOccurance_DateMonth = cgbOccurancedate[0];
	    String cgbOccurance_DateYear = convertStringToInteger(cgbOccurancedate[2]);
	    int cgbOccurance_dateYear = Integer.parseInt(cgbOccurance_DateYear);
	    String cgbOccurance_DateDate = convertStringToInteger(cgbOccurancedate[1]);
	    Integer cgbOccurance_dateDate = Integer.parseInt(cgbOccurance_DateDate);
	    cgbOccurance_DateDate = cgbOccurance_dateDate.toString();

	    DatePicker_Enhanced dp_cgbOccurance_date = new DatePicker_Enhanced();
	    dp_cgbOccurance_date.expDate = cgbOccurance_DateDate;
	    dp_cgbOccurance_date.expMonth = cgbOccurance_DateMonth;
	    dp_cgbOccurance_date.expYear = cgbOccurance_dateYear;
	    dp_cgbOccurance_date.pickExpDate("occurrenceDate");

		// Child Service Levels - Edit Page - COMMENTS AND ATTACHMENTS
		addFieldValue("cgb_comment_box_textarea", cgbComments);
		    
		addFieldValue("cgb_requested_by_dropdown", cgbRequestedBy);
		    
		String[] cgbActualdate = cgbActualDate.split("-");
	    String cgbActual_DateMonth = cgbActualdate[0];
	    String cgbActual_DateYear = convertStringToInteger(cgbActualdate[2]);
	    int cgbActual_dateYear = Integer.parseInt(cgbActual_DateYear);
	    String cgbActual_DateDate = convertStringToInteger(cgbActualdate[1]);
	    Integer cgbActual_dateDate = Integer.parseInt(cgbActual_DateDate);
	    cgbActual_DateDate = cgbActual_dateDate.toString();

	    DatePicker_Enhanced dp_cgbActual_date = new DatePicker_Enhanced();
	    dp_cgbActual_date.expDate = cgbActual_DateDate;
	    dp_cgbActual_date.expMonth = cgbActual_DateMonth;
	    dp_cgbActual_date.expYear = cgbActual_dateYear;
	    dp_cgbActual_date.pickExpDate("actualDate");
		    
		addFieldValue("cgb_change_request_dropdown", cgbChangeRequest);
		
		if (!cgbUploadFile.equalsIgnoreCase("")) {
			locateElementBy("entity_create_page_upload_browse_button").sendKeys(System.getProperty("user.dir")+"\\file-upload\\Child GB\\" + cgbUploadFile);
			}

	    Assert.assertNotNull(locateElementBy("cbg_edit_button"));
	    ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", locateElementBy("cbg_edit_button"));
	    locateElementBy("cbg_edit_button").click();

	    
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
		else if (testResult.getStatus()== ITestResult.SUCCESS) {
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
