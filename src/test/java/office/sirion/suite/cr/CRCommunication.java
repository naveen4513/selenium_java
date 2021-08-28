package office.sirion.suite.cr;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import office.sirion.util.DatePicker_Enhanced;
import office.sirion.util.TestUtil;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.testng.Assert;
import org.testng.ITestResult;
import org.testng.SkipException;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

public class CRCommunication extends TestSuiteBase {
  String result = null;
  String runmodes[] = null;
  static int count = -1;
  static boolean fail = false;
  static boolean skip = false;
  static boolean isTestPass = true;
  static DateFormat dateFormat = new SimpleDateFormat("dd/MMM/yyyy");
  static Date date1 = new Date();
  @BeforeTest
  public void checkTestSkip() {
    if (!TestUtil.isTestCaseRunnable(cr_suite_xls, this.getClass().getSimpleName())) {
      Logger.debug("Skipping Test Case " + this.getClass().getSimpleName() + " as runmode set to NO");
      throw new SkipException("Skipping Test Case " + this.getClass().getSimpleName() + " as runmode set to NO");
    }
    runmodes = TestUtil.getDataSetRunmodes(cr_suite_xls, this.getClass().getSimpleName());
  }

  @Test(dataProvider = "getTestData")
  public void CRCommunicationTest(String testCaseId, String crComments, String crActualDate, String crRequestedBy, String crChangeRequest,
      String crUploadFile) throws InterruptedException {

    count++;
    if (!runmodes[count].equalsIgnoreCase("Y")) {
      skip = true;
      throw new SkipException("Runmode for Test Data Set to NO -- " + count);
    }

    Logger.debug("Executing Test Case Change Request Communication");

    // Launch The Browser
    openBrowser();
    endUserLogin(CONFIG.getProperty("endUserURL"), CONFIG.getProperty("endUserUsername"), CONFIG.getProperty("endUserPassword"));

      driver.get(CONFIG.getProperty("endUserURL"));

  	fluentWaitMethod(locateElementBy("cr_quick_link"));

    locateElementBy("cr_quick_link").click();
    fluentWaitMethod(locateElementBy("cr_listing_page_first_entry_link"));
    locateElementBy("cr_listing_page_first_entry_link").click();
    fluentWaitMethod(driver.findElement(By.xpath("//button[contains(.,'Save Comment/Attachment')]")));

    // Child Governance Body - Edit Page - COMMENTS AND ATTACHMENTS
    addFieldValue("cr_comment_box_textarea", crComments);
    locateElementBy("cr_blank_area").click();
	addFieldValue("cr_requested_by_dropdown", crRequestedBy);
	    
	DatePicker_Enhanced date = new DatePicker_Enhanced();
	date.selectCalendar(crActualDate,"actualDate");
		    
	addFieldValue("cr_change_request_dropdown", crChangeRequest);
	
	if (!crUploadFile.equalsIgnoreCase("")) {
		locateElementBy("entity_create_page_upload_browse_button").sendKeys(System.getProperty("user.dir")+"\\file-upload\\CR\\" + crUploadFile);
		}
	
    Assert.assertNotNull(driver.findElement(By.xpath("//button[contains(.,'Save Comment/Attachment')]")));
    ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);",
        driver.findElement(By.xpath("//button[contains(.,'Save Comment/Attachment')]")));
    driver.findElement(By.xpath("//button[contains(.,'Save Comment/Attachment')]")).click();

    if (getObject("cr_error_notifications") != null) {
      List<WebElement> e = driver.findElement(By.xpath(".//*[@id='genericErrors']/list")).findElements(By.tagName("li"));
      for (WebElement element : e) {
        String errors_create_page = element.getText();
        if (errors_create_page.contains("Please add a comment or upload files")) {
          Logger.debug("Please add a comment or upload files");
          Logger.debug("Errors: " + errors_create_page);
        }
      }
      
    }

    driver.findElement(By.linkText("COMMUNICATION")).click();
    waitF.until(ExpectedConditions.visibilityOf(driver.findElement(By.id("table_819_wrapper"))));
	 
	 Assert.assertEquals(locateElementBy("cr_communication_status").getText(), "Comment/Attachment");
	 
	 if (!crComments.equalsIgnoreCase("")) {
		 	locateElementBy("cr_communication_latest_entry_link").click();
		 	
		 	try{
			Assert.assertEquals(locateElementBy("cr_communication_comment").getText(), crComments,"Comment Matched");
			driver.switchTo().frame("iframe_textEditor_showpage");
			Assert.assertEquals(locateElementBy("cr_communication_details_comment").getText(), crComments,"Comment Matched");
			driver.switchTo().defaultContent();
		 	}catch (AssertionError err) {
		 		System.out.println("Comment: " + crComments + "and" + "Comment under ComunicationTab: " + locateElementBy("cr_communication_comment").getText() + "Does not Match");
		 	}
			
			}else{
				Assert.assertEquals(locateElementBy("cr_communication_comment").getText(), "-");
			}
	
	 if (!crUploadFile.equalsIgnoreCase("")) {
		 	locateElementBy("cr_communication_latest_entry_link").click();
		 	
		 	try{
			Assert.assertEquals(locateElementBy("cr_communication_documents_link").getText(), crUploadFile,"File Name Matched");
			Assert.assertEquals(locateElementBy("cr_communication_details_documents_link").getText(), crUploadFile, "File Name Matched");
		 	}catch (AssertionError err) {
		 		System.out.println("Uploaded File Name: " + crUploadFile + "and" + "File Name under ComunicationTab: " + locateElementBy("cr_communication_documents_link").getText() + "Does not Match");
		 	}
			}else{
				
				Assert.assertEquals(locateElementBy("cr_communication_documents_link").getText(), "-");
			}
	 
	 if (!crActualDate.equalsIgnoreCase("")) {
		 
		 String[] TimeOfcr = locateElementBy("cr_communication_time_of_action").getText().split("-");
		 String[] crActualdate = crActualDate.split("-");
		 	locateElementBy("cr_communication_latest_entry_link").click();
		 	try {
			Assertion(TimeOfcr[0],crActualdate[1]);
			Assertion(TimeOfcr[1],crActualdate[0].replace("e", ""));
			Assertion(TimeOfcr[2],crActualdate[2]);
		 	}catch (AssertionError err) {
		 		System.out.println("Actual Date: " + crActualDate + " and " + "Time of cr under ComunicationTab: " + locateElementBy("cr_communication_time_of_action").getText() + " Does not Match");
		 	}
			String [] DetailsTimeOfcr = locateElementBy("cr_communication_details_time_of_action").getText().replace("Time of cr: ", "").split("-");
			
			try {
			Assertion(DetailsTimeOfcr[0],crActualdate[1]);
			Assertion(DetailsTimeOfcr[1],crActualdate[0].replace("e", ""));
			Assertion(DetailsTimeOfcr[2],crActualdate[2]);
			}catch (AssertionError err) {
				System.out.println("Actual Date: " + crActualDate + " and " + "Time of cr under ComunicationTab: " + locateElementBy("cr_communication_time_of_action").getText()  + " Does not Match");
		 	}
			
			}else{

				locateElementBy("cr_communication_latest_entry_link").click();
				try{
				Assert.assertEquals(locateElementBy("cr_communication_time_of_action").getText(), dateFormat.format(date1), "Date, Month and Year Matched");
				Assert.assertEquals(locateElementBy("cr_communication_details_time_of_action").getText().replaceAll("Time of cr: ", ""), dateFormat.format(date1), "Date, Month and Year Matched");
				}catch (AssertionError err) {
			 		System.out.println("Actual Date: " + crActualDate + " and " + "Time of cr under ComunicationTab: " + locateElementBy("cr_communication_time_of_action").getText()  + " Does not Match");
			 	}
			} 
	 
	 if (!crRequestedBy.equalsIgnoreCase("")) {
		 	locateElementBy("cr_communication_latest_entry_link").click();
		 	
		 	try{
			Assert.assertEquals(locateElementBy("cr_communication_completed_by").getText(), crRequestedBy, "Requested By Name Matched");
			Assert.assertEquals(locateElementBy("cr_communication_details_completed_by").getText().replaceAll("Completed By: ", ""), crRequestedBy, "Requested By Name Matched");
		 	}catch (AssertionError err) {
		 		System.out.println("Requested By: " + crRequestedBy + " and " + "Requested By under ComunicationTab: " + locateElementBy("cr_communication_completed_by").getText() + " Does not Match");
		 	}
			
	 		}else{
				locateElementBy("cr_communication_latest_entry_link").click();
				String logged_in_user = locateElementBy("cr_communication_logged_in_user_full_name").getText();
				
				try{
				Assert.assertEquals(locateElementBy("cr_communication_completed_by").getText(), logged_in_user, "Requested By Name Matched");
				Assert.assertEquals(locateElementBy("cr_communication_details_completed_by").getText().replaceAll("Completed By: ", ""), logged_in_user, "Requested By Name Matched");
				}catch (AssertionError err) {
			 		System.out.println("Requested By: " + crRequestedBy + " and " + "Requested By under ComunicationTab: " + locateElementBy("cr_communication_completed_by").getText() + " Does not Match");
			 	}
			}
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
